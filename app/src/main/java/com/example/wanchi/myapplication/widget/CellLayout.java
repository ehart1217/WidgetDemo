package com.example.wanchi.myapplication.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Scroller;

import com.example.wanchi.myapplication.utils.DensityUtil;

/**
 * 宫格根布局
 * Created by wanchi on 2016/11/24.
 */

public class CellLayout extends ViewGroup implements View.OnLongClickListener {

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

    //拖拽Item的子View
    private ImageView dragImageView;
    //拖拽View对应的位图
    private Bitmap mDragBitmap;
    private Context mContext;

    private int mDownX;
    private int mDownY;

    //页面滚动的Scroll管理器
    private Scroller mScroller;

    private int mDragOffsetX;
    private int mDragOffsetY;
    private int mDragPosition;
    private int temChangPosition;

    private int halfBitmapWidth;
    private int halfBitmapHeight;
    private Rect frame;

    private int colCount = 4;
    private int rowCount = 6;

    //行间距
    private int rowSpace = 0;
    //列间距
    private int colSpace = 0;

    //item的宽度
    private int childWidth = 0;
    //item的高度
    private int childHeight = 0;

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

    public CellLayout(Context context) {
        this(context, null);
    }

    public CellLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.mScroller = new Scroller(context);

        setOnLongClickListener(this);
        addAllChildClickListener(this);
    }


    @Override
    public void addView(View child, int index, LayoutParams params) {
        child.setClickable(true);
        if (child.getVisibility() != View.VISIBLE)
            child.setVisibility(View.VISIBLE);
        super.addView(child, index, params);
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
        int usedWidth = width - leftPadding - rightPadding - (colCount - 1)
                * colSpace;
        int usedheight = ((height - topPadding - bottomPadding - (rowCount - 1)
                * rowSpace));
        int childWidth = usedWidth / colCount;
        int childHeight = usedheight / rowCount;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                childWidth = childView.getMeasuredWidth();
                childHeight = childView.getMeasuredHeight();

                int row = i / colCount % rowCount;
                int col = i % colCount;
                int left = leftPadding + col
                        * (colSpace + childWidth);
                int top = topPadding + row * (rowSpace + childHeight);

                childView.layout(left, top, left + childWidth,
                        top + childView.getMeasuredHeight());
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

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        // 判断是否长按了子View
        if (v.getTag() instanceof CellInfo) {
            v.destroyDrawingCache();
            v.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(v.getDrawingCache());
            v.setVisibility(View.GONE);
            startDrag(bm, (int) (mLastMotionX), (int) (mLastMotionY), v);
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
                if (mScroller.isFinished()) {
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                    temChangPosition = mDragPosition = pointToPosition((int) x, (int) y);
                    mDragOffsetX = (int) (ev.getRawX() - x);
                    mDragOffsetY = (int) (ev.getRawY() - y);

                    mLastMotionX = x;
                    mLastMotionY = y;
                    mDownX = (int) x;
                }
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
                if (dragImageView != null) {
//                    animationMap.clear();
                    showDropAnimation((int) x, (int) y);
                }
                mDownX = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
//                showEdit(false);
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
    private void startDrag(Bitmap bm, int x, int y, View cellView) {

        mDragPointX = x - cellView.getLeft();
        mDragPointY = y - cellView.getTop();
        mWindowParams = new WindowManager.LayoutParams();

        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.x = cellView.getLeft();
        mWindowParams.y = cellView.getTop();
        mWindowParams.height = LayoutParams.WRAP_CONTENT;
        mWindowParams.width = LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        mWindowParams.alpha = 0.8f;

        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);
        mDragBitmap = bm;
        mWindowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        mWindowManager.addView(iv, mWindowParams);
        dragImageView = iv;

        halfBitmapWidth = bm.getWidth() / 2;
        halfBitmapHeight = bm.getHeight() / 2;

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).getBackground().setAlpha((int) (0.8f * 255));
        }
    }

    //根据手势绘制不断变化位置的dragView
    private void onDrag(int x, int y) {
        if (dragImageView != null) {
            mWindowParams.alpha = 0.8f;
            mWindowParams.x = x - mDragPointX + mDragOffsetX;
            mWindowParams.y = y - mDragPointY + mDragOffsetY;
            mWindowManager.updateViewLayout(dragImageView, mWindowParams);
        }
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != -1) {
            mDragPosition = tempPosition;
        }
        View view = getChildAt(temChangPosition);
        if (view == null) {
            stopDrag();
            return;
        }
        view.setVisibility(View.INVISIBLE);
        if (temChangPosition != mDragPosition) {
            View dragView = getChildAt(temChangPosition);
            movePositionAnimation(temChangPosition, mDragPosition);
            removeViewAt(temChangPosition);
            addView(dragView, mDragPosition);
            getChildAt(mDragPosition).setVisibility(View.INVISIBLE);
//            this.getSaAdapter().exchange(temChangPosition, mDragPosition);
            temChangPosition = mDragPosition;
        }
    }

    //根据坐标，判断当前item所属的位置，即编号
    public int pointToPosition(int x, int y) {

        if (frame == null)
            frame = new Rect();
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    //停止拖动
    private void stopDrag() {
//        recoverChildren();
        if (mMode == MODE_DRAGGING) {
            if (getChildAt(mDragPosition).getVisibility() != View.VISIBLE)
                getChildAt(mDragPosition).setVisibility(View.VISIBLE);
            mMode = MODE_FREE;
//            mContext.sendBroadcast(new Intent("com.stg.menu_move"));
        }
    }

    private boolean isCanMove() {
        return true;
    }

    //执行位置动画
    private void movePositionAnimation(int oldP, int newP) {
        int moveNum = newP - oldP;
        if (moveNum != 0 && !isMovingFastConflict(moveNum)) {
            int absMoveNum = Math.abs(moveNum);
            for (int i = 0; i < absMoveNum; i++) {
                int holdPosition = (moveNum > 0) ? oldP + 1 : oldP - 1;
                View view = getChildAt(holdPosition);
                if (view != null) {
                    view.startAnimation(animationPositionToPosition(oldP, newP));
                }
                oldP = holdPosition;
            }
        }
    }

    //返回滑动的位移动画，比较复杂，有兴趣的可以看看
    private Animation animationPositionToPosition(int oldP, int newP) {

        PointF oldPF = positionToPoint2(oldP);
        PointF newPF = positionToPoint2(newP);

        TranslateAnimation animation;

        // regular animation between two neighbor items
        animation = new TranslateAnimation(newPF.x - oldPF.x, 0, newPF.y
                - oldPF.y, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
//        animation.setAnimationListener(new NotifyDataSetListener(oldP));

        return animation;
    }

    //执行松手动画
    private void showDropAnimation(int x, int y) {
        ViewGroup moveView = (ViewGroup) getChildAt(mDragPosition);
        TranslateAnimation animation = new TranslateAnimation(x
                - halfBitmapWidth - moveView.getLeft(), 0, y - halfBitmapHeight
                - moveView.getTop(), 0);
        animation.setFillAfter(false);
        animation.setDuration(300);
        moveView.setAnimation(animation);
        mWindowManager.removeView(dragImageView);
        dragImageView = null;

        if (mDragBitmap != null) {
            mDragBitmap = null;
        }

        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
    }


    /**
     * @param moveNum
     * @return
     */
    //判断滑动的一系列动画是否有冲突
    private boolean isMovingFastConflict(int moveNum) {
//        int itemsMoveNum = Math.abs(moveNum);
//        int temp = mDragPosition;
//        for (int i = 0; i < itemsMoveNum; i++) {
//            int holdPosition = moveNum > 0 ? temp + 1 : temp - 1;
//            if (animationMap.containsKey(holdPosition)) {
//                return true;
//            }
//            temp = holdPosition;
//        }
        return false;
    }

    public PointF positionToPoint2(int position) {
        PointF point = new PointF();

        int row = position / colCount % rowCount;
        int col = position % colCount;
        int left = leftPadding + col * (colSpace + childWidth);
        int top = topPadding + row * (rowSpace + childHeight);

        point.x = left;
        point.y = top;
        return point;

    }

    public static class CellInfo {
        View cell;
        int cellX = -1;
        int cellY = -1;
        int spanX;
        int spanY;
    }
}
