import java.util.ArrayList;

/**
 * Team main class
 * Created by PD on 19.04.2017.
 */
class Team {

    private int role;
    private ArrayList<Player> players = new ArrayList<>();

    Team (int role) {
        this.role = role;
    }

    void addPlayer(Player player) {

    }

    void removePlayer(Player player) {

    }

    int numberOfPlayers() {
        return players.size();
    }


}
