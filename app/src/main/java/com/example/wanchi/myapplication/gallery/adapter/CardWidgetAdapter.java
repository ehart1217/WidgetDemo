package com.example.wanchi.myapplication.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.wanchi.myapplication.R;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuyan on 2016/11/25.
 */

public class CardWidgetAdapter extends RecyclerView.Adapter<CardWidgetAdapter.WidgetViewHolder> implements DraggableItemAdapter<CardWidgetAdapter.WidgetViewHolder>, SwipeableItemAdapter <CardWidgetAdapter.WidgetViewHolder>{

    private Context mContext;
    private List<Integer> arrayList = new ArrayList<>();

    public CardWidgetAdapter(Context context) {
        mContext = context;
        setHasStableIds(true);
        for (int i = 0; i < 6; i++) {
            arrayList.add(i, i);
        }
    }

    @Override
    public WidgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_widget_item, parent, false);
        WidgetViewHolder viewHolder = new WidgetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WidgetViewHolder holder, int position) {
        int tempPos = arrayList.get(position);
        if (tempPos % 2 == 0) {
            Glide.with(mContext).load(R.mipmap.gupiao).into(holder.item_card_widget);
        } else {
            Glide.with(mContext).load(R.mipmap.sprot).into(holder.item_card_widget);
        }


    }

    @Override
    public long getItemId(int position) {
        return arrayList.get(position); // need to return stable (= not change even after reordered) value
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public boolean onCheckCanStartDrag(WidgetViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(WidgetViewHolder holder, int position) {
        return null;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        int mPosition = arrayList.remove(fromPosition);
        arrayList.add(toPosition, mPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public int onGetSwipeReactionType(WidgetViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(WidgetViewHolder holder, int position, int type) {

    }

    @Override
    public SwipeResultAction onSwipeItem(WidgetViewHolder holder, int position, int result) {
        if (result == Swipeable.RESULT_CANCELED) {
            return new SwipeResultActionDefault();
        } else {
            return new MySwipeResultActionRemoveItem(this, position);
        }
    }

    public class WidgetViewHolder extends AbstractDraggableItemViewHolder {

        private ImageButton item_card_widget;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            item_card_widget = (ImageButton) itemView.findViewById(R.id.item_card_widget);
        }
    }
    interface Swipeable extends SwipeableItemConstants {
    }

    static class MySwipeResultActionRemoveItem extends SwipeResultActionRemoveItem {
        private CardWidgetAdapter adapter;
        private int position;

        public MySwipeResultActionRemoveItem(CardWidgetAdapter adapter, int position) {
            this.adapter = adapter;
            this.position = position;
        }

        @Override
        protected void onPerformAction() {
            adapter.arrayList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}
