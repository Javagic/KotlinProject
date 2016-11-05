package com.ilya.portfolioproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    public static final String WEB_SITE_URL = "http://www.vestifinance.ru/";
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ProgressDialog mProgressDialog;
    List<ArticlesItem> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... params) {
            photoList = new ArrayList<ArticlesItem>();
            Call<ResponseBody> requestBodyCall = getAuthenticationService().getArticles();
            requestBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                            result = new StringBuffer();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }
                            Document doc = Jsoup.parse(result.toString());
                            Elements img = doc.getElementsByClass("Main");

                            for (Element el : img) {
                                String title = el.getElementsByClass("Title").text();
                                String date = el.getElementsByClass("Date").text();
                                String description = el.getElementsByClass("Desc").text();
                                String src = el.select("img").attr("src");
                                String rubrics = el.getElementsByClass("Rubrics").text();
                                photoList.add(new ArticlesItem(title,date,description,src,rubrics));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            return null;
        }

        public Retrofit getRetrofit() {
            if (mRetrofit == null) {
                return new Retrofit.Builder()
                        .baseUrl(WEB_SITE_URL)
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
            recyclerAdapter = new RecyclerAdapter(MainActivity.this, photoList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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


}
