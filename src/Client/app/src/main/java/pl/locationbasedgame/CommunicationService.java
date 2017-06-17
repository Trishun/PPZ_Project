package pl.locationbasedgame;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommunicationService extends Service {

    private SocketHandler socketHandler;
    private IBinder binder = new ServerBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    void initializeConnection() {
        AssetManager assetManager = getBaseContext().getAssets();
        try {
            InputStream inputStream = assetManager.open("connection.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            socketHandler = new SocketHandler(properties.getProperty("IP"),
                    Integer.valueOf(properties.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void closeConnection() {
        socketHandler.closeSocket();
    }

    AccountResponse sendLoginRequestToServer(String name, String password, String locale) {
        Authenticator authenticator = new Authenticator();
        return authenticator.authenticate(socketHandler, name, password, locale);
    }

    AccountResponse sendRegisterRequestToServer(String name, String password, String mail, String locale) {
        Registrator registrator = new Registrator();
        return registrator.registerUser(socketHandler, name, password, mail, locale);
    }

    int createNewLobby(LobbyManager manager) {
        return manager.createLobbyAndGetCode(socketHandler);
    }

    void sendPoint(final LatLng location, final String hint) {
        Thread messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("header", "ccreate");
                    object.put("locx", location.longitude);
                    object.put("locy", location.latitude);
                    object.put("desc", hint);
                    socketHandler.send(object.toString());
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        messageThread.start();
        try {
            messageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    Integer joinExistingLobby(LobbyManager manager, int id) {
        return manager.joinLobby(socketHandler, id);
    }

    String getServerMessage() {
        return socketHandler.listen();
    }

    void sendSimpleMessage(final String headerValue) {
        Thread messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("header", headerValue);
                    socketHandler.send(object.toString());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        messageThread.start();
        try {
            messageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void sendComplexMessage(final String headerValue, final String param1name, final String param1value) {
        // TODO: extend params
        Thread signalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("header", headerValue);
                    object.put(param1name, param1value);
                    socketHandler.send(object.toString());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        signalThread.start();

        try {
            signalThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ServerBinder extends Binder {
        CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
