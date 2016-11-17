package com.example.wanchi.myapplication;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PICK_APPWIDGET = 1;
    private static final int APPWIDGET_HOST_ID = 2;
    public static final int REQUEST_CREATE_APPWIDGET = 3;

    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private ViewGroup mContainer;//widget容器
    private View mChooseBtn;
    private View mClearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findWidget();
        initWidget();
        addListener();
    }

    private void findWidget() {
        mContainer = (ViewGroup) findViewById(R.id.activity_widget_container);
        mChooseBtn = findViewById(R.id.activity_choose_widget_btn);
        mClearBtn = findViewById(R.id.activity_clear_widget_btn);
    }

    private void initWidget() {
        mContainer = (ViewGroup) findViewById(R.id.activity_widget_container);
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
    }

    private void addListener() {
        mChooseBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_choose_widget_btn) {
            selectWidget();
        } else if (id == R.id.activity_clear_widget_btn) {
            clearWidget();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppWidgetHost.startListening();//set to listen the change of widget
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAppWidgetHost.stopListening();
    }


    private void selectWidget() {
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
    protected void onActivityResult(int requestCode, int resultCode,
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

    private void clearWidget() {
        int viewCount = mContainer.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View widgetView = mContainer.getChildAt(i);
            if (widgetView instanceof AppWidgetHostView) {
                mAppWidgetHost.deleteAppWidgetId(((AppWidgetHostView) widgetView).getAppWidgetId());
            }
        }
        mContainer.removeAllViews();
    }

    private void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo =
                mAppWidgetManager.getAppWidgetInfo(appWidgetId);
        AppWidgetHostView hostView =
                mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);
        mContainer.addView(hostView);
    }
}
