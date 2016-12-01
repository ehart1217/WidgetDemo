package com.example.wanchi.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.wanchi.myapplication.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 宫格根布局
 * Created by wanchi on 2016/11/24.
 */

public class CellLayout extends ViewGroup implements View.OnLongClickListener {

    private static final String TAG_DRAG = CellLayout.class.getSimpleName();

    // 状态
    private int MODE_FREE = 0; //静止状态
    private int MODE_DRAGGING = 1; //当前页面下，拖动状态

    private int mMode = MODE_FREE;

    float mLastMotionX;
    float mLastMotionY;

    //拖动点的X坐标（加上当前屏数 * screenWidth）
    private int mDragPointX;
    //拖动点的Y坐标
    private int mDragPointY;

    //window管理器，负责随手势显示拖拽View
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;

    //被选中的view，即被拖动的view的本体（与正在被拖动的view不是同一个）
    private View mDraggingView;
    //生成的正在拖拽的view
    private ImageView mDraggingImageView;
    //拖拽View对应的位图
    private Bitmap mDragBitmap;
    private Context mContext;

    private int mDownX;
    private int mDownY;

    private int mDragOffsetX; // drag的偏移量，计算container的相对位置，例如计算状态栏的高度。
    private int mDragOffsetY;
    private Point mCurrentPassPosition;
    private Point mDownDragPosition;

    private int halfBitmapWidth;
    private int halfBitmapHeight;
    private Rect frame;

    private int mColumn = 4;
    private int mRow = 6;

    //行间距
    private int rowSpace = 0;
    //列间距
    private int colSpace = 0;

    //item的宽度
    private float mCellWidth = 0;
    //item的高度
    private float mCellHeight = 0;

    //左边距
    private int leftPadding = 0;
    //右边距
    private int rightPadding = 0;
    //上边距
    private int topPadding = 0;
    //下边距
    private int bottomPadding = 0;
    private int screenWidth;
    private int screenHeight;

    // 空间被占用情况
    private boolean[][] mOccupied;
    private HashMap<Point, View> mViewMap;
    private Point mFromPositionCache; // 为了取消拖动，恢复原来的位置，保存的一个信息。
    private Point mLastPassPosition; // 为了得到挤压方向，保存交换位置前的最后一个经过的位置。
    private Point mToPositionCache;
    private Point mDraggingSpan; //被拖拽的View的占地
    private Point mCurrentPassOriginPosition;
    private OnLongClickEmptyListener mLongClickEmptyListener;

    public CellLayout(Context context) {
        this(context, null);
    }

    public CellLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mOccupied = new boolean[mColumn][mRow];
        mViewMap = new HashMap<>();

