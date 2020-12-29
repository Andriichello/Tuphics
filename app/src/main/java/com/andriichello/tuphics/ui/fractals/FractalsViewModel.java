package com.andriichello.tuphics.ui.fractals;

import android.util.Range;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andriichello.tuphics.types.Shape;

import java.util.HashMap;

public class FractalsViewModel extends ViewModel {
    public static final String HELP_URL = "https://en.wikipedia.org/wiki/Fractal";

    private static final int SPLITS_COUNT = 18;
    private static final Range<Integer> PROPORTION_RANGE = new Range<>(1, SPLITS_COUNT / 2 - 1);
    private static final Range<Integer> ITERATIONS_RANGE = new Range<>(0, 9);

    private MutableLiveData<Integer> proportion;
    private MutableLiveData<Integer> iterations;
    private MutableLiveData<Shape> shape;

    public FractalsViewModel() {
        shape = new MutableLiveData<>();
        shape.setValue(Shape.Triangle);

        iterations = new MutableLiveData<>();
        iterations.setValue(ITERATIONS_RANGE.getLower());

        proportion = new MutableLiveData<>();
        proportion.setValue(PROPORTION_RANGE.getLower());
    }

    public static int getSplitsCount() {return SPLITS_COUNT; }

    public static Range<Integer> getProportionRange() {
        return PROPORTION_RANGE;
    }

    public static Range<Integer> getIterationsRange() {
        return ITERATIONS_RANGE;
    }

    public MutableLiveData<Integer> getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        if (PROPORTION_RANGE.contains(proportion))
            this.proportion.setValue(proportion);
    }

    public MutableLiveData<Integer> getIterations() {
        return iterations;
    }

    public void setIterations(Integer iterations) {
        if (ITERATIONS_RANGE.contains(iterations))
            this.iterations.setValue(iterations);
    }

    public MutableLiveData<Shape> getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape.setValue(shape);
    }
}
