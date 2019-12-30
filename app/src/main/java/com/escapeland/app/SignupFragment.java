package com.escapeland.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.escapeland.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SignupFragment extends Fragment implements View.OnClickListener{


    FirebaseDatabase mDataabse = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = mDataabse.getReference();
    EditText login_input_email, login_input_password, input_signup_name, input_signup_email, input_signup_password, input_signup_confirm_password;
    Button bottom_login_button, bottom_signup_signup_btn, top_signup_login_btn;
    private FirebaseAuth mFireBaseAuth;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup, container, false);
        TextView alredy_have_account_id = view.findViewById(R.id.alredy_have_account_id);
        mFireBaseAuth = FirebaseAuth.getInstance();
        ConstraintLayout signup_constraint_layout = view.findViewById(R.id.signup_constraint_layout);
        Log.d("onCreate", "in the oncreate method" + mFireBaseAuth);

        alredy_have_account_id.setOnClickListener(this);
        input_signup_name = (EditText) view.findViewById(R.id.input_signup_name);
        input_signup_email = (EditText) view.findViewById(R.id.input_signup_email);
        input_signup_password = (EditText) view.findViewById(R.id.input_signup_password);
        input_signup_confirm_password = (EditText) view.findViewById(R.id.input_signup_confirm_password);
        bottom_signup_signup_btn  = (Button) view.findViewById(R.id.bottom_signup_signup_btn);
        bottom_signup_signup_btn.setOnClickListener(this);

        TextViewEventHandler(signup_constraint_layout);

        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.bottom_signup_signup_btn) {
            CreateNewUser(input_signup_name.getText().toString(), input_signup_email.getText().toString(), input_signup_password.getText().toString(), input_signup_confirm_password.getText().toString());
        }
        else if(i==R.id.alredy_have_account_id){
            Log.d("signup-fragment", "in alreaydayhaveaccount");

            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.main_login_signup_fragmentlayout, new LoginFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void CreateNewUser(final String signup_name, final String signup_email, final String signup_password, String signup_conf_password){

        if(ValidatingSignInputs(signup_name,signup_email,signup_password,signup_conf_password)) {

            Log.d("Createnewuser", "creatign new user");

            mFireBaseAuth.createUserWithEmailAndPassword(signup_email, signup_password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Log.d("task","success");
                        Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_LONG).show();
                        try {
                            FirebaseUser user = mFireBaseAuth.getCurrentUser();
                            UserData userData = new UserData("https://instagram.fstv6-1.fna.fbcdn.net/vp/7a18ad98f70de23ddbed28e921958446/5E6F52D2/t51.2885-19/s320x320/74891097_962550957457673_3724327908009836544_n.jpg?_nc_ht=instagram.fstv6-1.fna.fbcdn.net",signup_name, signup_email, false, signup_password,getCurrentTime(), "basic-signup");
                            databaseReference.child("escapeland-users").child(user.getUid()).setValue(userData);

                            // sending varification link to email
                            sendVerificationLink();


                        }catch (Exception e ){

                            Log.d("exception-insigin-act","data = "+e);
                        }

                    } else {
                        Log.d("in else","data");
                        Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
                        try {
                            throw task.getException();
                        }
                        catch (FirebaseAuthUserCollisionException existsEmail){
                            input_signup_email.setError("Email already in use");
                            input_signup_email.requestFocus();
                        }
                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                        {
                            input_signup_email.setError("Please enter a valid email");
                            input_signup_email.requestFocus();
                        }
                        catch (Exception e){
                            Log.d("exception", "unknown firebase exception"+e);
                        }

                    }
                }
            });
        }
        else {

        }
    }

    public boolean ValidatingSignInputs(String signup_name,String signup_email,String signup_password,String signup_conf_password){

        if (signup_name.isEmpty()) {
            input_signup_name.setError("Please enter the name");
            input_signup_name.requestFocus();
            return false;
        }
        else if (signup_email.isEmpty()) {
            input_signup_email.setError("Please enter the email");
            input_signup_email.requestFocus();
            return false;
        }
        else if (signup_password.isEmpty()) {
            input_signup_password.setError("Please enter the password");
            input_signup_password.requestFocus();
            return false;
        }
        else if (signup_conf_password.isEmpty()) {
            input_signup_confirm_password.setError("Please enter the confirm password");
            input_signup_confirm_password.requestFocus();
            return false;
        }

        else if(!signup_password.equals(signup_conf_password)){
            input_signup_confirm_password.setError("Password does not match");
            input_signup_confirm_password.requestFocus();
            return false;
        }
        else if(!VALID_EMAIL_ADDRESS_REGEX.matcher(signup_email).matches()){
            input_signup_email.setError("Please enter a valid email address");
            input_signup_email.requestFocus();
            return false;
        }
        else if(signup_password.length() <= 8){
            input_signup_password.setError("Please use a strong password");
            input_signup_password.requestFocus();
            return false;
        }
        return true;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public void sendVerificationLink(){
        Log.d("sendVerificationLink", "");
        mFireBaseAuth.getCurrentUser().sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("sendVerificationLink", "success");
                    startActivity(new Intent(getActivity(), VerifyEmailActivity.class));
                    }
                    else {
                        Log.d("sendVerificationLink", "failed");
                    }
                }
                });
    }

    public String getCurrentTime(){
        Date date= new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String currentTime = new SimpleDateFormat("yyyy/MM/dd").format(ts.getTime());
        return currentTime;
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
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

}
