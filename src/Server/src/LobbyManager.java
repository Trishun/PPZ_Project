import java.util.ArrayList;

/**
 * Lobby management class
 * Created by PD on 24.04.2017.
 */
class LobbyManager {

    private static ArrayList<Lobby> lobbyList = new ArrayList<>();


    Lobby createLobby(Player initiator) {
        Lobby lobby = new Lobby(lobbyList.size(), initiator);
        lobbyList.add(lobby);
        Debug.Log("Lobby " + lobby.getId() + " created!");
        return lobby;
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
        player.setLobby(lobby);
        Debug.Log("Player " + player.getPlayerName() + " (" + player.getPlayerId() + ") joined lobby " + lobby.getId());
    }

    void removeFromLobby(Player player) {
        Lobby lobby = player.getLobby();
        lobby.removeFromLobby(player);
        if (lobby.getPlayerCount() == 0) {
            lobbyList.remove(lobby);
            Debug.Log("Lobby " + lobby.getId() + " closed!");
            lobby.close();
        } else {
            lobby.broadcastLobbyStructure();
        }
        Debug.Log("Player " + player.getPlayerName() + " (" + player.getPlayerId() + ") left lobby " + lobby.getId());
    }

    Integer getLobbyEnterCode(int id) {
        Lobby lobby = getLobbyById(id);
        try {
            return lobby.getEnterCode();
        } catch (NullPointerException e) {
            return null;
        }
    }

    Lobby getLobbyById(int id) {
        for (Lobby lobby : lobbyList) {
            if (lobby.getId() == id) {
                return lobby;
            }
        }
        return null;
    }
}
