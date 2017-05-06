package pl.locationbasedgame;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommunicationService extends Service {

    private SocketHandler socketHandler;
    private IBinder binder = new ServerBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                socketHandler = new SocketHandler();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    void closeConnection() {
        socketHandler.closeSocket();
    }

    boolean sendLoginRequestToServer(String name, String password, String locale) {
        Authenticator authenticator = new Authenticator();
        return authenticator.authenticate(socketHandler, name, password, locale);
    }

    boolean sendRegisterRequestToServer(String name, String password, String mail, String locale) {
        Registrator registrator = new Registrator();
        return registrator.registerUser(socketHandler, name, password, mail, locale);
    }

    int createNewLobby(LobbyManager manager) {
        return manager.createLobbyAndGetCode(socketHandler);
    }

    JSONArray joinExistingLobby(LobbyManager manager, int id) {
        return manager.joinLobby(socketHandler, id);
    }

    class ServerBinder extends Binder {
        CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
