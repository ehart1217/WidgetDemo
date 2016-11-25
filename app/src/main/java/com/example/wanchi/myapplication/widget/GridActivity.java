package com.example.wanchi.myapplication.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.wanchi.myapplication.R;

/**
 * Created by wanchi on 2016/11/22.
 */

public class GridActivity extends AppCompatActivity {

    public static void navigateTo(Activity activity) {
        startActivity(new Intent(this, activity.getClass()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        Fragment gridFragment = GridFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_grid_fragment_container, gridFragment).commit();
    }
}
