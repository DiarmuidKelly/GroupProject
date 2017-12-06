package com.homecare.VCA.models;

import android.location.Location;
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
    private Location location;
    private @ServerTimestamp Date timestamp;
    public String lastUpdateTime;

    public Geoposition() {}

    public Geoposition(String user) {
        this.userId = user;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Location getLocation() {
        return location;
    }
}
