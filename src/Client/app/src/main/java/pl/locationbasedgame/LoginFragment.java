package pl.locationbasedgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static butterknife.ButterKnife.*;
import static pl.locationbasedgame.PreferencesHelper.storeUser;

public class LoginFragment extends Fragment {

    private String TAG = "LOGIN";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO: 06-May-17 unsubscribe
    }

    /**
     * Send credentials to server if they are specified by user.
     */
    @OnClick(R.id.btn_login)
    void login() {
        EditText nameEditText = findById(getView(), R.id.et_name);
        EditText passwordEditText = findById(getView(), R.id.et_password);

        final String name = nameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String locale = Locale.getDefault().toString();

        if (areSpecified(name, password)) {

            Single<AccountResponse> singleLoginTask = Single.fromCallable(new Callable<AccountResponse>() {
                @Override
                public AccountResponse call() throws Exception {
                    return StartActivity.getService().sendLoginRequestToServer(name, password, locale);
                }
            });

            singleLoginTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AccountResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountResponse response) {
                        processResponse(response, name, password);
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

    private void processResponse(AccountResponse response, String name, String password) {
        if (response.isSuccess()) {
            Log.i(TAG, "OK");
            storeUser(getActivity().getApplicationContext(), name, password);
            goToMainMenu();
        } else {
            Toast.makeText(getContext(), response.getAlertMessage(), Toast.LENGTH_LONG).show();
            Log.i(TAG, response.getAlertMessage());
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
