import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread{

    private BufferedReader socketReader;
    private PrintWriter socketWriter;
    private DatabaseCommunicator databaseCommunicator;
    private Server up;
    private int lobby;

    /**
     * Class constructor
     * @param clientSocket Socket to be handled.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     * @throws IOException
     */
    Player(Socket clientSocket, DatabaseCommunicator databaseCommunicator, Server up) throws IOException {
        this.databaseCommunicator = databaseCommunicator;
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.up = up;
    }

    /** TODO
     * Main TH function.
     */
    public void run() {

    }

    public ArrayList<Player> refreshViewData() {
        return up.refreshViewData(this);
    }

    private void joinLobby(int enterCode) {
        up.addToLobby(enterCode, this);
    }

    public void setLobby (int lobby) {
        this.lobby = lobby;
    }

    public int getLobby() {
        return lobby;
    }

}
