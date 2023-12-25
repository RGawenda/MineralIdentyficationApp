package com.apps.mineralidentyficationapp.adapters;

import static com.apps.mineralidentyficationapp.utils.FileUtils.convertBase64ToBitmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.mineralidentyficationapp.R;
import com.apps.mineralidentyficationapp.collection.MineralMessage;
import com.apps.mineralidentyficationapp.rest.MineralAppApiClient;
import com.apps.mineralidentyficationapp.rest.RxCallback;

import java.util.ArrayList;
import java.util.List;


public class MineralInCollectionAdapter extends BaseAdapter {
    Context context;
    private List<MineralMessage> itemList;
    LayoutInflater layoutInflater;
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    private String username = "";

    public MineralInCollectionAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<>();
        loadMoreData();
    }

    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view == null) {
            view = layoutInflater.inflate(R.layout.mineral_item, null);
        }

        ImageView imageView = view.findViewById(R.id.mineral_image);
        TextView textView = view.findViewById(R.id.mineral_name);

        if (itemList != null && itemList.size() > i && itemList.get(i) != null) {
            List<String> images = itemList.get(i).getImages();
            if (images != null && !images.isEmpty()) {
                imageView.setImageBitmap(convertBase64ToBitmap(images.get(0)));
            }
            textView.setText(itemList.get(i).getName());
        }

        return view;
    }

    private void loadMoreData() {
        if (!isLoading && hasMoreData) {
            isLoading = true;

            MineralAppApiClient myApiClient = new MineralAppApiClient(context);

            myApiClient.getCollection(new RxCallback<>() {
                @Override
                public void onSuccess(List<MineralMessage> mineralsResult) {
                    Log.i("loadMoreData", "success");
                    if (mineralsResult != null && !mineralsResult.isEmpty()) {
                        Log.i("loadMoreData", "set");
                        itemList = mineralsResult;
                        notifyDataSetChanged();
                    } else {
                        hasMoreData = false;
                    }
                    isLoading = false;
                }

                @Override
                public void onError(String errorMessage) {
                    Log.i("loadMoreData", "error: " + errorMessage);
                    isLoading = false;
                }
            }, currentPage, pageSize, username);
            currentPage++;
        }
    }

    public void handleScrolling(int visibleItemCount, int totalItemCount, int firstVisibleItem) {
        int lastVisibleItem = firstVisibleItem + visibleItemCount;
        if (lastVisibleItem >= totalItemCount - 5) {
            loadMoreData();
        }
    }
}
