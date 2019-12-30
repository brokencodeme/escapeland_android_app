package com.escapeland.app;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.escapeland.app.R;
import com.google.android.material.tabs.TabLayout;

public class LoginSignupActivity  extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Boolean isBackpressedSecondTime;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        isBackpressedSecondTime = false;
        setContentView(R.layout.login_signup_container);
        setWindowsFlags();
        hideNavigation();
        setSystemVisibilityChangeTasks();


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.main_login_signup_fragmentlayout, new LoginFragment()).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {

        super.onResume();
        hideNavigation();
    }

    @Override
    public void onBackPressed() {
        if(isBackpressedSecondTime){
            finish();
        }
        else {
            Toast.makeText(this, "Press Back again to Exit.",Toast.LENGTH_SHORT).show();
            isBackpressedSecondTime = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackpressedSecondTime = false;
                }
            }, 3 * 1000);
        }

    }

    public void setWindowsFlags(){

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void setSystemVisibilityChangeTasks(){
        Log.d("l-s-a", "initial value"+View.SYSTEM_UI_FLAG_HIDE_NAVIGATION+" "+View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    public void hideNavigation(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
