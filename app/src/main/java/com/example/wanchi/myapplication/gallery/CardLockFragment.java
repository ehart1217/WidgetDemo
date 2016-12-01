package com.example.wanchi.myapplication.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.gallery.adapter.CardAdapter;
import com.example.wanchi.myapplication.gallery.photopicker.ImageGridActivity;


public class CardLockFragment extends BaseCardFragment implements View.OnClickListener {

    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImageView imageView;
    private Button set_lockpage_btn;
    private TextView set_lockpage_tv;
    private CardView mCardView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_lock_card, container, false);
        initView(view);
        setListener();
        Glide.with(this).load(R.mipmap.pager_lock).into(imageView);
        return view;
    }


    private void initView(View view) {
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        set_lockpage_btn = (Button) view.findViewById(R.id.set_lockpage_btn);
        set_lockpage_tv = (TextView) view.findViewById(R.id.set_lockpage_tv);
    }

    private void setListener() {
        set_lockpage_btn.setOnClickListener(this);
        set_lockpage_tv.setOnClickListener(this);
    }

    public CardView getCardView() {
        return mCardView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_lockpage_btn:
                //toSystemSelectLockPic();
                toYotaSelectLockPic();
                break;
        }
    }

    /**
     * 自定義圖庫
     */
    private void toYotaSelectLockPic() {
        //打开预览
        Intent intentPreview = new Intent(getActivity(), ImageGridActivity.class);
        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
    }

    /**
     * 系統圖庫
     */
    private void toSystemSelectLockPic() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            Log.e("uri", uri.toString());
//            ContentResolver cr = getActivity().getContentResolver();
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                /* 将Bitmap设定到ImageView */
//                imageView.setImageBitmap(bitmap);
//                Glide.with(this).load(bitmap).into(imageView);
//            } catch (FileNotFoundException e) {
//                Log.e("Exception", e.getMessage(),e);
//            }
//            Glide.with(this).load(uri).into(imageView);
//        }

        //v1 直接显示
//        if (requestCode == REQUEST_CODE_PREVIEW && resultCode == 10001) {1其
//            ImageItem imageItem = (ImageItem) data.getSerializableExtra("imageItem");
//            String path = imageItem.path;
//            String type = imageItem.mimeType;
//            if (type.equals("type_res"))
//                Glide.with(this).load(Integer.parseInt(imageItem.path)).into(imageView);
//            else
//                Glide.with(this).load(path).into(imageView);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
