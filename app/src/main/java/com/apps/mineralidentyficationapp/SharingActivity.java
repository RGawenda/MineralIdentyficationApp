package com.apps.mineralidentyficationapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.mineralidentyficationapp.adapters.UsersAdapter;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;
import com.apps.mineralidentyficationapp.rest.messages.RegisterRequest;
import com.apps.mineralidentyficationapp.utils.AccountType;

import java.util.ArrayList;
import java.util.List;

public class SharingActivity extends AppCompatActivity implements UsersAdapter.OnRecordDeleteClickListener {
    private List<String> usersList = new ArrayList<>();
    private MineralAppApiClient myApiClient;
    private EditText editTextUsername;
    private UsersAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton radioBtnOption1, radioBtnOption2;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        radioGroup = findViewById(R.id.sharingUserTypeButton);
        radioBtnOption1 = findViewById(R.id.sharingRadioButtonOption1);
        radioBtnOption2 = findViewById(R.id.sharingRadioButtonOption2);
        change = findViewById(R.id.sharingChangeButton);

        Button shareButton = findViewById(R.id.sharingAddButton);
        myApiClient = new MineralAppApiClient(getBaseContext());

        editTextUsername = findViewById(R.id.sharingUsername);

        RecyclerView recyclerView = findViewById(R.id.sharingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAccountType();

        adapter = new UsersAdapter(usersList);
        adapter.setOnRecordDeleteClickListener(this);
        recyclerView.setAdapter(adapter);

        RegisterRequest registerRequest = new RegisterRequest();
        getUsersShared();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextUsername.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    shareCollection(text);
                    usersList.add(text);
                    adapter.notifyItemInserted(usersList.size() - 1);
                    editTextUsername.getText().clear();
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    changeTypeAccount(getOptionFromRadioButton(selectedRadioButton));
                } else {
                    changeTypeAccount(AccountType.PRIVATE);
                }

            }
        });

    }

    @Override
    public void onRecordDeleteClick(int position) {
        Log.i("deleteTag2", "delete:" + position);

        unsharedTheCollection(adapter.getItem(position));
        adapter.removeRecord(position);
    }

    private void shareCollection(String username) {
        myApiClient.shareCollection(new RxCallback<>() {
            @Override
            public void onSuccess(String info) {
                Log.i("shareCollection", "success");
                getUsersShared();
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("shareCollection", "error: " + errorMessage);
            }
        }, username);
    }

    private void unsharedTheCollection(String username) {
        myApiClient.unsharedTheCollection(new RxCallback<>() {
            @Override
            public void onSuccess(String info) {
                Log.i("unshareTheCollection", "success");
                getUsersShared();
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("unshareTheCollection", "error: " + errorMessage);
            }
        }, username);
    }

    private void getUsersShared() {
        myApiClient.getUsersShare(new RxCallback<>() {
            @Override
            public void onSuccess(List<String> users) {
                Log.i("shareCollection", "success");
                adapter.updateList(users);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("shareCollection", "error: " + errorMessage);
            }
        });
    }

    private void changeTypeAccount(AccountType accountType) {
        myApiClient.changeTypeAccount(new RxCallback<>() {
            @Override
            public void onSuccess(String info) {
                Log.i("changeTypeAccount", "success");

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("changeTypeAccount", "error: " + errorMessage);
            }
        }, accountType);
    }

    private void getAccountType() {
        myApiClient.getAccountType(new RxCallback<>() {
            @Override
            public void onSuccess(AccountType accountType) {
                Log.i("getAccountType", "success");
                setRadioButton(accountType);

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("getAccountType", "error: " + errorMessage);
            }
        });
    }

    private AccountType getOptionFromRadioButton(RadioButton radioButton) {
        if (radioBtnOption1.isChecked()) {
            return AccountType.PUBLIC;
        }
        return AccountType.PRIVATE;
    }

    private void setRadioButton(AccountType accountType) {
        if (AccountType.PUBLIC == accountType) {
            radioBtnOption1.setChecked(true);
            radioBtnOption2.setChecked(false);
            return;
        }
        radioBtnOption1.setChecked(false);
        radioBtnOption2.setChecked(true);
    }


}