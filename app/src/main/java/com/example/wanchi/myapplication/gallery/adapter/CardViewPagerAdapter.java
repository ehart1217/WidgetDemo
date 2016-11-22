package com.example.wanchi.myapplication.gallery.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wanchi.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuyan on 2016/11/16.
 */

public class CardViewPagerAdapter extends PagerAdapter implements CardAdapter{
    List<Integer> mList;
    private float mBaseElevation;
    private List<CardView> mViews;

    public CardViewPagerAdapter(List<Integer> list) {
        mList = list;
        mViews = new ArrayList<>();
        for(int i  =  0;i<mList.size();i++){
            mViews.add(null);
        }
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.gallery_lock_card, container, false);
        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        imageView.setImageResource(mList.get(position));

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }


    @Override
    public float getBaseElevation() {
        return 0;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }
}