        setOnLongClickListener(this);
        addAllChildClickListener(this);
    }


    public void addCell(View child, Point position, Point size) {
        child.setClickable(true);
        CellInfo cellInfo = new CellInfo();
        cellInfo.cell = child;
        cellInfo.spanX = size.x;
        cellInfo.spanY = size.y;
        cellInfo.position = position;
        child.setTag(cellInfo);
        mViewMap.put(position, child);
        if (child.getVisibility() != View.VISIBLE) {
            child.setVisibility(View.VISIBLE);
        }
        super.addView(child);
        addAllChildClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        MeasureSpec.getMode(widthMeasureSpec);

        final int height = MeasureSpec.getSize(heightMeasureSpec);
        MeasureSpec.getMode(heightMeasureSpec);

        screenWidth = width;
        screenHeight = height;
        int usedWidth = width - leftPadding - rightPadding - (mColumn - 1)
                * colSpace;
        int usedHeight = ((height - topPadding - bottomPadding - (mRow - 1)
                * rowSpace));
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getTag() instanceof CellInfo) {
                CellInfo cellInfo = (CellInfo) child.getTag();
                int childWidth = usedWidth / mColumn * cellInfo.spanX;
                int childHeight = usedHeight / mRow * cellInfo.spanY;

                int childWidthSpec = getChildMeasureSpec(
                        MeasureSpec
                                .makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        20, childWidth);
                int childHeightSpec = getChildMeasureSpec(
                        MeasureSpec.makeMeasureSpec(childHeight,
                                MeasureSpec.EXACTLY), 20, childHeight);
                child.measure(childWidthSpec, childHeightSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        mCellWidth = getMeasuredWidth() / mColumn;
        mCellHeight = getMeasuredHeight() / mRow;

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {

                if (childView.getTag() instanceof CellInfo) {
                    CellInfo cellInfo = (CellInfo) childView.getTag();
                    int left = (int) (mCellWidth * cellInfo.position.x);
                    int top = (int) (mCellHeight * cellInfo.position.y);
                    childView.layout(left, top, left + childView.getMeasuredWidth(),
                            top + childView.getMeasuredHeight());
                }

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void addAllChildClickListener(OnLongClickListener l) {

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setOnLongClickListener(l);
        }
    }

    public void setOnLongClickEmptyListener(OnLongClickEmptyListener onLongClickEmptyListener) {
        mLongClickEmptyListener = onLongClickEmptyListener;
    }

    @Override
    public boolean onLongClick(View v) {
        // 判断是否长按了子View
        if (v.getTag() instanceof CellInfo) {
            v.destroyDrawingCache();
            v.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
            startDrag(bm, (int) (mLastMotionX), (int) (mLastMotionY), v);
        } else if (mLongClickEmptyListener != null) {
            mLongClickEmptyListener.onLongClickEmpty(v);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        int thresholdX = DensityUtil.dip2px(mContext, 8);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) x;
                mDownDragPosition = mCurrentPassPosition = pointToPosition((int) x, (int) y);
                mLastPassPosition = mDownDragPosition;

                mDragOffsetX = (int) (ev.getRawX() - x);
                mDragOffsetY = (int) (ev.getRawY() - y);

                mLastMotionX = x;
                mLastMotionY = y;
                mDownX = (int) x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_DRAGGING) {
                    onDrag((int) x, (int) y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == MODE_DRAGGING) {
                    stopDrag();
                }
                if (mDraggingImageView != null) {
//                    animationMap.clear();
                    showDropAnimation((int) x, (int) y);
                }
                mDownX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
//                showEdit(false);
                break;
        }
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * @param bm       被拖动的bitmap
     * @param x        当前手指拖到的位置X
     * @param y        当前手指拖到的位置Y
     * @param cellView 长按的View
     */
    private void startDrag(Bitmap bm, int x, int y, final View cellView) {
        Log.e(TAG_DRAG, "startDrag");
        mDraggingView = cellView;

        mDragPointX = x - cellView.getLeft();
        mDragPointY = y - cellView.getTop();
        mWindowParams = new WindowManager.LayoutParams();

        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.x = cellView.getLeft() + mDragOffsetX;
        mWindowParams.y = cellView.getTop() + mDragOffsetY;
        mWindowParams.height = (int) (cellView.getMeasuredHeight() * 1.1);
        mWindowParams.width = (int) (cellView.getMeasuredWidth() * 1.1);
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        mWindowParams.alpha = 0.8f;

        final ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);
        mDragBitmap = bm;
        mWindowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);

        cellView.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv.setVisibility(VISIBLE);
                cellView.setVisibility(View.INVISIBLE);
                mMode = MODE_DRAGGING;
            }
        }, 200);
        mWindowManager.addView(iv, mWindowParams);
        iv.setScaleX(1.1f);
        iv.setScaleY(1.1f);
        iv.setVisibility(INVISIBLE);
        mDraggingImageView = iv;

        halfBitmapWidth = bm.getWidth() / 2;
        halfBitmapHeight = bm.getHeight() / 2;

