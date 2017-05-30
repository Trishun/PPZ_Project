package pl.locationbasedgame;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
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

    JSONArray joinExistingLobby(LobbyManager manager, int id) {
        return manager.joinLobby(socketHandler, id);
    }

    String getServerMessage() {
        return socketHandler.listen();
    }

    void sendEndConnectionSignal() {
        // // TODO: 30-May-17 refactor
        Thread signalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = new JSONObject();
                    object.put("header", "endcon");
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
