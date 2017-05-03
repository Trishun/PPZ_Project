import org.json.simple.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

/**
 * Game management class
 * Created by PD on 25.04.2017.
 */
class GameManager {

    private ArrayList<Checkpoint> checkpoints;
    private TeamManager teamManager;
    private Instant gameStarted;
    private Integer timeElepsed;
    private Timer timer;
    private int thisCheckpoint = -1;
    private int nextCheckpoint = -1;


    GameManager(TeamManager teamManager) {
        this.teamManager = teamManager;
        checkpoints = new ArrayList<>();
    }

    void begin() {
        HashMap<String, Object> message = new HashMap<>();
        message.put("gbegin", true);
        teamManager.broadcastToAll(new JSONObject(message));
        gameStarted = Instant.now();
    }

    void end() {
        Long elapsed = Duration.between(gameStarted, Instant.now()).abs().toMinutes();
        timeElepsed = (Integer.valueOf(elapsed.intValue()));

    }

    void addCheckpoint(double[] location, String description) {
        checkpoints.add(new Checkpoint(location, description));
    }

    void visitCheckpoint() {
        Checkpoint current = checkpoints.get(thisCheckpoint);
        HashMap<String, Object> message = new HashMap<>();
        message.put("cmessage", current.getTask().getDescription());
        teamManager.broadcastToTeam(1, new JSONObject(message));
        current.setTime_visited(Instant.now());
        current.setVisited();
    }

    void resolveTask(int checkpoint, String answer) {
        Checkpoint current = checkpoints.get(checkpoint);
        current.getTask().setAnswer(answer);
        current.setTime_completed(Instant.now());
        current.setCompleted();
    }

    void getTasks() {

    }

    void verifyTask(int checkpoint) {

    }

    void submitTasks () {

    }

    void countPoints() {

    }

}
