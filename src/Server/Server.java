package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Server {

    private static ArrayList<TaskHandler> taskHandlerList = new ArrayList<TaskHandler>();
    private DatabaseCommunicator databaseCommunicator;

    /**
     * Class constructor.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     */
    Server(DatabaseCommunicator databaseCommunicator) {
        this.databaseCommunicator = databaseCommunicator;
    }

    void Run() {
        new Thread(() -> mainTask()).start();
    }

    void mainTask() {
        try {
            ServerSocket serverSocket = new ServerSocket(80);

            while (true) {
                //Accept connection
                System.out.println("Oczekiwanie na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Połączono!");

                //Create thread to handle connection
                TaskHandler taskHandler = new TaskHandler(socket, databaseCommunicator);
                taskHandlerList.add(taskHandler);
                taskHandler.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
