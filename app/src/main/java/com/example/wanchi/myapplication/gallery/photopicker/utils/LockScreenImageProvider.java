package com.example.wanchi.myapplication.gallery.photopicker.utils;

import android.content.Context;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageItem;

import java.util.ArrayList;

/**
 * Created by liyuyan on 2016/11/23.
 */

public class LockScreenImageProvider {
    private static final String ResType = "type_res";
    private static int[] lockPic = {R.mipmap.lock1, R.mipmap.lock2, R.mipmap.pager_lock};
    private static ArrayList<ImageItem> mimages;

    public static ArrayList<ImageItem> getLockImages(Context context) {
        mimages = new ArrayList<>();
        for (int i = 0; i < lockPic.length; i++) {
            ImageItem imageItem = new ImageItem();
//            public String name;       //图片的名字
//            public String path;       //图片的路径
//            public long size;         //图片的大小
//            public int width;         //图片的宽度
//            public int height;        //图片的高度
//            public String mimeType;   //图片的类型
//            public long addTime;      //图片的创建时间
            imageItem.name = "lock";
            imageItem.path = lockPic[i] + "";
            imageItem.size = 0;
            imageItem.width = 0;
            imageItem.height = 0;
            imageItem.mimeType = ResType;
            imageItem.addTime = 0;
            mimages.add(imageItem);
        }
        return mimages;
    }

}
