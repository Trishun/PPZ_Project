package pl.locationbasedgame;

/**
 * Created by Patryk Ligenza on 07-May-17.
 */

class AccountResponse {

    private boolean success;
    private String alertMessage;

    AccountResponse(boolean success, String alertMessage) {
        this.success = success;
        this.alertMessage = alertMessage;
    }

    boolean isSuccess() {
        return success;
    }

    String getAlertMessage() {
        return alertMessage;
    }
}
