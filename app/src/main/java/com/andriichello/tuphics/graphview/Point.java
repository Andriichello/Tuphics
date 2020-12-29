package com.andriichello.tuphics.graphview;


import androidx.annotation.NonNull;

/**
 * Point encapsulates a pair of double values representing a point
 * in the x,y-plane using world coordinates.
 */
public class Point implements Comparable<Point>
  {
    private double x;
    private double y;

    public Point(double x, double y)
      {
        this.x = x;
        this.y = y;
      }

    double getX()
      {
        return x;
      }

    double getY()
      {
        return y;
      }

    @Override
    public int compareTo(@NonNull Point other)
      {
        if (x < other.x)
            return -1;
        else if (x == other.x)
            return 0;
        else
            return 1;
      }
  }
