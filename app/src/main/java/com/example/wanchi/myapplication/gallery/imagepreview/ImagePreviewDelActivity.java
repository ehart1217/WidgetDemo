package com.example.wanchi.myapplication.gallery.imagepreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.WidgetApplication;
import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageItem;
import com.example.wanchi.myapplication.gallery.photopicker.utils.ImagePicker;


public class ImagePreviewDelActivity extends ImagePreviewBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ImageView mBtnDel = (ImageView) findViewById(R.id.btn_del);
//        mBtnDel.setOnClickListener(this);
        // mBtnDel.setVisibility(View.VISIBLE);
        //topBar.findViewById(R.id.btn_back).setOnClickListener(this);

//        mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
//        //滑动ViewPager的时候，根据外界的数据改变当前的选中状态和当前的图片的位置描述文本
//        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                mCurrentPosition = position;
//                mTitleCount.setText(getString(R.string.preview_image_count, mCurrentPosition + 1, mImageItems.size()));
//            }
//        });
        cb_check.setOnClickListener(this);
    }

    @Override
    public void onImageSingleTap() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_del) {
            //showDeleteDialog();
        } else if (id == R.id.btn_back) {
            onBackPressed();
        } else if (id == R.id.cb_check) {
            ImageItem imageItem = mImageItems.get(mCurrentPosition);
            if (null != imageItem) {
                WidgetApplication.getInstance().setImageItem(imageItem);
                finish();
            } else {
                Toast.makeText(this, "图片选择出错，请选择其他图片", Toast.LENGTH_LONG);
                return;
            }

        }
    }

//

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        //带回最新数据
        intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, mImageItems);
        setResult(ImagePicker.RESULT_CODE_BACK, intent);
        finish();
        super.onBackPressed();
    }

//    /** 单击时，隐藏头和尾 */
//    @Override
//    public void onImageSingleTap() {
//        if (topBar.getVisibility() == View.VISIBLE) {
//            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_out));
//            topBar.setVisibility(View.GONE);
//            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
//            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        } else {
//            topBar.setAnimation(AnimationUtils.loadAnimation(this, R.anim.top_in));
//            topBar.setVisibility(View.VISIBLE);
//            tintManager.setStatusBarTintResource(R.color.status_bar);//通知栏所需颜色
//            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
//            if (Build.VERSION.SDK_INT >= 16) content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }
//    }
}