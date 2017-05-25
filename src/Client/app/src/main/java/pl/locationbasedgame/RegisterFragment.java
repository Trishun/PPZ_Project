package pl.locationbasedgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.Locale;
import java.util.concurrent.Callable;

public class RegisterFragment extends Fragment {

    @BindView(R.id.et_name)
    EditText nameEditText;
    @BindView(R.id.et_password)
    EditText passwordEditText;
    @BindView(R.id.et_repeated_password)
    EditText repeatedPasswordEditText;
    @BindView(R.id.et_mail)
    EditText mailEditText;

    static final String TAG = "REGISTER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_register)
    void register() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatedPassword = repeatedPasswordEditText.getText().toString();
        String mail = mailEditText.getText().toString();
        String locale = Locale.getDefault().toString();

        if (areSpecified(name, password, repeatedPassword, mail)) {
            if (areBothPasswordsNotEqual(password, repeatedPassword)) {
                Toast.makeText(getContext(), R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
            } else {
                performRegisterRequest(name, password, mail, locale);
            }
        } else {
            Toast.makeText(getContext(), R.string.fill_form, Toast.LENGTH_SHORT).show();
        }
    }

    private void performRegisterRequest(final String name, final String password, final String mail, final String locale) {

        Single<AccountResponse> registerTask = Single.fromCallable(new Callable<AccountResponse>() {
            @Override
            public AccountResponse call() throws Exception {
                return StartActivity.getService().sendRegisterRequestToServer(name, password, mail, locale);
            }
        });

        registerTask
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AccountResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(AccountResponse response) {
                        Toast.makeText(getContext(), response.isSuccess()
                                ? getString(R.string.registration_success)
                                : response.getAlertMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i(TAG, e.getMessage());
                    }
                });
    }

    private boolean areSpecified(String name, String password, String repeated, String mail) {
        return !name.isEmpty() && !password.isEmpty() && !repeated.isEmpty() && !mail.isEmpty();
    }

    private boolean areBothPasswordsNotEqual(String password, String repeatedPassword) {
        return !password.equals(repeatedPassword);
    }
}
