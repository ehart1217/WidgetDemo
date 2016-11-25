package com.example.wanchi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wanchi.myapplication.gallery.GalleryMain;
import com.example.wanchi.myapplication.widget.WidgetMainActivity;

/**
 * Created by liyuyan on 2016/11/21.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button gallery;
    private View mGridBtn;
    private View mWidgetBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGridBtn = findViewById(R.id.activity_navigate_to_grid);
        mGridBtn.setOnClickListener(this);

        mWidgetBtn = findViewById(R.id.activity_navigate_to_widgets);
        mWidgetBtn.setOnClickListener(this);

        gallery = (Button) findViewById(R.id.gallery);
        gallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gallery:
                Intent intent = new Intent(this, GalleryMain.class);
                startActivity(intent);
                break;
            case R.id.activity_navigate_to_grid:
                break;
            case R.id.activity_navigate_to_widgets:
                WidgetMainActivity.navigateTo(this);
                break;
        }
    }
}
