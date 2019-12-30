package com.escapeland.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.escapeland.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText reset_password_email_input;
    FirebaseAuth mfirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_forgot_password);

        reset_password_email_input  = (EditText)findViewById(R.id.reset_password_email_input);
        findViewById(R.id.reset_password_btn).setOnClickListener(this);
        mfirebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i==R.id.reset_password_btn){
            sendNewPasswordlink(reset_password_email_input.getText().toString());
        }
    }

    public void sendNewPasswordlink(String resetEmail){
        if(ValidatingInputEmail(resetEmail)){
            mfirebaseAuth.sendPasswordResetEmail(resetEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset password link send", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginSignupActivity.class));
                            } else {
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e){
                                    Log.d("emailresetexception", ""+e);
                                    Toast.makeText(ForgotPasswordActivity.this, "No email found for this account", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
        else {
            Toast.makeText(ForgotPasswordActivity.this, "Inputs are invalid", Toast.LENGTH_LONG).show();
        }
    }

    public boolean ValidatingInputEmail(String resetEmail){

        if(resetEmail.isEmpty()){
            reset_password_email_input.setError("Email can not be empty");
            reset_password_email_input.requestFocus();
            return false;
        }
        else if(!VALID_EMAIL_ADDRESS_REGEX.matcher(resetEmail).matches()){
            reset_password_email_input.setError("Please enter a valid email address");
            reset_password_email_input.requestFocus();
            return false;
        }
        return true;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
}
