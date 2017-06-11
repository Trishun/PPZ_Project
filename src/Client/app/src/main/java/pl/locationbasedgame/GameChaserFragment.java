package pl.locationbasedgame;


import android.app.Fragment;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameChaserFragment extends Fragment {

    private OpenGLRenderer openGLRenderer;

    public GameChaserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        Context context = getActivity();
        GLSurfaceView view = new GLSurfaceView(context);
        openGLRenderer = new OpenGLRenderer(context);
        view.setRenderer(openGLRenderer);
        rootView = inflater.inflate(R.layout.fragment_game_chaser, container, false);

        FrameLayout layout = (FrameLayout) rootView;
        layout.addView(view, 0);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        openGLRenderer.compassStop();
        openGLRenderer.locationStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        openGLRenderer.compassStart();
        openGLRenderer.locationStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        openGLRenderer.compassStop();
        openGLRenderer.locationStop();
    }

    void setDestination(double latitude, double longitude) {
        openGLRenderer.setDestination(latitude, longitude);
    }
}
