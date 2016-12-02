package com.example.wanchi.myapplication;

import android.app.Application;

import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageItem;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by liyuyan on 2016/11/24.
 */

public class WidgetApplication extends Application {

    private static WidgetApplication mInstance;

    int mRectHeight = 0;

    private ImageItem mImageItem;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initBugly();
        initKlog();
    }


    public static WidgetApplication getInstance() {
        return mInstance;
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "12b3e713b4", true);
    }

    private void initKlog() {

    }


    public void setRectHeight(int rectHeight) {
        mRectHeight = rectHeight;
    }

    public int getRectHeight() {
        return mRectHeight;
    }


    public void setImageItem(ImageItem imageItem) {
        mImageItem = imageItem;
    }

    public ImageItem getImageItem() {
        return mImageItem;
    }
}
