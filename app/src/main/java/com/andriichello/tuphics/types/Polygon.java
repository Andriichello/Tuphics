package com.andriichello.tuphics.types;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {
    public List<PointF> points = new ArrayList<>();

    public Polygon() {

    }

    public Polygon(Polygon polygon) {
        if (polygon != null && polygon.points != null && !polygon.points.isEmpty()) {
            for (PointF p : polygon.points) {
                points.add(new PointF(p.x, p.y));
            }
        }
    }

    public Polygon(List<PointF> points) {
        if (points != null && !points.isEmpty()) {
            for (PointF p : points) {
                this.points.add(new PointF(p.x, p.y));
            }
        }
    }

    public Polygon rotate(double degrees, PointF origin) {
        Polygon polygon = new Polygon(this);
        if (points == null || points.isEmpty() || degrees == 0)
            return polygon;

        if (origin == null)
            origin = new PointF(0, 0);

        Line line;
        for (int i = 0; i < polygon.points.size(); i++) {
            PointF point = polygon.points.get(i);
            if (point == origin)
                continue;

            line = new Line(origin, point);
            line = line.rotate(degrees, origin);

            polygon.points.set(i, new PointF((float) line.end.x, (float) line.end.y));
        }

        return polygon;
    }

    public Polygon scale(double xFactor, double yFactor) {
        Polygon polygon = new Polygon(this);
        if (points == null || points.isEmpty())
            return polygon;

        for (int i = 0; i < polygon.points.size(); i++) {
            PointF point = polygon.points.get(i);

            polygon.points.set(i, new PointF((float) (point.x * xFactor), (float) (point.y * yFactor)));
        }

        return polygon;
    }

    public double maxSideLength() {
        if (isEmpty() || points.size() == 1)
            return 0.0;

        double max = 0.0;
        for (int i = 0, a = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                double length = Line.length(points.get(i), points.get(j));
                if (max < length)
                    max = length;
            }
        }

        return max;
    }

    public boolean isEmpty() {
        if (points == null || points.size() == 0)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "Polygon{" + points + '}';
    }
}
