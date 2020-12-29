package com.andriichello.tuphics.types;

import android.graphics.PointF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Fractal {
    public Segment shape;
    public Segment fractal;

    public Fractal(@NonNull Segment shape) {
        this.shape = new Segment(shape.points);
        this.fractal = new Segment(shape.points);
    }


}
