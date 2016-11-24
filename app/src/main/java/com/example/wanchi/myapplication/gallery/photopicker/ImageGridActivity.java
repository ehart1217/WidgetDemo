package com.example.wanchi.myapplication.gallery.photopicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.gallery.photopicker.adapter.ImageGridAdapter;
import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageFolder;
import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageItem;
import com.example.wanchi.myapplication.gallery.photopicker.utils.GlideImageLoader;
import com.example.wanchi.myapplication.gallery.photopicker.utils.ImageDataSource;
import com.example.wanchi.myapplication.gallery.photopicker.utils.ImagePicker;
import com.example.wanchi.myapplication.gallery.photopicker.utils.LockScreenImageProvider;

import java.util.List;


/**
 * Created by liyuyan on 2016/11/22.
 */

public class ImageGridActivity extends AppCompatActivity implements ImageDataSource.OnImagesLoadedListener, ImagePicker.OnImageSelectedListener, View.OnClickListener, ImageGridAdapter.OnImageItemClickListener {
    private RecyclerView image_grid_rv;
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private List<ImageFolder> mImageFolders;   //所有的图片文件夹
    private ImagePicker imagePicker;
    private ImageGridAdapter mImageGridAdapter;
    private TextView tv_des;
    private ImageView btn_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.clear();
        imagePicker.addOnImageSelectedListener(this);


        image_grid_rv = (RecyclerView) findViewById(R.id.image_grid_rv);
        mImageGridAdapter = new ImageGridAdapter(this, null);
        tv_des = (TextView) findViewById(R.id.tv_des);
        btn_back = (ImageView) findViewById(R.id.btn_back);

        //6.0检查权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new ImageDataSource(this, null, this);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
            }
        }

        tv_des.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }


    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        imagePicker.setImageFolders(imageFolders);
        if (imageFolders.size() == 0) mImageGridAdapter.refreshData(null);
        else mImageGridAdapter.refreshData(imageFolders.get(0).images);
        image_grid_rv.setAdapter(mImageGridAdapter);
        mImageGridAdapter.setOnImageItemClickListener(this);
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImageDataSource(this, null, this);
            } else {
                Toast.makeText(this, "请打开权限，无法选择本地图片", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        Log.i("lyy08", position + "");
        Intent intent = new Intent();
        intent.putExtra("imageItem",imageItem);
        this.setResult(10001, intent);
        finish();
    }

    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
//        if (imagePicker.getSelectImageCount() > 0) {
//            mBtnOk.setText(getString(R.string.select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
//            mBtnOk.setEnabled(true);
//            mBtnPre.setEnabled(true);
//        } else {
//            mBtnOk.setText(getString(R.string.complete));
//            mBtnOk.setEnabled(false);
//            mBtnPre.setEnabled(false);
//        }
//        mBtnPre.setText(getResources().getString(R.string.preview_count, imagePicker.getSelectImageCount()));
        Log.i("lyy08", position + "");
        mImageGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_des:
                if (tv_des.getText().toString().contains("全部")) {
                    tv_des.setText("锁屏");
                    mImageGridAdapter.refreshData(LockScreenImageProvider.getLockImages(this));
                    break;
                }
                if (tv_des.getText().toString().contains("锁屏")) {
                    tv_des.setText("全部");
                    mImageGridAdapter.refreshData(mImageFolders.get(0).images);
                    break;
                }
            case R.id.btn_back:
                finish();
                break;
        }
    }

}
