package pl.locationbasedgame;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 15-Apr-17.
 */

class Registrator {

    AccountResponse registerUser(SocketHandler socketHandler, String name, String password, String mail, String locale) {
        String message = constructRegistrationMessage(name, password, mail, locale);
        String response = socketHandler.sendMessageAndGetResponse(message);

        try {
            JSONObject json = new JSONObject(response);
            return json.getBoolean("register")
                    ? new AccountResponse(true, null)
                    : new AccountResponse(false, json.getString("alert"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new AccountResponse(false, null);
    }

    private String constructRegistrationMessage(String name, String password, String mail, String locale) {
        JSONObject json = new JSONObject();
        try {
            json.put("header", "register");
            json.put("uname", name);
            json.put("upass", password);
            json.put("backup_code", "1234");
            json.put("email", mail);
            json.put("locale", locale);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
