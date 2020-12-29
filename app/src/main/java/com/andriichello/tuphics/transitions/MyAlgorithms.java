package com.andriichello.tuphics.transitions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.andriichello.tuphics.types.Polygon;
import com.andriichello.tuphics.ui.coloring.ImageColoringFragment;

import kotlin.jvm.internal.Ref;

public class MyAlgorithms {
    public static Bitmap drawPolygon(Polygon polygon, float minX, float minY, float maxX, float maxY, int xSteps, int ySteps, float padding, float margin, float width, float height) {
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(Color.GRAY);

        float step = Math.max(width, height) / (xSteps + 1);

        float x = step;
        while (x <= width) {
            canvas.drawLine(x, 0, x, height, paint);
            x += step;
        }

        float y = 0;
        while (y <= height - step) {
            canvas.drawLine(0, y, width, y, paint);
            y += step;
        }

        paint.setColor(Color.RED);
        for (int i = 1; i < polygon.points.size(); i++) {
            canvas.drawLine(polygon.points.get(i - 1).x - minX, polygon.points.get(i - 1).y - minY, polygon.points.get(i).x - minX, polygon.points.get(i).y - minY, paint);
        }

        return bitmap;
    }
}
