package com.homecare.VCA.viewHolder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.homecare.VCA.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                                Button signInButton = (Button) findViewById(R.id.SignInBtn);
                                if(mSignedIn == true){
                                    signInButton.setText("Sign Out");
                                }else if(mSignedIn == false) {
                                    signInButton.setText("Sign In");
                                }
                                signInButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        System.out.println("Sign In Button Clicked");
                                        if( mSignedIn == true){
                                            startSignOut();
                                        }else if(mSignedIn == false) {
                                            startSignIn();
                                        }

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
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();



    }

    private void startHomeCare() {
        Intent homeCareIntent = new Intent(MainActivity.this, HomeCare.class);
        startActivity(homeCareIntent);

    }
    private void startMedical() {
        Intent medicalIntent = new Intent(MainActivity.this, Medical.class);
        startActivity(medicalIntent);

    }
    private void startSignIn() {
        Intent signInIntent = new Intent(MainActivity.this, SignIn.class);
        startActivity(signInIntent);

    }
    private void startSignOut(){
        Intent signOutIntent = new Intent(MainActivity.this, SignOut.class);
        startActivity(signOutIntent);
    }
    private void startManagement() {
        Intent managementIntent = new Intent(MainActivity.this, Management.class);
        startActivity(managementIntent);

    }
}
