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

    ArrayList<Player> getPlayers() {
        return players;
    }
}
