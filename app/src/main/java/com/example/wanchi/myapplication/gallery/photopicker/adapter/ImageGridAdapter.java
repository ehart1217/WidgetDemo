package com.example.wanchi.myapplication.gallery.photopicker.adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.gallery.photopicker.ImageGridActivity;
import com.example.wanchi.myapplication.gallery.photopicker.bean.ImageItem;
import com.example.wanchi.myapplication.gallery.photopicker.utils.ImagePicker;
import com.example.wanchi.myapplication.gallery.photopicker.utils.Utils;

import java.util.ArrayList;

/**
 * Created by liyuyan on 2016/11/22.
 */

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.MyViewHolder> {
    private Activity mActivity;
    private ArrayList<ImageItem> mImages;       //当前需要显示的所有的图片数据
    private int mImageSize;               //每个条目的大小
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> mSelectedImages; //全局保存的已经选中的图片数据
    private OnImageItemClickListener listener;   //图片被点击的监听

    public ImageGridAdapter(ImageGridActivity imageGridActivity, ArrayList<ImageItem> images) {
        mActivity = imageGridActivity;
        if (images == null || images.size() == 0) mImages = new ArrayList<>();
        else this.mImages = images;

        mImageSize = Utils.getImageItemWidth(mActivity);
        imagePicker = ImagePicker.getInstance();
        mSelectedImages = imagePicker.getSelectedImages();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.adapter_image_list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ImageItem imageItem = mImages.get(position);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) holder.iv_thumb.getLayoutParams();
        layoutParams.topMargin = 10;
        layoutParams.bottomMargin = 10;
        if (position % 2 == 0) {
            layoutParams.leftMargin = 30;
            layoutParams.rightMargin = 12;

        } else {
            layoutParams.leftMargin = 12;
            layoutParams.rightMargin = 30;
        }
        holder.iv_thumb.setLayoutParams(layoutParams);
        //显示图片
        if (mImages.get(position).mimeType.equals("type_res"))
            imagePicker.getImageLoader().displayImageFromRes(mActivity, Integer.parseInt(imageItem.path), holder.iv_thumb, 0, 0);
        else
            imagePicker.getImageLoader().displayImage(mActivity, imageItem.path, holder.iv_thumb, 0, 0);
        // 如果设置了回调，则设置点击事件
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    listener.onImageItemClick(v, imageItem, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void refreshData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) mImages = new ArrayList<>();
        else mImages = images;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_thumb;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);
    }
}
