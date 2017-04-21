package pl.locationbasedgame;

import android.os.AsyncTask;

/**
 * Created by Patryk Ligenza on 15-Apr-17.
 */

interface RegistrationResultListener {
    void onRegistrationSuccess();
    void onRegistrationFailure(String error);
}

class Registrator extends AsyncTask<String, Void, String> {

    private RegistrationResultListener caller;
    private SocketHandler handler;

    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String password = params[1];
        String mail = params[2];

        String message = constructRegistrationMessage(name, password, mail);
        return handler.sendMessageAndGetResponse(message);
    }

    @Override
    protected void onPostExecute(String response) {
        processResponse(response);
        super.onPostExecute(response);
    }

    private void processResponse(String response) {
        if (response.contains("true")) {
            caller.onRegistrationSuccess();
        }
        else {
            caller.onRegistrationFailure(response);
        }
    }

    private String constructRegistrationMessage(String name, String password, String mail) {
        return "register&" + name + '%' + password + "%temp%" + mail;
    }

    void setCaller(RegistrationResultListener caller) {
        this.caller = caller;
    }

    void setHandler(SocketHandler handler) {
        this.handler = handler;
    }
}
