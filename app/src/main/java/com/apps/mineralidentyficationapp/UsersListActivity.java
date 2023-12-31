package com.apps.mineralidentyficationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.mineralidentyficationapp.adapters.MineralAdapter;
import com.apps.mineralidentyficationapp.adapters.UsersListAdapter;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    private MineralAppApiClient myApiClient;
    private List<String> users = new ArrayList<>();

    UsersListAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        myApiClient = new MineralAppApiClient(getBaseContext());
        recyclerView = findViewById(R.id.usersRecyclerView);
        EditText searchEditText = findViewById(R.id.usersEditText);
        context = getBaseContext();
        layoutManager = new LinearLayoutManager(this);
        adapter = new UsersListAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getUsers();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void loadUserCollection(String username){
        Intent intent = new Intent(context, CollectionActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("controller", "user-collection");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void getUsers() {
        myApiClient.getUsers(new RxCallback<>() {
            @Override
            public void onSuccess(List<String> usersUsername) {
                Log.i("unshareTheCollection", "success"+usersUsername.size()+usersUsername.get(0));
                users = usersUsername;

                adapter = new UsersListAdapter(users);

                adapter.setOnItemClickListener(username -> {
                    loadUserCollection(username);
                });

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("unshareTheCollection", "error: " + errorMessage);
            }
        });
    }
}