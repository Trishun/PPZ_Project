/**
 * Task class
 * Created by PD on 01.05.2017.
 */
class Task {

    private String description;
    private String answer;

    Task(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
