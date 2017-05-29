import java.time.Instant;

/**
 * Checkpoint class
 * Created by PD on 22.04.2017.
 */
class Checkpoint {

    private Task task;
    private double[] location;
    private Instant time_created;
    private Instant time_visited;
    private Instant time_completed;
    private boolean visited = false;
    private boolean completed = false;
    private boolean checked = false;
    private Boolean correct = null;
    private Boolean last;

    /**
     * Instantiates a new Checkpoint.
     *
     * @param location    the location
     * @param description the description
     */
    Checkpoint(double[] location, String description) {
        this.location = location;
        task = new Task(description);
        time_created = Instant.now();
        if (description.equalsIgnoreCase("last"))
            last = true;
    }

    /**
     * Get location double [ ].
     *
     * @return the double [ ]
     */
    double[] getLocation() {
        return location;
    }

    /**
     * Gets task.
     *
     * @return the task
     */
    Task getTask() {
        return task;
    }

    /**
     * Gets time created.
     *
     * @return the time created
     */
    Instant getTime_created() {
        return time_created;
    }

    /**
     * Sets time visited.
     *
     * @param time_visited the time visited
     */
    void setTime_visited(Instant time_visited) {
        this.time_visited = time_visited;
    }

    /**
     * Gets time visited.
     *
     * @return the time visited
     */
    Instant getTime_visited() {
        return time_visited;
    }

    /**
     * Gets time completed.
     *
     * @return the time completed
     */
    Instant getTime_completed() {
        return time_completed;
    }

    /**
     * Sets time completed.
     *
     * @param time_completed the time completed
     */
    void setTime_completed(Instant time_completed) {
        this.time_completed = time_completed;
    }

    /**
     * Is visited boolean.
     *
     * @return the boolean
     */
    boolean isVisited() {
        return visited;
    }

    /**
     * Sets visited.
     */
    void setVisited() {
        this.visited = true;
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    boolean isCompleted() {
        return completed;
    }

    /**
     * Sets completed.
     */
    void setCompleted() {
        this.completed = true;
    }

    /**
     * Is correct boolean.
     *
     * @return the boolean
     */
    boolean isCorrect() {
        return correct;
    }

    /**
     * Sets correct.
     *
     * @param correct the correct
     */
    void setCorrect(boolean correct) {
        this.correct = correct;
    }

    /**
     * Sets checked.
     *
     * @param checked the checked
     */
    void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * Is checked boolean.
     *
     * @return the boolean
     */
    Boolean isChecked() {
        return checked;
    }

    /**
     * Sets last.
     *
     * @param last the last
     */
    void setLast(Boolean last) {
        this.last = last;
    }

    /**
     * Is last boolean.
     *
     * @return the boolean
     */
    Boolean isLast() {
        return last;
    }
}
