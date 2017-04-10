import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread{

    private boolean pollFlag = true;
    private DatabaseCommunicator databaseCommunicator;
    private Server up;
    private int lobby;
    private MessageProvider messageProvider;
    private int playerId = 0;
    private String playerName = "not logged";

    /**
     * Class constructor
     * @param clientSocket Socket to be handled.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     * @throws IOException when there's no data incomming.
     */
    Player(Socket clientSocket, DatabaseCommunicator databaseCommunicator, Server up) throws IOException {
        this.databaseCommunicator = databaseCommunicator;
        this.up = up;
        messageProvider = new MessageProvider(clientSocket);
    }

    /** TODO
     * Main Player function.
     */
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new PollTask(), 0, 30000);
        while (true) {
            Message message = messageProvider.getMessage();
            if (message != null) {
                String header = message.getHeader();
                if (header.equalsIgnoreCase("endcon")) {
                    disconnect();
                    return;
                } else if (header.equalsIgnoreCase("login")) {
                    //do stuff
                } else if (header.equalsIgnoreCase("register")) {
                    //do stuff
                } else if (header.equalsIgnoreCase("lcreate")) {
                    // do stuff
                } else if (header.equalsIgnoreCase("ljoin")) {
                    // do stuff
                } else if (header.equalsIgnoreCase("lleave")) {
                    // do stuff
                } else if (header.equalsIgnoreCase("poll")) {
                    pollFlag = true;
                }
            }
        }
    }

    /**
     * Poll management task
     */
    class PollTask extends TimerTask {
        @Override
        public void run() {
            if (!pollFlag)
                disconnect();
            pollFlag = false;
            poll();
        }

        private void poll () {
            messageProvider.sendPoll();
        }
    }

    // Player management


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private void disconnect() {
        up.disconnectPlayer(this);
        Thread.currentThread().interrupt();
    }

    //Lobby management

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


