/**
 * Task class
 * Created by PD on 01.05.2017.
 */
class Task {

    private String description;
    private String answer = null;

    Task(String description) {
        this.description = description;
    }

    String getDescription() {
        return description;
    }

    public String getAnswer() {
        if (answer != null)
            return answer;
        return "Not answered yet";
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
