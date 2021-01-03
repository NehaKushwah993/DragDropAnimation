package com.neha.dragdropanimation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Neha Kushwah on 6/8/15.
 */
public class BitmapHelper {

    /***
     * returns circular bitmap image
     *
     * @param width
     * @param color
     * @return Bitmap
     */
    public static Bitmap getCircleBitmap(int width, int color) {
        final Bitmap output = Bitmap.createBitmap(width,
                width, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
    }

    /***
     * returns circular bitmap image
     *
     * @param width
     * @param color
     * @return
     */
    public static Bitmap getCircleBitmap(int width, int color, int boundry_color, int boundry_width) {
        final Bitmap output = Bitmap.createBitmap(width,
                width, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return scaleCenterCrop(output, width, boundry_color, boundry_width);
    }

    /**
     * returns scaled bitmap image to center crop
     *
     * @param bitmap
     * @param newWidth
     * @param boundaryColor
     * @param boundaryWidth
     * @return
     */
    public static Bitmap scaleCenterCrop(Bitmap bitmap, int newWidth, int boundaryColor, int
            boundaryWidth) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(boundaryColor);
        p.setStrokeWidth(boundaryWidth);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        return output;
    }
}
