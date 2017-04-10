import org.jetbrains.annotations.Nullable;

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
            ServerSocket serverSocket = new ServerSocket(9090);

            while (true) {
                //Accept connection
                System.out.println("Oczekiwanie na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Połączono!");

                //Create thread to handle connection
                Player player = new Player(socket, databaseCommunicator, this);
                playerList.add(player);
                player.start();
                stats();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void stats() {
        System.out.println("Players: "+String.valueOf(playerList.size()));
        System.out.println("Lobbys: "+String.valueOf(lobbyList.size()));
    }

    void disconnectPlayer(Player playerToDisconnect) {
        for (Player player:playerList) {
            if (player == playerToDisconnect) {
                playerList.remove(player);
            }
        }
        for (Lobby lobby:lobbyList) {
            for (Player player:lobby.getPlayers()) {
                if (player == playerToDisconnect) {
                    lobby.removeFromLobby(player);
                    refreshViewData(lobby);
                }
            }
        }
        System.out.println("Player" + playerToDisconnect.getPlayerName() + " (" + playerToDisconnect.getPlayerId() + ") disconnected");

    }

    int createLobby(Player initiator) {
        Lobby lobby = new Lobby(lobbyList.size(), initiator);
        lobbyList.add(lobby);
        System.out.println("Lobby " + lobby.getId() + " created!");
        refreshViewData(lobby);
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

    void addToLobby(Lobby lobby, Player player) {
        lobby.addToLobby(player);
        player.setLobby(lobby.getId());
        System.out.println("Player" + player.getPlayerName() + " (" + player.getPlayerId() + ") joined lobby " + lobby.getId());
        refreshViewData(lobby);
    }

    void removeFromLobby(int lobbyId, Player player) {
        Lobby lobby = getLobbyById(lobbyId);
        lobby.removeFromLobby(player);
        System.out.println("Player" + player.getPlayerName() + " (" + player.getPlayerId() + ") left lobby " + lobbyId);
        refreshViewData(lobby);
    }

    void refreshViewData(Lobby lobby) {
        for (Player player : lobby.getPlayers()) {
            player.refreshViewData();
        }
    }
    ArrayList<Player> refreshViewData(Player player) {
        stats();
        return getLobbyById(player.getLobby()).getPlayers();
    }

    @Nullable
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
        return initiator.hashCode();
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
