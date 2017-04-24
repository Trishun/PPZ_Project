import java.util.ArrayList;
import java.util.Random;

/**
 * Lobby class
 * Created by PD on 19.04.2017.
 */

class Lobby {
    private int id;
    private Player initiator;
    private ArrayList<Player> players = new ArrayList<>();
    private int enterCode;
    private Random generator;
    private TeamManager teamManager = new TeamManager();

    Lobby(int id, Player initiator) {
        generator = new Random();
        this.id = id;
        this.initiator = initiator;
        enterCode = enterCodeGeneration();
        players.add(initiator);
    }

    private int enterCodeGeneration() {
        return (int) (generator.nextDouble()*initiator.hashCode())/1000;
    }

    int getEnterCode() {
        return enterCode;
    }

    int getId() {
        return id;
    }

    void addToLobby(Player newPlayer) {
        players.add(newPlayer);
        for (Player player: players) {
            player.updatePlayers(players);
        }
    }

    void removeFromLobby(Player player) {
        players.remove(player);
        for (Player player1: players) {
            player1.updatePlayers(players);
        }
    }

    ArrayList<Player> getPlayers() {
        return players;
    }

    TeamManager getTeamManager() {
        return teamManager;
    }
}
