package com.escapeland.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.escapeland.app.R;

public class    LinkAlreadySendActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_link_already_send);
        findViewById(R.id.bottom_login_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if(i==R.id.bottom_login_btn){
            startActivity(new Intent(LinkAlreadySendActivity.this, LoginSignupActivity.class));
        }
    }
}

