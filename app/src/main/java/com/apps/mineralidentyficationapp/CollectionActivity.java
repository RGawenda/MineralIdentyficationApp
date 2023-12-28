package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.MainMineralActivity.getDoubleFromString;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.apps.mineralidentyficationapp.adapters.MineralCollectionAdapter;
import com.apps.mineralidentyficationapp.collection.FoundMineralFilter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.databinding.ActivityCollectionBinding;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ActivityCollectionBinding binding;
    private Spinner spinnerTag;
    private MineralCollectionAdapter mineralInCollectionAdapter;
    private LinearLayout filterContainer;
    Button saveFilters;
    Long userID = 1L;
    FoundMineralFilter filter = new FoundMineralFilter();
    String selectedTag;
    String emptyTag;
    String selectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spinnerTag = findViewById(R.id.collectionSpinnerTags);
        filter.setUserID(1L);
        mineralInCollectionAdapter = new MineralCollectionAdapter(CollectionActivity.this, filter);
        binding.gridView.setAdapter(mineralInCollectionAdapter);
        filterContainer = findViewById(R.id.filterContainer);
        saveFilters = findViewById(R.id.collectionFilterSaveButton);
        emptyTag = getString(R.string.activity_collectionEmptyTag);
        selectedTag = emptyTag;
        downloadTagsList();
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

        spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTag = (String) parentView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        saveFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter = new FoundMineralFilter();
                filter.setMineralName(binding.collectionMineralName.getText().toString());
                filter.setComment(binding.collectionComment.getText().toString());
                filter.setDiscoveryPlace(binding.collectionDiscoveryPlace.getText().toString());
                filter.setName(binding.collectionName.getText().toString());

                if (!binding.collectionSizeMax.getText().toString().isEmpty()) {
                    filter.setMaxSize(getDoubleFromString(binding.collectionSizeMax.getText().toString()));
                }
                if (!binding.collectionValueMax.getText().toString().isEmpty()) {
                    filter.setMaxValue(getDoubleFromString(binding.collectionValueMax.getText().toString()));
                }
                if (!binding.collectionWeightMax.getText().toString().isEmpty()) {
                    filter.setMaxWeight(getDoubleFromString(binding.collectionWeightMax.getText().toString()));
                }
                if (!binding.collectionMohsScaleMax.getText().toString().isEmpty()) {
                    filter.setMaxWeight(getDoubleFromString(binding.collectionMohsScaleMax.getText().toString()));
                }
                if (!binding.collectionSizeMin.getText().toString().isEmpty()) {
                    filter.setMinSize(getDoubleFromString(binding.collectionSizeMin.getText().toString()));
                }
                if (!binding.collectionValueMin.getText().toString().isEmpty()) {
                    filter.setMinValue(getDoubleFromString(binding.collectionValueMin.getText().toString()));
                }
                if (!binding.collectionWeightMin.getText().toString().isEmpty()) {
                    filter.setMinWeight(getDoubleFromString(binding.collectionWeightMin.getText().toString()));
                }
                if (!binding.collectionMohsScaleMin.getText().toString().isEmpty()) {
                    filter.setMinWeight(getDoubleFromString(binding.collectionMohsScaleMin.getText().toString()));
                }
                if (!selectedTag.equals(emptyTag)) {
                    filter.setTagName(selectedTag);
                }

                filter.setUserID(userID);
                mineralInCollectionAdapter = new MineralCollectionAdapter(CollectionActivity.this, filter);
                binding.gridView.setAdapter(mineralInCollectionAdapter);

            }
        });
    }

    private void downloadTagsList() {
        MineralAppApiClient myApiClient = new MineralAppApiClient(getBaseContext());
        myApiClient.getTags(new RxCallback<>() {
            @Override
            public void onSuccess(List<String> result) {
                Log.i("downloadTagsList", "success");
                result.add(0, emptyTag);
                selectedTag = emptyTag;
                ArrayAdapter<String> mineralAdapter = new ArrayAdapter<>(CollectionActivity.this, android.R.layout.simple_spinner_item, result);
                mineralAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTag.setAdapter(mineralAdapter);

                if (selectedTags != null && !selectedTags.isEmpty()) {
                    int spinnerPosition = mineralAdapter.getPosition(selectedTags);
                    spinnerTag.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("downloadTagsList", "error: " + errorMessage);
            }
        }, userID);
    }

    public void toggleFilters(View view) {
        //TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.filterContainer));
        if (filterContainer.getVisibility() == View.VISIBLE) {
            filterContainer.setVisibility(View.GONE);
        } else {
            filterContainer.setVisibility(View.VISIBLE);
        }
    }

}