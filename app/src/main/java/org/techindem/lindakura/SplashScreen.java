package org.techindem.lindakura;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int PHOTO_PERMISSIONS_REQUEST_CODE = 0;
    protected Location mLastLocation;
    TextView tvLatlong;
    FusedLocationProviderClient mFusedLocationClient;
    double[] myLocation = new double[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final ImageView iv = (ImageView) findViewById(R.id.imageView3);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        iv.startAnimation(an);
        tvLatlong = (TextView) findViewById(R.id.tvLatlong);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!photoPermissions()) {
            requestPhotoPermissions();
        } else {
            locPermission();
        }
    }
    private void locPermission(){

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }

    }
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
        final ImageView iv = (ImageView) findViewById(R.id.imageView3);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        iv.startAnimation(an2);
        an2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation){

            }

            @Override
            public void onAnimationEnd(Animation animation){
                //iv.startAnimation(an2);
                finish();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                if(mLastLocation !=null) {
                    i.putExtra("Longitude", mLastLocation.getLongitude());
                    i.putExtra("Latitude", mLastLocation.getLatitude());
                }
                startActivity(i);

            }

            @Override
            public void onAnimationRepeat(Animation animation){

            }
        });
    }
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.splash_screen_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }
    private boolean photoPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startPhotoPermissionRequest() {
        ActivityCompat.requestPermissions(this,
                new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PHOTO_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPhotoPermissions() {
        boolean shouldProvidephotoRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA);

        if (shouldProvidephotoRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationalex, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startPhotoPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startPhotoPermissionRequest();
        }
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(SplashScreen.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }
    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");

        if (requestCode == PHOTO_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else  if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                locPermission();
            } else {
                final ImageView iv = (ImageView) findViewById(R.id.imageView3);
                final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                showSnackbar(getString(R.string.permission_denied_explanationx));
                iv.startAnimation(an2);
                an2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation){

                    }

                    @Override
                    public void onAnimationEnd(Animation animation){
                        //iv.startAnimation(an2);
                        //Intent intent = new Intent(Intent.ACTION_MAIN);
                        //intent.addCategory(Intent.CATEGORY_HOME);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivity(intent);
                        locPermission();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation){

                    }
                });

            }
        }else if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                final ImageView iv = (ImageView) findViewById(R.id.imageView3);
                final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                showSnackbar(getString(R.string.permission_denied_explanation));
                iv.startAnimation(an2);
                an2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation){

                    }

                    @Override
                    public void onAnimationEnd(Animation animation){
                        //iv.startAnimation(an2);
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation){

                    }
                });


            }
        }
    }
}

