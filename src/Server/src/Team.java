import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Team main class
 *
 * @author PD
 */
class Team {

    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Add player.
     *
     * @param player the player
     */
    void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Has player boolean.
     *
     * @param player the player
     * @return the boolean
     */
    Boolean hasPlayer(Player player) {
        return players.contains(player);
    }

    /**
     * Remove player boolean.
     *
     * @param player the player
     * @return the boolean
     */
    Boolean removePlayer(Player player) {
        if (hasPlayer(player)){
            players.remove(player);
            return true;
        }
        return false;
    }

    /**
     * Number of players int.
     *
     * @return the int
     */
    int numberOfPlayers() {
        return players.size();
    }

    /**
     * Send message to players.
     *
     * @param message the message
     */
    void sendMessageToPlayers(JSONObject message) {
        for (Player player : players) {
            player.getMessageProvider().sendMessage(message);
        }
    }

    /**
     * Send simple message to players.
     *
     * @param header  the header
     * @param content the content
     */
    void sendSimpleMessageToPlayers(String header, String content) {
        for (Player player : players) {
            player.getMessageProvider().sendSimpleMessage(header, content);
        }
    }

}
