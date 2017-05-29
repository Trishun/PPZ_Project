import java.net.ServerSocket;
import java.net.Socket;

/**
 * Players' shared content
 * Created by PD on 05.04.2017.
 */
public class Server {

    private PlayerManager playerManager;
    private SettingsProvider settingsProvider;


    /**
     * Main I/O server class
     *
     * @param settingsProvider Import global settings
     * @param playerManager    Player global thread manager
     */
    Server(SettingsProvider settingsProvider, PlayerManager playerManager) {
        this.settingsProvider = settingsProvider;
        this.playerManager = playerManager;
    }

    /**
     * Run.
     */
    void Run() {
        new Thread(() -> incoming()).start();
    }

    private void incoming() {
        System.out.println("JVM started");
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(settingsProvider.get("port")));
            Debug.Log("Init");
            while (true) {
                //Accept connection
                Socket socket = serverSocket.accept();
                Debug.Log("Connected: " + socket.getRemoteSocketAddress().toString());

                //Create thread to handle connection
                playerManager.createPlayer(socket);
            }
        } catch (Exception e) {
            Debug.Log("Error in Server/incoming: " + e);
        }
    }
}