package com.neha.dragdropanimation;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Neha Kushwah on 6/8/15.
 */
public class DragContainer extends ViewGroup {
    private final Context context;
    protected boolean mOnDrag;
    private View target;
    private float mDragX;
    private float mDragY;

    public DragContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragContainer(Context context) {
        super(context, null);
        this.context = context;
    }

    public DragContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i = getChildCount();
        if (i != 1) {
            throw new IllegalStateException("Only Support One Child");
        }
        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(l, t, r, b);
    }

    @SuppressLint("NewApi")
    public boolean startDragChild(View child, ClipData data,
                                  Object myLocalState, int flags) {
        setDragTarget(child);
        return child.startDrag(data, new EmptyDragShadowBuilder(child),
                myLocalState, flags);
    }

    /**
     * this is similar to the constructor of DragShadowBuilder
     *
     * @param v
     */
    protected void onSetDragTarget(View v) {
        // Empty
    }

    public View getDragTarget() {
        return target;
    }

    private void setDragTarget(View v) {
        target = v;
        onSetDragTarget(v);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        super.dispatchDraw(canvas);
        if (mOnDrag && target != null) {
            canvas.save();
            drawDragShadow(canvas);
            canvas.restore();
        }
    }

    protected void drawDragShadow(Canvas canvas) {
        int h = target.getHeight();
        int w = target.getWidth();
        canvas.translate(mDragX - w / 2, mDragY - h / 2);
        target.draw(canvas);
    }

    protected float getDragX() {
        return mDragX;
    }

    protected float getDragY() {
        return mDragY;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        Log.v("DragView", event.toString());
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                int[] loc = new int[2];
                getLocationOnScreen(loc);
                mDragX = event.getX() - loc[0];
                mDragY = event.getY() - loc[1];
                mOnDrag = true;
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                mOnDrag = false;
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                loc = new int[2];
                getLocationOnScreen(loc);
                mDragX = event.getX() - loc[0];
                mDragY = event.getY() - loc[1];
                break;
            default:
                mDragX = event.getX();
                mDragY = event.getY();
                break;
        }
        invalidate();
        return super.dispatchDragEvent(event);
    }


}
