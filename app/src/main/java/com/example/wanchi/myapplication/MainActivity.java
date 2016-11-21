package com.example.wanchi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wanchi.myapplication.gallery.GalleryMain;

/**
 * Created by liyuyan on 2016/11/21.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button gallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        }

    }
}
