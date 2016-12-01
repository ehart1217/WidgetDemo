package com.example.wanchi.myapplication.drag02;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanchi.myapplication.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import static com.tencent.bugly.crashreport.inner.InnerApi.context;

/**
 * Created by liyuyan on 2016/11/30.
 */

public class MyAdapter extends SwipeMenuAdapter<MyAdapter.MyViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    private List<Item> mResults;

    public MyAdapter(List<Item> results) {
        mResults = results;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_item_list, parent, false);
    }

    @Override
    public MyViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new MyViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageView.setImageResource(mResults.get(position).getImg());
        holder.textView.setText(mResults.get(position).getName());
        holder.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener mOnItemClickListener;

        public TextView textView;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = width/4;
            itemView.setLayoutParams(layoutParams);
            textView = (TextView) itemView.findViewById(R.id.item_text);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(String title) {
//            this.tvTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
