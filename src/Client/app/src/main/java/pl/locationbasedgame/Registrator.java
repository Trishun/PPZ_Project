package pl.locationbasedgame;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 15-Apr-17.
 */

interface RegistrationResultListener {
    void onRegistrationSuccess();
    void onRegistrationFailure(String alertMessage);
}

class Registrator extends AsyncTask<String, Void, String> {

    private RegistrationResultListener caller;
    private SocketHandler handler;

    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String password = params[1];
        String mail = params[2];
        String locale = params[3];

        String message = constructRegistrationMessage(name, password, mail, locale);
        return handler.sendMessageAndGetResponse(message);
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            processResponse(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(response);
    }

    private void processResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        if (jsonResponse.getBoolean("register")) {
            caller.onRegistrationSuccess();
        } else {
            caller.onRegistrationFailure(jsonResponse.getString("alert"));
        }
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

    void setCaller(RegistrationResultListener caller) {
        this.caller = caller;
    }

    void setHandler(SocketHandler handler) {
        this.handler = handler;
    }
}
