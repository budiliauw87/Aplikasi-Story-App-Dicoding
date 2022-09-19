package com.liaudev.dicodingbpaa.ui.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import com.liaudev.dicodingbpaa.App;
import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.customs.CustomProgressDialog;
import com.liaudev.dicodingbpaa.data.remote.response.LoginResponse;
import com.liaudev.dicodingbpaa.databinding.ActivityLoginBinding;
import com.liaudev.dicodingbpaa.ui.BaseActivity;
import com.liaudev.dicodingbpaa.ui.main.MainActivity;
import com.liaudev.dicodingbpaa.ui.register.RegisterActivity;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;

import java.util.Map;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private CustomProgressDialog customProgressDialog;
    private ActivityResultLauncher<Intent> myActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateStatusBarColor(getColor(R.color.transparent));
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance(this);
        loginViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) viewModelFactory).get(LoginViewModel.class);

        myActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                if (data != null) {
                    binding.edtUserEmail.setText(data.getStringExtra("email"));
                }
            }
        }));
        binding.edtUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnLogin.setEnabled(!binding.edtUserEmail.isHasError() && !binding.edtUserPass.isHasError());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.edtUserPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnLogin.setEnabled(!binding.edtUserEmail.isHasError() && !binding.edtUserPass.isHasError());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnLogin.setOnClickListener((v) -> {
            View _view = this.getCurrentFocus();
            if (_view != null) {
                binding.edtUserEmail.clearFocus();
                binding.edtUserPass.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
            }

            loginViewModel.loginAccount(binding.edtUserEmail.getText().toString(), binding.edtUserPass.getText().toString()).observe(this, (loginResponseResource) -> {
                switch (loginResponseResource.status) {
                    case ERROR:
                        customProgressDialog.hide();
                        Toast.makeText(getApplicationContext(), loginResponseResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        if (customProgressDialog == null) {
                            customProgressDialog = new CustomProgressDialog(LoginActivity.this);
                            customProgressDialog.setCancelable(false);
                            customProgressDialog.setCanceledOnTouchOutside(false);
                        }
                        customProgressDialog.show();
                        break;
                    case SUCCESS:
                        customProgressDialog.hide();
                        LoginResponse loginResponse = loginResponseResource.data;
                        if (loginResponse != null) {
                            Map<String, String> userMap = loginResponse.getLoginResult();

                            String userId = userMap.get("userId");
                            String email = binding.edtUserEmail.getText().toString();
                            String name = userMap.get("name");
                            String token = userMap.get("token");
                            Toast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            App.getInstance().saveMyPref(userId, name, token, email, true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                        break;
                }
            });

        });

        binding.tvToRegister.setOnClickListener((v) -> {
            Intent intentToRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            myActivityResultLauncher.launch(intentToRegister);
        });


    }


    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        binding.imgLogo.setTransitionName(null);
        binding.tvTitle.setTransitionName(null);
        binding.tvSubTitle.setTransitionName(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}