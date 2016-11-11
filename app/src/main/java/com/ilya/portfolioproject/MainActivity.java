package com.ilya.portfolioproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    public static final String WEB_SITE_URL = "http://www.vestifinance.ru/articles";
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ProgressDialog mProgressDialog;
    List<ArticlesItem> articlesList;

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
            articlesList = new ArrayList<ArticlesItem>();
            Document doc = null;
            try {
                doc = Jsoup.connect(WEB_SITE_URL).get();
                Elements img = doc.getElementsByClass("Main");

                for (Element el : img) {
                    String title = el.getElementsByClass("Title").text();
                    String date = el.getElementsByClass("Date").text();
                    String description = el.getElementsByClass("Desc").text();
                    String src = el.select("img").attr("src");
                    String rubrics = el.getElementsByClass("Rubrics").text();
                    articlesList.add(new ArticlesItem(title,date,description,src,rubrics));
                }
                for(int i=0;i<6;i++) articlesList.remove(articlesList.size()-1);//потому что кто-то криво верстает
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Call<ResponseBody> requestBodyCall = getAuthenticationService().getArticles();
//            try {
//                requestBodyCall.execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            requestBodyCall.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful()) {
//                        try {
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
//                            result = new StringBuffer();
//                            String line;
//                            while ((line = reader.readLine()) != null) {
//                                result.append(line);
//                            }

//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
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


}
