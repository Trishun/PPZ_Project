import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Thread (player) handing class
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread {

    private Socket clientSocket;
    private DatabaseCommunicator databaseCommunicator;
    private Server up;
    private Integer lobby;
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
        this.clientSocket = clientSocket;
    }

    /**
     * TODO
     * Main Player function.
     */
    public void run() {
        while (true) {
            JSONObject message = messageProvider.getMessage();
            if (message != null) {
                String header = String.valueOf(message.get("header"));
                if (header.equalsIgnoreCase("endcon")) {
                    disconnect();
                    return;
                } else if (header.equalsIgnoreCase("login")) {
                    handleLogin(message);
                } else if (header.equalsIgnoreCase("register")) {
                    handleRegister(message);
                } else if (header.equalsIgnoreCase("newpass")){
                    handleNewPass(message);
                } else if (header.equalsIgnoreCase("changepass")){
                    handleChangePass(message);
                } else if (header.equalsIgnoreCase("lcreate")) {
                    handleLCreate();
                } else if (header.equalsIgnoreCase("ljoin")) {
                    handleLJoin(message);
                } else if (header.equalsIgnoreCase("lleave")) {
                    handleLLeave();
                }
            }
            else {
                disconnect();
                break;
            }
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
    private void setPlayerId(int playerId) {
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
    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     *
     */
    private void disconnect() {
            up.disconnectPlayer(this);
        try {
            clientSocket.close();
            System.out.println("User " + getPlayerName() + " (" + getPlayerId() + ") logged off");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.out.println("Exception in Player/disconnect: " + e);
        }
    }

    // DB management

    /**
     * Checks for user in the database, compares passwords
     * Sends appropriate message to the client afterwards
     *
     * @param message String message which method uses
     */
    private void handleLogin(JSONObject message) {
        JSONObject response = new JSONObject();
        try {
            String uName = String.valueOf(message.get("uname"));
            String uPasswd = encryptionProvider.encrypt(String.valueOf(message.get("upass")));

            String query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("login", false);
                response.put("alert", "Wrong username!");
                messageProvider.sendMessage(response);
                return;
            }
            resultSet.next();
            if (resultSet.getString("password").equals(uPasswd)) {
                response.put("login", true);
                messageProvider.sendMessage(response);
                System.out.println("User " + uName + " (" + resultSet.getInt("user_id") + ") logged in");
                setPlayerName(uName);
                setPlayerId(resultSet.getInt("user_id"));
            } else {
                response.put("login", false);
                response.put("alert", "Wrong password!");
                messageProvider.sendMessage(response);
                System.out.println("User " + uName + " (" + resultSet.getInt("user_id") + ") failed to login \nReason: Wrong Password.");
            }
        } catch (ArrayIndexOutOfBoundsException e){
            response.put("login", false);
            response.put("alert", "Internal error");
            messageProvider.sendMessage(response);
        } catch (Exception e) {
            response.put("login", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(response);
        }
    }

    /**
     * Handles user registration process
     * Sends appropriate message to the client afterwards
     *
     * @param message String message which method uses
     */
    private void handleRegister(JSONObject message) {
        JSONObject response = new JSONObject();
        try {
            String uName = String.valueOf(message.get("uname"));
            String uPasswd = encryptionProvider.encrypt(String.valueOf(message.get("upass")));
            String backupPin = String.valueOf(message.get("backupPin"));
            String email = String.valueOf(message.get("email"));

            //Check if email already registered
            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                response.put("register", false);
                response.put("alert", "e-mail already registered!");
                messageProvider.sendMessage(response);
                return;
            }

            //Check for username availability
            query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            resultSet = databaseCommunicator.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                response.put("register", false);
                response.put("alert", "Username taken");
                messageProvider.sendMessage(response);
                return;
            }

            //Register user
            //Get new user id
            query = "SELECT user_id FROM accounts WHERE user_id = (SELECT max(user_id) FROM accounts)";
            resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("register", false);
                response.put("alert", "Internal database error");
                messageProvider.sendMessage(response);
                return;
            }
            //Insert user info into table
            resultSet.next();
            int userID = resultSet.getInt("user_id");
            userID++;
            query = "INSERT INTO accounts (user_id, nickname, password, backup_code, email) VALUES (" +
                    userID + ",'" + uName + "','" + uPasswd + "','" + backupPin + "','" + email + "')";
            databaseCommunicator.executeUpdate(query);
            response.put("register", true);
            messageProvider.sendMessage(response);

        } catch (Exception e) {
            response.put("register", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(response);
        }
    }

    private void handleNewPass(JSONObject message) {
        JSONObject response = new JSONObject();
        try {
            String uPasswd = encryptionProvider.encrypt(String.valueOf(message.get("upass")));
            String backupPin = String.valueOf(message.get("backupPin"));
            String email = String.valueOf(message.get("email"));

            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("newPass", false);
                response.put("alert", "e-mail not found!");
                messageProvider.sendMessage(response);
                return;
            }
            if (resultSet.getString("backup_code").equals(backupPin)) {
                query = "UPDATE accounts SET password='"+uPasswd+"' WHERE email='"+email+"'";
                databaseCommunicator.executeUpdate(query);
                response.put("newPass", true);
                response.put("alert", "e-mail not found!");
                messageProvider.sendMessage(response);
            } else {
                response.put("newPass", false);
                response.put("alert", "Wrong code!");
                messageProvider.sendMessage(response);
            }
        } catch (Exception e) {
            response.put("newPass", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(response);
        }
    }

    private void handleChangePass(JSONObject message) {
        JSONObject response = new JSONObject();
        try {
            String oldPassword = encryptionProvider.encrypt(String.valueOf(message.get("oldpass")));
            String newPassword = encryptionProvider.encrypt(String.valueOf(message.get("newpass")));
            String email = String.valueOf(message.get("email"));

            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("changepass", false);
                response.put("alert", "e-mail not found!");
                messageProvider.sendMessage(response);
                return;
            }
            if (resultSet.getString("password").equals(oldPassword)) {
                query = "UPDATE accounts SET password='"+newPassword+"' WHERE email='"+email+"'";
                databaseCommunicator.executeUpdate(query);
                response.put("changepass", true);
                messageProvider.sendMessage(response);
            } else {
                response.put("changepass", false);
                response.put("alert", "Wrong password!");
                messageProvider.sendMessage(response);
            }
        } catch (Exception e) {
            response.put("changepass", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(response);
        }
    }

    private void handleLCreate() {
        JSONObject response = new JSONObject();
        createLobby();
        response.put("lcreate", up.getLobbyEnterCode(getLobby()));
        messageProvider.sendMessage(response);
    }

    private void handleLJoin(JSONObject message) {
        JSONObject response = new JSONObject();
        int enterCode = (Integer) message.get("enterCode");
        if (joinLobby(enterCode)) {
            response.put("ljoin", true);
        } else {
            response.put("ljoin", false);
            response.put("alert", "Wrong code!");
        }
        messageProvider.sendMessage(response);
    }

    private void handleLLeave() {
        JSONObject response = new JSONObject();
        leaveLobby();
        response.put("lleave", true);
        messageProvider.sendMessage(response);
    }

    //Lobby management

    private void createLobby() {
        setLobby(up.createLobby(this));
    }

    private boolean joinLobby(int enterCode) {
         return up.addToLobby(enterCode, this);
    }

    void setLobby(Integer lobby) {
        this.lobby = lobby;
    }

    private Integer getLobby() {
        return lobby;
    }

    private void leaveLobby() {
        up.removeFromLobby(lobby, this);
        setLobby(null);
    }

    // TODO
    void updatePlayers(ArrayList<Player> players) {
        JSONObject message = new JSONObject();

//        for (Player player : players) {
//            message.(player.getPlayerName());
//        }
//        message.put("llist");
        message.put("alert", "Not implemented yet!");
        messageProvider.sendMessage(message);
    }
}
