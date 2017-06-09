import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lobby class
 *
 * @author PD
 */
class Lobby {
    private int id;
    private Player initiator;
    private ArrayList<Player> players = new ArrayList<>();
    private int enterCode;
    private TeamManager teamManager = new TeamManager();
    private GameManager gameManager = new GameManager(teamManager);

    /**
     * Instantiates a new Lobby.
     *
     * @param id        the id
     * @param initiator the initiator
     */
    Lobby(int id, Player initiator) {
        this.id = id;
        this.initiator = initiator;
        enterCode = enterCodeGeneration();
        players.add(initiator);
    }

    private int enterCodeGeneration() {
        return initiator.hashCode() / 10000;
    }

    /**
     * Gets enter code.
     *
     * @return the enter code
     */
    int getEnterCode() {
        return enterCode;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    int getId() {
        return id;
    }

    /**
     * Add to lobby.
     *
     * @param newPlayer the new player
     */
    void addToLobby(Player newPlayer) {
        players.add(newPlayer);
        broadcastLobbyStructure();
    }

    /**
     * Remove from lobby boolean.
     *
     * @param player the player
     * @return the boolean
     */
    Boolean removeFromLobby(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            return true;
        }
        return false;
    }

    /**
     * Gets initiator.
     *
     * @return the initiator
     */
    Player getInitiator() {
        return initiator;
    }

    /**
     * Close.
     */
    void close() {
        Thread.currentThread().interrupt();
    }

    private ArrayList<Player> getPlayers() {
        return players;
    }

    private HashMap<String, Object> setLobbyStructure() {
        ArrayList<String> team0 = new ArrayList<>();
        ArrayList<String> team1 = new ArrayList<>();
        ArrayList<String> lobby = new ArrayList<>();

        for (Player player : players) {
            if (teamManager.getTeam(0).hasPlayer(player))
                team0.add(player.getPlayerName());
            else if (teamManager.getTeam(1).hasPlayer(player))
                team1.add(player.getPlayerName());
            else
                lobby.add(player.getPlayerName());
        }
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        output.add(team0);
        output.add(team1);
        output.add(lobby);

        HashMap<String, Object> message = new HashMap<>();
        message.put("llist", output);
        message.put("initiator", initiator.getPlayerName());
        return message;
    }

    /**
     * Broadcast lobby structure.
     */
    void broadcastLobbyStructure() {
        HashMap<String, Object> lobbyStructure = setLobbyStructure();
        for (Player player : players) {
            player.updatePlayers(lobbyStructure);
        }
    }

    /**
     * Gets player count.
     *
     * @return the player count
     */
    Integer getPlayerCount() {
        return players.size();
    }

    /**
     * Gets team manager.
     *
     * @return the team manager
     */
    TeamManager getTeamManager() {
        return teamManager;
    }

    /**
     * Gets game manager.
     *
     * @return the game manager
     */
    GameManager getGameManager() {
        return gameManager;
    }

    /**
     * Begin game.
     */
    void beginGame() {
        if (teamManager.teamsReady(getPlayers()))
            gameManager.begin();
    }

}
