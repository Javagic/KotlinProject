package com.ilya.portfolioproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ilya on 11/2/2016.
 */
public class SplashScreen extends Activity {
    Thread splashTread;
    public static final long LIFE_TIME = 5500;

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
        setContentView(R.layout.activity_splashscreen);
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

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(LIFE_TIME);
                    Intent intent = new Intent(SplashScreen.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }
}
