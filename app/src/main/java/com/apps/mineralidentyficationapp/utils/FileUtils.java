package com.apps.mineralidentyficationapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static String convertBitmapToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String base64String) {
        if (base64String.startsWith("data:image/png;base64,")) {
            base64String = base64String.replace("data:image/png;base64,", "");
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static List<Bitmap> convertBase64ListToBitmap(List<String> base64Strings) {
        List<Bitmap> convertedImages = new ArrayList<>();
        for(String image: base64Strings){
            convertedImages.add(convertBase64ToBitmap(image));
        }
        return convertedImages;
    }

    public static List<String> convertListBitmapToBase64(List<Bitmap> images) {
        List<String> convertedImages = new ArrayList<>();
        for (Bitmap image : images) {
        convertedImages.add(convertBitmapToBase64(image));
        }
        return convertedImages;
    }

}
