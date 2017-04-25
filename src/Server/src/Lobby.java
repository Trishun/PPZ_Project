import java.util.ArrayList;

/**
 * Lobby class
 * Created by PD on 24.04.2017.
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
        for (Player player : players) {
            player.updatePlayers(players);
        }
    }

    Boolean removeFromLobby(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            return true;
        }
        return false;
    }

    Player getInitiator() {
        return initiator;
    }

    ArrayList<Player> getPlayers() {
        return players;
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
