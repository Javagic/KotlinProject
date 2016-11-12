package com.ilya.portfolioproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ilya on 11/4/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ArticlesItem> articlesItemList;
    Context context;//TODO будут ли утечки?
    public OnRecycleCallback onRecycleCallback;

    public RecyclerAdapter(Context context, List<ArticlesItem> photos, OnRecycleCallback onRecycleCallback) {
        articlesItemList = photos;
        this.context=context;
        this.onRecycleCallback=onRecycleCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_photos, parent, false);
        PhotoViewHolder vh = new PhotoViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Picasso.with(context).load(articlesItemList.get(position).imageUrl).resize(60,48).into(((PhotoViewHolder) holder).itemPhoto);
        ((PhotoViewHolder) holder).itemTitle.setText(articlesItemList.get(position).title);
        ((PhotoViewHolder) holder).itemDate.setText(articlesItemList.get(position).date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecycleCallback.onItemSelect(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesItemList.size();
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView itemPhoto;
        TextView itemTitle;
        TextView itemDate;
        public PhotoViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            itemPhoto = (ImageView) itemLayoutView.findViewById(R.id.photo_image);
            itemDate = (TextView) itemLayoutView.findViewById(R.id.item_date);
            itemTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
        }
    }

    public interface OnRecycleCallback {
        public void onItemSelect(int position);
    }
}