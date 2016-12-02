package com.example.wanchi.myapplication.share;

import android.content.Context;

import com.example.wanchi.myapplication.gallery.photopicker.utils.ImageLoader;

/**
 * Created by liyuyan on 2016/12/2.
 */

public interface BaseImageLoaderStrategy {
    void loadImage(Context ctx, ImageLoader img);
}
