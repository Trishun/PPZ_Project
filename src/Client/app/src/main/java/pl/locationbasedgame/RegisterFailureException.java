package pl.locationbasedgame;

/**
 * Created by Patryk Ligenza on 06-May-17.
 */

class RegisterFailureException extends RuntimeException {

    RegisterFailureException(String reason) {
        super(reason);
    }
}
