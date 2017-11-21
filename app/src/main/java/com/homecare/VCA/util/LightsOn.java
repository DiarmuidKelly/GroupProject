package com.homecare.VCA.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.homecare.VCA.viewHolder.BaseActivity;

/**
 * Created by dok-1 on 21/11/2017.
 */

public class LightsOn extends BaseActivity {

    Boolean state = true;

    private Task<Void> updateLightState() {
        if (state != null) {
            localUser.setLocation(mCurrentLocation);
            localUser.setLocationTime(mLastUpdateTime);
            final DocumentReference geoRef = this.mUserRef.collection("geoPosition").document();
            return this.mFirestore.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    // Commit to Firestore
                    transaction.set(geoRef, localUser.geo);

                    return null;
                }
            });
        }
        if(mLastUpdateTime != "")
            return null;

        return null;
    }
}
