package com.ilya.portfolioproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilya.portfolioproject.Utils.Constants;
import com.ilya.portfolioproject.databinding.ActivitySplashscreenBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Ilya on 11/2/2016.
 */
public class SplashScreen extends Activity {
    ActivitySplashscreenBinding binding;
    public static final long LIFE_TIME = 3500;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splashscreen);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.splash_background);
        l.clearAnimation();
        l.startAnimation(anim);

        final Animation rocketAnim = AnimationUtils.loadAnimation(this, R.anim.rocket_trans);
        rocketAnim.reset();
        final ImageView iv = (ImageView) findViewById(R.id.rocket_img);
        iv.setVisibility(View.INVISIBLE);
        iv.clearAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv.setVisibility(View.VISIBLE);
                iv.startAnimation(rocketAnim);
            }
        }, 1000);

        final Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_trans);
        anim.reset();
        final TextView title_tv = (TextView) findViewById(R.id.splash_title);
        title_tv.setVisibility(View.INVISIBLE);
        title_tv.clearAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title_tv.setVisibility(View.VISIBLE);
                title_tv.startAnimation(titleAnim);
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.VISIBLE);

                Single.create(new Single.OnSubscribe<ArrayList<ArticlesItem>>() {
                    @Override
                    public void call(SingleSubscriber<? super ArrayList<ArticlesItem>> singleSubscriber) {
                        singleSubscriber.onSuccess(articlesFromServer());
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ArrayList<ArticlesItem>>() {
                            @Override
                            public void call(ArrayList<ArticlesItem> articlesItems) {
                                startActivity(MainActivity.intent(SplashScreen.this, articlesItems));
                                SplashScreen.this.finish();
                            }
                        });
            }
        }, LIFE_TIME);


    }

    private ArrayList<ArticlesItem> articlesFromServer() {
        ArrayList<ArticlesItem> articlesList = new ArrayList<>();
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
                articlesList.add(new ArticlesItem(title, date, description, src, rubrics, id));
            }
            for (int i = 0; i < 6; i++)
                articlesList.remove(articlesList.size() - 1);//потому что кто-то криво верстает
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articlesList;
    }
}
