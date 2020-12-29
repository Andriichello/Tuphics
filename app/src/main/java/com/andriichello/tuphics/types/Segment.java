package com.andriichello.tuphics.types;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Segment {
    public List<PointF> points = new ArrayList<>();


    public Segment(@NonNull Line line) {
        this.points.add(line.beg);
        this.points.add(line.end);
    }

    public Segment(@NonNull PointF beg, @NonNull PointF end) {
        this.points.add(beg);
        this.points.add(end);
    }

    public Segment(List<PointF> points) {
        if (points != null && !points.isEmpty())
            this.points.addAll(points);
    }

    public static Segment koch(Line line, @NonNull Shape shape, int splits, int a, int b) {
        if (line == null || line.beg == null || line.end == null)
            return null;

            PointF A = line.point((float) a / splits);
            PointF B = line.point((float) (splits - b) / splits);

//            The formula for that involves basic trig functions.
//            new_x = old_x + Math.cos(angle) * distance;
//            new_y = old_y + Math.sin(angle) * distance;

//            By the way, angle should be in radians.
//            radians = degrees * Math.PI / 180.0;

            if (shape == Shape.Square) {
                // distance is the length of square side
                Line part = new Line(A, B);
                double distance = part.length();
                double angle = part.angle();
                double radians = Math.toRadians(90 + part.angle());

                PointF P1 = new PointF(
                        (float) (A.x + Math.cos(radians) * distance),
                        (float) (A.y + Math.sin(radians) * distance));
                PointF P2 = new PointF(
                        (float) (B.x + Math.cos(radians) * distance),
                        (float) (B.y + Math.sin(radians) * distance));
                return new Segment(Arrays.asList(line.beg, part.beg, P1, P2, part.end, line.end));
            }

            if (shape == Shape.Triangle) {
                // distance is the height of the triangle
                // h = a * Math.sqrt(3) / 2
                Line part = new Line(A, B);
                double distance = part.length() * Math.sqrt(3) / 2;
                double angle = part.angle();
                double radians = Math.toRadians(90 + part.angle());
//                if (angle > 0)
//                    radians = Math.toRadians(-90 + part.angle());

                PointF M = part.point((float) 0.5);
                PointF P = new PointF(
                        (float) (M.x + Math.cos(radians) * distance),
                        (float) (M.y + Math.sin(radians) * distance));
                return new Segment(Arrays.asList(line.beg, part.beg, P, part.end, line.end));
            }

            return null;

//
//        List<Line> lines = new ArrayList<>();
//        for (int i = 1; i < l.points.size(); i++)
//            lines.add(new Line(l.points.get(i - 1), l.points.get(i)));
//
//        for (Line line : lines) {
//            Segment s =
//        }
    }

    public static Segment koch(Segment segment, @NonNull Shape shape, int splits, int a, int b) {
        if (segment == null || segment.points == null || segment.points.size() < 2)
            return null;

        Segment result = null;
        List<Line> lines = new ArrayList<>();
        for (int i = 1; i < segment.points.size(); i++)
            lines.add(new Line(segment.points.get(i - 1), segment.points.get(i)));

        for (Line line : lines) {
            Segment s = koch(line, shape, splits, a, b);
            if (!s.isEmpty()) {
                if (result == null)
                    result = new Segment(s.points);
                else
                    result.concat(s);
            }
        }

        return result;
    }

    public Segment concat(Segment segment) {
        if (segment == null || segment.isEmpty())
            return this;

        if (this.isEmpty()) {
            this.points.addAll(segment.points);
        } else {
//            if (this.points.get(this.points.size() - 1) == segment.points.get(0)) {
//                this.points.remove(this.points.size() - 1);
//                this.points.addAll(segment.points);
//            }
            this.points.addAll(segment.points);
        }

        return this;
    }

    public boolean isEmpty() {
        return this.points == null || this.points.isEmpty();
    }

}
