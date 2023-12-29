package com.apps.mineralidentyficationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;
import com.apps.mineralidentyficationapp.rest.messages.AuthRequest;
import com.apps.mineralidentyficationapp.rest.messages.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonToRegister = findViewById(R.id.buttonToRegister);

        sessionManager = new SessionManager(getApplicationContext());
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthRequest authRequest = new AuthRequest();
                authRequest.setUsername(editTextUsername.getText().toString());
                authRequest.setPassword(editTextPassword.getText().toString());

                login(authRequest);
            }
        });

        buttonToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void login(AuthRequest username) {
        MineralAppApiClient myApiClient = new MineralAppApiClient(getBaseContext());
        myApiClient.login(new RxCallback<>() {
            @Override
            public void onSuccess(AuthenticationResponse result) {
                Log.i("login", "success");
                sessionManager.createLoginSession(username.getUsername(),result.getToken());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Log.i("login"," username: "+username.getUsername()+" token: "+result.getToken());

                startActivity(intent);
                finish();

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("login", "error: " + errorMessage);
            }
        }, username);
    }

}