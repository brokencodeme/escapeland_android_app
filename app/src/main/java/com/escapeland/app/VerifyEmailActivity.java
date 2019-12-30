package com.escapeland.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.escapeland.app.R;

public class VerifyEmailActivity extends AppCompatActivity
{


    Button btn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_verify_email);
        Button btn = findViewById(R.id.verify_email_login__btn);

        Log.d("VerifyEmailActivity ", "");
        Toast.makeText(VerifyEmailActivity.this, "in VerifyEmailActivity", Toast.LENGTH_SHORT).show();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifyEmailActivity.this, LoginSignupActivity.class));
            }
        });


    }
}

