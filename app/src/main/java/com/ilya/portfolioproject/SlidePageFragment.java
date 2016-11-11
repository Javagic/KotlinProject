package com.ilya.portfolioproject;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilya.portfolioproject.databinding.FSlidePageBinding;

import org.parceler.Parcels;

/**
 * Created by Ilya on 11/6/2016.
 */
public class SlidePageFragment extends Fragment {
    ArticlesItem articlesItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FSlidePageBinding binding = DataBindingUtil.inflate (inflater, R.layout.f_slide_page, container,false);
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
    }
}
