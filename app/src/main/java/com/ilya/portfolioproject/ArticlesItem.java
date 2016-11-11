package com.ilya.portfolioproject;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcel;

/**
 * Created by Ilya on 11/5/2016.
 */
@Parcel
public class ArticlesItem {
    String title;
    String date;
    String description;
    String rubrics;
    String imageUrl;

    public ArticlesItem(){}

    public ArticlesItem(String title, String date, String description, String imageUrl, String rubrics) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rubrics = rubrics;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.example)
                .fit()
                .into(view);
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

}
