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

    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String password = params[1];
        String mail = params[2];

        ServerHandler serverHandler = new ServerHandler();
        String message = constructRegistrationMessage(name, password, mail);
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
}
