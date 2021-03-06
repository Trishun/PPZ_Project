import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Thread (player) handing class
 * Created by PD on 05.04.2017.
 */
public class Player extends Thread {

    private Socket clientSocket;
    private DatabaseCommunicator databaseCommunicator;
    private PlayerManager playerManager;
    private LobbyManager lobbyManager;
    private Lobby lobby;
    private MessageProvider messageProvider;
    private LocaleProvider localeProvider;
    private EncryptionProvider encryptionProvider;
    private int playerId = 0;
    private String playerName = "not logged";

    /**
     * Instantiates a new Player.
     *
     * @param socket               the socket
     * @param playerManager        the player manager
     * @param lobbyManager         the lobby manager
     * @param databaseCommunicator the database communicator
     */
    Player(Socket socket, PlayerManager playerManager, LobbyManager lobbyManager, DatabaseCommunicator databaseCommunicator) {
        this.clientSocket = socket;
        this.playerManager = playerManager;
        this.lobbyManager = lobbyManager;
        this.databaseCommunicator = databaseCommunicator;
        this.messageProvider = new MessageProvider(socket);
        this.encryptionProvider = new EncryptionProvider();
        localeProvider = new LocaleProvider(databaseCommunicator.getSettingsProvider());
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
                } else if (header.equalsIgnoreCase("login")) {          //login
                    localeProvider.setLocale(String.valueOf(message.get("locale")));
                    handleLogin(message);
                } else if (header.equalsIgnoreCase("register")) {       //register
                    localeProvider.setLocale(String.valueOf(message.get("locale")));
                    handleRegister(message);
                } else if (header.equalsIgnoreCase("newpass")) {        //passward forgotten
                    localeProvider.setLocale(String.valueOf(message.get("locale")));
                    handleNewPass(message);
                } else if (header.equalsIgnoreCase("changepass")) {     //new password
                    localeProvider.setLocale(String.valueOf(message.get("locale")));
                    handleChangePass(message);
                } else if (header.equalsIgnoreCase("lcreate")) {        //create lobbyId
                    handleLCreate();
                } else if (header.equalsIgnoreCase("ljoin")) {          //join lobbyId
                    handleLJoin(message);
                } else if (header.equalsIgnoreCase("lleave")) {         //leave lobbyId
                    handleLLeave();
                } else if (header.equalsIgnoreCase("tjoin")) {          //join team
                    handleTJoin(message);
                } else if (header.equalsIgnoreCase("tleave")) {         //leave team
                    handleTLeave();
                } else if (header.equalsIgnoreCase("gbegin")) {         //begin game
                    handleGbegin();
                } else if (header.equalsIgnoreCase("ccreate")) {        //create checkpoint
                    handleCCreate(message);
                } else if (header.equalsIgnoreCase("cdesc")) {          //get task description
                    handleCVisit();
                } else if (header.equalsIgnoreCase("csans")) {          //set task answer
                    handleCSAns(message);
                } else if (header.equalsIgnoreCase("cvans")) {          //verify task answer
                    handleCVAns(message);
                } else if (header.equalsIgnoreCase("cgtask")) {         //get all tasks info
                    handleCGTasks();
                } else if (header.equalsIgnoreCase("stats")) {          //player stats
                    handleStats(message);
                }
            } else {
                disconnect();
                break;
            }
        }
    }

    // Player management

    /**
     * Gets player id.
     *
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
     * Gets player name.
     *
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
     * Gets message provider.
     *
     * @return the message provider
     */
    MessageProvider getMessageProvider() {
        return messageProvider;
    }

    /**
     * Disconnect management
     */
    private void disconnect() {
        playerManager.disconnectPlayer(this);
        try {
            clientSocket.close();
            Debug.Log("User " + getPlayerName() + " (" + getPlayerId() + ") logged off");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            Debug.Log("Exception in Player/disconnect: " + e);
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
        HashMap<String, Object> response = new HashMap<>();
        try {
            String uName = String.valueOf(message.get("uname"));
            String uPasswd = encryptionProvider.encrypt(String.valueOf(message.get("upass")));

            String query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("login", false);
                response.put("alert", localeProvider.get("wrongUsername"));
                messageProvider.sendMessage(new JSONObject(response));
                return;
            }
            for (Player player: playerManager.getPlayerList()) {
                if (player.getName().equals(uName)) {
                    response.put("login", false);
                    response.put("alert", localeProvider.get("alreadyLogged"));
                    messageProvider.sendMessage(new JSONObject(response));
                }
            }
            resultSet.next();
            if (resultSet.getString("password").equals(uPasswd)) {
                response.put("login", true);
                messageProvider.sendMessage(new JSONObject(response));
                Debug.Log("User " + uName + " (" + resultSet.getInt("user_id") + ") logged in");
                setPlayerName(uName);
                setPlayerId(resultSet.getInt("user_id"));
            } else {
                response.put("login", false);
                response.put("alert", localeProvider.get("wrongPassword"));
                messageProvider.sendMessage(new JSONObject(response));
                Debug.Log("User " + uName + " (" + resultSet.getInt("user_id") + ") failed to login \nReason: Wrong Password.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            response.put("login", false);
            response.put("alert", localeProvider.get("internalError"));
            messageProvider.sendMessage(new JSONObject(response));
        } catch (Exception e) {
            response.put("login", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(new JSONObject(response));
        }
    }

    /**
     * Handles user registration process
     * Sends appropriate message to the client afterwards
     *
     * @param message String message which method uses
     */
    private void handleRegister(JSONObject message) {
        HashMap<String, Object> response = new HashMap<>();

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
                response.put("alert", localeProvider.get("emailExists"));
                messageProvider.sendMessage(new JSONObject(response));
                return;
            }

            //Check for username availability
            query = "SELECT * FROM accounts WHERE nickname = '" + uName + "'";
            resultSet = databaseCommunicator.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                response.put("register", false);
                response.put("alert", localeProvider.get("usernameTaken"));
                messageProvider.sendMessage(new JSONObject(response));
                return;
            }

            //Register user
            //Get new user id
            query = "SELECT user_id FROM accounts WHERE user_id = (SELECT max(user_id) FROM accounts)";
            resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("register", false);
                response.put("alert", localeProvider.get("internalError"));
                messageProvider.sendMessage(new JSONObject(response));
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
            messageProvider.sendMessage(new JSONObject(response));

        } catch (Exception e) {
            response.put("register", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(new JSONObject(response));
        }
    }

    private void handleNewPass(JSONObject message) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            String uPasswd = encryptionProvider.encrypt(String.valueOf(message.get("upass")));
            String backupPin = String.valueOf(message.get("backupPin"));
            String email = String.valueOf(message.get("email"));

            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("newPass", false);
                response.put("alert", localeProvider.get("emailNotFound"));
                messageProvider.sendMessage(new JSONObject(response));
                return;
            }
            if (resultSet.getString("backup_code").equals(backupPin)) {
                query = "UPDATE accounts SET password='" + uPasswd + "' WHERE email='" + email + "'";
                databaseCommunicator.executeUpdate(query);
                response.put("newPass", true);
                response.put("alert", localeProvider.get("changed"));
                messageProvider.sendMessage(new JSONObject(response));
            } else {
                response.put("newPass", false);
                response.put("alert", localeProvider.get("wrongCode"));
                messageProvider.sendMessage(new JSONObject(response));
            }
        } catch (Exception e) {
            response.put("newPass", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(new JSONObject(response));
        }
    }

    /**
     * @param message
     */
    private void handleChangePass(JSONObject message) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            String oldPassword = encryptionProvider.encrypt(String.valueOf(message.get("oldpass")));
            String newPassword = encryptionProvider.encrypt(String.valueOf(message.get("newpass")));
            String email = String.valueOf(message.get("email"));

            String query = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = databaseCommunicator.executeQuery(query);
            if (!resultSet.isBeforeFirst()) {
                response.put("changepass", false);
                response.put("alert", localeProvider.get("emailNotFound"));
                messageProvider.sendMessage(new JSONObject(response));
                return;
            }
            if (resultSet.getString("password").equals(oldPassword)) {
                query = "UPDATE accounts SET password='" + newPassword + "' WHERE email='" + email + "'";
                databaseCommunicator.executeUpdate(query);
                response.put("changepass", true);
                messageProvider.sendMessage(new JSONObject(response));
            } else {
                response.put("changepass", false);
                response.put("alert", localeProvider.get("wrongPassword"));
                messageProvider.sendMessage(new JSONObject(response));
            }
        } catch (Exception e) {
            response.put("changepass", false);
            response.put("alert", String.valueOf(e));
            messageProvider.sendMessage(new JSONObject(response));
        }
    }

    //Lobby management

    private void handleLCreate() {
        setLobby(lobbyManager.createLobby(this));
        messageProvider.sendSimpleMessage("lcreate", lobbyManager.getLobbyEnterCode(lobby.getId()));
        lobby.broadcastLobbyStructure();
    }

    private void handleLJoin(JSONObject message) {
        HashMap<String, Object> response = new HashMap<>();
        Integer enterCode = Integer.valueOf((String) message.get("enterCode"));
        if (lobbyManager.addToLobby(enterCode, this)) {
            response.put("ljoin", true);
        } else {
            response.put("ljoin", false);
            response.put("alert", localeProvider.get("wrongCode"));
        }
        messageProvider.sendMessage(new JSONObject(response));
    }

    private void handleLLeave() {
        lobbyManager.removeFromLobby(this);
        setLobby(null);
        messageProvider.sendSimpleMessage("lleave", true);
    }

    /**
     * Sets lobby.
     *
     * @param lobby the lobby
     */
    void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Gets lobby.
     *
     * @return the lobby
     */
    Lobby getLobby() {
        return lobby;
    }

    /**
     * Update players.
     *
     * @param message the message
     */
    void updatePlayers(HashMap<String, Object> message) {
        messageProvider.sendMessage(new JSONObject(message));
    }

    // Team management
    private void handleTJoin(JSONObject message) {
        int team = Integer.valueOf(message.get("team").toString());
        lobby.getTeamManager().addPlayerToTeam(this, team);
        lobby.broadcastLobbyStructure();
        messageProvider.sendSimpleMessage("tjoin", true);
    }

    private void handleTLeave() {
        lobby.getTeamManager().removePlayer(this);
        lobby.broadcastLobbyStructure();
        messageProvider.sendSimpleMessage("tleave", true);
    }

    private void handleGbegin() {
        if (lobby.getInitiator() == this)
            lobby.beginGame();
    }

    private void handleCCreate(JSONObject message) {
        double[] location = {Double.valueOf(message.get("locx").toString()), Double.valueOf(message.get("locy").toString())};
        String description = String.valueOf(message.get("desc"));
        lobby.getGameManager().addCheckpoint(location, description);
    }

    private void handleCVisit() {
        lobby.getGameManager().visitCheckpoint();
    }

    private void handleCSAns(JSONObject message) {
        GameManager gm = lobby.getGameManager();
        gm.resolveTask(String.valueOf(message.get("ans")));
        gm.sendCheckpointCoords();
    }

    private void handleCVAns(JSONObject message) {
        lobby.getGameManager().verifyTask(Integer.valueOf(String.valueOf(message.get("cnum"))), Boolean.valueOf(String.valueOf(message.get("var"))));
    }

    private void handleCGTasks() {
        messageProvider.sendMessage(lobby.getGameManager().getTasks());
    }

    private void handleStats(JSONObject message) {

    }
}