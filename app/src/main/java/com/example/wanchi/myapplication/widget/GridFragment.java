package com.example.wanchi.myapplication.widget;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wanchi.myapplication.R;

/**
 * Created by wanchi on 2016/11/22.
 */

public class GridFragment extends Fragment {

    public static final String TAG = "GridFragment";
    private CellLayout mCellLayout;

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
        mCellLayout = (CellLayout) rootView.findViewById(R.id.fragment_cell_layout);
        mCellLayout.addCell(createChildView(), new Point(1, 2));
        mCellLayout.addCell(createChildView(), new Point(3, 4));
        return rootView;
    }

    private View createChildView() {
        TextView view = new TextView(getActivity());
        view.setText("haha");
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setPadding(30, 30, 30, 30);
        view.setBackgroundColor(Color.BLACK);
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(lp);
        return view;
    }

}
