package com.homecare.VCA.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.homecare.VCA.models.User;
import com.homecare.VCA.util.SignOut;
import com.homecare.VCA.viewHolder.BaseActivity;
import com.homecare.VCA.viewHolder.MainActivity;
import com.homecare.VCA.viewHolder.SignIn;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mandy on 12/8/17.
 */

public class UserDataChangeListener extends Service {

    private static String TAG = UserDataChangeListener.class.getName();

    private User localUser = BaseActivity.getLocalUser();
    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFirestore = FirebaseFirestore.getInstance();
        mUserRef = mFirestore.collection("users").document(localUser.getUID());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "UserDataChangedListener service is working in the background");

        requestUserData(localUser.getUID());

        return super.onStartCommand(intent, flags, startId);
    }


    public void requestUserData(String userId){
        // Get additional information from database
        mFirestore.collection("users")
                .document(localUser.getUID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                ? "Local" : "Server";

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
                            setLocalUserFields(snapshot);

                        } else {
                            Log.d(TAG, "No such document");
                            localUser.getIUserDataChangeListener().onUserDataChanged(false);
                        }
                    }
                });
    }

    private void setLocalUserFields(DocumentSnapshot document){
        // get user fields
        localUser.setLastName((String) document.getData().get("lName"));
        localUser.setFirstName((String) document.getData().get("fName"));
        localUser.setPhoneNumber((String) document.getData().get("phoneNumber"));
        localUser.setRole((String) document.getData().get("role"));

        if(localUser.getRole().equals("patient")){

            localUser.setHeating((boolean) document.getData().get("heating"));
            localUser.setLights((boolean) document.getData().get("lights"));

            // get geofence object
            HashMap<String, Object> geoFence = (HashMap<String, Object>) document.getData().get("geoFence");
            localUser.setGeofenceObject(geoFence);
            localUser.setGeofenceEnabled((boolean) geoFence.get("geoFenceEnabled"));

            if(localUser.isGeofenceEnabled()){
                localUser.setInsideGeofence((boolean) geoFence.get("insideGeofence"));
                localUser.setRadius((long) geoFence.get("radius"));

                // if the user was just registered we must set the initial geofence center
                // this will be done when the location service runs the first time
                if(geoFence.get("longitude")==null || geoFence.get("latitude") == null){
                    localUser.setCoordinatesSet(false);
                } else {
                    localUser.setCoordinatesSet(true);
                }
            }

            // get associated caretakers
            mUserRef.collection("caretakers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> caretakerIds = new ArrayList<String>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    caretakerIds.add(document.getId());
                                    getCaretakerInfo(document.getId());
                                }

                                // update the user object
                                localUser.setCaretakerIds(caretakerIds);
                                localUser.getIUserDataChangeListener().onUserDataChanged(true);

                            } else {
                                Log.d(TAG, "Error getting patient documents: ", task.getException());
                            }
                        }
                    });

        } else {
            // add patients to caretaker list
            mUserRef.collection("patients")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> patientIds = new ArrayList<String>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    patientIds.add(document.getId());
                                    getPatientInfo(document.getId());
                                }

                                // update the user object
                                localUser.setPatientIds(patientIds);
                                localUser.getIUserDataChangeListener().onUserDataChanged(true);

                            } else {
                                Log.d(TAG, "Error getting patient documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void getPatientInfo(String patientId){
        // Get additional information from database
        mFirestore.collection("users")
                .document(patientId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                ? "Local" : "Server";

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, source + " data: " + snapshot.getData());

                            User existingPatient = localUser.getPatientById(snapshot.getId());
                            if(existingPatient!=null){
                                localUser.changeExistingPatient(existingPatient, setPatientData(snapshot));
                            } else {
                                localUser.addPatient(setPatientData(snapshot));
                            }

                            // TODO add PatientChangeListener
                            //localUser.getIUserDataChangeListener().onUserDataChanged(null);

                        } else {
                            Log.d(TAG, source + " data: null");
                        }
                    }
                });
    }

    private User setPatientData(DocumentSnapshot document){
        User patient = new User();
        patient.setLastName((String) document.getData().get("lName"));
        patient.setFirstName((String) document.getData().get("fName"));
        patient.setPhoneNumber((String) document.getData().get("phoneNumber"));
        patient.setRole((String) document.getData().get("role"));
        patient.setUID((String) document.getData().get("userId"));
        patient.setHeating((boolean) document.getData().get("heating"));
        patient.setLights((boolean) document.getData().get("lights"));
        patient.setMessageBoardId((String) document.getData().get("messageBoard"));

        // get geofence object
        HashMap<String, Object> geoFence =
                (HashMap<String, Object>) document.getData().get("geoFence");
        patient.setGeofenceObject(geoFence);
        patient.setGeofenceEnabled((boolean) geoFence.get("geoFenceEnabled"));

        if(patient.isGeofenceEnabled()){
            patient.setInsideGeofence((boolean) geoFence.get("insideGeofence"));
            patient.setRadius((long) geoFence.get("radius"));
        }

        return patient;
    }

    private void getCaretakerInfo(String caretakerId){
        mFirestore.collection("users")
                .document(caretakerId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                ? "Local" : "Server";

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, source + " data: " + snapshot.getData());

                            localUser.addCaretaker(setCaretakerInfo(snapshot));

                        } else {
                            Log.d(TAG, source + " data: null");
                        }
                    }
                });
    }

    private User setCaretakerInfo(DocumentSnapshot document){
        User caretaker = new User();
        caretaker.setFirstName((String) document.getData().get("fName"));
        caretaker.setLastName((String) document.getData().get("lName"));
        caretaker.setRole((String) document.getData().get("role"));
        caretaker.setPhoneNumber((String) document.getData().get("phoneNumber"));
        caretaker.setUID((String) document.getData().get("userId"));

        return caretaker;
    }
}
