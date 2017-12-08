package com.homecare.VCA.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.homecare.VCA.models.User;
import com.homecare.VCA.services.GeofenceService;
import com.homecare.VCA.services.LocationService;
import com.homecare.VCA.viewHolder.BaseActivity;
import com.homecare.VCA.viewHolder.SignIn;


public class SignOut extends BaseActivity {

    public SignOut(){
        mAuth.signOut();
        localUser.resetAll();
        //stopService(new Intent(getApplicationContext(), LocationService.class));

    }

}