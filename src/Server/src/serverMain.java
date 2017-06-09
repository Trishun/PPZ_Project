/**
 * Server init
 * Created by PD on 05.04.2017.
 */
public class serverMain {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
        Debug.initLog();
        SettingsProvider settingsProvider = new SettingsProvider();
        DatabaseCommunicator databaseCommunicator = new DatabaseCommunicator(settingsProvider);
        LobbyManager lobbyManager = new LobbyManager();
        PlayerManager playerManager = new PlayerManager(lobbyManager, databaseCommunicator);
        Server mainServer = new Server(settingsProvider, playerManager);

        mainServer.Run();
        new HTTPPart(settingsProvider).Run();
    }
}
