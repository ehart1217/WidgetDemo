package com.example.wanchi.myapplication.base;

/**
 * 用来数据交换的接口
 * Created by wanchi on 2016/12/1.
 */

public interface IDataSwitch {

    int TYPE_SCREEN_LOCKER = 0; // 锁屏
    int TYPE_SCREEN_NEWS = 1; // 新闻
    int TYPE_SCREEN_TODAY = 2; // today

    /**
     * 得到数据
     *
     * @param o 数据
     */
    void onReceiveData(Object o);

    /**
     * 得到数据类型
     *
     * @return 数据类型
     */
    int getType();
}
