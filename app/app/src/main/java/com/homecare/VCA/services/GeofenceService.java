package com.homecare.VCA.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.homecare.VCA.R;
import com.homecare.VCA.models.User;
import com.homecare.VCA.viewHolder.BaseActivity;
import com.homecare.VCA.viewHolder.MainActivity;

import java.util.ArrayList;
import java.util.List;


public class GeofenceService extends Service implements OnCompleteListener<Void> {

    // Constants
    private static final String TAG = "GeofenceService";
    private static final String GEOFENCE_KEY = "VCA_Geofence";
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    // local user object
    private User localUser;

    // Geofence relevant
    private GeofencingClient mGeofencingClient;
    private long mGeoFenceRadius;
    private double baseLongitude;
    private double baseLatitude;

    private Geofence geofence;
    private PendingIntent mGeofencePendingIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        localUser = BaseActivity.getLocalUser();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // stop geofence service
        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.w(TAG, "deleting geofence was successful!");
                } else {
                    // Get the status code for the error and log it using a user-friendly message.
                    Log.w(TAG, "deleting geofence was not successful");
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // check if geofence is enabled for the local user
        if(localUser.isGeofenceEnabled()){

            mGeoFenceRadius = localUser.getRadius();
            baseLatitude = localUser.getLatitude();
            baseLongitude = localUser.getLongitude();

            // start setting up the geofence
            geofence = new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(GEOFENCE_KEY)

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            baseLatitude,
                            baseLongitude,
                            mGeoFenceRadius
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build();

            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.w(TAG, "adding geofence was successful!");
                            } else {
                                // Get the status code for the error and log it using a user-friendly message.
                                Log.w(TAG, "adding geofence was not successful");
                            }
                        }
                    });
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(geofence);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.w(TAG, "task was successful!");
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            Log.w(TAG, "task was not successful");
        }
    }

}
