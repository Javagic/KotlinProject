package com.ilya.portfolioproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Ilya on 11/4/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ArticlesItem> articlesItemList;
    private ImageLoader imageLoader;

    public RecyclerAdapter(Context context, List<ArticlesItem> photos) {
        articlesItemList = photos;
        imageLoader = new ImageLoader(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_photos, parent, false);
        PhotoViewHolder vh = new PhotoViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        imageLoader.DisplayImage(articlesItemList.get(position).source, ((PhotoViewHolder) holder).photoImage);
    }

    @Override
    public int getItemCount() {
        return articlesItemList.size();
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImage;

        public PhotoViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            photoImage = (ImageView) itemLayoutView.findViewById(R.id.photo_image);
        }
    }

    public interface OnRecycleCallback {

    }
}