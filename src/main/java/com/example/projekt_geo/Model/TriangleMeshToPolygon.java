package com.example.projekt_geo.Model;

import java.util.ArrayList;

public class TriangleMeshToPolygon {
    public TriangleMeshToPolygon()
    {

    }

    public static ArrayList<Polygon> convert(ArrayList<TriangleMesh>  triangleMeshes)
    {
        ArrayList<Polygon> result = new ArrayList<>();
        for(TriangleMesh tm: triangleMeshes)
        {
            Polygon converted = new Polygon();
            converted.addVertex(tm.points[0]);
            converted.addVertex(tm.points[1]);
            converted.addVertex(tm.points[2]);
            result.add(converted);
        }

        return result;
    }
}
