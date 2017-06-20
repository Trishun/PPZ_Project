package pl.locationbasedgame;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalFragment extends Fragment {


    private ProgressDialog progressDialog;
    private Context context;

    public FinalFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_final, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.map_loading));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    void showResult(final String result) {
        progressDialog.hide();
        ((TextView)((Activity) context).findViewById(R.id.tv_final_result))
                .setText(result.equalsIgnoreCase("win") ? R.string.result_win : R.string.result_lose);
    }

    @OnClick(R.id.btn_final_ok)
    void finalOkButtonClick() {
      startActivity(new Intent(context, MainMenuActivity.class));
    }

}
