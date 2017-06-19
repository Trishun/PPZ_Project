import org.json.simple.JSONObject;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;


/**
 * The type Game manager.
 */
class GameManager {

    private ArrayList<Checkpoint> checkpoints;
    private TeamManager teamManager;
    private Instant gameStarted;
    private Integer timeElapsed;
    private Timer timer;
    private int currentCheckpoint = -1;
    private int lastCheckpoint = -1;


    /**
     * Instantiates a new Game manager.
     *
     * @param teamManager the team manager
     */
    GameManager(TeamManager teamManager) {
        this.teamManager = teamManager;
        checkpoints = new ArrayList<>();
    }

    /**
     * Begin.
     */
    void begin() {
        teamManager.broadcastSimpleToAll("gbegin", "true");
        gameStarted = Instant.now();
    }

    private void end() {
        Long elapsed = Duration.between(gameStarted, Instant.now()).abs().toMinutes();
        timeElapsed = (elapsed.intValue());
        teamManager.broadcastSimpleToAll("gend", "true");
    }

    /**
     * Add checkpoint.
     *
     * @param location    the location
     * @param description the description
     */
    void addCheckpoint(double[] location, String description) {
        checkpoints.add(new Checkpoint(location, description));
        if (lastCheckpoint == -1) {
            currentCheckpoint++;
            sendCheckpointCoords();
        }
        lastCheckpoint++;
    }

    /**
     * Visit checkpoint.
     */
    void visitCheckpoint() {
        Checkpoint current = checkpoints.get(currentCheckpoint);
        HashMap<String, Object> message = new HashMap<>();
        message.put("desc", current.getTask().getDescription());
        teamManager.broadcastToTeam(1, new JSONObject(message));
        current.setTime_visited(Instant.now());
        current.setVisited();
        if (current.isLast()) {
            finishGame();
        }
    }

    /**
     * Resolve task.
     *
     * @param answer the answer
     */
    boolean resolveTask(String answer) {
        Checkpoint current = checkpoints.get(currentCheckpoint);
        current.getTask().setAnswer(answer);
        current.setTime_completed(Instant.now());
        current.setCompleted();
        currentCheckpoint++;
        try {
            checkpoints.get(currentCheckpoint);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get coords double [ ].
     *
     * @return the double [ ]
     */
    private double[] getCoords() {
        return checkpoints.get(currentCheckpoint).getLocation();
    }

    /**
     * Send checkpoint coords.
     */
    void sendCheckpointCoords() {
        HashMap<String, Object> response = new HashMap<>();
        double[] coords = getCoords();
        response.put("coords", true);
        response.put("locx", coords[0]);
        response.put("locy", coords[1]);
        teamManager.broadcastToTeam(1, new JSONObject(response));
    }

    /**
     * Gets tasks.
     *
     * @return the tasks
     */
    JSONObject getTasks() {
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
        return (new JSONObject(message));
    }

    /**
     * Verify task.
     *
     * @param checkpoint the checkpoint
     * @param value      the value
     */
    void verifyTask(int checkpoint, Boolean value) {
        checkpoints.get(checkpoint).setChecked(true);
        checkpoints.get(checkpoint).setCorrect(value);
    }

    /**
     * Force end.
     *
     * @param team   the team
     * @param reason the reason
     */
    void forceEnd(int team, String reason) {
        end();
        HashMap<String, Object> message = new HashMap<>();
        message.put("reason", reason);
        teamManager.broadcastToTeam(team, new JSONObject(message));
    }

    void finishGame() {
        end();
        countPoints();
    }

    private void countPoints() {
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
        points += (int) (1.2 * timeElapsed) - (int) Duration.between(gameStarted, checkpoints.get(checkpoints.size() - 1).getTime_created()).toMinutes();
        if (points > 0) {
            teamManager.broadcastSimpleToTeam(1, "result", "win");
            teamManager.broadcastSimpleToTeam(0, "result", "lose");
        } else if (points < 0) {
            teamManager.broadcastSimpleToTeam(1, "result", "lose");
            teamManager.broadcastSimpleToTeam(0, "result", "win");
        } else {
            teamManager.broadcastSimpleToAll("result", "draw");
        }
    }

}
