package com.liaudev.dicodingbpaa.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.customs.CustomProgressDialog;
import com.liaudev.dicodingbpaa.databinding.ActivityRegisterBinding;
import com.liaudev.dicodingbpaa.ui.BaseActivity;
import com.liaudev.dicodingbpaa.viewmodel.ViewModelFactory;

/**
 * Created by Budiliauw87 on 2022-05-14.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */

public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    private CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        updateStatusBarColor(getColor(R.color.transparent));
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance(this);
        registerViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) viewModelFactory).get(RegisterViewModel.class);
        binding.tvToLogin.setOnClickListener((v)->{
            finish();
        });

        binding.btnRegister.setOnClickListener((v)->{
            View _view = this.getCurrentFocus();
            if (_view != null) {
                binding.edtUserName.clearFocus();
                binding.edtUserEmail.clearFocus();
                binding.edtUserPass.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
            }
            final String name = binding.edtUserName.getText().toString();
            final String email = binding.edtUserEmail.getText().toString();
            final String pass = binding.edtUserPass.getText().toString();

            registerViewModel.registerAccount(name,email,pass).observe(this, (registerResponseResource)-> {
                switch (registerResponseResource.status){
                    case LOADING:
                        if (customProgressDialog == null) {
                            customProgressDialog = new CustomProgressDialog(RegisterActivity.this);
                            customProgressDialog.setCancelable(false);
                            customProgressDialog.setCanceledOnTouchOutside(false);
                        }
                        customProgressDialog.show();
                        break;
                    case ERROR:
                        customProgressDialog.hide();
                        Toast.makeText(getApplicationContext(), registerResponseResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        customProgressDialog.hide();
                        Intent result = new Intent();
                        result.putExtra("email",email);
                        setResult(Activity.RESULT_OK, result);
                        finish();
                        break;
                }
            });

        });

        binding.edtUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnRegister.setEnabled(!binding.edtUserEmail.isHasError() && !binding.edtUserPass.isHasError()
                        && !binding.edtUserName.isHasError());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.btnRegister.setEnabled(!binding.edtUserEmail.isHasError() && !binding.edtUserPass.isHasError()
                        && !binding.edtUserName.isHasError());
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
                binding.btnRegister.setEnabled(!binding.edtUserEmail.isHasError() && !binding.edtUserPass.isHasError()
                        && !binding.edtUserName.isHasError());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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