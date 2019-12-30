package com.escapeland.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.escapeland.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserProfileDetailsActivity extends AppCompatActivity {

    TextView user_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("User-profile-details", "in user profile details");

        FirebaseAuth mfirebaseAuth;
        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mfirebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("escapeland-users");
        final DatabaseReference  databaseRef = myRef.child(currentUser.getUid());


        setContentView(R.layout.view_global_menu_header);
    }
}
