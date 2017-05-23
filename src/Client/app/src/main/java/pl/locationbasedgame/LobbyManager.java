package pl.locationbasedgame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 01-May-17.
 */

class LobbyManager {

    int createLobbyAndGetCode(SocketHandler handler) {
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

    JSONArray joinLobby(SocketHandler handler, int id) {
        JSONObject message = new JSONObject();

        try {
            message.put("header", "ljoin");
            message.put("enterCode", String.valueOf(id));

            JSONObject responseJSON = new JSONObject(handler.sendMessageAndGetResponse(message.toString()));

            return responseJSON.has("llist") ? responseJSON.getJSONArray("llist") : new JSONArray();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
