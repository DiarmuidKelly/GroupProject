package com.homecare.VCA;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button homeCareButton = (Button) findViewById(R.id.HomeCareBtn);
        homeCareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Home Care Button Clicked");
                startHomeCare();
            }
        });
        Button medicalButton = (Button) findViewById(R.id.MedicalBtn);
        medicalButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Home Care Button Clicked");
                startMedical();
            }
        });
        Button securityButton = (Button) findViewById(R.id.SecurityBtn);
        securityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Home Care Button Clicked");
                startSecurity();
            }
        });
        Button managementButton = (Button) findViewById(R.id.ManagementBtn);
        managementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("Home Care Button Clicked");
                startManagement();
            }
        });
    }

    private void startHomeCare() {
        this.setTitle("Home Care");
        Intent homeCareIntent = new Intent(MainActivity.this, HomeCare.class);
        startActivity(homeCareIntent);

    }
    private void startMedical() {
        this.setTitle("Medical");
        Intent medicalIntent = new Intent(MainActivity.this, Medical.class);
        startActivity(medicalIntent);

    }
    private void startSecurity() {
        this.setTitle("Security");
        Intent securityIntent = new Intent(MainActivity.this, Security.class);
        startActivity(securityIntent);

    }
    private void startManagement() {
        this.setTitle("Management");
        Intent managementIntent = new Intent(MainActivity.this, Management.class);
        startActivity(managementIntent);

    }
}
