package com.homecare.VCA.viewHolder;

import android.os.Bundle;
import com.homecare.VCA.models.User;


public class SignOut extends BaseActivity{

    SignOut(){
        mAuth.signOut();
        localUser.resetAll();

    }


}