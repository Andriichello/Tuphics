package com.andriichello.tuphics.types;

import com.andriichello.tuphics.ui.transitions.TransitionsViewModel;

import java.util.Arrays;

public class TransitionMatrix {
    public double[][] matrix = new double[3][3];

    public TransitionMatrix(double value) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j)
                    matrix[i][j] = 1.0;
                else
                    matrix[i][j] = value;
            }
        }
    }

    public TransitionMatrix() {
        this(0.0);
    }

    public TransitionMatrix(TransitionMatrix m) {
        this(0.0);

        if (m != null) {
            for (int i = 0; i < m.matrix.length && i < matrix.length; i++) {
                if (m.matrix[i] == null)
                    continue;

                for (int j = 0; j < matrix[i].length && j < m.matrix[i].length; j++) {
                    matrix[i][j] = m.matrix[i][j];
                }
            }
        }
    }

    public TransitionMatrix(double[][] m) {
        this(0.0);

        if (m != null) {
            for (int i = 0; i < m.length && i < matrix.length; i++) {
                if (m[i] == null)
                    continue;

                for (int j = 0; j < matrix[i].length && j < m[i].length; j++) {
                    matrix[i][j] = m[i][j];
                }
            }
        }
    }

    public static double[][] multiply(double[][] a, double[][] b) {
        if (a == null || a.length == 0 || b == null || b.length == 0 || a[0].length != b.length)
            return null;

        double[][] result = new double[a.length][b[0].length];
        for (int row = 0; row < a.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = 0;

                for (int i = 0; i < a[row].length && i < b.length; i++) {
                    double v1 = a[row][i];
                    double v2 = b[i][row];
                    double r = v1 * v2;

                    result[row][col] += a[row][i] * b[i][row];
                }
            }

        }
        return result;
    }

    public static double[][] multiply(TransitionMatrix a, TransitionMatrix b) {
        if (a == null || b == null)
            return null;

        return multiply(a.matrix, b.matrix);
    }

    public static String toOutput(double[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return "Matrix { empty }";

        StringBuilder output = new StringBuilder();
        output.append("Matrix {\n");
        for (double[] row : matrix) {
            int counter = 0;
            for (double col : row) {
                if (counter > 0)
                    output.append("\t").append(col);
                else
                    output.append(col);
                counter++;
            }
            output.append("\n");
        }
        output.append("}");
        return output.toString();
    }

    public double[] toCoordinates(double[] coordinates) {
        if (coordinates == null || coordinates.length == 0)
            return null;

        double[] results = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            results[i] = 0;

            for (int j = 0; j < coordinates.length && j < matrix.length && i < matrix[j].length; j++) {
                results[i] += matrix[j][i] * coordinates[j];
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return toOutput(matrix);
    }

    public static class Builder {
        public static TransitionMatrix transition(double m, double n) {
            TransitionMatrix r = new TransitionMatrix(0.0);
            r.matrix[r.matrix.length - 1][0] = m;
            r.matrix[r.matrix.length - 1][1] = n;
            return r;
        }

        public static TransitionMatrix dilation(double a, double d) {
            TransitionMatrix r = new TransitionMatrix(0.0);
            r.matrix[0][0] = a;
            r.matrix[1][1] = d;
            return r;
        }

        public static TransitionMatrix rotation(double radians) {
            TransitionMatrix r = new TransitionMatrix(0.0);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            r.matrix[0][0] = cos;
            r.matrix[0][1] = sin;

            r.matrix[1][0] = -sin;
            r.matrix[1][1] = cos;
            return r;
        }
    }
}
