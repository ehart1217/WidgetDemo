package com.example.wanchi.myapplication.share;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by liyuyan on 2016/12/2.
 */

public class TestActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader mImageLoader = new ImageLoader.Builder().url("img url").placeHolder(mImgView).build();
        ImageLoaderUtil.getInstance().loadImage(this,mImageLoader);
    }
}
