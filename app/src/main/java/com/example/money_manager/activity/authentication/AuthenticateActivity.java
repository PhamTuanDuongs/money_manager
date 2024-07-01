package com.example.money_manager.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;

public class AuthenticateActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authenticate);
        ((Button)findViewById(R.id.btn_nav_sign_up)).setOnClickListener(this);
        ((TextView)findViewById(R.id.txt_nav_login)).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_nav_sign_up){
            startActivity(new Intent(AuthenticateActivity.this, RegisterActivity.class));
        }else if(v.getId() == R.id.txt_nav_login){
            startActivity(new Intent(AuthenticateActivity.this, LoginActivity.class));
        }
    }
}