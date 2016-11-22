package com.example.wanchi.myapplication.gallery.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wanchi.myapplication.R;


public class CardSohuFragment extends BaseCardFragment {

    private CardView mCardView;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_souhu_card, container, false);
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(this).load(R.mipmap.pager_sohu).into(imageView);
        return view;
    }

    public CardView getCardView() {
        return mCardView;
    }
}
