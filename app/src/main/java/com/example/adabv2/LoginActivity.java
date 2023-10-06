package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.adabv2.databinding.ActivityLoginBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonRegisterInLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        binding.buttonLogin.setOnClickListener(v -> {
            validasi();
        });


    }

    private void validasi(){
        String email = binding.emailLogin.getText().toString();
        String password = binding.passwordLogin.getText().toString();

        if(email.isEmpty()){
            binding.emailLogin.setError("username must field");
            binding.emailLogin.requestFocus();
            return;
        }
        else if(password.isEmpty()){
            binding.passwordLogin.setError("Password must field");
            binding.passwordLogin.requestFocus();
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUser_email(email);
        loginRequest.setUser_password(password);
        Call<ResponseBody> callLogin = ApiClient.request().loginUser(loginRequest);
        callLogin.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    if(response.code() == 401){
                        Toast.makeText(LoginActivity.this, "Data sudah pernah diinput sebelumnya", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_LONG).show();
            }
        });


    }
}