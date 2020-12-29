package com.andriichello.tuphics.fractals;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.andriichello.tuphics.types.Fractal;
import com.andriichello.tuphics.types.Segment;
import com.andriichello.tuphics.types.Shape;

import java.util.Arrays;

public class MyAlgorithms {
    public static Bitmap kochFractalBitmap(int iterations, int a, int b, int splits, Shape shape, int width, int height) {
        Fractal koch = null;
        if (shape == Shape.Triangle) {
            PointF A, B, C;
            double sideLength = width * 0.6;
            double perpendicularLength = sideLength * Math.sqrt(3) / 2;

            A = new PointF((int) ((width - sideLength) / 2), (int) (height / 2 - perpendicularLength / 2));
            B = new PointF((int) (width - (width - sideLength) / 2), (int) (height / 2 - perpendicularLength / 2));
            C = new PointF((int) (sideLength / 2 + (width - sideLength) / 2), (int) (height / 2 + perpendicularLength / 2));

            Segment segment = new Segment(Arrays.asList(C, B, A, C));
//            Segment segment = new Segment(Arrays.asList(A, B));
            koch = new Fractal(segment);
            for (int i = iterations; i > 0; i--)
                segment = Segment.koch(segment, shape, splits, a, b);

            koch.fractal = segment;
        }

        if (shape == Shape.Square) {
            PointF A, B, C, D;
            double sideLength = width * 0.5;

            A = new PointF((int) ((width - sideLength) / 2), (int) (height / 2 - sideLength / 2));
            B = new PointF((int) (width - (width - sideLength) / 2), (int) (height / 2 - sideLength / 2));
            C = new PointF((int) (width - (width - sideLength) / 2), (int) (height / 2 + sideLength / 2));
            D = new PointF((int) ((width - sideLength) / 2), (int) (height / 2 + sideLength / 2));

            Segment segment = new Segment(Arrays.asList(D, C, B, A, D));
//            Segment segment = new Segment(Arrays.asList(A, B));
            koch = new Fractal(segment);
            for (int i = iterations; i > 0; i--)
                segment = Segment.koch(segment, shape, splits, a, b);

            koch.fractal = segment;
        }

        if (koch == null || koch.fractal == null || koch.fractal.isEmpty())
            return null;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

//        paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.GREEN);
//        paint.setStrokeWidth(5.0f);
//
//        for (int i = 1; i < koch.shape.points.size(); i++)
//        {
//            canvas.drawLine(koch.shape.points.get(i - 1).x, koch.shape.points.get(i - 1).y,
//                    koch.shape.points.get(i).x, koch.shape.points.get(i).y, paint);
//        }

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);

        for (int i = 1; i < koch.fractal.points.size(); i++)
        {
            canvas.drawLine(koch.fractal.points.get(i - 1).x, koch.fractal.points.get(i - 1).y,
                    koch.fractal.points.get(i).x, koch.fractal.points.get(i).y, paint);
        }

        return bitmap;
    }
}
