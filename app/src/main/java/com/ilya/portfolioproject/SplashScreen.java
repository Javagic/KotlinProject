package com.ilya.portfolioproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
        rocketAnim.setStartOffset(1000);
        final ImageView iv = (ImageView) findViewById(R.id.rocket_img);
        iv.clearAnimation();
        iv.startAnimation(rocketAnim);

        final Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_trans);
        anim.setStartOffset(1500);
        anim.reset();
        final TextView title_tv = (TextView) findViewById(R.id.splash_title);
        title_tv.clearAnimation();
        title_tv.startAnimation(titleAnim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.progressBar.setVisibility(View.VISIBLE);
                articlesFromServer1();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private ArrayList<ArticlesItem> articlesFromServer1() {
        ArrayList<ArticlesItem> articlesList = new ArrayList<>();

        Observable.just(Constants.ARTICLES_LIST)
                .subscribeOn(Schedulers.io())
                .flatMap((Func1<String, Observable<?>>) document -> {
                    Elements articlesElements = null;
                    try {
                        Document doc = Jsoup.connect(document).get();
                        articlesElements = doc.getElementsByClass("Main");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Observable.from(articlesElements);
                })
                .map(el -> {
                    String title = ((Element) el).getElementsByClass("Title").text();
                    String id = ((Element) el).getElementsByClass("Title").select("a").attr("href");
                    String date = ((Element) el).getElementsByClass("Date").text();
                    String description = ((Element) el).getElementsByClass("Desc").text();
                    String src = ((Element) el).select("img").attr("src");
                    String rubrics = ((Element) el).getElementsByClass("Rubrics").text();
                    return new ArticlesItem(title, date, description, src, rubrics, id);
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(articlesItems -> {
                    for (int i = 0; i < 6; i++)
                        articlesItems.remove(articlesItems.size() - 1);//потому что кто-то криво верстает
                    startActivity(MainActivity.intent(SplashScreen.this, articlesItems));
                    SplashScreen.this.finish();
                });

        return articlesList;
    }
}
