package com.example.wanchi.myapplication.drag02;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.WidgetApplication;

/**
 * Created by liyuyan on 2016/11/30.
 */

public class DragMain extends AppCompatActivity implements View.OnClickListener {
    int rectHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_main);

        findViewById(R.id.list).setOnClickListener(this);
        findViewById(R.id.grid).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list:
                Intent intentList = new Intent(this, DragListActivity.class);
                startActivity(intentList);
                break;
            case R.id.grid:
                Intent intentGrid = new Intent(this, DragGridActivity.class);
                startActivity(intentGrid);
                break;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //View布局区域宽高 （除去状态栏以及ToolBAR）
        Rect rectView = new Rect();
        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rectView);
        rectHeight = rectView.height();
        WidgetApplication.getInstance().setRectHeight(rectHeight);
    }
}
