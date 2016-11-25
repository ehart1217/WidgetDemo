package com.example.wanchi.myapplication;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by liyuyan on 2016/11/24.
 */

public class WidgetApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initBugly();
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "12b3e713b4", true);
    }
}
