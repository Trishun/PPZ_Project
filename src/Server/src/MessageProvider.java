import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Message provider
 * Created by PD on 10.04.2017.
 */
class MessageProvider {

    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    /**
     * Instantiates a new Message provider.
     *
     * @param clientSocket the client socket
     */
    MessageProvider(Socket clientSocket) {
        try {
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            Debug.Log("Exception in MessageProvider/constructor: " + e);
        }
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    JSONObject getMessage() {
        try {
            String data = socketReader.readLine();
            Debug.Log("Message got: " + data);
            return processMessage(data);
        } catch (SocketException e) {
            Debug.Log("Connention Closed");
        } catch (Exception e) {
            Debug.Log("Exception in MessageProvider/getMessage: " + e);
        }
        return null;
    }

    /**
     * Send message.
     *
     * @param message the message
     */
    void sendMessage(JSONObject message) {
        try {
            String jsonString = message.toJSONString();
            Debug.Log("Message sent: " + jsonString);
            socketWriter.println(jsonString);
            socketWriter.flush();
        } catch (Exception e) {
            Debug.Log("Exception in MessageProvider/sendMessage: " + e);
        }
    }

    /**
     * Send simple message.
     *
     * @param header  the header
     * @param content the content
     */
    void sendSimpleMessage(String header, Object content) {
        HashMap<String, Object> message = new HashMap<>();
        message.put(header, content);
        sendMessage(new JSONObject(message));
    }

    private JSONObject processMessage(String data) {
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject)parser.parse(data);
        } catch (Exception e) {
            Debug.Log("Exception in MessageProvider/processMessage: " + e);
        }
        return null;
    }
}
