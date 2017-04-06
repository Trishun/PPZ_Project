package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Server {

    private static ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();
    private static ArrayList<Player> playerList = new ArrayList<Player>();
    private DatabaseCommunicator databaseCommunicator;

    /**
     * Class constructor.
     * @param databaseCommunicator DatabaseCommunicator to be used.
     */
    Server(DatabaseCommunicator databaseCommunicator) {
        this.databaseCommunicator = databaseCommunicator;
    }

    void Run() {
        new Thread(() -> incoming()).start();
    }

    private void incoming() {
        try {
            ServerSocket serverSocket = new ServerSocket(80);

            while (true) {
                //Accept connection
                System.out.println("Oczekiwanie na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Połączono!");

                //Create thread to handle connection
                Player player = new Player(socket, databaseCommunicator, this);
                playerList.add(player);
                player.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createLobby(Player initiator) {
        Lobby lobby = new Lobby(lobbyList.size(), initiator);
        lobbyList.add(lobby);
        refreshViewData(lobby);
    }

    public void addToLobby(int enterCode, Player player) {
        for (Lobby lobby : lobbyList) {
            if (enterCode == lobby.getEnterCode()) {
                addToLobby(lobby, player);
                return;
            }
        }
    }

    public void addToLobby(Lobby lobby, Player player) {
        lobby.addToLobby(player);
        player.setLobby(lobby.getId());
        refreshViewData(lobby);
    }

    public void refreshViewData(Lobby lobby) {
        for (Player player : lobby.getLobbyList()) {
            player.refreshViewData();
        }
    }
    public ArrayList<Player> refreshViewData(Player player) {
        return getLobbyById(player.getLobby()).getLobbyList();
    }

    private Lobby getLobbyById(int id) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getId() == id) {
                return lobby;
            }
        }
        return null;
    }

}

class Lobby {
    private int id;
    private Player initiator;
    private ArrayList<Player> lobbyList = new ArrayList<Player>();
    private int enterCode;

    Lobby(int id, Player initiator) {
        this.id = id;
        this.initiator = initiator;
        enterCode = enterCodeGeneration();
        lobbyList.add(initiator);
    }

    private int enterCodeGeneration() {
        return initiator.hashCode();
    }

    public int getEnterCode() {
        return enterCode;
    }

    public int getId() {
        return id;
    }

    public void addToLobby(Player newPlayer) {
        lobbyList.add(newPlayer);
    }

    public void removeFromLobby(Player player) {
        lobbyList.remove(player);
    }

    public ArrayList<Player> getLobbyList() {
        return lobbyList;
    }
}
