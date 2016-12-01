package com.example.wanchi.myapplication.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wanchi on 2016/12/1.
 */

public abstract class BaseFragment extends Fragment implements IDataSwitch {

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        InfoClient.getClient().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
