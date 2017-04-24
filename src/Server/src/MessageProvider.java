import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

//import org.json.JSONObject;

/**
 * Message provider
 * Created by PD on 10.04.2017.
 */
class MessageProvider {

    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    MessageProvider(Socket clientSocket) {
        try {
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Exception in MessageProvider/constructor: " + e);
        }
    }

    JSONObject getMessage() {
        try {
            String data = socketReader.readLine();
            return processMessage(data);
        } catch (SocketException e) {
            System.out.println("Connention Closed");
        } catch (Exception e) {
            System.out.println("Exception in MessageProvider/getMessage: " + e);
        }
        return null;
    }

    void sendMessage(JSONObject message) {
        try {
            message.writeJSONString(socketWriter);
        } catch (IOException e) {
            System.out.println("Exception in MessageProvider/sendMessage: " + e);
        }
    }

    private JSONObject processMessage(String data) {

//        Diagnostics
//        System.out.println("Message received: " + data);
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject)parser.parse(data);
        } catch (Exception e) {
            System.out.println("Exception in MessageProvider/processMessage: " + e);
        }
        return null;
    }
}
