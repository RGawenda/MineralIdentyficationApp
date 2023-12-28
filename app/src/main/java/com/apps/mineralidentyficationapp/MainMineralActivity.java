package com.apps.mineralidentyficationapp;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBase64ListToBitmap;
import static com.apps.mineralidentyficationapp.utils.FileUtils.convertListBitmapToBase64;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.apps.mineralidentyficationapp.adapters.TagsAdapter;
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

    private Button galleryButton, cameraButton, saveButton, deleteButton, deleteImageButton, addTag;

    MineralAppApiClient myApiClient;

    private TextView mineralName, mohsScale, chemicalFormula, occurrencePlace;
    private ImagePagerAdapter imagePagerAdapter;
    List<String> mineralsList;
    List<String> tagsList = new ArrayList<>();
    List<Bitmap> mineralBitmapList;
    List<Long> imagesID = new ArrayList<>();
    String selectedMineral = "";

    MineralMessage mineralMessage;

    private TagsAdapter adapter;
    Context context;

    Long id = 1L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myApiClient = new MineralAppApiClient(getBaseContext());
        setContentView(R.layout.activity_main_mineral);

        imageViewPager = findViewById(R.id.mineralImageViewPager);
        editTextName = findViewById(R.id.mineralEditTextName);
        editTextValue = findViewById(R.id.mineralEditTextValue);
        editTextWeight = findViewById(R.id.mineralEditTextWeight);
        editTextSize = findViewById(R.id.mineralEditTextSize);
        editTextInclusion = findViewById(R.id.mineralEditTextInclusion);
        editTextClarity = findViewById(R.id.mineralEditTextClarity);
        editTextComment = findViewById(R.id.mineralEditTextComment);
        editTextTags = findViewById(R.id.mineralEditTextTags);
        spinnerMineralName = findViewById(R.id.mineralSpinnerMineralName);
        mineralName = findViewById(R.id.mineralItemMineralNameTextView);
        mohsScale = findViewById(R.id.mineralMohsScaleTextView);
        chemicalFormula = findViewById(R.id.mineralChemicalFormulaTextView);
        occurrencePlace = findViewById(R.id.mineralOccurrencePlaceTextView);
        discoveryPlace = findViewById(R.id.mineralEditTextDiscoveryPlace);
        cameraButton = findViewById(R.id.mineralAddImageButton);
        galleryButton = findViewById(R.id.mineralAddImageGalleryButton);
        saveButton = findViewById(R.id.mineralSaveMineralButton);
        deleteButton = findViewById(R.id.mineralDeleteMineralButton);
        deleteImageButton = findViewById(R.id.mineralDeleteImageButton);
        addTag = findViewById(R.id.mineralAddTagButton);

        RecyclerView recyclerView = findViewById(R.id.mineralRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TagsAdapter(tagsList);
        recyclerView.setAdapter(adapter);

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

        context = getBaseContext();

        downloadMineralsList();

        downloadMineralInfo();

        imagePagerAdapter = new ImagePagerAdapter(this, mineralBitmapList);
        imageViewPager.setAdapter(imagePagerAdapter);

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = imageViewPager.getCurrentItem();
                if (currentPosition >= 0 && currentPosition < mineralBitmapList.size()) {
                    deletePhoto(currentPosition);
                }
            }
        });

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
                    if (mineralMessage.getId() != null) {
                        updateMineral(getMessage());
                    } else {
                        addMineral(getMessage());
                    }
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mineralMessage.getId() != null) {
                    deleteMineral();
                }

                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextTags.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    tagsList.add(text);
                    adapter.notifyItemInserted(tagsList.size() - 1);
                    editTextTags.getText().clear();
                }
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


    private void deletePhoto(int position) {
        if (!mineralBitmapList.isEmpty() && position >= 0 && position < mineralBitmapList.size()) {
            mineralBitmapList.remove(position);
            if (mineralMessage.getId() != null && position < mineralMessage.getImagesID().size()) {
                Log.i("position: ", "" + position);
                Log.i("id to delete: ", mineralMessage.getImagesID().get(position).toString());
                imagesID.add(mineralMessage.getImagesID().get(position));
                mineralMessage.getImagesID().remove(position);
            }

            imagePagerAdapter.updateData(mineralBitmapList);
            imagePagerAdapter.notifyDataSetChanged();
            Log.i("delete", "delete :" + position);
            imageViewPager.setAdapter(imagePagerAdapter);
            int newPosition = Math.min(position, mineralBitmapList.size() - 1);
            imageViewPager.setCurrentItem(newPosition, true);

        }
    }

    private boolean valid() {
        if (editTextName.length() > 0) {
            return true;
        } else {
            Toast.makeText(context, "name no be null", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private MineralMessage getMessage() {
        if (mineralMessage.getId() != null) {
            mineralMessage.setMineralName(mineralName.getText().toString());
            mineralMessage.setName(editTextName.getText().toString());
            mineralMessage.setComment(editTextComment.getText().toString());
            mineralMessage.setSize(getDoubleFromString(editTextSize.getText().toString()));
            mineralMessage.setDiscoveryPlace(discoveryPlace.getText().toString());
            mineralMessage.setWeight(getDoubleFromString(editTextWeight.getText().toString()));
            mineralMessage.setValue(getDoubleFromString(editTextValue.getText().toString()));
            mineralMessage.setClarity(editTextClarity.getText().toString());
            mineralMessage.setInclusion(editTextInclusion.getText().toString());
            List<String> toSend = convertListBitmapToBase64(mineralBitmapList);

            if (mineralMessage.getImagesID().size() > 0) {
                toSend.subList(0, mineralMessage.getImagesID().size()).clear();
            }

            mineralMessage.setImages(toSend);
            mineralMessage.setDeletedImages(imagesID);
            mineralMessage.setTags(tagsList);
            return mineralMessage;
        }

        return MineralMessage.builder()
                .mineralName(mineralName.getText().toString())
                .name(editTextName.getText().toString())
                .comment(editTextComment.getText().toString())
                .size(getDoubleFromString(editTextSize.getText().toString()))
                .discoveryPlace(discoveryPlace.getText().toString())
                .weight(getDoubleFromString(editTextWeight.getText().toString()))
                .value(getDoubleFromString(editTextValue.getText().toString()))
                .clarity(editTextClarity.getText().toString())
                .inclusion(editTextInclusion.getText().toString())
                .images(convertListBitmapToBase64(mineralBitmapList))
                .tags(tagsList)
                .build();
    }

    public static Double getDoubleFromString(String value) {
        Log.i("convertToDouble", value);
        return !value.isEmpty() ? Double.parseDouble(value) : 0.00;
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
        tagsList.addAll(mineralMessage.getTags());
    }


    private void addMineral(MineralMessage mineralMessage) {
        myApiClient.addNewMineral(new RxCallback<>() {
            @Override
            public void onSuccess(MineralMessage minerals) {
                Log.i("addMineral", "success");
                Intent myIntent = new Intent(context, MainActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("addMineral", "error: " + errorMessage);
            }
        }, id, mineralMessage);

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

    private void downloadMineralsList() {
        myApiClient.getMineralsNames(new RxCallback<>() {
            @Override
            public void onSuccess(List<String> result) {
                Log.i("downloadMineralsList", "success");
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
                Log.i("downloadMineralsList", "error: " + errorMessage);
            }
        });
    }

    private void updateMineral(MineralMessage toSend) {
        myApiClient.editMineral(new RxCallback<>() {
            @Override
            public void onSuccess(MineralMessage result) {
                Log.i("updateMineral", "success");
                Intent myIntent = new Intent(context, CollectionActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onError(String errorMessage) {
                Log.i("updateMineral", "error: " + errorMessage);
            }
        }, id, toSend);
    }

    private void deleteMineral() {
        myApiClient.deleteMineral(new RxCallback<>() {
            @Override
            public void onSuccess(Object result) {
                Log.i("deleteMineral", "success");

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("deleteMineral", "error: " + errorMessage);
            }
        }, mineralMessage.getId());
    }
}