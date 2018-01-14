package com.nwhacks.safetalk;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by nandinibulusu on 2018-01-13.
 */

public class SignUpActivity {
    private FirebaseAuth mAuth;
    protected void OnCreate (Bundle savedInstanceState) {
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }

    }
}
