package pl.locationbasedgame;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Patryk Ligenza on 11-Apr-17.
 */

class SocketHandler {

    private String TAG = "SOCKET";
    private Socket socket;

    SocketHandler() {
        try {
            socket = new Socket(ConnectionConfig.IP, ConnectionConfig.PORT);
            Log.i(TAG, "SOCKET CONNECTED");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "CONNECTING FAILURE");
        }
    }

    /**
     * Push data string into stream and returns
     * response for further processing.
     *
     * @param data String to send
     */
     String sendMessageAndGetResponse(String data) {
        if (socket != null) {
            try {
                send(data);
                String response = receive();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "EXCHANGING DATA FAILURE");
            }
        }
        return null;
    }

    private void send(String data) throws IOException {
        Log.i("REQUEST", data);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(data);
        writer.flush();
    }

    private String receive() throws IOException {
        InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(inputStream);
        String response = reader.readLine();
        Log.i("RESPONSE", response);
        return response;
    }

    void closeSocket() {
        try {
            socket.close();
            Log.i(TAG, "SOCKET CLOSED");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "CLOSURE ERROR");
        }
    }
}
