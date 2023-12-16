package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertListBitmapToBase64;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.mineralidentyficationapp.adapters.ImagePagerAdapter;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.collection.Minerals;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMineralActivity extends AppCompatActivity {
    private ViewPager imageViewPager;
    private EditText editTextName, editTextValue, editTextWeight, editTextSize,
            editTextInclusion, editTextClarity, editTextComment, editTextTags, discoveryPlace;
    private Spinner spinnerMineralName;

    private Button galleryButton, cameraButton, saveButton, deleteButton, deleteImageButton;

    MineralAppApiClient myApiClient;

    private TextView mineralName, mohsScale, chemicalFormula, occurrencePlace;
    private ImagePagerAdapter imagePagerAdapter;
    List<String> mineralsList;
    List<Bitmap> mineralBitmapList;
    String selectedMineral = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApiClient = new MineralAppApiClient(getBaseContext());
        setContentView(R.layout.activity_main_mineral);

        imageViewPager = findViewById(R.id.imageViewPager);
        editTextName = findViewById(R.id.editTextName);
        editTextValue = findViewById(R.id.editTextValue);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextSize = findViewById(R.id.editTextSize);
        editTextInclusion = findViewById(R.id.editTextInclusion);
        editTextClarity = findViewById(R.id.editTextClarity);
        editTextComment = findViewById(R.id.editTextComment);
        editTextTags = findViewById(R.id.editTextTags);
        spinnerMineralName = findViewById(R.id.spinnerMineralName);
        mineralName = findViewById(R.id.mineralNameTextView);
        mohsScale = findViewById(R.id.mohsScaleTextView);
        chemicalFormula = findViewById(R.id.chemicalFormulaTextView);
        occurrencePlace = findViewById(R.id.occurrencePlaceTextView);
        discoveryPlace = findViewById(R.id.discoveryPlace);
        cameraButton = findViewById(R.id.addImage);
        galleryButton = findViewById(R.id.addImageFromGallery);
        saveButton = findViewById(R.id.saveMineral);
        deleteButton = findViewById(R.id.deleteMineral);
        deleteImageButton = findViewById(R.id.deleteImage);
        setupDeleteImageButton();
        Intent intent = getIntent();
        if (intent != null) {
            selectedMineral = (String) intent.getSerializableExtra("selectedMineralName");
            Log.i("reciv", selectedMineral);
            mineralBitmapList = (List<Bitmap>) intent.getSerializableExtra("mineralBitmapList");
        }

        downloadMineralsList();

        downloadMineralInfo();


        imagePagerAdapter = new ImagePagerAdapter(this, mineralBitmapList);
        imageViewPager.setAdapter(imagePagerAdapter);

        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Dodaj tutaj kod, który ma być wywołany podczas przewijania
            }

            @Override
            public void onPageSelected(int position) {
                // Dodaj tutaj kod, który ma być wywołany po wybraniu konkretnej strony
                Toast.makeText(MainMineralActivity.this, "Wybrano stronę " + (position + 1), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Dodaj tutaj kod, który ma być wywołany po zmianie stanu przewijania
            }
        });

        spinnerMineralName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedMineral = (String) parentView.getItemAtPosition(position);
                Log.i("list", selectedMineral);
                downloadMineralInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valid()) {
                    addMineral(getMessage());
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        int imageSize = 256;
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                mineralBitmapList.add(image);
                imagePagerAdapter.updateData(mineralBitmapList);

            } else {
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                mineralBitmapList.add(image);
                imagePagerAdapter.updateData(mineralBitmapList);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupDeleteImageButton() {
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = imageViewPager.getCurrentItem();
                if (currentPosition >= 0 && currentPosition < mineralBitmapList.size()) {
                    deletePhoto(currentPosition);
                }
            }
        });
    }

    private void deletePhoto(int position) {
        if (!mineralBitmapList.isEmpty() && position >= 0 && position < mineralBitmapList.size()) {
            mineralBitmapList.remove(position);
            imagePagerAdapter.updateData(mineralBitmapList);
            imagePagerAdapter.notifyDataSetChanged();
            Log.i("delete", "delete :"+position);
            imageViewPager.setAdapter(imagePagerAdapter);
            int newPosition = Math.min(position, mineralBitmapList.size()-1);
            imageViewPager.setCurrentItem(newPosition, true);

        }
    }

    private boolean valid() {
        return false;
    }


    private MineralMessage getMessage() {
        return MineralMessage.builder()
                .mineralName(mineralName.getText().toString())
                .name(editTextName.getText().toString())
                .comment(editTextComment.getText().toString())
                .size(editTextSize.getText().toString())
                .discoveryPlace(discoveryPlace.getText().toString())
                .weight(editTextWeight.getText().toString())
                .value(editTextValue.getText().toString())
                .clarity(editTextClarity.getText().toString())
                .inclusion(editTextInclusion.getText().toString())
                .images(convertListBitmapToBase64(mineralBitmapList))
                .build();
    }

    private void loadMineralData(MineralMessage message) {

        mineralName.setText(message.getMineralName());
        editTextName.setText(message.getName());
        editTextComment.setText(message.getComment());
        editTextSize.setText(message.getSize());
        discoveryPlace.setText(message.getDiscoveryPlace());
        editTextWeight.setText(message.getWeight());
        editTextValue.setText(message.getValue());
        editTextClarity.setText(message.getClarity());
        editTextInclusion.setText(message.getInclusion());
        //mineralBitmapList.addAll(message.getImages());
        //imagePagerAdapter.updateData(mineralBitmapList);
    }

    private void addMineral(MineralMessage mineralMessage) {
        myApiClient.addNewMineral(new RxCallback<>() {
            @Override
            public void onSuccess(MineralMessage minerals) {
                Log.i("getMineralStats", "success");

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("getMineralStats", "error: " + errorMessage);
            }
        }, mineralMessage);

    }

    private void downloadMineralInfo() {
        myApiClient.getMineral(new RxCallback<>() {
            @Override
            public void onSuccess(Minerals minerals) {
                Log.i("getMineralStats", "success");

                if (minerals != null) {
                    mineralName.setText(minerals.getMineralName());
                    mohsScale.setText(minerals.getMohsScale().toString());
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

    private void downloadMineralsList() {
        myApiClient.getMineralsNames(new RxCallback<>() {
            @Override
            public void onSuccess(List<String> result) {
                Log.i("classification", "success");
                mineralsList = result;
                ArrayAdapter<String> mineralAdapter = new ArrayAdapter<>(MainMineralActivity.this, android.R.layout.simple_spinner_item, result);
                mineralAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMineralName.setAdapter(mineralAdapter);

                if (selectedMineral != null && !selectedMineral.isEmpty()) {
                    int spinnerPosition = mineralAdapter.getPosition(selectedMineral);
                    spinnerMineralName.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("classification", "error: " + errorMessage);
            }
        });
    }
}