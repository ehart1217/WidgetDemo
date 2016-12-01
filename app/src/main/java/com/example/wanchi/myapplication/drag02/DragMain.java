package com.example.wanchi.myapplication.drag02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.wanchi.myapplication.R;

/**
 * Created by liyuyan on 2016/11/30.
 */

public class DragMain extends AppCompatActivity implements View.OnClickListener {
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
}
