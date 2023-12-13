package com.apps.mineralidentyficationapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MineralInCollectionAdapter extends BaseAdapter {
    Context context;
    String[] mineralNames;
    int[] images;
    LayoutInflater layoutInflater;

    public MineralInCollectionAdapter(Context context, String[] mineralNames, int[] images){
        this.context = context;
        this.mineralNames = mineralNames;
        this.images = images;
    }
    @Override
    public int getCount() {
        return mineralNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i("adapter", "getView");
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null){
            view = layoutInflater.inflate(R.layout.mineral_item, null);
        }

        ImageView imageView = view.findViewById(R.id.mineral_image);
        TextView textView = view.findViewById(R.id.mineral_name);

        imageView.setImageResource(images[i]);
        textView.setText(mineralNames[i]);
        return view;
    }
}
