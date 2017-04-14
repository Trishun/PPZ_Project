package pl.locationbasedgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("ewgggg");
        textView.setGravity(Gravity.CENTER);
        //assignListeners();
        return textView;
    }

//    private void assignListeners() {
//        Button registerButton = (Button) getView().findViewById(R.id.btn_register);
//        registerButton.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "PLAYER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
