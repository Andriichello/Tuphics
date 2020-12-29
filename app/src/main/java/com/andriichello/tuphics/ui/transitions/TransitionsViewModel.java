package com.andriichello.tuphics.ui.transitions;

import android.graphics.PointF;
import android.icu.number.Scale;
import android.util.Range;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andriichello.tuphics.types.Pair;
import com.andriichello.tuphics.types.Shape;

import java.util.ArrayList;
import java.util.List;

public class TransitionsViewModel extends ViewModel {
    public static final String HELP_URL = "https://en.wikipedia.org/wiki/Affine_transformation";

    public static final int SCALE_MIDDLE_VALUE = 5;
    public static final int ROTATION_MIN_VALUE = 0, ROTATION_MAX_VALUE = 360;

    public static final Range<Integer> SCALE_RANGE = new Range<>(1, SCALE_MIDDLE_VALUE * 2);
    public static final Range<Integer> ROTATION_RANGE = new Range<>(ROTATION_MIN_VALUE, ROTATION_MAX_VALUE);
    public static final Range<Character> ANCHORS_RANGE = new Range<>('A', 'D');

    private MutableLiveData<Integer> scale;
    private MutableLiveData<Integer> rotation;
    private MutableLiveData<Character> anchor;

    private MutableLiveData<List<Pair<String, Pair<Float, Float>>>> anchors;

    public TransitionsViewModel() {
        this.scale = new MutableLiveData<>(SCALE_MIDDLE_VALUE);
        this.rotation = new MutableLiveData<>(ROTATION_RANGE.getLower());
        this.anchor = new MutableLiveData<>(ANCHORS_RANGE.getLower());

        List<Pair<String, Pair<Float, Float>>> anchors = new ArrayList<>();
        for (char c = TransitionsViewModel.ANCHORS_RANGE.getLower(); c <= TransitionsViewModel.ANCHORS_RANGE.getUpper(); c++)
            anchors.add(new Pair<>(String.valueOf(c), null));
        this.anchors = new MutableLiveData<>(anchors);
    }

    public MutableLiveData<Integer> getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        if (SCALE_RANGE.contains(scale))
            this.scale.setValue(scale);
    }

    public MutableLiveData<Integer> getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        if (ROTATION_RANGE.contains(rotation))
            this.rotation.setValue(rotation);
    }

    public MutableLiveData<Character> getAnchor() {
        return anchor;
    }

    public void setAnchor(Character anchor) {
        if (ANCHORS_RANGE.contains(anchor))
            this.anchor.setValue(anchor);
    }

    public Pair<Float, Float> getAnchorPoint() {
        if (anchors.getValue() == null)
            return null;

        Pair<Float, Float> a = new Pair<>(0f, 0f);
        for (int i = 0; anchors.getValue() != null && i < anchors.getValue().size(); i++) {
            if (anchors.getValue().get(i).first.equals(anchor.getValue()) && anchors.getValue().get(i).second != null) {
                a.first = anchors.getValue().get(i).second.first;
                a.second = anchors.getValue().get(i).second.second;
                break;
            }
        }

        return a;
    }

    public PointF getAnchorPointF() {
        if (anchors.getValue() == null)
            return null;

        PointF a = null;
        for (int i = 0; anchors.getValue() != null && i < anchors.getValue().size(); i++) {
            if (anchors.getValue().get(i).first.equals(anchor.getValue().toString()) && anchors.getValue().get(i).second != null) {
                a = new PointF(anchors.getValue().get(i).second.first, anchors.getValue().get(i).second.second);
                break;
            }
        }

        return a;
    }

    public MutableLiveData<List<Pair<String, Pair<Float, Float>>>> getAnchors() {
        return anchors;
    }

    public void setAnchors(List<Pair<String, Pair<Float, Float>>> anchors) {
        if (anchors != null && !anchors.isEmpty()) {
            anchors.removeIf(pair -> pair.first == null);

            // making sure that list contains only unique values
            for (int i = 0; i < anchors.size(); i++) {
                for (int j = i + 1; j < anchors.size(); j++) {
                    if (anchors.get(i).first.equals(anchors.get(j).first)) {
                        anchors.remove(j);
                        j--;
                    }
                }
            }

            this.anchors.setValue(anchors);
        }
    }
}