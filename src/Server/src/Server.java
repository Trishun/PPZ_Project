import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
public class Server {

    private static ArrayList<Lobby> lobbyList = new ArrayList<Lobby>();
    private static ArrayList<Player> playerList = new ArrayList<Player>();
    private DatabaseCommunicator databaseCommunicator;
    private SettingsProvider settingsProvider;

    /**
     * Class constructor.
     *
     * @param databaseCommunicator DatabaseCommunicator to be used.
     */
    Server(DatabaseCommunicator databaseCommunicator, SettingsProvider settingsProvider) {
        this.databaseCommunicator = databaseCommunicator;
        this.settingsProvider = settingsProvider;
    }

    void Run() {
        new Thread(() -> incoming()).start();
    }

    private void incoming() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(settingsProvider.get("port")));

            while (true) {
                //Accept connection
                System.out.println("Oczekiwanie na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Połączono z " + socket.getRemoteSocketAddress().toString());

                //Create thread to handle connection
                Player player = new Player(socket, databaseCommunicator, this);
                playerList.add(player);
                player.start();
                stats();
            }
        } catch (Exception e) {
            System.out.println("Error in Server/incoming: "+e);
        }
    }

    private void stats() {
        System.out.println("Players: " + String.valueOf(playerList.size()));
        System.out.println("Lobbys: " + String.valueOf(lobbyList.size()));
    }

    void disconnectPlayer(Player playerToDisconnect) {
        try {
            for (Player player : playerList) {
                if (player == playerToDisconnect) {
                    playerList.remove(player);
                    break;
                }
            }
        } catch (ConcurrentModificationException ee) {
            System.out.println("Exception 1 in Server/disconnectPlayer: " + ee);
        }
        try {
            for (Lobby lobby : lobbyList) {
                for (Player player : lobby.getPlayers()) {
                    if (player == playerToDisconnect) {
                        lobby.removeFromLobby(player);
                        break;
                    }
                }
            }
        } catch (ConcurrentModificationException ee) {
            System.out.println("Exception 2 in Server/disconnectPlayer: " + ee);
        }
        System.out.println("Player " + playerToDisconnect.getPlayerName() + " (" + playerToDisconnect.getPlayerId() + ") disconnected");
    }

    int createLobby(Player initiator) {
        Lobby lobby = new Lobby(lobbyList.size(), initiator);
        lobbyList.add(lobby);
        System.out.println("Lobby " + lobby.getId() + " created!");
        stats();
        return lobby.getId();
    }

    void addToLobby(int enterCode, Player player) {
        for (Lobby lobby : lobbyList) {
            if (enterCode == lobby.getEnterCode()) {
                addToLobby(lobby, player);
                return;
            }
        }
    }

    private void addToLobby(Lobby lobby, Player player) {
        lobby.addToLobby(player);
        player.setLobby(lobby.getId());
        System.out.println("Player " + player.getPlayerName() + " (" + player.getPlayerId() + ") joined lobby " + lobby.getId());
    }

    void removeFromLobby(int lobbyId, Player player) {
        Lobby lobby = getLobbyById(lobbyId);
        lobby.removeFromLobby(player);
        System.out.println("Player " + player.getPlayerName() + " (" + player.getPlayerId() + ") left lobby " + lobbyId);
    }

    Integer getLobbyEnterCode(int id) {
        Lobby lobby = getLobbyById(id);
        try {
            return lobby.getEnterCode();
        } catch (NullPointerException e) {
            return null;
        }
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
    private ArrayList<Player> players = new ArrayList<Player>();
    private int enterCode;

    Lobby(int id, Player initiator) {
        this.id = id;
        this.initiator = initiator;
        enterCode = enterCodeGeneration();
        players.add(initiator);
    }

    private int enterCodeGeneration() {
        return initiator.hashCode()/10000;
    }

    int getEnterCode() {
        return enterCode;
    }

    int getId() {
        return id;
    }

    void addToLobby(Player newPlayer) {
        players.add(newPlayer);
    }

    void removeFromLobby(Player player) {
        players.remove(player);
    }

    ArrayList<Player> getPlayers() {
        return players;
    }
}
