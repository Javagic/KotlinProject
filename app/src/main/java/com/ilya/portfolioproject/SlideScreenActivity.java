package com.ilya.portfolioproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.ilya.portfolioproject.Utils.ScreenSlidePagerAdapter;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Ilya on 11/6/2016.
 */
public class SlideScreenActivity extends FragmentActivity {

    List<ArticlesItem> articlesList;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

//    public static Intent intent(Context context, int deviceId) {
//        Intent intent = new Intent(context, SongListActivity.class);
//        intent.putExtra(EXTRA_ALARM_ID, deviceId);
//        return intent;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_screen);
        articlesList = Parcels.unwrap(getIntent().getParcelableExtra("articles"));
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),articlesList);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


}
