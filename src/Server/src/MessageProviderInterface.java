import java.util.ArrayList;

/**
 *
 * Created by PD on 10.04.2017.
 */
public interface MessageProviderInterface {

    Message getMessage();
    boolean sendPoll();
    boolean replyPoll();
    boolean sendMessage(Message message);
    Message processMessage(String data);
}
