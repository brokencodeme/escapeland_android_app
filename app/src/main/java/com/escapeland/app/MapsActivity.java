package com.escapeland.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private DrawerLayout map_drawer;
    FirebaseAuth mfirebaseAuth;
    CircleImageView circularImageView;
    TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_maps);

        NavigationView map_nav_view = findViewById(R.id.map_nav_view);
        map_drawer = (DrawerLayout) findViewById(R.id.map_drawer);
        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        View header_layout = map_nav_view.inflateHeaderView(R.layout.view_global_menu_header_location);
        circularImageView = (CircleImageView) header_layout.findViewById(R.id.map_circularImageView);

        Log.d("map-activity", "header view "+header_layout);
        user_name = (TextView) header_layout.findViewById(R.id.map_user_name);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("escapeland-users");
        final DatabaseReference databaseRef = myRef.child(currentUser.getUid());

        setProfilePicture(databaseRef, circularImageView);
        setWindowsFlags();
        hideNavigation();
        setupToolbar();
        map_nav_view.setNavigationItemSelectedListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.319695, 73.113621);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void setWindowsFlags() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    public void hideNavigation() {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {


            case R.id.book_now_icon:
                map_drawer.closeDrawer(Gravity.LEFT);
                Uri uri = Uri.parse("https://in.bookmyshow.com/events/ritviz-at-escapeland-festival/ET00119568"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.home_icon:
                startActivity(new Intent(this, HomeActivity.class));
                map_drawer.closeDrawer(Gravity.LEFT);
                break;

            case R.id.location_icon:
                map_drawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(this, MapsActivity.class));
                break;

            case R.id.logout_icon:

                FirebaseUser firebaseUser = mfirebaseAuth.getCurrentUser();

                if (checkFbUser(firebaseUser)) {
                    LoginManager.getInstance().logOut();
                } else {
                    FirebaseAuth.getInstance().signOut();
                }
                startActivity(new Intent(this, LoginSignupActivity.class));
                map_drawer.closeDrawer(Gravity.LEFT);
                break;

            default:
                return false;
        }
        return true;
    }

    protected void setupToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.map_bottom_appbar);
        setSupportActionBar(toolbar);
        toolbar.setCollapseIcon(R.drawable.drawer_btn);
//            getSupportActionBar().setTitle("EscapeLand");
//            toolbar.setTitleTextColor(Color.BLACK);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, map_drawer, toolbar, R.string.nav_open, R.string.nav_close);
        toggle.syncState();
        Log.d("map-activity", ""+map_drawer);
        map_drawer.addDrawerListener(toggle);


        if (map_drawer.isDrawerOpen(GravityCompat.START)) {
            map_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            map_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map_drawer.openDrawer(Gravity.LEFT);
            }
        });
//            toggle.syncState();

    }

    public boolean checkFbUser(FirebaseUser firebaseUser) {
        for (UserInfo profile : firebaseUser.getProviderData()) {

            if (profile.getProviderId().equals("facebook.com")) {
                return true;
            }
        }
        Log.d(" pratik", "2 checking fb user return false");
        return false;
    }


    public void setProfilePicture(DatabaseReference databaseRef, final ImageView circularImageView) {

        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        Log.d("map-activity", ""  );

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.d("home", " data " + data.getKey());

                    if (data.getKey().equals("name")) {
                        user_name.setText("@" + data.getValue());
                    }

                    if (data.getKey().equals("profileUri")) {

                        Log.d("home", " profile url " + data.getValue());

                        Glide.with(getApplicationContext())
                                .load(data.getValue()) // image url
                                .placeholder(R.drawable.poster_one) // any placeholder to load at start
                                .error(R.drawable.poster_one)  // any image in case of error
                                //.override(130, 130) // resizing
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
}

