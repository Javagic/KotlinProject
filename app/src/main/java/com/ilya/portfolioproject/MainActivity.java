package com.ilya.portfolioproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ilya.portfolioproject.Utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    List<ArticlesItem> articlesList;

    public static Intent intent(Context context, List<ArticlesItem> articlesItems){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("articlesItems", Parcels.wrap(articlesItems));
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articlesList = Parcels.unwrap(getIntent().getParcelableExtra("articlesItems"));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerAdapter = new RecyclerAdapter(MainActivity.this, articlesList, new RecyclerAdapter.OnRecycleCallback() {
            @Override
            public void onItemSelect(int position) {
                Intent intent = new Intent(MainActivity.this, SlideScreenActivity.class);
                intent.putExtra("articles", Parcels.wrap(articlesList));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        Drawable divider = ContextCompat.getDrawable(MainActivity.this,R.drawable.item_divider_bottom);
        recyclerView.addItemDecoration(new ItemDecorator(divider));
        recyclerView.setAdapter(recyclerAdapter);
    }
}
