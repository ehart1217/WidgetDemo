package com.example.wanchi.myapplication.gallery.photopicker.utils;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

public interface ImageLoader extends Serializable {

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

    void displayImageFromRes(Activity activity, int res, ImageView imageView, int width, int height);

    void clearMemoryCache();
}
