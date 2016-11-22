package com.example.wanchi.myapplication.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.gallery.adapter.CardFragmentPagerAdapter;
import com.example.wanchi.myapplication.gallery.adapter.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuyan on 2016/11/21.
 */

public class GalleyWidgetsActivity extends AppCompatActivity{

    private ViewPager vp_galley_widgets;

    private CardFragmentPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    private List<Integer> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galler_widgets);

        initView();
        initData();
        setHubViewPager();
    }



    private void initView() {
        vp_galley_widgets = (ViewPager) findViewById(R.id.vp_galley_widgets);

    }

    private void initData() {
        //test
        mList.add(R.mipmap.pic4);
        mList.add(R.mipmap.pic5);
        mList.add(R.mipmap.pic6);
        //getHubGalleryList();
    }

    private void setHubViewPager() {
        mCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),dpToPixels(2, this));
        mCardShadowTransformer = new ShadowTransformer(vp_galley_widgets, mCardAdapter);
        mCardShadowTransformer.enableScaling(true);
        vp_galley_widgets.setAdapter(mCardAdapter);
        vp_galley_widgets.setPageTransformer(false, mCardShadowTransformer);
        vp_galley_widgets.setOffscreenPageLimit(3);
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }
}
