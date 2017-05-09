import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lobby class
 * @author PD
 */

class Lobby {
    private int id;
    private Player initiator;
    private ArrayList<Player> players = new ArrayList<>();
    private int enterCode;
    private TeamManager teamManager = new TeamManager();
    private GameManager gameManager = new GameManager(teamManager);

    Lobby(int id, Player initiator) {
        this.id = id;
        this.initiator = initiator;
        enterCode = enterCodeGeneration();
        players.add(initiator);
    }

    private int enterCodeGeneration() {
        return initiator.hashCode() / 10000;
    }

    int getEnterCode() {
        return enterCode;
    }

    int getId() {
        return id;
    }

    void addToLobby(Player newPlayer) {
        players.add(newPlayer);
        broadcastLobbyStructure();
    }

    Boolean removeFromLobby(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            broadcastLobbyStructure();
            return true;
        }
        return false;
    }

    Player getInitiator() {
        return initiator;
    }

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
        message.put("initiator", initiator);
        return message;
    }

    private void broadcastLobbyStructure() {
        HashMap<String, Object> lobbyStructure = setLobbyStructure();
        for (Player player : players) {
            player.updatePlayers(lobbyStructure);
        }
    }

    Integer getPlayerCount() {
        return players.size();
    }

    TeamManager getTeamManager() {
        return teamManager;
    }

    GameManager getGameManager() {
        return gameManager;
    }

    void beginGame() {
        if (teamManager.teamsReady(getPlayers()))
            gameManager.begin();
    }

}
