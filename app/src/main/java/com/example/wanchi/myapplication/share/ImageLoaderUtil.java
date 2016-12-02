package com.example.wanchi.myapplication.share;

import android.content.Context;

import com.example.wanchi.myapplication.gallery.photopicker.utils.ImageLoader;

/**
 * Created by liyuyan on 2016/12/2.
 */

/**
 *
 */
public class ImageLoaderUtil {

    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderStrategy mStrategy;

    /**
     * 代码中，通过setLoadImgStrategy方法注入不同的图片加载实现，可以是的
     * 本类更加简单，健壮，也是ImageLoader更加具有扩展性，灵活性高。
     */

    private ImageLoaderUtil(){
        mStrategy =new GlideImageLoaderStrategy();
    }

    //single instance
    public static ImageLoaderUtil getInstance(){
        if(mInstance ==null){
            synchronized (ImageLoaderUtil.class){
                if(mInstance == null){
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img){
        mStrategy.loadImage(context,img);
    }

    /**
     * 依赖注入，通过该函数实现图片加载策略，依赖接口，并不依赖具体实现
     * @param strategy
     */

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy){
        mStrategy =strategy;
    }
}
