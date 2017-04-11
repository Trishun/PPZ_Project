package pl.locationbasedgame;

import android.os.AsyncTask;

/**
 * Created by Patryk Ligenza on 11-Apr-17.
 */

class Authenticator extends AsyncTask<String, Void, String> {

    private AuthenticationResultListener caller;

    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String password = params[1];

        ServerHandler serverHandler = new ServerHandler();
        String message = constructLoginMessage(name, password);
        String response = serverHandler.sendMessageAndGetResponse(message);
        serverHandler.closeSocket();
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        processResponse(response);
        super.onPostExecute(response);
    }

    private void processResponse(String response) {
        if (response.contains("true")) {
            caller.onAuthenticationSuccess();
        }
        else {
            caller.onAuthenticationFailure();
        }
    }

    private String constructLoginMessage(String name, String password) {
        String request = "login&" + name + "%" + password;
        return request;
    }

    void setCaller(AuthenticationResultListener caller) {
        this.caller = caller;
    }
}
