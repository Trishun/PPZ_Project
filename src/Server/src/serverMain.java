/**
 * Server init
 * Created by PD on 05.04.2017.
 */
public class serverMain {
    public static void main(String args[]) {
        SettingsProvider settingsProvider = new SettingsProvider();
        DatabaseCommunicator databaseCommunicator = new DatabaseCommunicator(settingsProvider);
        Server mainServer = new Server(databaseCommunicator, settingsProvider);

        mainServer.Run();
    }
}
