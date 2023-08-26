package com.example.projekt_geo.Model;

import java.util.Arrays;

public class TriangleMesh {
    public int points[];
    public TriangleMesh(int p0, int p1, int p2)
    {
        points = new int[3];
        points[0] = p0;
        points[1] = p1;
        points[2] = p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriangleMesh that = (TriangleMesh) o;
        return Arrays.equals(points, that.points);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }
}
