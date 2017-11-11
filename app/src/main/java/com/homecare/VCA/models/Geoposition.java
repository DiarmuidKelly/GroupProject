package com.homecare.VCA.models;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Model POJO for a rating.
 */
public class Geoposition {

    private String userId;
    private String userName;
    private double longitude;
    private double latitude;
    private @ServerTimestamp Date timestamp;

    public Geoposition() {}

    public Geoposition(FirebaseUser user, double lon, double lat) {
        this.userId = user.getUid();
        this.userName = user.getDisplayName();
        if (TextUtils.isEmpty(this.userName)) {
            this.userName = user.getEmail();
        }

        this.longitude = lon;
        this.latitude = lat;
    }

}
