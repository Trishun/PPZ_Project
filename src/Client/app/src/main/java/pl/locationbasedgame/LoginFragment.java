package pl.locationbasedgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static pl.locationbasedgame.PreferencesHelper.storeUser;

public class LoginFragment extends Fragment {

    private String TAG = "LOGIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        assignListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: 06-May-17 unsubscribe
    }

    private void assignListeners() {
        Button loginButton = (Button) getView().findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * Send credentials to server if they are specified by user.
     */
    private void login() {
        EditText nameEditText = (EditText) getView().findViewById(R.id.et_name);
        EditText passwordEditText = (EditText) getView().findViewById(R.id.et_password);

        final String name = nameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String locale = Locale.getDefault().toString();

        if (areSpecified(name, password)) {

            Single<Boolean> singleLoginTask = Single.fromCallable(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return StartActivity.getService().sendLoginRequestToServer(name, password, locale);
                }
            });

            singleLoginTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        processResponse(success, name, password);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError");
                        e.printStackTrace();
                    }
                });
        } else {
            Toast.makeText(getContext(), R.string.credentials_not_specified, Toast.LENGTH_SHORT).show();
        }
    }

    private void processResponse(boolean success, String name, String password) {
        if (success) {
            Log.i(TAG, "OK");
            storeUser(getActivity().getApplicationContext(), name, password);
            goToMainMenu();
        } else {
            Toast.makeText(getContext(), R.string.wrong_credentials, Toast.LENGTH_LONG).show();
            Log.i(TAG, "FAIL");
        }
    }

    /**
     * Checks if two passed strings are not empty.
     */
    private boolean areSpecified(String name, String password) {
        return !name.isEmpty() && !password.isEmpty();
    }

    void goToMainMenu() {
        Intent mainMenuIntent = new Intent(getActivity(), MainMenuActivity.class);
        startActivity(mainMenuIntent);
    }
}
