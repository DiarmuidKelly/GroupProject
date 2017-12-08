package com.homecare.VCA.viewHolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.homecare.VCA.R;
import com.homecare.VCA.services.IUserDataChangeListener;

public class HomeCare extends BaseActivity implements IUserDataChangeListener {

    private static final String TAG = HomeCare.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private Switch lightSwitch, heatingSwitch;
    private Button taxiButton, openCameraButton, playMusicButton;

    private final String[] takeaway_menu_options = {"Please choose", "Italian", "Chinese", "Indian", "Mexican"};

    private final String[] shopping_menu_options = {"Please choose", "Tesco", "Supervalu", "Marks & Spencer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get firestore reference
        mFirestore = FirebaseFirestore.getInstance();

        // initiate view's
        setContentView(R.layout.activity_home_care);

        // register listener
        localUser.setIUserDataChangeListener(HomeCare.this);
        updateUI();
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
        lightSwitch = (Switch) findViewById(R.id.lightSwitch);
        heatingSwitch = (Switch) findViewById(R.id.heatingSwitch);
        taxiButton = (Button) findViewById(R.id.taxiBttn);
        openCameraButton = (Button) findViewById(R.id.cameraBttn);
        playMusicButton = (Button) findViewById(R.id.musicBttn);

        lightSwitch.setChecked(localUser.isLights());
        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                changeLightsSetting(localUser.getUID(),isChecked);
            }
        });

        heatingSwitch.setChecked(localUser.isHeating());
        heatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                changeHeatingSetting(localUser.getUID(),isChecked);
            }
        });

        Spinner order_takeaway_spinner = (Spinner) findViewById(R.id.order_take_away_spinner);
        Spinner order_shopping_spinner = (Spinner) findViewById(R.id.order_shopping_spinner);

        ArrayAdapter<String> takeawayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_entry, takeaway_menu_options);
        ArrayAdapter<String> shoppingAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_entry, shopping_menu_options);

        takeawayAdapter.setDropDownViewResource(R.layout.spinner_entry);
        shoppingAdapter.setDropDownViewResource(R.layout.spinner_entry);

        order_takeaway_spinner.setAdapter(takeawayAdapter);
        order_shopping_spinner.setAdapter(shoppingAdapter);

        order_takeaway_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView <?> parent,
                                       View view, int pos, long id) {

                if (pos == 1){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://order.apache.ie/"));
                    startActivity(browserIntent);
                }
                if (pos == 2){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lemontreecork.ie/"));
                    startActivity(browserIntent);
                }
                if (pos == 3){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://indianmoon.ie/"));
                    startActivity(browserIntent);
                }
                if (pos == 4){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.boojummex.com/clickncollectroi/"));
                    startActivity(browserIntent);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
                Toast.makeText(parent.getContext(), "onNothingSelected,is never called ", Toast.LENGTH_LONG).show();
            }
        });

        order_shopping_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView <?> parent,
                                       View view, int pos, long id) {

                if (pos == 1){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tesco.ie/groceries/?sc_cmp=ppc*sl*me*bg*px_-_campaign_not_set*tesco%20online%20shopping&gclid=Cj0KCQiAjO_QBRC4ARIsAD2FsXNAQDQcgW_IaZsHUektwRQN9nikWgqsT2oEhdYVlOGJDIWWNjuIsUoaAi3OEALw_wcB"));
                    startActivity(browserIntent);
                }
                if (pos == 2){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shop.supervalu.ie/shopping/selected-offers"));
                    startActivity(browserIntent);
                }
                if (pos == 3){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://christmasfood.marksandspencer.ie/?intid=CFTO-1"));
                    startActivity(browserIntent);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
                Toast.makeText(parent.getContext(), "onNothingSelected,is never called ", Toast.LENGTH_LONG).show();
            }
        });

        // add button listener
        taxiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:(021) 434 6666"));

                if (ActivityCompat.checkSelfPermission(HomeCare.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                startActivity(callIntent);
            }
        });

        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraButton.onCancelPendingInputEvents();
            }
        });
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
        localUser.setIUserDataChangeListener(HomeCare.this);
    }
}
