package com.homecare.VCA.viewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.homecare.VCA.R;
import com.homecare.VCA.util.SignOut;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseUser currentUser = mAuth.getCurrentUser();


        if(mAuth != null){
            localUser.setAuth(mAuth);
            if(currentUser != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                localUser.setAuth(mAuth);
                localUser.setEmail(user.getEmail());
                localUser.setUsername(user.getDisplayName());
                if(TextUtils.isEmpty((localUser.getEmail()))){
                    localUser.setUsername(user.getEmail());
                };
                localUser.setUID(user.getUid());
                localUser.setFBUser(user);
                localUser.setSignedIn(true);
            }
        }

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Boolean SignedIn = localUser.getSignedIn();
                                Button signInButton = (Button) findViewById(R.id.SignInBtn);
                                TextView UserIDField =  (TextView) findViewById(R.id.UserID);
                                if(localUser.getSignedIn() == true){
                                    UserIDField.setText("Signed in: " + localUser.getEmail());
                                    signInButton.setText("Sign Out");
                                }else if(localUser.getSignedIn() == false) {
                                    UserIDField.setText("Signed Out");
                                    signInButton.setText("Sign In");
                                }
                                signInButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        System.out.println("Sign In Button Clicked");
                                        if( localUser.getSignedIn() == true){
                                            new com.homecare.VCA.util.SignOut();
                                        }else if(localUser.getSignedIn() == false) {
                                            startSignIn();
                                        }

                                    }
                                });
                                ////////////////////////////////////////////////////
                                Button accountButton = (Button) findViewById(R.id.AccountBtn);
                                accountButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        System.out.println("Account Button Clicked");
                                        startAccount();
                                    }
                                });
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
                                        System.out.println("Medical Button Clicked");
                                        startMedical();
                                    }
                                });
                                Button managementButton = (Button) findViewById(R.id.ManagementBtn);
                                managementButton.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        System.out.println("Management Button Clicked");
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
    private void startAccount() {
        Intent accountIntent = new Intent(MainActivity.this, Account.class);
        startActivity(accountIntent);

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
    private void startManagement() {
        Intent managementIntent = new Intent(MainActivity.this, Management.class);
        startActivity(managementIntent);

    }
}
