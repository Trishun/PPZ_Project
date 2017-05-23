import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Team manager Class
 * @author PD
 */
class TeamManager {

    private Team escapeTeam = new Team();
    private Team pursuitTeam = new Team();

    /**
     * Checks bigger team
     *
     * @param team Team code
     * @return True desired team has less or equal num of players, otherwise false.
     */
    private Boolean checkPossibility(int team) {
        return (getTeam(team).numberOfPlayers() <= getTeam(Math.abs(1 - team)).numberOfPlayers());
    }

    /**
     * Add player to team
     *
     * @param player Player instance
     * @param team   Team code
     */
    void addPlayerToTeam(Player player, int team) {
        if (checkPossibility(team)) {
            addPlayerToTeam(player, getTeam(team));
        }
    }

    /**
     * Add player to team
     *
     * @param player Player instance
     * @param team   Team instance
     */
    private void addPlayerToTeam(Player player, Team team) {
        team.addPlayer(player);
    }

    /**
     * Remove player from team
     *
     * @param player Player instance
     */
    void removePlayer(Player player) {
        if (!escapeTeam.removePlayer(player))
            pursuitTeam.removePlayer(player);
    }

    /**
     * Get team instance basen on team code
     *
     * @param team Team code
     * @return Team instance
     */
    @Nullable
    @Contract(pure = true)
    Team getTeam(int team) {
        switch (team) {
            case 0:
                return escapeTeam;
            case 1:
                return pursuitTeam;
        }
        return null;
    }

    /**
     * Auto division of remaining players
     *
     * @param players Player instance list
     * @return true after auto division
     */
    boolean teamsReady(ArrayList<Player> players) {
        for (Player player : players) {
            if (!escapeTeam.hasPlayer(player) && !pursuitTeam.hasPlayer(player)) {
                if (checkPossibility(0)) {
                    addPlayerToTeam(player, getTeam(1));
                } else {
                    addPlayerToTeam(player, getTeam(0));
                }
            }
        }
        return true;
    }

    void broadcastToTeam(int team, JSONObject message) {
        getTeam(team).sendMessageToPlayers(message);
    }

    void broadcastToAll(JSONObject message) {
        broadcastToTeam(0, message);
        broadcastToTeam(1, message);
    }

}
