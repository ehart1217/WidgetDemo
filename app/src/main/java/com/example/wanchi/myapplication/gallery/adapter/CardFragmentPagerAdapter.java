package com.example.wanchi.myapplication.gallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.example.wanchi.myapplication.gallery.BaseCardFragment;
import com.example.wanchi.myapplication.gallery.CardLockFragment;
import com.example.wanchi.myapplication.gallery.CardSohuFragment;
import com.example.wanchi.myapplication.gallery.CardWidgetFragment;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<BaseCardFragment> mFragments;
    private float mBaseElevation;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
        addCardFragment(new CardLockFragment());
        addCardFragment(new CardSohuFragment());
        addCardFragment(new CardWidgetFragment());
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (BaseCardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(BaseCardFragment fragment) {
        mFragments.add(fragment);
    }

}
