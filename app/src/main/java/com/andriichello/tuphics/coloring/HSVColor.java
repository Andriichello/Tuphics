package com.andriichello.tuphics.coloring;

public class HSVColor {
    public static final double MIN_H = 0, MAX_H = 360;
    public static final double MIN_S = 0, MAX_S = 100;
    public static final double MIN_V = 0, MAX_V = 100;

    double H, S, V;

    public HSVColor() {
        setHSV(0, 0, 0);
    }

    public HSVColor(double h, double s, double v) {
        setHSV(h, s, v);
    }

    public HSVColor(HSVColor hsv) {
        this();

        if (hsv != null) {
            this.H = hsv.H;
            this.S = hsv.S;
            this.V = hsv.V;
        }
    }

    public double getH() {
        return H;
    }

    public void setH(double h) {
        if (h < MIN_H)
            this.H = MIN_H;
        else if (h > MAX_H)
            this.H = MAX_H;
        else
            this.H = h;
    }

    public double getS() {
        return S;
    }

    public void setS(double s) {
        if (s < MIN_S)
            this.S = MIN_S;
        else if (s > MAX_S)
            this.S = MAX_S;
        else
            this.S = s;
    }

    public double getV() {
        return V;
    }

    public void setV(double v) {
        if (v < MIN_V)
            this.V = MIN_V;
        else if (v > MAX_V)
            this.V = MAX_V;
        else
            this.V = v;
    }

    public void setHSV(double h, double s, double v) {
        setH(h);
        setS(s);
        setV(v);
    }

    public void setHSV(HSVColor hsv) {
        if (hsv != null)
            setHSV(hsv.H, hsv.S, hsv.V);
    }

    public RGBColor toRGB() {
        double s = ((float) S) / MAX_S;
        double v = ((float) V) / MAX_V;
        double C = s * v;
        double X = C * (1 - Math.abs((H / 60.0) % 2) - 1);
        double m = v - C;

        double r, g, b;
        if (H >= 0 && H < 60) {
            r = C;
            g = X;
            b = 0;
        } else if (H >= 60 && H < 120) {
            r = X;
            g = C;
            b = 0;
        } else if (H >= 120 && H < 180) {
            r = 0;
            g = C;
            b = X;
        } else if (H >= 180 && H < 240) {
            r = 0;
            g = X;
            b = C;
        } else if (H >= 240 && H < 300) {
            r = X;
            g = 0;
            b = C;
        } else {
            r = C;
            g = 0;
            b = X;
        }
        double R = (double) ((r + m) * RGBColor.MAX);
        double G = (double) ((g + m) * RGBColor.MAX);
        double B = (double) ((b + m) * RGBColor.MAX);
        return new RGBColor(R, G, B);
    }

    public float[] toFloats() {
        return new float[]{(float) H, (float) (S / MAX_S), (float) (V / MAX_V)};
    }

    @Override
    public String toString() {
        return "HSV(" + H +
                ", " +  S +
                ", " + V +
                ')';
    }
}
