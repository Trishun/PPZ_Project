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
     * Main socket I/O task.
     *
     * @param
     */
    Server(SettingsProvider settingsProvider, PlayerManager playerManager) {
        this.settingsProvider = settingsProvider;
        this.playerManager = playerManager;
    }

    void Run() {
        new Thread(() -> incoming()).start();
    }

    private void incoming() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(settingsProvider.get("port")));

            while (true) {
                //Accept connection
                System.out.println("Oczekiwanie na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Połączono z " + socket.getRemoteSocketAddress().toString());

                //Create thread to handle connection
                playerManager.createPlayer(socket);
            }
        } catch (Exception e) {
            System.out.println("Error in Server/incoming: " + e);
        }
    }
}