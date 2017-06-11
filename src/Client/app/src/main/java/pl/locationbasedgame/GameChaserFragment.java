package pl.locationbasedgame;


import android.app.Fragment;
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

    OpenGLRenderer openGLRenderer = new OpenGLRenderer(getActivity());

    public GameChaserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        GLSurfaceView view = new GLSurfaceView(getActivity());
        view.setRenderer(openGLRenderer);
        rootView = inflater.inflate(R.layout.fragment_game_chaser, container, false);

        FrameLayout layout = (FrameLayout) rootView;
        layout.addView(view, 0);
        return rootView;
    }

    void setDestination(double latitude, double longitude) {
        openGLRenderer.setDestination(latitude, longitude);
    }
}
