package com.apps.mineralidentyficationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apps.mineralidentyficationapp.adapters.MineralInCollectionAdapter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.databinding.ActivityCollectionBinding;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ActivityCollectionBinding binding;
    private Context context;
    private MineralInCollectionAdapter mineralInCollectionAdapter;
    private MineralAppApiClient mineralApiClient;
    private int currentPage = 0;
    private int pageSize = 10;

    private String username = "";

    List<MineralMessage> mineralMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = getBaseContext();

        mineralApiClient = new MineralAppApiClient(context);

//        String[] mineralNames = {"", "amentyst", "kwarc"};
//        int[] images = {R.drawable.image_0058362, R.drawable.image_0059505, R.drawable.kwarc7};

        mineralInCollectionAdapter = new MineralInCollectionAdapter(CollectionActivity.this);
        binding.gridView.setAdapter(mineralInCollectionAdapter);

        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MineralMessage mineralMessage = (MineralMessage) mineralInCollectionAdapter.getItem(position);
                if (mineralMessage != null) {
                    Log.i("test click", "click on item");
                    Intent myIntent = new Intent(view.getContext(), MainMineralActivity.class);
                    myIntent.putExtra("mineralMessage", mineralMessage);
                    startActivity(myIntent);
                    finish();
                }
            }
        });


        binding.gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mineralInCollectionAdapter.handleScrolling(visibleItemCount, totalItemCount, firstVisibleItem);
            }

        });
    }

    private void loadMoreData() {
        currentPage++;

        MineralAppApiClient myApiClient = new MineralAppApiClient(context);

        myApiClient.getCollection(new RxCallback<>() {
            @Override
            public void onSuccess(List<MineralMessage> mineralsResult) {
                Log.i("downloadMineralsNames", "success");
                mineralMessageList = mineralsResult;
                mineralInCollectionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("downloadMineralsNames", "error: " + errorMessage);
            }
        }, currentPage, pageSize, username);
    }

}