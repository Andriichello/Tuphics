package com.andriichello.tuphics.coloring;

import android.util.Range;

public class RGBColor {
    public static final double MIN = 0;
    public static final double MAX = 255;

    private double R, G, B;

    public RGBColor() {
        setRGB(0, 0, 0);
    }

    public RGBColor(double r, double g, double b) {
        setRGB(r, g, b);
    }

    public RGBColor(RGBColor rgb) {
        this();

        if (rgb != null) {
            this.R = rgb.R;
            this.G = rgb.G;
            this.B = rgb.B;
        }
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        if (r < MIN)
            this.R = MIN;
        else if (r > MAX)
            this.R = MAX;
        else
            this.R = r;
    }

    public double getG() {
        return G;
    }

    public void setG(double g) {
        if (g < MIN)
            this.G = MIN;
        else if (g > MAX)
            this.G = MAX;
        else
            this.G = g;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        if (b < MIN)
            this.B = MIN;
        else if (b > MAX)
            this.B = MAX;
        else
            this.B = b;
    }

    public void setRGB(double r, double g, double b) {
        setR(r);
        setG(g);
        setB(b);
    }

    public void setRGB(RGBColor rgb) {
        if (rgb != null)
            setRGB(rgb.R, rgb.G, rgb.B);
    }

    public HSVColor toHSV() {
        // R, G, B values are divided by 255
        // to change the range from 0..255 to 0..1
        double r = R / 255.0;
        double g = G / 255.0;
        double b = B / 255.0;

        // h, s, v = hue, saturation, value
        double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b
        double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b
        double diff = cmax - cmin; // diff of cmax and cmin.
        double h = -1, s = -1;

        // if cmax and cmax are equal then h = 0
        if (cmax == cmin)
            h = 0;

            // if cmax equal r then compute h
        else if (cmax == r)
            h = (60 * ((g - b) / diff) + 360) % 360;

            // if cmax equal g then compute h
        else if (cmax == g)
            h = (60 * ((b - r) / diff) + 120) % 360;

            // if cmax equal b then compute h
        else if (cmax == b)
            h = (60 * ((r - g) / diff) + 240) % 360;

        // if cmax equal zero
        if (cmax == 0)
            s = 0;
        else
            s = (diff / cmax) * 100;

        // compute v
        double v = cmax * 100;
        return new HSVColor(h, s, v);
    }

    @Override
    public String toString() {
        return "RGB(" +
                (int)R +
                ", " + (int)G +
                ", " + (int)B +
                ')';
    }
}
