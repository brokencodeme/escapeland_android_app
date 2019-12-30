package com.escapeland.app;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.escapeland.app.R;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mfirebaseAuth;
    private DrawerLayout mdrawerLayout;
    CircleImageView circularImageView;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.about_us_drawer_layout);

        NavigationView navigationView = findViewById(R.id.about_us_nav_view);
        View header_view = navigationView.inflateHeaderView(R.layout.view_global_menu_header_about_us);
        circularImageView = header_view.findViewById(R.id.about_us_circularImageView);
        username = header_view.findViewById(R.id.about_us_username);
        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("escapeland-users");
        final DatabaseReference databaseRef = myRef.child(currentUser.getUid());
        setupToolbar();
        navigationView.setNavigationItemSelectedListener(this);
        setProfilePicture(databaseRef, circularImageView);
        setWindowsFlags();
        hideNavigation();
    }

    protected void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.aboutus_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setCollapseIcon(R.drawable.black_hamburger);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        mdrawerLayout.addDrawerListener(toggle);

        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mdrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        toggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.about_icon:
                mdrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.home_icon:
                startActivity(new Intent(this, HomeActivity.class));
                mdrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.book_now_icon:
                mdrawerLayout.closeDrawer(Gravity.LEFT);
                Uri uri = Uri.parse("https://in.bookmyshow.com/events/ritviz-at-escapeland-festival/ET00119568"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.location_icon:
                startActivity(new Intent(this, MapsActivity.class));
                mdrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case R.id.logout_icon:
                FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();

                if (checkFbUser(firebaseUser)) {
                    LoginManager.getInstance().logOut();
                } else {
                    FirebaseAuth.getInstance().signOut();
                }

                mdrawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(this, LoginSignupActivity.class));
                break;

            default:
                return false;
        }

        return true;
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
                        username .setText("@"+data.getValue());
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
}