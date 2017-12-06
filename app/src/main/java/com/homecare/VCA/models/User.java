package com.homecare.VCA.models;

import android.location.Location;
import android.webkit.GeolocationPermissions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    // Firebase auth data
    public Boolean signedIn;
    public String username;
    public String email;
    public FirebaseAuth auth;
    public FirebaseUser fbUser;
    public String UID;

    // fields from the firebase database
    public Date age;
    public Address address;
    public String role;

    private boolean lights;
    private boolean heating;
    private String LastName;
    private String FirstName;

    // geofence data
    public Geoposition geo;
    HashMap<String, Object> GeofenceObject;
    private boolean insideGeofence;
    private long radius;
    private double longitude;
    private double latitude;


    //TODO Make user an interface for patient, carer, doctor
    public User() {

        this.resetAll();

    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getAge() {
        return age;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public FirebaseAuth getAuth() {
        return auth;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
    public void resetAll(){
        this.signedIn = false;
        this.username = null;
        this.email = null;
        this.age = null;
        this.address = new Address();
        this.role = null;
        this.auth = null;
        this.UID = null;
        this.geo = null;

    }

    public void setLocation(Location location) {
        this.geo = new Geoposition(UID);
        this.geo.setLocation(location);
    }

    public void setLocationTime(String locationTime) {

        this.geo.setLastUpdateTime(locationTime);
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getUID(){ return UID; }
    public Geoposition getGeo(){ return geo; }

    public void setFBUser(FirebaseUser FBUser) {
        this.fbUser = FBUser;
    }

    public Object getGeofenceObject() { return GeofenceObject; }

    public void setGeofenceObject(HashMap GeofenceObject) {
        this.GeofenceObject = GeofenceObject;
    }

    public boolean isLights() {
        return lights;
    }

    public void setLights(boolean lights) {
        this.lights = lights;
    }

    public boolean isHeating() {
        return heating;
    }

    public void setHeating(boolean heating) {
        this.heating = heating;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lName) {
        this.LastName = lName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String fName) {
        this.FirstName = fName;
    }

    public boolean isInsideGeofence() {
        return insideGeofence;
    }

    public void setInsideGeofence(boolean insideGeofence) {
        this.insideGeofence = insideGeofence;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
