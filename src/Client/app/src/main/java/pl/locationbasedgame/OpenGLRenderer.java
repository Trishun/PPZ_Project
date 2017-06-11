package pl.locationbasedgame;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLU;
import android.os.Bundle;
import android.widget.TextView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created on 25.05.2017.
 */
class OpenGLRenderer implements android.opengl.GLSurfaceView.Renderer, SensorEventListener {
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Context context;
    private Location location;
    private Location destination;

    private Pointer pointer = new Pointer();
    private Circle circle = new Circle();
    private Arrow arrow = new Arrow();

    private float angle = 0;
    private float heading = 0;

    OpenGLRenderer() {
    }

    OpenGLRenderer(Context context) {
        this.context = context;
        locationBegin();
        location = new Location(LocationManager.GPS_PROVIDER);
//        destination = new Location(LocationManager.GPS_PROVIDER);

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        //TEMP
        setDestination(50.295321, 18.932156);
        //
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Ustawienie koloru tła na czarny ( rgba ).
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
        // Włączenie cieniowania
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Ustawienie bufora głębokości
        gl.glClearDepthf(1.0f);
        // Włączenie testowania bufora głębokości
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Ustawienia dotyczące perspektywy
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -10);
        gl.glPushMatrix();

        drawText(String.valueOf(countDistance(location, destination)) + " m");

        float[] pointerCoords = countCoords(heading - angle + 90, 1.8f);
        pointer.draw(gl, heading - angle, pointerCoords[0], pointerCoords[1]);

        circle.draw(gl, 1.8f, 0, 0, 0);
        circle.draw(gl, 1.6f, 1, 1, 1);

        float[] pointerCoords2 = countCoords(heading + 90, 0f);
        arrow.draw(gl, heading, pointerCoords2[0], pointerCoords2[1]);

        gl.glPopMatrix();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        // Wybranie macierzy projekcji
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Wyczyszczenie macierzy projekcji
        gl.glLoadIdentity();
        // Ustawienie perspektywy
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        // Wybranie macierzy widoku
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Wyczyszczenie macierzy widoku
        gl.glLoadIdentity();
    }

    private float[] countCoords(float angle, float distanceToCenter) {
        float[] xy = new float[2];
        xy[0] = distanceToCenter * (float) Math.cos(Math.toRadians(angle));
        xy[1] = distanceToCenter * (float) Math.sin(Math.toRadians(angle));
        return xy;
    }

    private void drawText(final String text) {
        final Activity main = (Activity) context;
        main.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) main.findViewById(R.id.meters);
                textView.setText(text);
            }
        });
    }

    private Integer countDistance(Location player, Location point) {
        angle = player.bearingTo(point);
        return (int) player.distanceTo(point);
    }

    @Deprecated
    private Integer countDistance(double playerLon, double playerLat, double pointLon, double pointLat) {
        int R = 6371; // km
        double dLat = Math.toRadians(pointLon - playerLon);
        double dLon = Math.toRadians(pointLat - playerLat);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(playerLon)) * Math.cos(Math.toRadians(pointLon)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        angle = (float) Math.toDegrees(Math.atan2(pointLat - playerLat, pointLon - playerLon));
        double d = R * c;
        return (int) (d * 1000);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        heading = Math.round(event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void locationBegin() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        final String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.addGpsStatusListener(mGPSStatusListener);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                setLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        location = locationManager.getLastKnownLocation(locationProvider);
    }

    private GpsStatus.Listener mGPSStatusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
//                    Toast.makeText(context, "GPS_SEARCHING", Toast.LENGTH_SHORT).show();
                    System.out.println("TAG - GPS searching: ");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    System.out.println("TAG - GPS Stopped");
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:

                /*
                 * GPS_EVENT_FIRST_FIX Event is called when GPS is locked
                 */
//                    Toast.makeText(context, "GPS_LOCKED", Toast.LENGTH_SHORT).show();
                    Location gpslocation = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (gpslocation != null) {
                        System.out.println("GPS Info:" + gpslocation.getLatitude() + ":" + gpslocation.getLongitude());

                    /*
                     * Removing the GPS status listener once GPS is locked
                     */
                        locationManager.removeGpsStatusListener(mGPSStatusListener);
                    }

                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    //                 System.out.println("TAG - GPS_EVENT_SATELLITE_STATUS");
                    break;
            }
        }
    };

    private void setLocation(Location location) {
        this.location = location;
    }

    void setDestination(Location destination) {
        this.destination = destination;
    }

    void setDestination(double latitude, double longitude) {
        destination = new Location(LocationManager.GPS_PROVIDER);
        destination.setLongitude(longitude);
        destination.setLatitude(latitude);
    }

}