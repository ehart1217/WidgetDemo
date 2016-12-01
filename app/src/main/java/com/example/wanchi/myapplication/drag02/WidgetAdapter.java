package com.example.wanchi.myapplication.drag02;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wanchi.myapplication.R;

import java.util.List;

import static com.tencent.bugly.crashreport.inner.InnerApi.context;


/**
 * Created by liyuyan on 2016/12/1.
 */

public class WidgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<Item> mResults;

    private int mRectHeight;

    private final int ITEM_TYPE_6x1 = 1;

    private final int ITEM_TYPE_6x2 = 2;

    private final int ITEM_TYPE_6x3 = 3;


    public WidgetAdapter(List<Item> results, int rectHeight) {
        mResults = results;
        mRectHeight = rectHeight;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_item_list, parent, false);
        if (viewType == ITEM_TYPE_6x1) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return new WidgetAdapter.MyViewHolder6X1(view);
        } else if (viewType == ITEM_TYPE_6x2) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return new WidgetAdapter.MyViewHolder6X2(view);
        } else if (viewType == ITEM_TYPE_6x3) {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return new WidgetAdapter.MyViewHolder6X3(view);
        } else{
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return new WidgetAdapter.MyViewHolder6X1(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mResults.get(position).getId() == 1) {
            return ITEM_TYPE_6x1;
        } else if (mResults.get(position).getId() == 2) {
            return ITEM_TYPE_6x2;
        } else if (mResults.get(position).getId() == 3) {
            return ITEM_TYPE_6x3;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WidgetAdapter.MyViewHolder6X2) {
            ((WidgetAdapter.MyViewHolder6X2) holder).imageView.setImageResource(mResults.get(1).getImg());
        } else if (holder instanceof WidgetAdapter.MyViewHolder6X3)
            ((WidgetAdapter.MyViewHolder6X3) holder).imageView.setImageResource(mResults.get(0).getImg());
        else
            ((WidgetAdapter.MyViewHolder6X1) holder).imageView.setImageResource(mResults.get(2).getImg());
    }

    @Override
    public int getItemCount() {
        return mResults == null ? 0 : mResults.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);

        void onItemLongClick(View view);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(v);
        }

    }


    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    class MyViewHolder6X1 extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder6X1(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = mRectHeight / 6;
            itemView.setLayoutParams(layoutParams);
            // textView = (TextView) itemView.findViewById(R.id.item_text);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }

    }

    class MyViewHolder6X2 extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder6X2(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = mRectHeight / 6 * 2;
            itemView.setLayoutParams(layoutParams);
            //  textView = (TextView) itemView.findViewById(R.id.item_text);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }

    }

    class MyViewHolder6X3 extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder6X3(View itemView) {
            super(itemView);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.height = mRectHeight / 6 * 3;
            itemView.setLayoutParams(layoutParams);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
