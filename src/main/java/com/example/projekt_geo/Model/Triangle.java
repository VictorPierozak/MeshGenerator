package com.example.projekt_geo.Model;

import java.util.Arrays;

public class Triangle {
    PointINT[] points;
    public Triangle(PointINT a0, PointINT a1, PointINT a2)
    {
        points = new PointINT[3];
        points[0] = a0;
        points[1] = a1;
        points[2] = a2;
    }
    public PointINT[] getPoints() {
        return points;
    }

    public void setPoints(PointINT[] points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Arrays.equals(points, triangle.points);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }
}
