package com.example.wanchi.myapplication.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.base.BaseFragment;
import com.example.wanchi.myapplication.base.IDataSwitch;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by wanchi on 2016/11/22.
 */

public class GridFragment extends BaseFragment implements CellLayout.OnLongClickEmptyListener {

    private static final int REQUEST_PICK_APPWIDGET = 1;
    private static final int APPWIDGET_HOST_ID = 2;
    public static final int REQUEST_CREATE_APPWIDGET = 3;

    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;

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
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
        mCellLayout = (CellLayout) rootView.findViewById(R.id.fragment_cell_layout);

        mAppWidgetManager = AppWidgetManager.getInstance(getActivity());
        mAppWidgetHost = new AppWidgetHost(getActivity(), APPWIDGET_HOST_ID);

        mCellLayout.setOnLongClickEmptyListener(this);
        return rootView;
    }

    private View createChildView(String name) {
        TextView view = new TextView(getActivity());
        view.setText(name);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setPadding(30, 30, 30, 30);
        view.setBackgroundColor(getRandomColor());
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        view.setLayoutParams(lp);
        return view;
    }

    private int getRandomColor() {
        int random = (int) (Math.random() * 5);
        switch (random) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GRAY;
            default:
                return Color.BLACK;
        }
    }

    private void selectWidget() {
        //点击选择小部件，跳转选择页面。
        int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
        Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        addEmptyData(pickIntent);
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET);
    }

    private void addEmptyData(Intent pickIntent) {
        ArrayList<AppWidgetProviderInfo> customInfo =
                new ArrayList<>();
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
        ArrayList<Bundle> customExtras = new ArrayList<>();
        pickIntent.putParcelableArrayListExtra(
                AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data);
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                createWidget(data);
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            int appWidgetId =
                    data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId);
            }
        }
    }

    private void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo =
                mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure != null) {
            Intent intent =
                    new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
        } else {
            createWidget(data);
        }
    }

    private void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo =
                mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView =
                mAppWidgetHost.createView(getActivity(), appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        Log.d(TAG, "appWidgetInfo:" + appWidgetInfo);
        int frameWidth = appWidgetInfo.minWidth / 4;
        int height = appWidgetInfo.minHeight / frameWidth + 1;
        mCellLayout.addCell(hostView, new Point(0, 0), new Point(4, height));
    }

    @Override
    public void onLongClickEmpty(View view) {
        selectWidget();
    }

    @Override
    public int getType() {
        return IDataSwitch.TYPE_SCREEN_TODAY;
    }

    @Override
    public void onReceiveData(Object o) {
        //o
    }
}
