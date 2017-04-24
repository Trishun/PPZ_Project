package pl.locationbasedgame;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

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

    void sendLoginRequestToServer(String name, String password, LoginFragment fragment) {
        Authenticator authenticator = new Authenticator();
        authenticator.setCaller(fragment);
        authenticator.setHandler(socketHandler);
        authenticator.execute(name, password);
    }

    void sendRegisterRequestToServer(String name, String password, String mail, RegisterFragment fragment) {
        Registrator registrator = new Registrator();
        registrator.setCaller(fragment);
        registrator.setHandler(socketHandler);
        registrator.execute(name, password, mail);
    }

    class ServerBinder extends Binder {
        CommunicationService getService() {
            return CommunicationService.this;
        }
    }
}
