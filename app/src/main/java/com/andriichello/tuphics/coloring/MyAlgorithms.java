package com.andriichello.tuphics.coloring;

import android.database.AbstractWindowedCursor;
import android.graphics.Bitmap;
import android.graphics.Color;

public class MyAlgorithms {

    public static Bitmap applyHSV(Bitmap bitmap, HSVColor hsvColor, int sensitivity) {
        if (bitmap == null || hsvColor == null)
            return null;

        if (sensitivity <= 0)
            sensitivity = 30;

        Bitmap bm = bitmap.copy(Bitmap.Config.RGB_565, true);

        int width = bm.getWidth();
        int height = bm.getHeight();

        int size = width * height;
        int[] pixels = new int[size];

        int top = 0, left = 0;
        int offset = 0, stride = bm.getWidth();

        bm.getPixels(pixels, offset, stride, top, left, width, height);

        int pixel = 0;
        int alpha = 0;
        RGBColor rgb = new RGBColor();
        for (int i = 0; i < size; i++) {
            pixel = pixels[i];
            alpha = Color.alpha(pixel);

            rgb.setRGB(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
            HSVColor hsv = rgb.toHSV();

            for (int j = sensitivity; (j - sensitivity) <= 360; j += sensitivity) {
                if (hsvColor.getH() >= j - sensitivity && hsvColor.getH() < j) {
                    if (hsv.getH() >= j - sensitivity && hsv.getH() < j) {
                        hsv.setHSV(hsv.getH(), hsvColor.getS(), hsvColor.getV());

                        pixels[i] = Color.HSVToColor(alpha, hsv.toFloats());
                    }
                }
            }

//            for (int j = 0; j <= 360; j += sensitivity) {
//                if (hsv.getH() >= hsvColor.getH() - sensitivity / 2.0 && hsv.getH() < hsvColor.getH() + sensitivity / 2.0) {
//                    hsv.setHSV(hsv.getH(), hsvColor.getS(), hsvColor.getV());
//
//                    pixels[i] = Color.HSVToColor(alpha, hsv.toFloats());
//                }
//            }
        }
        bm.setPixels(pixels, offset, stride, top, left, width, height);
        return bm;
    }
}
