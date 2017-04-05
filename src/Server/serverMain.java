package Server;

/**
 * Created by PD on 05.04.2017.
 */
public class serverMain {
    public static void main(String args[]) {
        DatabaseCommunicator databaseCommunicator = new DatabaseCommunicator();
        Server mainServer = new Server(databaseCommunicator);

        mainServer.Run();
    }
}
