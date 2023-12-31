package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.MainMineralActivity.getDoubleFromString;
import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBase64ListToBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.BitSet;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ActivityCollectionBinding binding;
    private Spinner spinnerTag;
    private MineralCollectionAdapter mineralInCollectionAdapter;
    private LinearLayout filterContainer;
    Button saveFilters;
    FoundMineralFilter filter = new FoundMineralFilter();
    String selectedTag;
    String emptyTag;
    String selectedTags;
    String username;
    String newImage;

    Boolean editable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        Log.i("collection", "start collection");

        setContentView(binding.getRoot());

        Intent intent = getIntent();

        SessionManager sessionManager = new SessionManager(getBaseContext());

        if (intent != null) {
            String message = intent.getStringExtra("controller");
            if("addImage".equals(message)){
                editable = true;
                Log.i("controller", "add image");
                username = sessionManager.getUsername();
                MineralMessage mineralSelected = (MineralMessage) intent.getSerializableExtra("mineralMessage");
                if(mineralSelected != null){
                    Log.i("collection", "ste image");

                    newImage = mineralSelected.getImages().get(0);
                    filter.setMineralName(mineralSelected.getMineralName());
                    binding.collectionMineralName.setText(mineralSelected.getMineralName());
                }

            } else if ("user-collection".equals(message)) {
                Log.i("controller", "user collection");
                editable = false;
                username = intent.getStringExtra("username");
            }else {
                Log.i("controller", "self collection");
                editable = true;
                username = sessionManager.getUsername();
            }
        }

        spinnerTag = findViewById(R.id.collectionSpinnerTags);
        filter.setUser(username);
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
                    if(editable){
                        Log.i("test click", "click on item");
                        Intent myIntent = new Intent(view.getContext(), MainMineralActivity.class);
                        if(newImage != null && !newImage.isEmpty()){
                            Log.i("collection", "added image");

                            mineralMessage.getImages().add(newImage);
                        }
                        myIntent.putExtra("mineralMessage", mineralMessage);
                        startActivity(myIntent);
                        finish();
                    }else{
                        Intent myIntent = new Intent(view.getContext(), UneditableMineralActivity.class);
                        myIntent.putExtra("mineralMessage", mineralMessage);
                        startActivity(myIntent);
                        finish();
                    }

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

                filter.setUser(username);
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
        }, username);
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