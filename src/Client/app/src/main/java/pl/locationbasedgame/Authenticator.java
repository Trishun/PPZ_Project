package pl.locationbasedgame;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patryk Ligenza on 11-Apr-17.
 */

interface AuthenticationResultListener {
    void onAuthenticationSuccess(String name, String password);
    void onAuthenticationFailure();
}

class Authenticator extends AsyncTask<String, Void, String> {

    private AuthenticationResultListener caller;
    private SocketHandler handler;
    private String name;
    private String password;

    @Override
    protected String doInBackground(String... params) {
        name = params[0];
        password = params[1];

        String message = constructLoginMessage(name, password);
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
        if (response == null) return;
        JSONObject jsonResponse = new JSONObject(response);
        if (jsonResponse.getBoolean("login")) {
            caller.onAuthenticationSuccess(name, password);
        } else {
            caller.onAuthenticationFailure();
        }
    }

    private String constructLoginMessage(String name, String password) {
        //return  "login&" + name + "%" + password;

        JSONObject json = new JSONObject();
        try {
            json.put("header", "login");
            json.put("uname", name);
            json.put("upass", password);
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    void setCaller(AuthenticationResultListener caller) {
        this.caller = caller;
    }

    void setHandler(SocketHandler handler) {
        this.handler = handler;
    }
}
