package com.homecare.VCA.models;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;
import com.homecare.VCA.services.IUserDataChangeListener;

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
    public String role;
    private boolean lights;
    private boolean heating;
    private String LastName;
    private String FirstName;
    private String phoneNumber;

    // geofence data
    private Geoposition geo;
    private HashMap<String, Object> GeofenceObject;
    private boolean insideGeofence;
    private long radius;
    private double longitude;
    private double latitude;
    private boolean geofenceEnabled;
    private boolean coordinatesSet = false;

    // caregiver relevant data
    private ArrayList<User> patients;
    private ArrayList<String> patientIds;
    private String messageBoardId;

    // caregiver data for patients
    private ArrayList<String> caretakerIds;
    private ArrayList<User> caretakers;

    // Userdata change listener
    IUserDataChangeListener IUserDataChangeListener;

    //TODO Make user an interface for patient, carer, doctor
    public User() {
        patients = new ArrayList<User>();
        caretakers = new ArrayList<User>();
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

    //public Address getAddress() {
   //     return address;
   // }

    //public void setAddress(Address address) {
    //    this.address = address;
    // }

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
        this.role = null;
        this.auth = null;
        this.UID = null;
        this.geo = null;
        if(patients!=null && patientIds!=null){
           this.patients.clear();
           this.patientIds.clear();
        }


    }

    public void setLocation(Location location) {
        this.geo = new Geoposition(UID);
        this.geo.setLocation(location);
        if(!coordinatesSet){
            this.longitude = geo.getLocation().getLongitude();
            this.latitude = geo.getLocation().getLatitude();
        }
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

    public boolean isGeofenceEnabled() {
        return geofenceEnabled;
    }

    public void setGeofenceEnabled(boolean geofenceEnabled) {
        this.geofenceEnabled = geofenceEnabled;
    }

    public boolean isCoordinatesSet() {
        return coordinatesSet;
    }

    public void setCoordinatesSet(boolean coordinatesSet){
        this.coordinatesSet = coordinatesSet;
    }

    public ArrayList<User> getPatients() {
        return patients;
    }

    public void addPatient(User patient){

        patients.add(patient);
    }

    public User getPatientById(String patientId){
        User result = null;
        for(User p : patients){
            if(patientId.equals(p.getUID())){
                result = p;
                break;
            }
        }

        return result;
    }

    public void changeExistingPatient(User oldPatient, User newPatient){
        User editedPatient = getPatientById(oldPatient.getUID());
        patients.remove(editedPatient);
        patients.add(newPatient);
    }

    public String getMessageBoardId() {
        return messageBoardId;
    }

    public void setMessageBoardId(String messageBoardId) {
        this.messageBoardId = messageBoardId;
    }

    public ArrayList<String> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(ArrayList<String> patientIds) {
        this.patientIds = patientIds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ArrayList<String> getCaretakerIds() {
        return caretakerIds;
    }

    public void setCaretakerIds(ArrayList<String> caretakerIds) {
        this.caretakerIds = caretakerIds;
    }

    public ArrayList<User> getCaretakers() {
        return caretakers;
    }

    public void addCaretaker(User caretaker){
        caretakers.add(caretaker);
    }

    public IUserDataChangeListener getIUserDataChangeListener() {
        return IUserDataChangeListener;
    }

    public void setIUserDataChangeListener(IUserDataChangeListener IUserDataChangeListener) {
        this.IUserDataChangeListener = IUserDataChangeListener;
        Log.i("USER", "Listener object was changed");
    }
}
