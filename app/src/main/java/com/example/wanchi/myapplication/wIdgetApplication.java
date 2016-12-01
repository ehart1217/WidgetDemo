package com.example.wanchi.myapplication;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by liyuyan on 2016/11/24.
 */

public class WidgetApplication extends Application {

    private static WidgetApplication mInstance;

    int mRectHeight = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initBugly();
        initKlog();
    }


    public static WidgetApplication getInstance()
    {
        return mInstance;
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "12b3e713b4", true);
    }

    private void initKlog() {

    }


    public  void setRectHeight(int rectHeight) {
        mRectHeight = rectHeight;
    }

    public  int getRectHeight() {
        return mRectHeight;
    }
}
