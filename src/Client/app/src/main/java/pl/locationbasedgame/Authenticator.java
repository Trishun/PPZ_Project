package pl.locationbasedgame;

import android.os.AsyncTask;

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
        processResponse(response);
        super.onPostExecute(response);
    }

    private void processResponse(String response) {
        if (response == null) return;
        if (response.contains("true")) {
            caller.onAuthenticationSuccess(name, password);
        }
        else {
            caller.onAuthenticationFailure();
        }
    }

    private String constructLoginMessage(String name, String password) {
        return  "login&" + name + "%" + password;
    }

    void setCaller(AuthenticationResultListener caller) {
        this.caller = caller;
    }

    void setHandler(SocketHandler handler) {
        this.handler = handler;
    }
}
