package com.neha.dragdropanimation;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

public class EmptyDragShadowBuilder extends View.DragShadowBuilder {

    public EmptyDragShadowBuilder(View arg0) {
        super(arg0);
    }

    @Override
    public void onProvideShadowMetrics(Point size, Point touch) {
        super.onProvideShadowMetrics(size, touch);

    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        // draw nothing
    }
}