package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBase64ListToBitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apps.mineralidentyficationapp.adapters.ImagePagerAdapter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.util.List;

public class UneditableMineralActivity extends AppCompatActivity {
    private TextView mineralName, mohsScale, chemicalFormula, occurrencePlace,editTextName, editTextValue, editTextWeight, editTextSize,
            editTextInclusion, editTextClarity, editTextComment, discoveryPlace;
    private ViewPager imageViewPager;
    String selectedMineral = "";
    MineralAppApiClient myApiClient;
    MineralMessage mineralMessage;
    List<Bitmap> mineralBitmapList;
    private ImagePagerAdapter imagePagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uneditable_mineral);

        myApiClient = new MineralAppApiClient(getBaseContext());

        imageViewPager = findViewById(R.id.unEditMineralImageViewPager);
        editTextName = findViewById(R.id.unEditMineralEditTextName);
        editTextValue = findViewById(R.id.unEditMineralEditTextValue);
        editTextWeight = findViewById(R.id.unEditMineralEditTextWeight);
        editTextSize = findViewById(R.id.unEditMineralEditTextSize);
        editTextInclusion = findViewById(R.id.unEditMineralEditTextInclusion);
        editTextClarity = findViewById(R.id.unEditMineralEditTextClarity);
        editTextComment = findViewById(R.id.unEditMineralEditTextComment);
        mineralName = findViewById(R.id.unEditMineralItemMineralNameTextView);
        mohsScale = findViewById(R.id.unEditMineralMohsScaleTextView);
        chemicalFormula = findViewById(R.id.unEditMineralChemicalFormulaTextView);
        occurrencePlace = findViewById(R.id.unEditMineralOccurrencePlaceTextView);
        discoveryPlace = findViewById(R.id.unEditMineralEditTextDiscoveryPlace);

        Intent intent = getIntent();
        if (intent != null) {
            mineralMessage = (MineralMessage) intent.getSerializableExtra("mineralMessage");
            if (mineralMessage != null && !mineralMessage.getImages().isEmpty()) {
                selectedMineral = mineralMessage.getMineralName();
                mineralBitmapList = convertBase64ListToBitmap(mineralMessage.getImages());
            }

            if (mineralMessage.getId() != null) {
                loadMineralData(mineralMessage);
            }
        }

        downloadMineralInfo();
        imagePagerAdapter = new ImagePagerAdapter(this, mineralBitmapList);
        imageViewPager.setAdapter(imagePagerAdapter);


        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void loadMineralData(MineralMessage message) {
        mineralName.setText(message.getMineralName());
        editTextName.setText(message.getName());
        editTextComment.setText(message.getComment());
        editTextSize.setText(String.valueOf(message.getSize()));
        discoveryPlace.setText(message.getDiscoveryPlace());
        editTextWeight.setText(String.valueOf(message.getWeight()));
        editTextValue.setText(String.valueOf(message.getValue()));
        editTextClarity.setText(message.getClarity());
        editTextInclusion.setText(message.getInclusion());
    }

    private void downloadMineralInfo() {
        myApiClient.getMineral(new RxCallback<>() {
            @Override
            public void onSuccess(Minerals minerals) {
                Log.i("getMineralStats", "success");

                if (minerals != null) {
                    mineralName.setText(minerals.getMineralName());
                    if (minerals.getMohsScale() != null) {
                        mohsScale.setText(Double.toString(minerals.getMohsScale()));
                    } else {
                        mohsScale.setText("no data");

                    }
                    chemicalFormula.setText(minerals.getChemicalFormula());
                    occurrencePlace.setText(minerals.getOccurrencePlace());
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("getMineralStats", "error: " + errorMessage);
            }
        }, selectedMineral);

    }
}