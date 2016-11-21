package com.example.wanchi.myapplication.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.wanchi.myapplication.R;

/**
 * Created by liyuyan on 2016/11/21.
 */

public class GalleryMain extends AppCompatActivity implements View.OnClickListener {
    private Button showHubGallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_main);
        showHubGallery = (Button) findViewById(R.id.showHubGallery);

        showHubGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showHubGallery:
                Intent intent = new Intent(this, GalleyWidgetsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