//        for (int i = 0; i < getChildCount(); i++) {
//            getChildAt(i).getBackground().setAlpha((int) (0.8f * 255));
//        }

        // 获得被拖拽的View的占地
        if (cellView.getTag() instanceof CellInfo) {
            CellInfo cellInfo = (CellInfo) cellView.getTag();
            mDraggingSpan = new Point(cellInfo.spanX, cellInfo.spanY);
        }
    }

    //根据手势绘制不断变化位置的dragView
    private void onDrag(int x, int y) {
        if (mDraggingImageView != null) {
            mWindowParams.alpha = 0.8f;
            mWindowParams.x = x - mDragPointX + mDragOffsetX;
            mWindowParams.y = y - mDragPointY + mDragOffsetY;
            mWindowManager.updateViewLayout(mDraggingImageView, mWindowParams);
        }
        if (mCurrentPassPosition != null && !mCurrentPassPosition.equals(pointToPosition(x, y))) {
            // 前一个经过的位置
            mLastPassPosition = mCurrentPassPosition;
        }
        mCurrentPassPosition = pointToPosition(x, y);
        mCurrentPassOriginPosition = calculateDraggingOriginPosition();

        if (mDraggingView == null) {
            stopDrag();
            return;
        }
        mDraggingView.setVisibility(View.INVISIBLE);

        //取消原本换了的位置
        if (mFromPositionCache != null && mToPositionCache != null && !isOverlap(mCurrentPassOriginPosition, mDraggingSpan, findViewByPosition(mFromPositionCache))) {
            View exchangedView = mViewMap.get(mFromPositionCache);
            if (exchangedView != null) {
                exchangedView.startAnimation(generateAnimation(mFromPositionCache, mToPositionCache, true));
            }
            mFromPositionCache = null;
        }

        // 交换位置
        if (!isInside(mCurrentPassPosition, mDraggingView) &&
                (mFromPositionCache == null ||
                        !isOverlap(mCurrentPassOriginPosition, mDraggingSpan, findViewByPosition(mFromPositionCache)))) {

            // 找到所有交叉的view
            List<View> viewList = findViewListByMultiPosition(mCurrentPassOriginPosition, mDraggingSpan);
            viewList.remove(mDraggingView);// 排除掉自己
            Log.e(TAG_DRAG, "findOverlapView size:" + viewList.size());
            if (viewList.size() == 1) {// TODO 目前只支持交换一个，交换多个待开发
                View currentView = viewList.get(0);
                if (currentView != null) {
                    mToPositionCache = findDestination(currentView, mLastPassPosition);
                    if (mToPositionCache != null) {
                        CellInfo currentViewInfo = (CellInfo) currentView.getTag();
                        mFromPositionCache = new Point(currentViewInfo.position);
                        Log.e(TAG_DRAG, "exchange from:" + mFromPositionCache + " to:" + mToPositionCache);
                        currentView.startAnimation(generateAnimation(mFromPositionCache, mToPositionCache, false));
                    }
                }
            }

        }
    }

    //根据坐标，判断当前item所属的位置
    public Point pointToPosition(int x, int y) {
        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;
        Point position = new Point();
        position.x = (int) (x / mCellWidth);
        position.y = (int) (y / mCellHeight);
        return position;
    }

    /**
     * 判断一个点是否在一个范围
     *
     * @param position       这个点
     * @param originPosition 这个范围的其中一个点
     * @param span           这个范围的大小
     * @return 在里面返回true，否则false；
     */
    private boolean isInside(Point position, Point originPosition, Point span) {
        boolean isXInside = position.x >= originPosition.x && position.x <= originPosition.x + span.x - 1;
        boolean isYInside = position.y >= originPosition.y && position.y <= originPosition.y + span.y - 1;
        return isXInside && isYInside;
    }

    private boolean isInside(Point position, View view) {
        CellInfo cellInfo = (CellInfo) view.getTag();
        Point span = new Point(cellInfo.spanX, cellInfo.spanY);
        return isInside(position, cellInfo.position, span);
    }


    private boolean isOverlap(Point originPosition, Point span, View view) {
        for (int x = originPosition.x; x < originPosition.x + span.x; x++) {
            for (int y = originPosition.y; y < originPosition.y + span.y; y++) {
                if (isInside(new Point(x, y), view)) {
                    return true;
                }
            }
        }
        return false;
    }

    private View findViewByPosition(Point position) {
        Collection<View> viewList = mViewMap.values();
        for (View view : viewList) {
            if (isInside(position, view)) {
                return view;
            }
        }
        return null;
    }

    private Point findOriginPosition(Point position) {
        View view = findViewByPosition(position);
        if (view != null) {
            CellInfo cellInfo = (CellInfo) view.getTag();
            return cellInfo.position;
        }
        return null;
    }

    private Point calculateDraggingOriginPosition() {
        // 被拖拽的view的起始坐标
        CellInfo cellInfo = (CellInfo) mDraggingView.getTag();
        int offsetX = mCurrentPassPosition.x - mDownDragPosition.x;
        int offsetY = mCurrentPassPosition.y - mDownDragPosition.y;
        return new Point(cellInfo.position.x + offsetX, cellInfo.position.y + offsetY);
    }

    private List<View> findViewListByMultiPosition(Point originPosition, Point span) {
        List<View> viewList = new ArrayList<>();
        if (originPosition == null) {
            return viewList;
        }
        for (int x = originPosition.x; x < originPosition.x + span.x; x++) {
            for (int y = originPosition.y; y < originPosition.y + span.y; y++) {
                View view = findViewByPosition(new Point(x, y));
                if (view != null && !viewList.contains(view)) {
                    viewList.add(view);
                }
            }
        }
        return viewList;
    }

    /**
     * 根据拖动的方向，寻找合适的位置
     *
     * @param view             将要被移走的view
     * @param lastPassPosition 最后一次经过的位置，用来决定拖动的方向
     * @return 得到目的地位置, 如果没有就返回null。
     */
    private Point findDestination(View view, Point lastPassPosition) {
        if (view.getTag() instanceof CellInfo) {
            CellInfo cellInfo = (CellInfo) view.getTag();
            Point currentPosition = cellInfo.position;

            int originX = currentPosition.x;
            int originY = currentPosition.y;

            if (lastPassPosition.y != mCurrentPassPosition.y) {
                int offset = lastPassPosition.y > mCurrentPassPosition.y ? -1 : 1;

                ArrayList<Point> destinationList = new ArrayList<>();
                destinationList.add(new Point(originX, originY + offset));
                destinationList.add(new Point(originX + offset, originY));
                destinationList.add(new Point(originX - offset, originY));
                destinationList.add(new Point(originX, originY - offset));

                for (Point destination : destinationList) {
                    if (isPositionEmpty(destination)) {
                        return destination;
                    }
                }
            }

            if (lastPassPosition.x != mCurrentPassPosition.x) {
                int offset = lastPassPosition.x > mCurrentPassPosition.x ? -1 : 1;

                ArrayList<Point> destinationList = new ArrayList<>();
                destinationList.add(new Point(originX + offset, originY));
                destinationList.add(new Point(originX, originY + offset));
                destinationList.add(new Point(originX, originY - offset));
                destinationList.add(new Point(originX - offset, originY));

                for (Point destination : destinationList) {
                    if (isPositionEmpty(destination)) {
                        return destination;
                    }
                }
            }
        }
        return null;
    }

    private boolean isPositionEmpty(Point position) {
        if (position.x >= 0 && position.x <= mColumn - 1 && position.y >= 0 && position.y <= mRow - 1) {
            if (position.equals(mDownDragPosition)) {
                return true;
            }
            if (mViewMap.get(position) == null) {
                return true;
            }
        }
        return false;
    }

    //停止拖动
    private void stopDrag() {
//        recoverChildren();
        if (mMode == MODE_DRAGGING) {
            View currentView = mViewMap.get(mCurrentPassPosition);
            if (currentView != null && currentView.getVisibility() != View.VISIBLE) {
                currentView.setVisibility(View.VISIBLE);
            }

            View mLastView = null;
            if (mFromPositionCache != null) {
                mLastView = mViewMap.get(mFromPositionCache);
                mFromPositionCache = null;
            }
            mLastPassPosition = null;
            // 正在被拖动的view调整
            changePosition(mDraggingView, mCurrentPassPosition, true);
            mDraggingView.setVisibility(VISIBLE);
            mMode = MODE_FREE;

            // 被换位置的view调整
            if (mLastView != null) {
                mLastView.setVisibility(VISIBLE);
                changePosition(mLastView, mToPositionCache, false);
            }
            requestLayout();
        }
    }

    /**
     * 改变View的位置
     * 1.改变tag里Info的值
     * 2.改变map里的值
     *
     * @param targetView           目标view
     * @param position             目标位置
     * @param removeOriginPosition 是否移除map中原先的position
     * @return 成功与否
     */
    private boolean changePosition(View targetView, Point position, boolean removeOriginPosition) {
        if (targetView.getTag() instanceof CellInfo) {
            CellInfo cellInfo = (CellInfo) targetView.getTag();
            if (removeOriginPosition) {
                mViewMap.remove(cellInfo.position);
            }
            int offsetX = position.x - mDownDragPosition.x;
            int offsetY = position.y - mDownDragPosition.y;
            cellInfo.position.x += offsetX;
            cellInfo.position.y += offsetY;
            if (cellInfo.position.x < 0) {
                cellInfo.position.x = 0;
            }
            if (cellInfo.position.x + cellInfo.spanX >= mColumn) {
                cellInfo.position.x = mColumn - cellInfo.spanX;
            }
            if (cellInfo.position.y < 0) {
                cellInfo.position.y = 0;
            }
            if (cellInfo.position.y + cellInfo.spanY >= mRow) {
                cellInfo.position.y = mRow - cellInfo.spanY;
            }
            mViewMap.put(cellInfo.position, targetView);
            return true;
        }
        return false;
    }

    private Animation generateAnimation(Point oldP, Point newP, boolean reverse) {

        PointF oldPF = getOriginOfPosition(oldP);
        PointF newPF = getOriginOfPosition(newP);

        TranslateAnimation animation;

        if (reverse) {
            animation = new TranslateAnimation(newPF.x - oldPF.x, 0, newPF.y - oldPF.y, 0);
        } else {
            animation = new TranslateAnimation(0, newPF.x - oldPF.x, 0, newPF.y - oldPF.y);
        }

        animation.setDuration(500);
        animation.setFillAfter(true);

        return animation;
    }


    //执行松手动画
    private void showDropAnimation(int x, int y) {
        TranslateAnimation animation = new TranslateAnimation(x
                - halfBitmapWidth - mDraggingView.getLeft(), 0, y - halfBitmapHeight
                - mDraggingView.getTop(), 0);
        animation.setFillAfter(false);
        animation.setDuration(300);
        mDraggingView.setAnimation(animation);
        mWindowManager.removeView(mDraggingImageView);
        mDraggingImageView = null;

        if (mDragBitmap != null) {
            mDragBitmap = null;
        }

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
    }

    public PointF getOriginOfPosition(Point position) {
        PointF point = new PointF();
        point.x = position.x * mCellWidth;
        point.y = position.y * mCellHeight;
        return point;

    }

    public static class CellInfo {
        View cell;
        Point position = new Point(-1, -1);
        int spanX;
        int spanY;
    }

    public interface OnLongClickEmptyListener {
        void onLongClickEmpty(View view);
    }
}
