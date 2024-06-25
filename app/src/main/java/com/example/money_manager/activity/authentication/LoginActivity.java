package com.example.money_manager.activity.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;
import com.example.money_manager.utils.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_email, edt_password;
    private TextView error_email, error_password;
    private Button btn_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        init();
        handleLogin();
    }


    private void init() {
        mAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_email_login);
        edt_password = findViewById(R.id.edt_password_login);
        error_email = findViewById(R.id.error_email_login);
        error_password = findViewById(R.id.error_password_login);
        btn_login = findViewById(R.id.btn_login);
        edt_email.requestFocus();
        btn_login.setEnabled(false);
        btn_login.setAlpha(0.5f);
        handleEmail();
        handlePassword();
        handleLogoClick();
    }


    private void handleLogoClick(){
        Toolbar toolbar = findViewById(R.id.toolbar_login_);
        setSupportActionBar(toolbar);

        try {
            Field logoField = Toolbar.class.getDeclaredField("mLogoView");
            logoField.setAccessible(true);
            ImageView logo = (ImageView) logoField.get(toolbar);
            if (logo != null) {
                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getOnBackPressedDispatcher().onBackPressed();
                    }
                });
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private void handlePassword(){
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    error_email.setText(getResources().getString(R.string.email_null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmailValid = !TextUtils.isEmpty(edt_email.getText()) && Validate.isValidEmailId(edt_email.getText().toString());
                boolean isPasswordValid = !TextUtils.isEmpty(s) && s.length() >= 8;
                if (s.length() < 8) {
                    error_password.setText("Length of password must be greater than 8.");
                } else if (isEmailValid && isPasswordValid) {
                    error_password.setText("");
                    btn_login.setAlpha(1.0f);
                    btn_login.setEnabled(true);
                }
            }
        });
    }
    private void handleEmail(){
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    error_email.setText(getResources().getString(R.string.email_null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmailValid = !TextUtils.isEmpty(s) && Validate.isValidEmailId(s.toString());
                boolean isPasswordValid = !TextUtils.isEmpty(edt_password.getText()) && s.length() >= 8;
                if (!TextUtils.isEmpty(s) && !Validate.isValidEmailId(s.toString())) {
                    error_email.setText(getResources().getString(R.string.email_error_address));
                } else if (isEmailValid && isPasswordValid) {
                    btn_login.setAlpha(1.0f);
                    btn_login.setEnabled(true);
                    error_email.setText("");
                } else {
                    error_email.setText("");
                }
            }
        });
    }
    private void handleLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }else {
                                        StyleableToast.makeText(LoginActivity.this, "Please verify your email before login", Toast.LENGTH_LONG, R.style.Warning).show();
                                    }
                                } else {
                                    StyleableToast.makeText(LoginActivity.this,  task.getException().getMessage(), Toast.LENGTH_LONG, R.style.Error).show();
                                }
                            }
                        });
            }
        });

    }
}