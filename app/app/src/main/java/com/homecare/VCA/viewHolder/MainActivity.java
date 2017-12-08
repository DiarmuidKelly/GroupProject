package com.homecare.VCA.viewHolder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homecare.VCA.R;
import com.homecare.VCA.services.GeofenceService;
import com.homecare.VCA.services.LocationService;
import com.homecare.VCA.services.IUserDataChangeListener;
import com.homecare.VCA.services.UserDataChangeListener;
import com.homecare.VCA.util.SignOut;

public class MainActivity extends BaseActivity implements IUserDataChangeListener {

    private static String TAG = MainActivity.class.getName();
    private FirebaseFirestore mFirestore;

    // Layout elements
    private Button managementButton;
    private Button medicalButton;
    private Button homeCareButton;
    private Button signOutButton;
    private Button messageBoardsButton;
    private TextView userId;


    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set layout resources
        userId = (TextView) findViewById(R.id.UserID);

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Location settings
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        // Get firestore reference
        mFirestore = FirebaseFirestore.getInstance();

        if(mAuth != null){
            localUser.setAuth(mAuth);
            if(currentUser != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                localUser.setAuth(mAuth);
                localUser.setEmail(user.getEmail());
                localUser.setUsername(user.getDisplayName());
                if(TextUtils.isEmpty((localUser.getEmail()))){
                    localUser.setUsername(user.getEmail());
                }
                localUser.setUID(user.getUid());
                localUser.setFBUser(user);
                localUser.setSignedIn(true);

                // Register listener for userdata
                localUser.setIUserDataChangeListener(MainActivity.this);
                startService(new Intent(MainActivity.this, UserDataChangeListener.class));

            }
        }

        signOutButton = findViewById(R.id.SignOutBtn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out the local user
                new SignOut();

                // stop all services
                stopService(new Intent(MainActivity.this,UserDataChangeListener.class));
                stopService(new Intent(MainActivity.this,GeofenceService.class));
                stopService(new Intent(MainActivity.this,LocationService.class));

                // restart sign in
                Intent signIn = new Intent(MainActivity.this,SignIn.class);
                startActivity(signIn);
                finish();
            }
        });

        homeCareButton = findViewById(R.id.HomeCareBtn);
        homeCareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startHomeCare();
            }
        });
        medicalButton = findViewById(R.id.MedicalBtn);
        medicalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startMedical();
            }
        });
        managementButton = findViewById(R.id.ManagementBtn);
        managementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startManagement();
            }
        });
        messageBoardsButton = findViewById(R.id.MessageBoardsBtn);
        messageBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMessageBoards();
            }
        });

    }

    private void loadMenuOptions(){
        if("patient".equals(localUser.getRole())){
            homeCareButton.setVisibility(View.VISIBLE);
            medicalButton.setVisibility(View.VISIBLE);
            managementButton.setVisibility(View.VISIBLE);
            messageBoardsButton.setVisibility(View.INVISIBLE);
        } else if ("caretaker".equals(localUser.getRole())){
            homeCareButton.setVisibility(View.INVISIBLE);
            medicalButton.setVisibility(View.INVISIBLE);
            managementButton.setVisibility(View.VISIBLE);
            messageBoardsButton.setVisibility(View.VISIBLE);
        }

    }

    private void startHomeCare() {
        Intent homeCareIntent = new Intent(MainActivity.this, HomeCare.class);
        startActivity(homeCareIntent);

    }
    private void startMedical() {
        Intent medicalIntent = new Intent(MainActivity.this, Medical.class);
        startActivity(medicalIntent);
    }

    private void startManagement() {

        Intent managementIntent = new Intent(MainActivity.this, Management.class);
        startActivity(managementIntent);

    }

    private void startMessageBoards(){

        Intent messagesIntent = new Intent(MainActivity.this, MessageBoardsActivity.class);
        startActivity(messagesIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // re-register onData changed listener
        localUser.setIUserDataChangeListener(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        if(checkPermissions()){
                            // Launch background service - Monitor location
                            Log.i(TAG, "trying to start location service..");
                            startService(new Intent(MainActivity.this, LocationService.class));

                            // Launch background service - Geofence
                            if(localUser.isGeofenceEnabled()){
                                Log.i(TAG, "trying to start geofence service..");
                                startService(new Intent(MainActivity.this, GeofenceService.class));
                            }
                        } else{
                            requestPermissions();
                        }



                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.
                Log.i(TAG, "Permission denied");
            }
        }
    }

    @Override
    public void onUserDataChanged(boolean result) {
        Log.i(TAG, "user was changed --> coming from service");
        if(!result){
            Log.d(TAG, "No such document");
            new SignOut();
            // restart sign in
            Intent signIn = new Intent(MainActivity.this,SignIn.class);
            startActivity(signIn);
            finish();
        } else {
            userId.setText("Welcome " + localUser.getFirstName() + " " + localUser.getLastName());
            loadMenuOptions();

            if(localUser.getRole().equals("patient")){
                startLocationUpdates();
            }
        }
    }
}
