package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class TaskHandler extends Thread{

    private BufferedReader socketReader;
    private PrintWriter socketWriter;
    private DatabaseCommunicator databaseCommunicator;

    /**
     * Class constructor
     * @param clientSocket Socket to be handled.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     * @throws IOException
     */
    TaskHandler(Socket clientSocket, DatabaseCommunicator databaseCommunicator) throws IOException {
        this.databaseCommunicator = databaseCommunicator;
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
    }

    /** TODO
     * Main TH function.
     */
    public void run() {

    }

}
