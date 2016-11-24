package com.example.wanchi.myapplication.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wanchi.myapplication.R;

/**
 * Created by wanchi on 2016/11/22.
 */

public class GridFragment extends Fragment {

    public static final String TAG = "GridFragment";

    public static GridFragment newInstance() {

        Bundle args = new Bundle();

        GridFragment fragment = new GridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);

        return rootView;
    }

}
