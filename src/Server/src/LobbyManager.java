import java.util.ArrayList;

/**
 * Lobby management class
 * Created by PD on 24.04.2017.
 */
class LobbyManager {

    private static ArrayList<Lobby> lobbyList = new ArrayList<>();


    int createLobby(Player initiator) {
        Lobby lobby = new Lobby(lobbyList.size(), initiator);
        lobbyList.add(lobby);
        System.out.println("Lobby " + lobby.getId() + " created!");
        return lobby.getId();
    }

    boolean addToLobby(int enterCode, Player player) {
        for (Lobby lobby : lobbyList) {
            if (enterCode == lobby.getEnterCode()) {
                addToLobby(lobby, player);
                return true;
            }
        }
        return false;
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

    void removeFromLobby(Player player) {
        for (Lobby lobby : lobbyList) {
            if (lobby.removeFromLobby(player))
                return;
        }
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
