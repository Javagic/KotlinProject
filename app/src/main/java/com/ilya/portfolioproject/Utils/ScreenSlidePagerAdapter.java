package com.ilya.portfolioproject.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ilya.portfolioproject.ArticlesItem;
import com.ilya.portfolioproject.SlidePageFragment;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Ilya on 11/11/2016.
 */
public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    List<ArticlesItem> articlesItems;
    public ScreenSlidePagerAdapter(FragmentManager fm,List<ArticlesItem> articlesItems) {
        super(fm);
        this.articlesItems=articlesItems;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putParcelable("articlesItem", Parcels.wrap(articlesItems.get(position)));
        SlidePageFragment slidePageFragment = new SlidePageFragment();
        slidePageFragment.setArguments(args);
        return slidePageFragment;
    }

    @Override
    public int getCount() {
        return articlesItems.size();
    }
}