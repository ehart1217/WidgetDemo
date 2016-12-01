package com.example.wanchi.myapplication.base;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by renxu on 2016/12/1.
 */

public class InfoClient {

    Context mContext;
    List<WeakReference<IDataSwitch>> dataSwitchList;
    IDataSwitch mDataSwitch;

    public static InfoClient getClient() {
        return null;
    }

    public void register(IDataSwitch dataSwitch) {
//        dataSwitch.getType()
    }

    public void send(int type, Object object) {
    }

    public void request(int type) {
    }

    public void onGet(int type, String s) {
    }
    //baseFragment 里的方法 onGet(Object ob);abstract 的 具体类具体实现
}
