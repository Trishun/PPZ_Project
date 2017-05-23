import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Team main class
 * @author PD
 */
class Team {

    private ArrayList<Player> players = new ArrayList<>();

    void addPlayer(Player player) {
        players.add(player);
    }

    Boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    Boolean removePlayer(Player player) {
        if (hasPlayer(player)){
            players.remove(player);
            return true;
        }
        return false;
    }

    int numberOfPlayers() {
        return players.size();
    }

    void sendMessageToPlayers(JSONObject message) {
        for (Player player : players) {
            player.getMessageProvider().sendMessage(message);
        }
    }

}
