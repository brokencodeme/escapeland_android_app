package com.escapeland.app;

import android.content.Intent;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.support.v7.widget.LinearSnapHelper;
//import android.support.v7.widget.SnapHelper;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
//import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
//import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
//import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.escapeland.app.R;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    EditText login_input_email, login_input_password, input_signup_name, input_signup_email, input_signup_password, input_signup_confirm_password;
    Button bottom_login_button, bottom_signup_signup_btn;
    private FirebaseAuth mFireBaseAuth;
    AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("main-activity", " "+ Resources.getSystem().getDisplayMetrics().widthPixels+" "+Resources.getSystem().getDisplayMetrics().heightPixels);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mFireBaseAuth = FirebaseAuth.getInstance();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
    }

    @Override
    public void onStart() {
        super.onStart();

        // check if user is already sign in

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
        Log.d("pratik", "user is active" + currentUser);


        if (currentUser != null) {

            if (checkIfUserIsFbUser(currentUser)) {
                Log.d("pratik", "2 fb-user " + currentUser.getUid() + " " + currentUser.getDisplayName() + "");
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else if (checkIfEmailVerified()) {
                Log.d("pratik", "2 email-verified");
                Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));


            } else {
                Log.d("pratik", " 2 email-not-verified");
                startActivity(new Intent(MainActivity.this, VerifyEmailActivity.class));
            }
        } else {
            Log.d("pratik", " 1 user not logged in");
            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRun", true);

            Log.d("is-first-run", ""+isFirstRun);
            if (isFirstRun) {
                //show start activity
                Log.d("shared-preference", " in main activity "+isFirstRun);

                try {
                    startActivity(new Intent(MainActivity.this, MainLoginSignupActivity.class));
                } catch (Exception e) {
                    Log.d("pratik", " 1 exception " + e);

                }
            }
            else {
                Log.d("shared-preference", " "+isFirstRun);

                startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
            }
        }
    }

    public boolean checkIfEmailVerified() {
        Log.d("9 pratik", "" + mFireBaseAuth.getCurrentUser().isEmailVerified());
//        Toast.makeText(MainActivity.this, "in checkIfemailVerified", Toast.LENGTH_SHORT).show();
        boolean isVerified;
        isVerified = mFireBaseAuth.getCurrentUser().isEmailVerified();
        return isVerified;
    }

    public void redirectToHome() {
        Log.d("10 ", "");
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    public boolean checkIfUserIsFbUser(FirebaseUser currentUser) {

        for (UserInfo profile : currentUser.getProviderData()) {

            if (profile.getProviderId().equals("facebook.com")) {
                Log.d(" pratik", "2 checking fb user return true" + profile.getUid());
                return true;
            }
        }
        Log.d(" pratik", "2 checking fb user return false");
        return false;
    }

}
