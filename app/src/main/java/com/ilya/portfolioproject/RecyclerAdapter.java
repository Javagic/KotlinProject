package com.ilya.portfolioproject;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.portfolioproject.databinding.IPhotosBinding;

import java.util.List;

/**
 * Created by Ilya on 11/4/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ArticlesViewHolder> {
    private List<ArticlesItem> articlesItemList;
    Context context;//TODO будут ли утечки?
    public OnRecycleCallback onRecycleCallback;

    public RecyclerAdapter(Context context, List<ArticlesItem> photos, OnRecycleCallback onRecycleCallback) {
        articlesItemList = photos;
        this.context=context;
        this.onRecycleCallback=onRecycleCallback;
    }


    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IPhotosBinding binding = IPhotosBinding.inflate(inflater, parent, false);
        return new ArticlesViewHolder(binding.getRoot());
    }



    @Override
    public void onBindViewHolder(final ArticlesViewHolder holder, int position) {
        ArticlesItem articlesItem = articlesItemList.get(position);
        holder.binding.setArticle(articlesItem);
        holder.binding.setClick(new OnArticleItemHandler() {
            @Override
            public void onItemClick(View view) {
                onRecycleCallback.onItemSelect(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesItemList.size();
    }

    public  class ArticlesViewHolder extends RecyclerView.ViewHolder {
        IPhotosBinding binding;
        public ArticlesViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            binding = DataBindingUtil.bind(itemLayoutView);
        }
    }

    public interface OnRecycleCallback {
        public void onItemSelect(int position);
    }
}