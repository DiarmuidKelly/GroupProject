package com.homecare.VCA.services;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by mandy on 12/8/17.
 */

public interface IUserDataChangeListener {

    public void onUserDataChanged(boolean result);
}
