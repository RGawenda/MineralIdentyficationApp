package com.apps.mineralidentyficationapp.utils;

import android.graphics.Bitmap;
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

    public static List<String> convertListBitmapToBase64(List<Bitmap> images) {
        List<String> convertedImages = new ArrayList<>();
        for (Bitmap image : images) {
        convertedImages.add(convertBitmapToBase64(image));
        }
        return convertedImages;
    }
}
