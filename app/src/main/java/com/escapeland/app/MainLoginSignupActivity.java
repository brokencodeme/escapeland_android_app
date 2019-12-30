package com.escapeland.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.escapeland.app.R;
import com.facebook.CallbackManager;


public class MainLoginSignupActivity extends AppCompatActivity implements View.OnClickListener{
    boolean btnPressedSecondTime= false;
    Button main_login_signup_btn;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login_signup);

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        SharedPreferences shared = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        Boolean data = (shared.getBoolean("PREFERENCE", true));
        Log.d("shared", " in main login sign up"+data);
        callbackManager = CallbackManager.Factory.create();
        setWindowsFlags();
        hideNavigation();
        setClickListner();
    }

    @Override
    public void onClick(View v) {

        Log.d("main-login-signup"," onclick");

        int i = v.getId();
        if(i==R.id.main_login_signup_btn){
            Intent LoginSignupActivityProcessing = new Intent(MainLoginSignupActivity.this, LoginSignupActivity.class);
            LoginSignupActivityProcessing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LoginSignupActivityProcessing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            LoginSignupActivityProcessing.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(LoginSignupActivityProcessing);
        }
    }

    @Override
    public void onBackPressed() {
        if (btnPressedSecondTime) {
            super.onBackPressed();
            return;
        }
        else {
            this.btnPressedSecondTime = true;
            Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    btnPressedSecondTime=false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigation();
    }

    // Inhouse methods

    public void setWindowsFlags(){

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void setClickListner(){
        findViewById(R.id.main_login_signup_btn).setOnClickListener(this);
    }

    public void hideNavigation(){
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }
}
