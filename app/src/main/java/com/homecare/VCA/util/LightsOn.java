package com.homecare.VCA.util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.homecare.VCA.viewHolder.BaseActivity;

/**
 * Created by dok-1 on 21/11/2017.
 */

public class LightsOn extends BaseActivity {

    Boolean state = true;


    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;

    public LightsOn(){

        mFirestore = FirebaseFirestore.getInstance();

        mUserRef = mFirestore.collection("users").document(localUser.getUID());

        updateLightState();
    }

    private Task<Void> updateLightState() {
        if (state != null) {
            final DocumentReference lightRef = this.mUserRef.collection("lights").document();
            return this.mFirestore.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    // Commit to Firestore
                    transaction.set(lightRef, lightState);

                    return null;
                }
            });
        }
        return null;
    }
}
