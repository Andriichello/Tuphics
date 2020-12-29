package com.andriichello.tuphics.types;

import android.graphics.PointF;

import androidx.annotation.NonNull;

public class Line {
    public PointF beg, end;

    public Line() {
        this.beg = null;
        this.end = null;
    }

    public Line(@NonNull PointF beg, @NonNull PointF end) {
        this.beg = beg;
        this.end = end;
    }

    public PointF point(float t) {
        if (beg == null || end == null)
            return null;

        return new PointF((1 - t) * beg.x + t * end.x, (1 - t) * beg.y + t * end.y);
    }

    public static double length(Line line) {
        if (line != null && line.isValid())
            return line.length();
        else return 0.0;
    }

    public static double length(PointF beg, PointF end) {
        if (beg != null && end != null)
            return Math.sqrt((end.x - beg.x) * (end.x - beg.x) + (end.y - beg.y) * (end.y - beg.y));
        else return 0.0;
    }

    public static double length(double begX, double begY, double endX, double endY) {
        return Math.sqrt((endX - begX) * (endX - begX) + (endY - begY) * (endY - begY));
    }

    public double length() {
        if (beg == null || end == null)
            return 0;

        return Math.sqrt((end.x - beg.x) * (end.x - beg.x) + (end.y - beg.y) * (end.y - beg.y));
    }

    // return angle to X axis in degrees
    public double angle() {
        if (beg == null || end == null)
            return 0;
        return Math.toDegrees(Math.atan2(end.y - beg.y, end.x - beg.x));
    }

    public double k() {
        if (beg == null || end == null)
            return 0;
        return (double) (end.y - beg.y) / (end.x - beg.x);
    }

    public double b() {
        return k() * beg.x + beg.y;
    }


    public Line rotate(double degrees, PointF origin) {
        if (origin == null)
            origin = new PointF(0, 0);

        double x, y;
        double difX, difY;
        double radians = Math.toRadians(degrees);

        PointF b, e;
        x = beg.x * Math.cos(radians) - beg.y * Math.sin(radians);
        y = beg.y * Math.cos(radians) + beg.x * Math.sin(radians);
        b = new PointF((float)x + origin.x, (float)y + origin.y);

        x = end.x * Math.cos(radians) - end.y * Math.sin(radians);
        y = end.y * Math.cos(radians) + end.x * Math.sin(radians);
        e = new PointF((float)x + origin.x, (float)y + origin.y);

        return new Line(b, e);
    }

    public boolean isValid() {
        return beg != null && end != null;
    }

    @Override
    public String toString() {
        return "Line{" + beg + " ; " + end + '}';
    }
}
