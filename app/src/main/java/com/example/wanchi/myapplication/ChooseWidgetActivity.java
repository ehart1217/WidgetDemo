package com.example.wanchi.myapplication;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by wanchi on 2016/11/17.
 */

public class ChooseWidgetActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int APPWIDGET_HOST_ID = 10;
    private AppWidgetHost mAppWidgetHost;
    private AppWidgetManager mAppWidgetManager;
    private ViewGroup mContainer;
    private View mScaleBtn;
    private View mMinusBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_widget_container);
        findView();
        initView();
        addListener();
        addAllWidget();
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

    private void addListener() {
        mScaleBtn.setOnClickListener(this);
        mMinusBtn.setOnClickListener(this);
    }

    private void initView() {
        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
    }

    private void findView() {
        mContainer = (ViewGroup) findViewById(R.id.activity_all_widget_container);
        mScaleBtn = findViewById(R.id.activity_all_widget_scale_btn);
        mMinusBtn = findViewById(R.id.activity_all_widget_minus_btn);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.activity_all_widget_scale_btn) {
            scaleContainer(1.2f);
        } else if (id == R.id.activity_all_widget_minus_btn) {
            scaleContainer(0.8f);
        }
    }

    private void scaleContainer(float scalePercent) {
        ViewGroup.LayoutParams lp = mContainer.getLayoutParams();
        lp.height *= scalePercent;
        lp.width *= scalePercent;
        mContainer.setLayoutParams(lp);
    }

    private void addAllWidget() {
        List<AppWidgetProviderInfo> appWidgetProviderInfoList = mAppWidgetManager.getInstalledProviders();
        int widgetCount = appWidgetProviderInfoList.size();
        for (int i = 0; i < widgetCount; i++) {
            AppWidgetProviderInfo currentWidgetInfo = appWidgetProviderInfoList.get(i);

            int widgetId = mAppWidgetHost.allocateAppWidgetId();

            AppWidgetHostView hostView =
                    mAppWidgetHost.createView(this, widgetId, currentWidgetInfo);
            hostView.setAppWidget(widgetId, currentWidgetInfo);

            //需要api-16
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Class clazz = mAppWidgetManager.getClass();
                try {
                    Method m1 = clazz.getDeclaredMethod("setBindAppWidgetPermission", String.class, Boolean.class);
                    try {
                        m1.invoke(mAppWidgetManager, this.getPackageName(), true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                boolean success = mAppWidgetManager.bindAppWidgetIdIfAllowed(widgetId, currentWidgetInfo.provider);
                Toast.makeText(this, "权限通过：" + success, Toast.LENGTH_SHORT).show();
            }

            mContainer.addView(createDivider());
            mContainer.addView(hostView);
        }
    }

    private View createDivider() {
        View view = new View(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        view.setLayoutParams(lp);
        view.setBackgroundColor(Color.BLACK);
        return view;
    }
}
