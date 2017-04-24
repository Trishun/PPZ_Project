/**
 * Team manager Class
 * Created by PD on 24.04.2017.
 */
class TeamManager {

    private Team escapeTeam = new Team(0);
    private Team pursuitTeam = new Team(1);

    void addPlayerToTeam(Player player, int team) {
        switch (team) {
            case 0:
                escapeTeam.addPlayer(player);
                break;
            case 1:
                pursuitTeam.addPlayer(player);
                break;
        }
    }

    void removePlayer(Player player) {

    }

}
