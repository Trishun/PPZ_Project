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
        timeElepsed = (elapsed.intValue());
    }

    void addCheckpoint(double[] location, String description) {
        checkpoints.add(new Checkpoint(location, description));
    }

    void visitCheckpoint() {
        Checkpoint current = checkpoints.get(thisCheckpoint);
        HashMap<String, Object> message = new HashMap<>();
        message.put("desc", current.getTask().getDescription());
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

    void getTasks(Player player) {
        HashMap<String, Object> message = new HashMap<>();
        ArrayList<JSONObject> messageContent = new ArrayList<>();
        HashMap<String, Object> task;
        for (Checkpoint checkpoint : checkpoints) {
            task = new HashMap<>();
            task.put("tasknum", String.valueOf(checkpoints.indexOf(checkpoint)));
            task.put("desc", checkpoint.getTask().getDescription());
            task.put("ans", checkpoint.getTask().getAnswer());
            task.put("checked", checkpoint.isChecked());
            task.put("correct", checkpoint.isCorrect());
            messageContent.add(new JSONObject(task));
        }
        message.put("tasks", messageContent);
        player.getMessageProvider().sendMessage(new JSONObject(message));
    }

    void verifyTask(int checkpoint, Boolean value) {
        checkpoints.get(checkpoint).setChecked(true);
        checkpoints.get(checkpoint).setCorrect(value);
    }

    void submitTasks() {

    }

    void countPoints() {
        int points = 0;
        for (Checkpoint checkpoint : checkpoints) {
            if (checkpoint.isVisited()) {
                if (checkpoint.isCompleted() && checkpoint.isCorrect()) {
                    points += 10;
                    points -= (int) Duration.between(checkpoint.getTime_visited(), checkpoint.getTime_completed()).abs().toMinutes();
                }
            } else {
                points -= 20;
            }
        }
        points += (int) (1.2 * timeElepsed) - (int) Duration.between(gameStarted, checkpoints.get(checkpoints.size() - 1).getTime_created()).toMinutes();
        if (points > 0) {
//            teamManager.broadcastToTeam(1, "win");
        }
    }

}
