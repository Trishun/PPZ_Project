package pl.locationbasedgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: 15-Apr-17 RESOLVE CODE CODE DUPLICATION SOMEHOW
public class RegisterFragment extends Fragment implements RegistrationResultListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        assignListeners();
    }

    private void assignListeners() {
        Button registerButton = (Button) getView().findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        EditText nameEditText = (EditText) getView().findViewById(R.id.et_name);
        EditText passwordEditText = (EditText) getView().findViewById(R.id.et_password);
        EditText mailEditText = (EditText) getView().findViewById(R.id.et_mail);

        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String mail = mailEditText.getText().toString();

        if (areSpecified(name, password, mail)) {
            sendLoginRequestToServer(name, password, mail);
        }
        else {
            Toast.makeText(getActivity(), R.string.fill_form, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLoginRequestToServer(String name, String password, String mail) {
        Registrator registrator = new Registrator();
        registrator.setCaller(this);
        registrator.execute(name, password, mail);
    }

    private boolean areSpecified(String name, String password, String mail) {
        return !name.isEmpty() && !password.isEmpty() && !mail.isEmpty();
    }


    @Override
    public void onRegistrationSuccess() {
        Toast.makeText(getContext(), R.string.registration_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRegistrationFailure(String error) {
        Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_LONG).show();
        Log.i("ERROR", error);
    }
}
