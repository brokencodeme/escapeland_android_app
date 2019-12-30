package com.escapeland.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.escapeland.app.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements View.OnClickListener {


    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    EditText input_login_email, input_login_password;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference databaseReference;
    CallbackManager callbackManager;
    AccessToken accessToken;
    Button login_btn;
    TextView forget_password_link;
    SignInButton google_sign_in_btn;
    LoginButton fb_login_btn;
    Button google_btn;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("login-fragment", " in login fragment");
        view = inflater.inflate(R.layout.fragment_login, container, false);

        callbackManager = CallbackManager.Factory.create();
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

       // ConstraintLayout login_constraint_layout = view.findViewById(R.id.login_constraint_layout);
        TextView donnot_have_account_id = view.findViewById(R.id.donnot_have_account_id);
        fb_login_btn = (LoginButton)view.findViewById(R.id.facebook_login_btn);
        login_btn = (Button) view.findViewById(R.id.login_btn);
        ConstraintLayout login_constraint_layout = (ConstraintLayout)view.findViewById(R.id.login_constraint_layout);
        forget_password_link = (TextView) view.findViewById(R.id.forget_password_link);
        google_sign_in_btn = (SignInButton) view.findViewById(R.id.google_sign_in_btn);
        input_login_email = (EditText)view.findViewById(R.id.input_login_email);
        input_login_password = (EditText)view.findViewById(R.id.input_login_password);

        fb_login_btn.setOnClickListener(this);
        donnot_have_account_id.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        forget_password_link.setOnClickListener(this);
        google_sign_in_btn.setOnClickListener(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        fb_login_btn.setPermissions("email", "public_profile");
        fb_login_btn.setFragment(this);
        setGooglePlusButtonText(google_sign_in_btn, "Log In");
        TextViewEventHandler(login_constraint_layout);

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", " "+requestCode+" "+resultCode+" "+data);
    }




    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i==R.id.login_btn){
            makeUserLoggedIn();
        }

        else if(i==R.id.forget_password_link){
            startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
        }

        else if(i==R.id.google_sign_in_btn){
            startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
        }

        else if(i==R.id.donnot_have_account_id){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.main_login_signup_fragmentlayout, new SignupFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        else if(i==R.id.facebook_login_btn) {

            try {

                Log.d("login-activity", "1 facebook button clicked" + R.id.facebook_login_btn);
                fb_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getActivity(), "Loading", Toast.LENGTH_LONG).show();
                        authWithFirebase(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_LONG).show();
                        Log.d("login-activity", "2 canceled");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("login-activity", "2 error " + error.getMessage());
                    }
                });
            }catch (Exception e){
                Log.d("login-exception", " error "+e);
            }
            }

    }

    public void makeUserLoggedIn(){

        if(ValidatingLoginInputs(input_login_email.getText().toString(), input_login_password.getText().toString())){
            Log.d("login-activity", "2 inputs are valid");

            try {
                mFirebaseAuth.signInWithEmailAndPassword(input_login_email.getText().toString(), input_login_password.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("login-activity", "3 checking task" + task.isSuccessful());

                        if (task.isSuccessful()) {

                            Log.d("login-activity", "4 task is success");
                            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                            if (firebaseUser != null) {

                                // checking if email is verified
                                if (checkIfemailVerified()) {

                                    Log.d("login-activity", "5 email is varified");

                                    // update user information that email is verified

                                    updateUserInfo(firebaseUser.getUid());

                                    Toast.makeText(getActivity(), "Loading..", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                } else {
                                    Log.d("login-activity", "5 email not verified");
                                    startActivity(new Intent(getActivity(), LinkAlreadySendActivity.class));
                                }

                            } else {
                                Log.d("login-activity", "5 user is null after logging...");

                            }
                        } else {
                            try {
                                throw task.getException();

                            } catch (FirebaseAuthInvalidUserException emailInvalid) {

                                Log.d("login-activity", "task exception "+emailInvalid.getMessage());
                                input_login_email.setError("Not a valid email");
                                input_login_email.requestFocus();

                            } catch (FirebaseAuthInvalidCredentialsException invalidPassword) {

                                Log.d("login-activity", "task exception "+invalidPassword.getMessage());
                                input_login_password.setError("Please enter a valid password");
                                input_login_password.requestFocus();

                            } catch (Exception e) {
                                Log.d("login-activity", "task exception "+e.getMessage());

                            }
                        }
                            }
                        });
            }
            catch (Exception e ){
                Log.d("login-activity", "3 unknow-error"+e.getMessage());
            }
        }
        else {
            Log.d("login-activity", "2 not valid inputs");
            Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_LONG).show();

        }
    }

    public boolean ValidatingLoginInputs(String login_email,String login_password){


        Log.d("validating", "validating inputs "+login_email+" "+login_password);
        if (login_email.isEmpty()) {
            input_login_email.setError("Please enter the email");
            input_login_email.requestFocus();
            return false;
        }
        else if (login_password.isEmpty()) {
            input_login_password.setError("Please enter the password");
            input_login_password.requestFocus();
            return false;
        }
        else if(!VALID_EMAIL_ADDRESS_REGEX.matcher(login_email).matches()){
            input_login_email.setError("Please enter a valid email address");
            input_login_email.requestFocus();
            return false;
        }
        else if(login_password.length() <= 8){
            input_login_password.setError("Please use a strong password");
            input_login_password.requestFocus();
            return false;
        }
        else {
            return true;
        }
    }
    public boolean checkIfemailVerified(){
        Log.d("login-activity","checking if email is varified");
        boolean isVerified;
        isVerified = mFirebaseAuth.getCurrentUser().isEmailVerified();
        return isVerified;
    }
    public void updateUserInfo(final String uuid){

        Log.d("login-activity ", "6 updating user information in database");
        databaseReference.child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isEmailVerified  = (Boolean) dataSnapshot.child("isEmailVerified").getValue();
                databaseReference.child(uuid).child("isEmailVerified").setValue(true);            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void authWithFirebase(LoginResult loginResult){

        final AccessToken fb_token = loginResult.getAccessToken();
        Log.d("login-activity", "3 handling authwithfirebase:" + fb_token+" c-a-t"+fb_token.getCurrentAccessToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(fb_token.getToken());

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("login-activty", "4 task "+task.isSuccessful());

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Log.d("login-activty", "4 task is "+task.isSuccessful());

                            if (user != null) {

                                if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                    Log.d("login-activty", " 5 user is new");
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    storeFacebookUserInformation(fb_token, firebaseUser);
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                } else {
                                    Log.d("login-activty", " 5 user is old");
                                    startActivity(new Intent(getActivity(), HomeActivity.class));
                                    getActivity().finish();
                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("login-activity", "5 task failure"+task.getException()+" "+task.getException().getMessage());
                            input_login_email.setError("Email already exists");
                            input_login_email.requestFocus();
                        }
                    }
                });
    }

    public void  storeUserInformation(JSONObject object, FirebaseUser firebaseUser){

        Log.d("login-activity", "6 storng information"+firebaseUser.getUid());
        Date date= new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String currentDate = new SimpleDateFormat("yyyy/MM/dd").format(ts.getTime());

        try {

            String personName = object.getString("name");
            String personEmail = object.getString("email");
            String profileUrl = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
            UserData userData = new UserData(profileUrl, personName, personEmail, true, "none", currentDate, "fb-signin");
            databaseReference.child(firebaseUser.getUid()).setValue(userData);

        }catch (Exception e){
            Log.d("login-activity", "6 exception in storing data"+e);
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            Log.d("childs", ""+signInButton.getChildCount());
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                Log.d("in textivew", "google sign in text view");
                TextView tv = (TextView) v;
                tv.setPadding(80, 0, 0, 0);
                tv.setTextColor(Color.BLACK);
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, );
                tv.setText(getResources().getString(R.string.google_signin_text));
                tv.setGravity(Gravity.CENTER);
                return;
            }
        }
    }

    public void TextViewEventHandler(ConstraintLayout constraintLayout){

        Log.d("childrens", ""+constraintLayout.getChildCount());


        for( int i = 0; i < constraintLayout.getChildCount(); i++ ) {
            View currentView = constraintLayout.getChildAt(i);
                if (currentView instanceof EditText) {
                    ((EditText)currentView).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {

                        if (hasFocus) {
                            showNavigation();
                        } else {
                            hideNavigatioin();
                        }
                    }
                });
            }
        }
    }

    public void showNavigation(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    public void hideNavigatioin(){
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
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

    public void storeFacebookUserInformation(AccessToken token, final FirebaseUser firebaseUser){

        Profile profile = Profile.getCurrentProfile();

        Log.d("login-activity","storing informatioin of user "+token.getDeclinedPermissions());
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("login-activity", response.toString());

                        storeUserInformation(object,firebaseUser);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
