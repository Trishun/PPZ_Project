package pl.locationbasedgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import butterknife.OnClick;

import static butterknife.ButterKnife.bind;
import static butterknife.ButterKnife.findById;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameEscaperFragment extends Fragment {

    private LocationManager locationManager;
    private LatLng currentLocation;
    private LocationListener locationListener;
    private boolean isLocationInitialized = false;
    private GoogleMap map;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_game_escaper, container, false);

        setLocationListening(view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.map_loading));
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setMap();
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

    @OnClick(R.id.btn_leave_hint)
    void onLeaveHintButtonClick() {
        final EditText hintEditText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.leave_hint);
        builder.setView(hintEditText);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hint = hintEditText.getText().toString();
                if (!Objects.equals(hint, "")) {
                    ((GameActivity) getActivity()).getService().sendPoint(currentLocation, hint);
                    stampHintOnMap(hint);
                }
            }
        });
        builder.show();
    }

    private void stampHintOnMap(String hint) {
        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title(hint));
    }

    private void setLocationListening(final View view) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (!isLocationInitialized) {
                    findById(view, R.id.btn_leave_hint).setVisibility(View.VISIBLE);
                }

                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                isLocationInitialized = true;

                if (map != null) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                    progressDialog.dismiss();
                }

                Log.i("CURRENT LOC", currentLocation.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }

            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) { }
        };
    }

    private void setMap() {
        MapView mapView = findById(getView(), R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setMyLocationEnabled(true);
                map.setBuildingsEnabled(true);
                progressDialog.setMessage(getString(R.string.player_pinpointing));
            }
        });
        mapView.onResume();
    }
}
