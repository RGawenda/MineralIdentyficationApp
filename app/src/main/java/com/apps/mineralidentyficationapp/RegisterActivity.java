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
import com.apps.mineralidentyficationapp.rest.messages.AuthenticationResponse;
import com.apps.mineralidentyficationapp.rest.messages.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    private SessionManager sessionManager;
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerRegisterButton);
        editTextUsername = findViewById(R.id.registerEditTextNewUsername);
        editTextPassword = findViewById(R.id.registerEditTextNewPassword);
        sessionManager = new SessionManager(getApplicationContext());


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextUsername.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
                    RegisterRequest registerRequest = new RegisterRequest();
                    registerRequest.setUsername(editTextUsername.getText().toString());
                    registerRequest.setPassword(editTextPassword.getText().toString());
                    register(registerRequest);
                }
            }
        });
    }


    private void register(RegisterRequest username) {
        MineralAppApiClient myApiClient = new MineralAppApiClient(getBaseContext());
        myApiClient.register(new RxCallback<>() {
            @Override
            public void onSuccess(AuthenticationResponse result) {
                Log.i("register", "success");
                sessionManager.createLoginSession(username.getUsername(),result.getToken());
                Log.i("register"," username: "+username.getUsername()+" token: "+result.getToken());
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("register", "error: " + errorMessage);
            }
        }, username);
    }

}