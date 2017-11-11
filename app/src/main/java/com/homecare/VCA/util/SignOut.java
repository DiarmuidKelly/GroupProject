package com.homecare.VCA.util;

import android.os.Bundle;
import com.homecare.VCA.models.User;
import com.homecare.VCA.viewHolder.BaseActivity;


public class SignOut extends BaseActivity {

    public SignOut(){
        mAuth.signOut();
        localUser.resetAll();

    }


}