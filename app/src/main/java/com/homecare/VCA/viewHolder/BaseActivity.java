package com.homecare.VCA.viewHolder;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.homecare.VCA.R;
import com.homecare.VCA.models.User;

/**
 * Created by dok-1 on 09/11/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public User user;

    public TextView mStatusTextView;
    public TextView mDetailTextView;
    public EditText mEmailField;
    public EditText mPasswordField;
    protected static Boolean mSignedIn = false;

    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
