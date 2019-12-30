package com.escapeland.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.projects.testing_project.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity
        extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener {

    Dialog dialog;
    VideoView home_page_video_view, nav_video_view;
    private DrawerLayout mDrawer;
    boolean btnPressedSecondTime = false;
    private RecyclerView home_recycle_view;
    FirebaseAuth mfirebaseAuth;
    List<HomeScrolledData> homeScrolledData;
    HomeImagesAdapter homeImagesAdapter;
    CircleImageView circularImageView;
    TextView user_name;
    TextView escapeland_heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        setWindowsFlags();
        hideNavigation();

        ConstraintLayout constraintLayout = findViewById(R.id.content);
        home_page_video_view = (VideoView) findViewById(R.id.home_page_video_view);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header_view = navigationView.inflateHeaderView(R.layout.view_global_menu_header);
        circularImageView = (CircleImageView) header_view.findViewById(R.id.circularImageView);
        home_recycle_view = (RecyclerView) findViewById(R.id.home_recycle_view);
        user_name = (TextView) header_view.findViewById(R.id.user_name);
        homeScrolledData = new ArrayList<>();
        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("escapeland-users");
        final DatabaseReference databaseRef = myRef.child(currentUser.getUid());
        navigationView.setNavigationItemSelectedListener(this);

        setupToolbar();
        setupScrollingImages();
        setupSlidingFragment();
        setProfilePicture(databaseRef, circularImageView);
        playHomeBackVide();
        setUpPopUp();
    }


    @Override
    public void onBackPressed() {

        if (btnPressedSecondTime) {

            super.onBackPressed();
            finish();
            return;

        } else {

            if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(Gravity.LEFT);
            } else {
                this.btnPressedSecondTime = true;
                Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        btnPressedSecondTime = false;
                    }
                }, 2000);
            }
        }
    }

    private void setupScrollingImages() {

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_one
                ));

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_two
                ));

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_three
                ));

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_four
                ));

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_six
                ));

        homeScrolledData.add(
                new HomeScrolledData(
                        false,
                        Uri.parse(""),
                        R.drawable.home_swipe_image_seven
                ));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        home_recycle_view.setLayoutManager(linearLayoutManager);
        HomeImagesAdapter homeImagesAdapter = new HomeImagesAdapter(this, homeScrolledData);
        home_recycle_view.setAdapter(homeImagesAdapter);


        //        SnapHelper snapHelper = new LinearSnapHelper();
        //        snapHelper.attachToRecyclerView(view_pager);
    }

    private void setupSlidingFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentCategory categoryActivity = (FragmentCategory) fm.findFragmentById(R.id.sliding_fragment);

        if (categoryActivity == null) {
            categoryActivity = new FragmentCategory();
            fm.beginTransaction().add(R.id.sliding_fragment, categoryActivity).commit();
        }
    }

    protected void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_bottom_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setCollapseIcon(R.drawable.drawer_btn);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(toggle);

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(Gravity.LEFT);
            }
        });
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.about_icon:
                startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                mDrawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.home_icon:
                mDrawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.book_now_icon:
                mDrawer.closeDrawer(Gravity.LEFT);
                Uri uri = Uri.parse("https://in.bookmyshow.com/events/ritviz-at-escapeland-festival/ET00119568"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.location_icon:
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                mDrawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.logout_icon:

                FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();

                if (checkFbUser(firebaseUser)) {
                    LoginManager.getInstance().logOut();
                } else {
                    FirebaseAuth.getInstance().signOut();
                }

                mDrawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(HomeActivity.this, LoginSignupActivity.class));

                break;

            default:
                return false;
        }

        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
        Log.d("on-start", " in the method");
        playHomeBackVide();
    }


    @Override
    protected void onResume() {

        super.onResume();
        Log.d("l-s-a", "onResume:");
        hideNavigation();
    }


    public void setWindowsFlags(){
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

    public void setProfilePicture(DatabaseReference databaseRef, final ImageView circularImageView){

        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        Log.d("home-activity", ""+currentUser.getDisplayName());

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                   Log.d("home", " data "+data.getKey());

                   if(data.getKey().equals("name")){
                       user_name.setText("@"+data.getValue());
                   }

                   if(data.getKey().equals("profileUri")){

                       Log.d("home", " profile url "+data.getValue());

                       Glide.with(getApplicationContext())
                           .load(data.getValue()) // image url
                           .placeholder(R.drawable.poster_one) // any placeholder to load at start
                           .error(R.drawable.poster_one)  // any image in case of error
                           .centerCrop()
                           .into(circularImageView);
                  }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean checkFbUser(FirebaseUser firebaseUser){
        for (UserInfo profile : firebaseUser.getProviderData()) {

            if (profile.getProviderId().equals("facebook.com")) {
                return true;
            }
        }
        Log.d(" pratik", "2 checking fb user return false");
        return false;
    }

    public void playHomeBackVide(){
        try {
            Uri home_bg_uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.home_animate_video);

            home_page_video_view.setVideoURI(home_bg_uri);
            home_page_video_view.start();
            home_page_video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            Log.d("media-error", ""+mp+" "+what+" "+extra);
                            return false;

                        }
                    });
                }
            });
        } catch (Exception e){
            Log.d("video-error",""+e.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d("home-activity", "in create");
    }

    public void setUpPopUp()
    {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        boolean home_popup_once_value = sharedPref.getBoolean("IS_HOME_POPUP_ONCE", true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.home_custom_popup);

        Log.d("HOME_POPUP_ONCE", " "+home_popup_once_value);
        if (home_popup_once_value)
        {
            dialog.show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("IS_HOME_POPUP_ONCE", false);
            editor.commit();
            boolean new_popup_value = sharedPref.getBoolean("IS_HOME_POPUP_ONCE", false);
            Log.d("HOME_POPUP_ONCE", "new value "+new_popup_value);
        }

        TextView close_btn = dialog.findViewById(R.id.popup_close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
