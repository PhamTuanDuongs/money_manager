package com.example.money_manager.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.money_manager.R;
import com.example.money_manager.contract.ProfileContract;
import com.example.money_manager.contract.presenter.ProfilePresenter;
import com.example.money_manager.utils.AccountState;
import com.example.money_manager.utils.DialogUtils;

public class ProfileFragment extends Fragment implements ProfileContract.View {

private TextView txt_edit;
private TextView txt_E_mail_address;
private ProfilePresenter presenter;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenter(ProfileFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        txt_E_mail_address = v.findViewById(R.id.txt_E_mail_address);
        txt_edit = v.findViewById(R.id.txt_reset);
        showProfile();
        return v;
    }

    @Override
    public void showProfile() {
        String email = AccountState.getEmail(requireContext(), "email");
        txt_E_mail_address.setText(email);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickChangePassword(txt_E_mail_address.getText().toString());
            }
        });
    }

    @Override
    public void showErrorNotifyResetPassword(String message) {
        DialogUtils.showDialogSuccess("Send email to reset password failed", message, requireContext());
    }

    @Override
    public void showNotifyResetPassword() {
        DialogUtils.showDialogSuccess("Send email to reset password successful", "Please go to your email address to reset.", requireContext());
    }
}