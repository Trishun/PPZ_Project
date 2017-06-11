package pl.locationbasedgame;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];

    private Context context;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private Location destination;
    final String locationGpsProvider = LocationManager.GPS_PROVIDER;
    final String locationNetworkProvider = LocationManager.NETWORK_PROVIDER;

    private Pointer pointer = new Pointer();
    private Circle circle = new Circle();
    private Arrow arrow = new Arrow();

    private float angle = 0;
    private float heading = 0;

    OpenGLRenderer(Context context) {
        this.context = context;

        compassInit();
        locationInit();

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];

                // mGravity = event.values;

                // Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
                // Log.e(TAG, Float.toString(event.values[0]));

            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                heading = (float) Math.toDegrees(orientation[0]); // orientation
                heading = (heading + 360) % 360;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void compassInit() {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        compassStart();
    }

    void compassStart() {
        sensorManager.registerListener(this, gsensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    void compassStop() {
        sensorManager.unregisterListener(this);
    }

    private void locationInit() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
        locationStart();
        location = new Location(locationGpsProvider);
        destination = new Location(locationGpsProvider);
    }

    void locationStart() {
        locationManager.requestLocationUpdates(locationGpsProvider, 3000, 5, locationListener);
        locationManager.requestLocationUpdates(locationNetworkProvider, 3000, 5, locationListener);
    }

    void locationStop() {
        locationManager.removeUpdates(locationListener);
    }

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