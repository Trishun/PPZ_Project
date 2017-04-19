import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Message provider
 * Created by PD on 10.04.2017.
 */
class MessageProvider{

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

    Message getMessage() {
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

    void sendMessage(Message message) {
        String data = String.valueOf(message.getContent());
        socketWriter.println(message.getHeader()+'&'+data);
        socketWriter.flush();
    }

    Message processMessage(String data) {

//        Diagnostics
//        System.out.println("Message received: " + data);
        Message message = new Message();
        String splittedData[] = data.split("&");
        message.setHeader(splittedData[0]);
        switch (message.getHeader()){
            case "boolean":
                message.setBoolContent(Boolean.valueOf(splittedData[1]));
                break;
            case "number":
                message.setNumberContent(Integer.valueOf(splittedData[1]));
                break;
            case "ljoin":
                message.setNumberContent(Integer.valueOf(splittedData[1]));
                break;
            case "coordinates":
                String values[] = splittedData[1].split("%");
                ArrayList<Float> al = new ArrayList<>();
                al.add(Float.valueOf(values[0]));
                al.add(Float.valueOf(values[1]));
                message.setCoordinatesContent(al);
                break;
            default:
                try {
                    message.setStringContent(splittedData[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    message.setStringContent(null);
                }
        }
        return message;
    }
}
