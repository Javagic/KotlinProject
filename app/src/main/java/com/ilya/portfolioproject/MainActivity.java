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
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    public static final String WEB_SITE_URL="http://formalista.org/lenta/order/1/page/2";
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ProgressDialog mProgressDialog;
    List<Photo> photoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RemoteDataTask().execute();
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
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
            photoList = new ArrayList<Photo>();

            try {
                Document doc = Jsoup.connect(WEB_SITE_URL).get();
                Elements img = doc.getElementsByClass("imgPath");

                for (Element el : img) {
                    String src = el.select("a").attr("href");
                    photoList.add(new Photo(src));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
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
