package pl.locationbasedgame;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 01-May-17.
 */

interface LobbyTaskCallback {
    void onLobbyCreated(int id);
    void onLobbyJoined();
}

class LobbyTask extends AsyncTask<Character, Void, Void> {

    private SocketHandler handler;
    private LobbyTaskCallback callerContext;
    private int id;

    private int createLobbyAndGetCode() {
        JSONObject message = new JSONObject();
        try {
            message.put("header", "lcreate");
            JSONObject responseJSON = new JSONObject(handler.sendMessageAndGetResponse(message.toString()));
            return responseJSON.getInt("lcreate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private boolean joinLobby() {
        JSONObject message = new JSONObject();
        try {
            message.put("header", "ljoin");
            message.put("enterCode", String.valueOf(id));
            JSONObject responseJSON = new JSONObject(handler.sendMessageAndGetResponse(message.toString()));
            return responseJSON.has("llist");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected Void doInBackground(Character... characters) {
        if (characters[0] == 'C') {
            int id = createLobbyAndGetCode();
            callerContext.onLobbyCreated(id);
        } else {
            if (joinLobby()) {
                callerContext.onLobbyJoined();
            }
        }
        return null;
    }

    void set(SocketHandler socketHandler, LobbyTaskCallback context, int id) {
        handler = socketHandler;
        callerContext = context;
        this.id = id;
    }
}
