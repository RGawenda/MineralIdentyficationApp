package com.apps.mineralidentyficationapp.config;

import android.content.Context;
import android.content.res.Resources;

import com.apps.mineralidentyficationapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MineralsIdentificationConfig {

    public static String getConfigProperties(Context context, String propertiesName) {
        Resources resources = context.getResources();
        InputStream rawResource = resources.openRawResource(R.raw.config);
        Properties properties = new Properties();
        try {
            properties.load(rawResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty(propertiesName);
    }

}
