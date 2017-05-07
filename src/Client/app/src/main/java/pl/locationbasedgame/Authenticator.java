package pl.locationbasedgame;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 11-Apr-17.
 */

class Authenticator {

    boolean authenticate(SocketHandler handler, String name, String password, String locale) {
        String message = constructLoginMessage(name, password, locale);
        String response = handler.sendMessageAndGetResponse(message);
        return ifAuthenticationSucceeded(response);
    }

    private String constructLoginMessage(String name, String password, String locale) {
        JSONObject json = new JSONObject();
        try {
            json.put("header", "login");
            json.put("uname", name);
            json.put("upass", password);
            json.put("locale", locale);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean ifAuthenticationSucceeded(String response) {
        if (response == null) {
            return false;
        }

        try {
            JSONObject json = new JSONObject(response);
            return json.getBoolean("login");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
