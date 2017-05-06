package pl.locationbasedgame;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONException;
import org.json.JSONObject;

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

    void sendRegisterRequestToServer(String name, String password, String mail, String locale, RegisterFragment fragment) {
        Registrator registrator = new Registrator();
        registrator.setCaller(fragment);
        registrator.setHandler(socketHandler);
        registrator.execute(name, password, mail, locale);
    }

    void createNewLobby(LobbyTask manager, LobbyTaskCallback context) {
        manager.set(socketHandler, context, -1);
        manager.execute('C');
    }

    void joinExistingLobby(LobbyTask manager, LobbyTaskCallback context, int id) {
        manager.set(socketHandler, context, id);
        manager.execute('J');
    }

    class ServerBinder extends Binder {
        CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
