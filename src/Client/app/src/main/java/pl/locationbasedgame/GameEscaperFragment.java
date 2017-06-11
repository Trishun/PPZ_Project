package pl.locationbasedgame;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameEscaperFragment extends Fragment {

    private LocationManager locationManager;
    private Location currentLocation;
    private LocationListener locationListener;

    public GameEscaperFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
                Log.i("CURRENT LOC", currentLocation.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };

        return inflater.inflate(R.layout.fragment_game_escaper, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, locationListener);
        } catch (SecurityException e) {
            //catched when permissions are not granted
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }
}
