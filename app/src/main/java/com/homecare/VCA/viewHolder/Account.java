package com.homecare.VCA.viewHolder;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.homecare.VCA.R;
import com.google.firebase.firestore.FirebaseFirestore;



/**
 * Created by dok-1 on 10/11/2017.
 */

public class Account extends BaseActivity {

    public static final String KEY_USER_ID = "key_user_id";



    private FirebaseFirestore mFirestore;
    private DocumentReference mUserRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String userID = getIntent().getExtras().getString(KEY_USER_ID);

        mFirestore = FirebaseFirestore.getInstance();

        mUserRef = mFirestore.collection("users").document(userID);

        Query ratingsQuery = mUserRef
                .collection("geoLocation")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);


    }


}