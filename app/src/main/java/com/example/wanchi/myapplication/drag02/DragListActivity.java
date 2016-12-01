package com.example.wanchi.myapplication.drag02;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.wanchi.myapplication.R;
import com.example.wanchi.myapplication.WidgetApplication;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liyuyan on 2016/11/30.
 */

public class DragListActivity extends AppCompatActivity {
    private Activity mContext;

    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;

    //private List<String> mStrings;
    private List<Item> results = new ArrayList<Item>();

    // private MenuAdapter mMenuAdapter;
    private WidgetAdapter mMyAdapter;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_list);
        mContext = this;
        initDatas();
//
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        mSwipeMenuRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));// 布局管理器。
        mSwipeMenuRecyclerView.setHasFixedSize(false);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mSwipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        //mSwipeMenuRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        // 这个就不用添加菜单啦，因为滑动删除和菜单是冲突的。

//        mSwipeMenuRecyclerView.setLongPressDragEnabled(true);// 开启长安拖拽。
//        mSwipeMenuRecyclerView.setItemViewSwipeEnabled(true);// 开启滑动删除。
//        mSwipeMenuRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
//        mSwipeMenuRecyclerView.setOnItemMovementListener(onItemMovementListener);


        itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags=0;
                if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){
                    dragFlags=ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                }
                return makeMovementFlags(dragFlags,0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int from=viewHolder.getAdapterPosition();
                int to=target.getAdapterPosition();
                Collections.swap(results,from,to);
                mMyAdapter.notifyItemMoved(from,to);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }
        });

        mMyAdapter = new WidgetAdapter(results, WidgetApplication.getInstance().getRectHeight());
        mMyAdapter.setOnItemClickListener(new WidgetAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position=mSwipeMenuRecyclerView.getChildAdapterPosition(view);
            }

            @Override
            public void onItemLongClick(View view) {
                itemTouchHelper.startDrag(mSwipeMenuRecyclerView.getChildViewHolder(view));
            }
        });
        itemTouchHelper.attachToRecyclerView(mSwipeMenuRecyclerView);
        mSwipeMenuRecyclerView.setAdapter(mMyAdapter);

    }

    private void initDatas() {
//        for (int i = 0; i < 3; i++) {
//            results.add(new Item(i * 8 + 0, "收款", R.mipmap.takeout_ic_category_brand));
//            results.add(new Item(i * 8 + 1, "转账", R.mipmap.takeout_ic_category_flower));
//            results.add(new Item(i * 8 + 2, "余额宝", R.mipmap.takeout_ic_category_fruit));
//            results.add(new Item(i * 8 + 3, "手机充值", R.mipmap.takeout_ic_category_medicine));
//            results.add(new Item(i * 8 + 4, "医疗", R.mipmap.takeout_ic_category_motorcycle));
//            results.add(new Item(i * 8 + 5, "彩票", R.mipmap.takeout_ic_category_public));
//            results.add(new Item(i * 8 + 6, "电影", R.mipmap.takeout_ic_category_store));
//            results.add(new Item(i * 8 + 7, "游戏", R.mipmap.takeout_ic_category_sweet));
//        }
        results.add(new Item(3, "时间", R.mipmap.time4x2));
        results.add(new Item(2, "运动", R.mipmap.sprot));
        results.add(new Item(1, "股票", R.mipmap.gupiao));
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(mContext, "我现在是第" + position + "条。", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 当Item移动的时候。
     */
//    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
//        @Override
//        public boolean onItemMove(int fromPosition, int toPosition) {
////            if (toPosition == 0) {// 保证第一个不被挤走。
////                return false;
////            }
////            Collections.swap(results, fromPosition, toPosition);
////            mMyAdapter.notifyItemMoved(fromPosition, toPosition);
////            return true;
//            itemTouchHelper.startDrag(mSwipeMenuRecyclerView.getChildViewHolder(view));
//
//        }
//
//        @Override
//        public void onItemDismiss(int position) {
//            results.remove(position);
//            mMyAdapter.notifyItemRemoved(position);
//            Toast.makeText(mContext, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    /**
//     * 当Item被移动之前。
//     */
//    public static OnItemMovementListener onItemMovementListener = new OnItemMovementListener() {
//        /**
//         * 当Item在移动之前，获取拖拽的方向。
//         * @param recyclerView     {@link RecyclerView}.
//         * @param targetViewHolder target ViewHolder.
//         * @return
//         */
//        @Override
//        public int onDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
//            // 我们让第一个不能拖拽。
////            if (targetViewHolder.getAdapterPosition() == 0) {
////                return OnItemMovementListener.INVALID;// 返回无效的方向。
////            }
//
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            if (layoutManager instanceof LinearLayoutManager) {// 如果是LinearLayoutManager。
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {// 横向的List。
//                    return (OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT); // 只能左右拖拽。
//                } else {// 竖向的List。
//                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN; // 只能上下拖拽。
//                }
//            } else if (layoutManager instanceof GridLayoutManager) {// 如果是Grid。
//                return OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT | OnItemMovementListener.UP | OnItemMovementListener.DOWN; // 可以上下左右拖拽。
//            }
//            return OnItemMovementListener.INVALID;// 返回无效的方向。
//        }
//
//        @Override
//        public int onSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder) {
//            // 我们让第一个不能滑动删除。
////            if (targetViewHolder.getAdapterPosition() == 0) {
////                return OnItemMovementListener.INVALID;// 返回无效的方向。
////            }
//
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            if (layoutManager instanceof LinearLayoutManager) {// 如果是LinearLayoutManager
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {// 横向的List。
//                    return OnItemMovementListener.UP | OnItemMovementListener.DOWN; // 只能上下滑动删除。
//                } else {// 竖向的List。
//                    return OnItemMovementListener.LEFT | OnItemMovementListener.RIGHT; // 只能左右滑动删除。
//                }
//            }
//            return OnItemMovementListener.INVALID;// 其它均返回无效的方向。
//        }
//    };
}
