package com.escapeland.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.escapeland.app.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GoogleSignInActivity extends AppCompatActivity {

    // declaring variables
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mFirebaseAuth;
    private final static int RC_SIGN_IN=2;
    FirebaseDatabase mDataabse = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = mDataabse.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_google_signup);

        setWindowsFlags();
        hideNavigation();

        // creating objects
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mFirebaseAuth = FirebaseAuth.getInstance();

        signIn();

    }


    private void signIn() {

        Log.d("google-sign-in", "1 in sign-in method");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onStart(){
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account==null){
            Log.d("user-active","user is not active");
        }
        else {
            Log.d("user-active","user is active");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("google-sign-in", "2 in activity result "+requestCode+" "+resultCode+" "+data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.d("google-sign-in", "beffore try method");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("google-sign-in", "3 sign in success "+account.getEmail()+account.getDisplayName()+" "+account.getPhotoUrl().toString());
                authWithFirebase(account);


                // Signed in successfully, show authenticated UI.
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                if(e.getStatusCode()==12501){

                    // User cancelled the sign in operation
                    Log.d("google-sign-in", "3 sign in cancelled ");
                    startActivity(new Intent(GoogleSignInActivity.this, LoginSignupActivity.class));

                }
                else if(e.getStatusCode()==12500){
                    // Unknown error
                    startActivity(new Intent(GoogleSignInActivity.this, LoginSignupActivity.class));
                }
                else if(e.getStatusCode()==7){
                    Toast.makeText(GoogleSignInActivity.this, "Check Internet", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(GoogleSignInActivity.this, LoginSignupActivity.class));
                }
                Log.d("google-sign-in", "3 expcetioin in sign in  "+ e+ " status code"+e.getStatusCode()+""+ e.getCause()+" "+e.getMessage()+" message"+e.getStatusMessage());
            }
        }
    }
    public void authWithFirebase(final GoogleSignInAccount account){

        Log.d("google-sign-in", "4 in authwithfirebase ");

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.d("google-sign-in", "5 task successfully");
                            Toast.makeText(GoogleSignInActivity.this, "Loading...", Toast.LENGTH_LONG).show();
                            FirebaseUser loggedInUser = mFirebaseAuth.getCurrentUser();
                            if(loggedInUser !=null){

                                if(task.getResult().getAdditionalUserInfo().isNewUser()) {
                                    Log.d("google-sign-in", "6 new user");
                                    storeUserInformation(account, loggedInUser);
                                    startActivity(new Intent(GoogleSignInActivity.this, HomeActivity.class));
                                }

                                else {
                                    Log.d("google-sign-in", "6 old user");
                                    startActivity(new Intent(GoogleSignInActivity.this, HomeActivity.class));
                                }
                            }
                        }
                        else {
                            try{
                                throw task.getException();
                            }
                            catch (Exception e){
                                Log.d("google-sign-in", "5 exception "+ e);
                            }
                        }
                    }
                });
    }

    public void storeUserInformation(GoogleSignInAccount account, FirebaseUser user){

        Date date= new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String currentTime = new SimpleDateFormat("yyyy/MM/dd").format(ts.getTime());
        Log.d("google-sign-in", "6 storing information");

        if(account!=null){

            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personEmail = account.getEmail();
            Uri profilePic= account.getPhotoUrl();
            String profileUrl = profilePic.toString();
            UserData userData = new UserData(profileUrl,personName,personEmail, true, "none", currentTime, "google-signin");
            databaseReference.child("escapeland-users").child(user.getUid()).setValue(userData);
            Log.d("google-sign-in", "7 "+profilePic+ " user id "+user.getUid());
        }
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
