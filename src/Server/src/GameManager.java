import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game management class
 * Created by PD on 25.04.2017.
 */
class GameManager {

    private ArrayList<Checkpoint> checkpoints;
    private TeamManager teamManager;
    private Instant gameStarted;
    private Timer timer;
    private int nextCheckpoint = -1;


    GameManager(TeamManager teamManager) {
        this.teamManager = teamManager;
        checkpoints = new ArrayList<>();
    }

    private void tenMinuteReminder() {
        timer = new Timer();
        timer.schedule(new RemindTask(1, "One minute left!"), 9*60000);
        timer.schedule(new RemindTask(1, "Time's up!"), 10*60000);
    }

    class RemindTask extends TimerTask {
        int team;
        String message;
        RemindTask(int team, String message) {
            this.message = message;
            this.team = team;
        }
        public void run() {
            teamManager.broadcastToTeam(team, message);
        }
    }

    void begin() {
        gameStarted = Instant.now();
        tenMinuteReminder();
    }

    void addCheckpoint(double[] location, String description) {
        checkpoints.add(new Checkpoint(location, description));
    }


}
