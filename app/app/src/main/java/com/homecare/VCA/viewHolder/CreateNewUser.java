package com.homecare.VCA.viewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.homecare.VCA.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateNewUser extends BaseActivity {

    private static final String TAG = CreateNewUser.class.getName();

    // UI elements
    private Button createNewUser;
    private RadioGroup radioGroup;
    private CheckBox geofenceCheckbox;
    private EditText editTextFirstname, editTextLastname, editTextPhonenumber;
    private EditText editTextRadius;
    private EditText editTextPatientPIN;
    private LinearLayout geofenceSetup;
    private LinearLayout patientPINLayout;

    private String role;
    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        // get firestore
        // Get firestore reference
        mFirestore = FirebaseFirestore.getInstance();

        // set up layout
        geofenceSetup = (LinearLayout) findViewById(R.id.geofenceSetup);
        patientPINLayout = (LinearLayout) findViewById(R.id.patientPINLayout);
        patientPINLayout.setVisibility(LinearLayout.GONE);

        editTextFirstname = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastname = (EditText) findViewById(R.id.editTextLastName);
        editTextRadius = (EditText) findViewById(R.id.editTextRadius);
        editTextPatientPIN = (EditText) findViewById(R.id.editTextPatientPIN);
        editTextPhonenumber = (EditText) findViewById(R.id.editTextPhonenumber);
        geofenceCheckbox = (CheckBox) findViewById(R.id.checkBox);

        //default settings
        role = "patient";

        radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.radioButtonPatient){
                    role = "patient";
                    geofenceSetup.setVisibility(LinearLayout.VISIBLE);
                    patientPINLayout.setVisibility(LinearLayout.GONE);
                } else {
                    role = "caretaker";
                    geofenceSetup.setVisibility(LinearLayout.GONE);
                    patientPINLayout.setVisibility(LinearLayout.VISIBLE);

                    EditText patientPIN = new EditText(getApplicationContext());
                    patientPIN.setLayoutParams(
                            new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT))
                    ;
                }
            }
        });

        geofenceCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    editTextRadius.setEnabled(true);
                } else {
                    editTextRadius.setEnabled(false);
                }
            }
        });

        createNewUser = (Button) findViewById(R.id.buttonCreateUser);
        createNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressDialog();

                // set up data to send to database
                final Map<String, Object> user = new HashMap<>();
                user.put("fName", editTextFirstname.getText().toString());
                user.put("lName", editTextLastname.getText().toString());
                user.put("phoneNumber", editTextPhonenumber.getText().toString());

                if(role.equals("patient")){
                    user.put("role","patient");
                    user.put("heating", false);
                    user.put("lights", false);

                    Map<String, Object> geoFence = new HashMap<>();
                    if(geofenceCheckbox.isChecked()){
                        geoFence.put("geoFenceEnabled", true);
                        geoFence.put("insideGeofence", true);
                        geoFence.put("radius", Long.parseLong(editTextRadius.getText().toString()));
                    } else {
                        geoFence.put("geoFenceEnabled", false);
                    }
                    user.put("geoFence", geoFence);

                    // send data to firebase to user object
                    saveUserToDatabase(user, null);
                } else {
                    user.put("role", "caretaker");

                    // start searching for patient using provided PIN
                    mFirestore.collection("codes")
                            .whereEqualTo("uniqueIdentifier",
                                    editTextPatientPIN.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        ArrayList<String> patientIds = new ArrayList<String>();
                                        for (DocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            String patientId = (String) document.getData().get("patientNo");
                                            patientIds.add(patientId);
                                        }

                                        if(patientIds.size()<0){
                                            hideProgressDialog();
                                            Toast.makeText(getApplicationContext(),
                                                    "No patient found ",Toast.LENGTH_SHORT).show();
                                        } else {
                                            // send data to firebase to user object
                                            saveUserToDatabase(user, patientIds.get(0));
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }

            }
        });

    }

    private void saveUserToDatabase(Map user, final String patientId){
        final DocumentReference userRef =
                mFirestore.collection("users").document(localUser.getUID());

        userRef.set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User was successfully added to database!");

                        if(role.equals("caretaker")){
                            // add the cargiver to the patient and vice versa
                            linkCaregiverAndPatient(patientId);
                        } else {
                            // Load the MainActivity
                            hideProgressDialog();
                            startActivity(new Intent(CreateNewUser.this,MainActivity.class));
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    private void linkCaregiverAndPatient(String patientId) {
        final Map<String, Object> emptyObject = new HashMap<>();

        mFirestore.collection("users").document(patientId)
                .collection("caretakers").document(localUser.getUID())
                .set(emptyObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Caregiver was added to patient!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        mFirestore.collection("users").document(localUser.getUID())
                .collection("patients").document(patientId)
                .set(emptyObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Patient was added to caregiver");
                        hideProgressDialog();
                        // Load the MainActivity
                        startActivity(new Intent(CreateNewUser.this,MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

}
