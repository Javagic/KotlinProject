package com.ilya.portfolioproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ilya Reznik
 * reznikid@altarix.ru
 * skype be3bapuahta
 * on 30.11.16 19:24.
 */
public class DataLoader extends AsyncTask<String,String,String> {
    Retrofit mRetrofit;
    private StringBuffer result = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        articlesList = new ArrayList<ArticlesItem>();
        Document doc = null;
        try {
            doc = Jsoup.connect(Constants.ARTICLES_LIST).get();
            Elements articlesElements = doc.getElementsByClass("Main");

            for (Element el : articlesElements) {
                String title = el.getElementsByClass("Title").text();
                String id = el.getElementsByClass("Title").select("a").attr("href");
                String date = el.getElementsByClass("Date").text();
                String description = el.getElementsByClass("Desc").text();
                String src = el.select("img").attr("src");
                String rubrics = el.getElementsByClass("Rubrics").text();
                articlesList.add(new ArticlesItem(title,date,description,src,rubrics, id));
            }
            for(int i=0;i<6;i++) articlesList.remove(articlesList.size()-1);//потому что кто-то криво верстает
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            return new Retrofit.Builder()
                    .baseUrl(Constants.ARTICLES_LIST)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public VestiService getAuthenticationService() {
        return getRetrofit().create(VestiService.class);
    }

    @Override
    protected void onPostExecute(Void result) {
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
        mProgressDialog.dismiss();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerAdapter.imageLoader.clearCache();
//                }
//            },5000);
    }
}