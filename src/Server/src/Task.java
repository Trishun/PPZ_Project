/**
 * Task class
 * Created by PD on 01.05.2017.
 */
class Task {

    private String description;
    private String answer = null;

    /**
     * Instantiates a new Task.
     *
     * @param description the description
     */
    Task(String description) {
        this.description = description;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    String getDescription() {
        return description;
    }

    /**
     * Gets answer.
     *
     * @return the answer
     */
    public String getAnswer() {
        if (answer != null)
            return answer;
        return "Not answered yet";
    }

    /**
     * Sets answer.
     *
     * @param answer the answer
     */
    void setAnswer(String answer) {
        this.answer = answer;
    }

}
