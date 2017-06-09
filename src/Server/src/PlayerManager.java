import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Player Management class
 * Created by PD on 24.04.2017.
 */
class PlayerManager {

    private static ArrayList<Player> playerList = new ArrayList<>();
    private DatabaseCommunicator databaseCommunicator;
    private LobbyManager lobbyManager;

    /**
     * Instantiates a new Player manager.
     *
     * @param lobbyManager         the lobby manager
     * @param databaseCommunicator the database communicator
     */
    PlayerManager(LobbyManager lobbyManager, DatabaseCommunicator databaseCommunicator) {
        this.lobbyManager = lobbyManager;
        this.databaseCommunicator = databaseCommunicator;
    }

    /**
     * Create player.
     *
     * @param socket the socket
     */
    void createPlayer(Socket socket) {
        Player player = new Player(socket, this, lobbyManager, databaseCommunicator);
        playerList.add(player);
        player.start();
    }

    /**
     * Disconnect player.
     *
     * @param playerToDisconnect the player to disconnect
     */
    void disconnectPlayer(Player playerToDisconnect) {
        try {
            playerList.remove(playerToDisconnect);
        } catch (ConcurrentModificationException ee) {
            Debug.Log("Exception 1 in PlayerManager/disconnectPlayer: " + ee);
        }
        try {
            if (playerToDisconnect.getLobby() != null)
                lobbyManager.removeFromLobby(playerToDisconnect);
        } catch (ConcurrentModificationException ee) {
            Debug.Log("Exception 2 in PlayerManager/disconnectPlayer: " + ee);
        }
        Debug.Log("Player " + playerToDisconnect.getPlayerName() + " (" + playerToDisconnect.getPlayerId() + ") disconnected");
    }

    /**
     * Gets player list.
     *
     * @return the player list
     */
    ArrayList<Player> getPlayerList() {
        return playerList;
    }

}
