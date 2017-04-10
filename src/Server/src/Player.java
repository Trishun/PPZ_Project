import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread{

    private DatabaseCommunicator databaseCommunicator;
    private Server up;
    private int lobby;

    /**
     * Class constructor
     * @param clientSocket Socket to be handled.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     * @throws IOException when there's no data incomming.
     */
    Player(Socket clientSocket, DatabaseCommunicator databaseCommunicator, Server up) throws IOException {
        this.databaseCommunicator = databaseCommunicator;
        this.up = up;
        MessageProvider messageProvider = new MessageProvider(clientSocket);
    }

    /** TODO
     * Main Player function.
     */
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new PollTask(this), 0, 15000);


    }

    /**
     * Poll management task
     */
    class PollTask extends TimerTask {
        Player player;

        PollTask(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            player.poll();
        }
    }

    private void poll () {
        if(!pollTest())
            Thread.currentThread().interrupt();
    }

//    TODO
    private boolean pollTest() {
        return true;
    }



    //Player management

    void createLobby () {
        setLobby(up.createLobby(this));
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

    void leaveLobby() {
        up.removeFromLobby(lobby, this);
    }

    public ArrayList<Player> refreshViewData() {
        return up.refreshViewData(this);
    }
}


