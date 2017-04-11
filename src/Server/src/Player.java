import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread {

    private boolean pollFlag = true;
    private DatabaseCommunicator databaseCommunicator;
    private Server up;
    private int lobby;
    private MessageProvider messageProvider;
    private EncryptionProvider encryptionProvider;
    private int playerId = 0;
    private String playerName = "not logged";

    /**
     * Class constructor
     *
     * @param clientSocket         Socket to be handled.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     * @throws IOException when there's no data incomming.
     */
    Player(Socket clientSocket, DatabaseCommunicator databaseCommunicator, Server up) throws IOException {
        this.databaseCommunicator = databaseCommunicator;
        this.up = up;
        messageProvider = new MessageProvider(clientSocket);
        encryptionProvider = new EncryptionProvider();
    }

    /**
     * TODO
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
                    handleLogin(message);
                } else if (header.equalsIgnoreCase("register")) {
                    handleRegister(message);
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

        private void poll() {
            messageProvider.sendPoll();
        }
    }

    // Player management

    /**
     * @return DB playerID
     */
    int getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId value got from DB
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * @return DB nickname
     */
    String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName value got from DB
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     *
     */
    private void disconnect() {
        up.disconnectPlayer(this);
        Thread.currentThread().interrupt();
    }

    // DB management

    /**
     * Checks for user in the database, compares passwords
     * Sends appropriate message to the client afterwards
     *
     * @param message String message which method uses
     */
    private void handleLogin(Message message) {
        try {
            String splitted[] = message.getStringMultipleContent();
            String uName = splitted[0];
            String uPasswd = encryptionProvider.encrypt(splitted[1]);

            String query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                messageProvider.sendMessage(new Message("alert", "Wrong username!"));
                return;
            }
            resultSet.next();
            if (resultSet.getString("password").equals(uPasswd)) {
                messageProvider.sendMessage(new Message("login", true));
                setPlayerName(uName);
                setPlayerId(resultSet.getInt("user_id"));
            } else {
                messageProvider.sendMessage(new Message("alert", "Wrong password!"));
            }
        } catch (Exception e) {
            messageProvider.sendMessage(new Message("alert", String.valueOf(e)));
        }
    }

    /**
     * Handles user registration process
     * Sends appropriate message to the client afterwards
     *
     * @param message String message which method uses
     */
    private void handleRegister(Message message) {
        try {
            String splitted[] = message.getStringMultipleContent();
            String uName = splitted[0];
            String uPasswd = encryptionProvider.encrypt(splitted[1]);
            String backupPin = splitted[2];
            String email = splitted[3];

            //Check if email already registered
            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                messageProvider.sendMessage(new Message("alert", "e-mail already registered!"));
                return;
            }

            //Check for username availibility
            query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            resultSet = databaseCommunicator.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                messageProvider.sendMessage(new Message("alert", "Username taken!"));
                return;
            }

            //Register user
            //Get new user id
            query = "SELECT user_id FROM accounts WHERE user_id = (SELECT max(user_id) FROM accounts)";
            resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                messageProvider.sendMessage(new Message("alert", "Internal database error"));
                return;
            }
            //Insert user info into table
            resultSet.next();
            int userID = resultSet.getInt("user_id");
            userID++;
            query = "INSERT INTO users (user_id, nickname, password, backup_code, email) VALUES (" +
                    userID + ",'" + uName + "','" + uPasswd + ",'" + backupPin + ",'" + email + "')";
            databaseCommunicator.executeUpdate(query);
            messageProvider.sendMessage(new Message("register", true));

        } catch (Exception e) {
            messageProvider.sendMessage(new Message("alert", String.valueOf(e)));
        }
    }

    //Lobby management

    void createLobby() {
        setLobby(up.createLobby(this));
    }

    private void joinLobby(int enterCode) {
        up.addToLobby(enterCode, this);
    }

    void setLobby(int lobby) {
        this.lobby = lobby;
    }

    int getLobby() {
        return lobby;
    }

    void leaveLobby() {
        up.removeFromLobby(lobby, this);
    }

    ArrayList<Player> refreshViewData() {
        return up.refreshViewData(this);
    }
}
