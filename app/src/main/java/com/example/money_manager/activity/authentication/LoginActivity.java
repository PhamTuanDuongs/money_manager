package com.example.money_manager.activity.authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.money_manager.R;
import com.example.money_manager.activity.MainActivity;
import com.example.money_manager.contract.LoginContract;
import com.example.money_manager.contract.presenter.LoginPresenter;
import com.example.money_manager.utils.NetworkUtils;
import com.example.money_manager.utils.ValidateUtils;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private EditText edt_email, edt_password;
    private TextView error_email, error_password;
    private Button btn_login;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        init();
        handleLogin();
    }

    private void handleLogin() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                if(NetworkUtils.isNetworkAvailable(LoginActivity.this)){
                  presenter.onLoginButtonClick(email, password);
                }else {
                    showPopupNetworkError();
                }
            }
        });
    }

    private void init() {
        presenter = new LoginPresenter(LoginActivity.this);
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

    private void handleLogoClick() {
        View toolbar = findViewById(R.id.toolbarLogin);
        ImageView backButton = toolbar.findViewById(R.id.nv_back);
        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Login");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }

    private void handlePassword() {
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
                boolean isEmailValid = !TextUtils.isEmpty(edt_email.getText()) && ValidateUtils.isValidEmailId(edt_email.getText().toString());
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

    private void handleEmail() {
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
                boolean isEmailValid = !TextUtils.isEmpty(s) && ValidateUtils.isValidEmailId(s.toString());
                boolean isPasswordValid = !TextUtils.isEmpty(edt_password.getText()) && s.length() >= 8;
                if (!TextUtils.isEmpty(s) && !ValidateUtils.isValidEmailId(s.toString())) {
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

    @Override
    public void showLoginError(String message) {
        StyleableToast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG, R.style.Error).show();
    }

    @Override
    public void showLoginErrorNeedToVerifyEmail(String message) {
        StyleableToast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG, R.style.Error).show();
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void showPopupNetworkError() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}