package com.ilya.portfolioproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilya.portfolioproject.Utils.Constants;
import com.ilya.portfolioproject.databinding.FSlidePageBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ilya on 11/6/2016.
 */
public class SlidePageFragment extends Fragment implements ArticlesItem.OnArticleItemCallback{
     ArticlesItem articlesItem;
    private FSlidePageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate (inflater, R.layout.f_slide_page, container,false);
        binding.setArticle(articlesItem);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        articlesItem = Parcels.unwrap(args.getParcelable("articlesItem"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new RemoteDataTask().execute();
    }

    @Override
    public void onDataLoaded() {

    }
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            mProgressDialog.setTitle("Parse.com Custom ListView Tutorial");
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(Constants.WEB_SITE_URL+articlesItem.idLink).get();//TODO делать загрузки в разных потоках?
                List<TextNode> list = doc.getElementById("Article").textNodes();
                articlesItem.mainText = getMainText(list);
                Elements images = doc.getElementById("Article").getElementsByClass("Incut");
                if(!images.isEmpty()){
                    articlesItem.mainImage = images.get(0).select("img").attr("src");//TODO разобраться с остальными картинками
                }
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
        private String getMainText(List<TextNode> list){
            StringBuilder builder = new StringBuilder();
            for(TextNode textNode : list) {
                if(!textNode.toString().isEmpty()){
                    builder.append(textNode.toString()).append("\n");
                }

            }
            return builder.toString();
        }
        @Override
        protected void onPostExecute(Void result) {
         binding.invalidateAll();
        }
    }
}
