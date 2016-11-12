package com.ilya.portfolioproject;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcel;

/**
 * Created by Ilya on 11/5/2016.
 */
@Parcel
public class ArticlesItem {
    public String title;
    public String date;
    public String description;
    public String rubrics;
    public String imageUrl;
    public String idLink;//*/articles/xxxx
    public String mainText;
    public String mainImage;

    public ArticlesItem() {
    }

    public ArticlesItem(String title, String date, String description, String imageUrl, String rubrics, String id) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rubrics = rubrics;
        this.idLink = id;
    }

    @BindingAdapter({"bind:mainImage"})
    public static void loadImage(ImageView view, String mainImage) {
        Picasso.with(view.getContext())
                .load(mainImage)
                .placeholder(R.drawable.example)
                .fit()
                .into(view);
    }

    public String getIdLink() {
        return idLink;
    }

    public String getMainText() {
        return mainText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getRubrics() {
        return rubrics;
    }

    @BindingAdapter({"bind:mainText"})
    public static void loadImage(TextView view, String mainText) {
        if (mainText != null && !mainText.isEmpty()) {
            view.setText(mainText);
            return;
        }

    }


    public interface OnArticleItemCallback {
        public void onDataLoaded();
    }
}
