package com.apps.mineralidentyficationapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.apps.mineralidentyficationapp.R;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<Bitmap> imageList;

    public ImagePagerAdapter(Context context, List<Bitmap> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(imageList.get(position));

        container.addView(itemView);

        return itemView;
    }
    public void updateData(List<Bitmap> newData) {
        this.imageList = newData;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}