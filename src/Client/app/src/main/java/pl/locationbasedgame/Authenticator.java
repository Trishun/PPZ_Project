package pl.locationbasedgame;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 11-Apr-17.
 */

class Authenticator {

    AccountResponse authenticate(SocketHandler handler, String name, String password, String locale) {
        String message = constructLoginMessage(name, password, locale);
        String response = handler.sendMessageAndGetResponse(message);

        try {
            JSONObject json = new JSONObject(response);
            return json.getBoolean("login")
                    ? new AccountResponse(true, null)
                    : new AccountResponse(false, json.getString("alert"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new AccountResponse(false, null);
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
}
