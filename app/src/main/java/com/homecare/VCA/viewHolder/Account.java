package com.homecare.VCA.viewHolder;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.homecare.VCA.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dok-1 on 10/11/2017.
 */

public class Account extends BaseActivity {

    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateNumber();

    }


    private void updateNumber() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("users").push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + getUid() + "/" + key, 1);

        mDatabase.updateChildren(childUpdates);
    }

}
