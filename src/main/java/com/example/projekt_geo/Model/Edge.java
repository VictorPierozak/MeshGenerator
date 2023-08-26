package com.example.projekt_geo.Model;

public class Edge {
    public PointINT p0;
    public PointINT p1;
    public Edge(PointINT p0, PointINT p1) {
        this.p0 = p0;
        this.p1 = p1;
    }

    @Override
    public boolean equals(Object e0)
    {
        if(e0.getClass() != Edge.class) return false;
        Edge e = (Edge) e0;
        return ((p0 == e.p0)&&(p1 == e.p1));
    }
}
