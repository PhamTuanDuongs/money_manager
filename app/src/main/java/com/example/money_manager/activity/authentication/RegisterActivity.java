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
import com.example.money_manager.utils.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterActivity extends AppCompatActivity {

     private EditText edt_email, edt_password;
    private TextView error_email, error_password;
    private Button btn_signup;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        init();
        handleSignUp();
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_email_register);
        edt_password = findViewById(R.id.edt_password_register);
        error_email = findViewById(R.id.error_email);
        error_password = findViewById(R.id.error_password);
        btn_signup = findViewById(R.id.btn_register);
        edt_email.requestFocus();
        btn_signup.setEnabled(false);
        btn_signup.setAlpha(0.5f);
        handleEmail();
        handlePassword();
        handleLogoClick();
    }

    private void handleLogoClick(){
        View toolbar = findViewById(R.id.toolbarSignUp);
        ImageView backButton = toolbar.findViewById(R.id.nv_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
private void handlePassword(){
    edt_password.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isEmailValid = !TextUtils.isEmpty(edt_email.getText()) && Validate.isValidEmailId(edt_email.getText().toString());
            boolean isPasswordValid = !TextUtils.isEmpty(s) && s.length() >= 8;
            if (s.length() < 8) {
                error_password.setText("Length of password must be greater than 8.");
            } else if (isEmailValid && isPasswordValid) {
                error_password.setText("");
                btn_signup.setAlpha(1.0f);
                btn_signup.setEnabled(true);
            } else {
                error_password.setText("");
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
                    btn_signup.setAlpha(1.0f);
                    btn_signup.setEnabled(true);
                    error_email.setText("");
                } else {
                    error_email.setText("");
                }
            }
        });
    }
    public void handleSignUp() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        StyleableToast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email", Toast.LENGTH_LONG, R.style.Success).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        edt_email.setText("");
                                        edt_password.setText("");
                                        error_password.setText("");
                                        btn_signup.setEnabled(false);
                                    }else{
                                        StyleableToast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG, R.style.Error).show();
                                    }
                                }
                            });
                        } else {
                            StyleableToast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG, R.style.Error).show();
                        }
                    }
                });
            }
        });
    }
}