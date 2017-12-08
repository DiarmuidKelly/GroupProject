package com.homecare.VCA.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.homecare.VCA.R;
import com.homecare.VCA.models.User;
import com.homecare.VCA.services.IUserDataChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Management extends BaseActivity implements IUserDataChangeListener {

    private static final String TAG = Management.class.getName();

    private LinearLayout patientLayout;
    private LinearLayout caretakerLayout;
    private TextView generatedPIN;
    private ListView patientsListView;
    private Button generateNewPINButton;

    private FirebaseFirestore mFirestore;
    final ArrayList<User> patients = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        // Get firestore reference
        mFirestore = FirebaseFirestore.getInstance();

        // Get layouts
        patientLayout = (LinearLayout) findViewById(R.id.managementPatient);
        caretakerLayout = (LinearLayout) findViewById(R.id.managementCaretaker);

        // register listener
        localUser.setIUserDataChangeListener(Management.this);

        updateUI();
    }

    private int generateRandomNumber(){
        Random r = new Random();
        int number = r.nextInt(59999 - 0);
        return number;
    }

    private void changeHeatingSetting(String patientId, boolean state){
        mFirestore.collection("users").document(patientId)
                .update(
                        "heating", state
                );
    }

    private void changeLightsSetting(String patientId, boolean state){
        mFirestore.collection("users").document(patientId)
                .update(
                        "lights", state
                );
    }

    private void updateUI(){

        if(localUser.getRole().equals("patient")){
            caretakerLayout.setVisibility(LinearLayout.GONE);
            generatedPIN = (TextView) findViewById(R.id.textViewGeneratedPIN);
            generateNewPINButton = (Button) findViewById(R.id.buttonGenerateRandomPIN);
            generateNewPINButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Implement generation of random number here
                    int newPin = generateRandomNumber();

                    // Create object to upload to database
                    final Map<String, Object> code = new HashMap<>();
                    code.put("patientNo", localUser.getUID());
                    code.put("uniqueIdentifier", String.valueOf(newPin));

                    // save the PIN to the database
                    mFirestore.collection("codes")
                            .add(code)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.i(TAG, "New code was saved to database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing code with PIN to database ", e);
                                }
                            });

                    // display the PIN on the UI
                    generatedPIN.setText(getString(R.string.generatedPIN) + " " + newPin);
                }
            });

        } else {
            patientLayout.setVisibility(LinearLayout.GONE);
            patientsListView = (ListView) findViewById(R.id.listViewPatients);

            final User[] array = new User[localUser.getPatientIds().size()];
            localUser.getPatients().toArray(array);

            PatientsArrayAdapter adapter = new PatientsArrayAdapter(getApplicationContext(),array);
            patientsListView.setAdapter(adapter);

        }
    }

    @Override
    public void onUserDataChanged(boolean result) {
        Log.i(TAG, "user was changed --> coming from service");
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register listener
        localUser.setIUserDataChangeListener(Management.this);
    }

    private class PatientsArrayAdapter extends ArrayAdapter<User> {
        private final Context context;
        private final User[] values;

        public PatientsArrayAdapter(Context context, User[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final User currentPatient = values[position];
            // create custom layout for each list item
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_view_item_patient, parent, false);

            TextView textViewPatientName = (TextView) rowView.findViewById(R.id.textViewPatientName);
            TextView textViewInGeofence = (TextView) rowView.findViewById(R.id.textViewGeofenceOutIn);

            textViewPatientName.setText(currentPatient.getFirstName() + " " + currentPatient.getLastName());

            if(currentPatient.isInsideGeofence()){
                textViewInGeofence.setText("is currently inside the geofence.");
                textViewInGeofence.setBackgroundColor(Color.GREEN);
            } else {
                textViewInGeofence.setText("is currently outside the geofence.");
                textViewInGeofence.setBackgroundColor(Color.RED);
            }

            Switch patientHeating = (Switch) rowView.findViewById(R.id.switchPatientHeating);
            patientHeating.setChecked(currentPatient.isHeating());
            patientHeating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    changeHeatingSetting(currentPatient.getUID(),b);
                }
            });

            Switch patientLights = (Switch) rowView.findViewById(R.id.switchPatientLight);
            patientLights.setChecked(currentPatient.isLights());
            patientLights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    changeLightsSetting(currentPatient.getUID(),b);
                }
            });

            Button showLastLocation = (Button) rowView.findViewById(R.id.buttonShowLastLocation);
            showLastLocation.setOnClickListener(locationButtonClickListener);

            return rowView;
        }

        private View.OnClickListener locationButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);

                // get the last known position from firebase
                mFirestore.collection("users")
                        .document(localUser.getPatientIds().get(position))
                        .collection("geoPosition")
                        .orderBy("lastUpdateTime").limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    String lon = document.get("longitude").toString();
                                    String lat = document.get("latitude").toString();

                                    // Create a Uri from an intent string. Use the result to create an Intent.
                                    Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+ lat +","+ lon);

                                    // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    // Make the Intent explicit by setting the Google Maps package
                                    mapIntent.setPackage("com.google.android.apps.maps");

                                    // Attempt to start an activity that can handle the Intent
                                    startActivity(mapIntent);
                                    break;
                                }

                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });


            }
        };

    }
}
