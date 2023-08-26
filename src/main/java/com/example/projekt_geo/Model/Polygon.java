package com.example.projekt_geo.Model;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Integer> vertecies;

    public Polygon() {
        vertecies = new ArrayList<>();
    }

    public Polygon(ArrayList<Integer> vertecies) {
        this.vertecies = vertecies;
    }

    public ArrayList<Integer> getVertecies() {
        return vertecies;
    }

    public void setVertecies(ArrayList<Integer> vertecies) {
        this.vertecies = vertecies;
    }

    public void addVertex(Integer p0)
    {
        vertecies.add(p0);
    }
}
