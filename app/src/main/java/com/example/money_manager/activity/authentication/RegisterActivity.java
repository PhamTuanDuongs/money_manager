package com.example.money_manager.activity.authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.money_manager.R;
import com.example.money_manager.contract.RegisterContract;
import com.example.money_manager.contract.presenter.RegisterPresenter;
import com.example.money_manager.utils.AESUtil;
import com.example.money_manager.utils.AccountState;
import com.example.money_manager.utils.DialogUtils;
import com.example.money_manager.utils.NetworkUtils;
import com.example.money_manager.utils.ValidateUtils;

import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

     private EditText edt_email, edt_password;
    private TextView error_email, error_password;
    private Button btn_signup;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        init();
        handleSignUp();
    }

    public void init() {
        registerPresenter = new RegisterPresenter(RegisterActivity.this);
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
    public void handleSignUp() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();
                if(NetworkUtils.isNetworkAvailable(RegisterActivity.this)){
                    registerPresenter.onRegisterButtonClick(email,password);
                }else{
                    showPopupNetworkError();
                }
            }
        });
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
            boolean isEmailValid = !TextUtils.isEmpty(edt_email.getText()) && ValidateUtils.isValidEmailId(edt_email.getText().toString());
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
                boolean isEmailValid = !TextUtils.isEmpty(s) && ValidateUtils.isValidEmailId(s.toString());
                boolean isPasswordValid = !TextUtils.isEmpty(edt_password.getText()) && s.length() >= 8;
                if (!TextUtils.isEmpty(s) && !ValidateUtils.isValidEmailId(s.toString())) {
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


    @Override
    public void showRegistrationError(String message) {
        DialogUtils.showDialogError("Account Exists", "The account already exists. Please use a different email.", this);
    }
    @Override
    public void showRegistrationSuccess(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration Successful");
        builder.setMessage("Your account has been successfully created. Please verify your email to continue.");
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        });
        AlertDialog alertDialog = builder.create();
        AccountState.saveEmail(this, "", "email");
        alertDialog.show();
    }
    @Override
    public void resetDataOfFields() {
        edt_email.setText("");
        edt_password.setText("");
        error_password.setText("");
        btn_signup.setEnabled(false);
    }

    @Override
    public void showPopupNetworkError(){
        DialogUtils.showDialogError("No internet Connection", "Please turn on internet connection to continue", this);
    }

}