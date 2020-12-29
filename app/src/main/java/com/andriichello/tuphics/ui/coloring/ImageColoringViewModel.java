package com.andriichello.tuphics.ui.coloring;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andriichello.tuphics.coloring.HSVColor;

public class ImageColoringViewModel extends ViewModel {
    public static final String HELP_URL = "https://en.wikipedia.org/wiki/HSL_and_HSV";


    private MutableLiveData<Uri> imageUri = new MutableLiveData<>(null);
    private MutableLiveData<Double> hue = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> saturation = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> value = new MutableLiveData<>(0.0);
    private MutableLiveData<Integer> sensitivity = new MutableLiveData<>(60);

    private MutableLiveData<Bitmap> inputBitmap = new MutableLiveData<>(null);
    private MutableLiveData<Bitmap> outputBitmap = new MutableLiveData<>(null);

    public MutableLiveData<Uri> getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri.setValue(imageUri);
    }

    public MutableLiveData<Double> getHue() {
        return hue;
    }

    public void setHue(Double hue) {
        this.hue.setValue(hue);
    }

    public MutableLiveData<Double> getSaturation() {
        return saturation;
    }

    public void setSaturation(Double saturation) {
        this.saturation.setValue(saturation);
    }

    public MutableLiveData<Double> getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value.setValue(value);
    }

    public MutableLiveData<Integer> getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(Integer sensitivity) {
        this.sensitivity.setValue(sensitivity);
    }

    public MutableLiveData<Bitmap> getInputBitmap() {
        return inputBitmap;
    }

    public void setInputBitmap(Bitmap inputBitmap) {
        this.inputBitmap.setValue(inputBitmap);
    }

    public MutableLiveData<Bitmap> getOutputBitmap() {
        return outputBitmap;
    }

    public void setOutputBitmap(Bitmap outputBitmap) {
        this.outputBitmap.setValue(outputBitmap);
    }

    public @NonNull HSVColor getHSVColor() {
        return new HSVColor(hue.getValue(), saturation.getValue(), value.getValue());
    }
}