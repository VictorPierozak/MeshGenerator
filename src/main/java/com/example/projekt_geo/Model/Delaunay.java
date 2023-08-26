package com.example.projekt_geo.Model;

import java.util.*;

import static java.lang.Math.pow;

public class Delaunay {
    List<Point> points;

    public Delaunay(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public ArrayList<TriangleMesh> generateMesh()
    {
        HashMap<String, TriangleMesh> result = new HashMap<>();

        double xmin = points.get(0).getX();
        double xmax = points.get(0).getX();
        double ymin = points.get(0).getY();
        double ymax = points.get(0).getY();
        for(int i = 1; i < points.size(); i++)
        {
            if(points.get(i).getX() < xmin) xmin = points.get(i).getX();
            if(points.get(i).getX() > xmax) xmax = points.get(i).getX();
            if(points.get(i).getY() < ymin) ymin = points.get(i).getY();
            if(points.get(i).getY() > ymax) ymax = points.get(i).getY();
        }

        Point pt0 = new Point(xmin-1000, ymin-1000);
        Point pt1 = new Point(xmax+1000, ymin-1000);
        Point pt2 = new Point((xmin+xmax)/2, ymax+1000);
        points.add(pt0);
        points.add(pt1);
        points.add(pt2);

        TriangleMesh superTriangle = new TriangleMesh(points.indexOf(pt0),points.indexOf(pt1),points.indexOf(pt2));
        result.put(getTriangleKey(superTriangle), superTriangle);

        for(int i = 0; i < points.size(); i++)
        {
            Point p0 = points.get(i);
            LinkedList<TriangleMesh> toRemove = new LinkedList<>();
            LinkedList<EdgeMesh> edgeBuffer = new LinkedList<>();

            for(Map.Entry<String, TriangleMesh> et0: result.entrySet())
            {
                TriangleMesh t0 = et0.getValue();
                Circle c0 = Circle.findCircle(points.get(t0.points[0]), points.get(t0.points[1]), points.get(t0.points[2]));
                if( pow(p0.getX()-c0.center.getX(), 2) + pow(p0.getY()-c0.center.getY(),2) <= pow(c0.radius,2))
                {
                    toRemove.add(t0);
                    EdgeMesh em1 = new EdgeMesh(t0.points[0], t0.points[1]);
                    if(edgeBuffer.contains(em1) == true)
                        edgeBuffer.remove(em1);
                    else edgeBuffer.add(em1);

                    EdgeMesh em2 = new EdgeMesh(t0.points[0], t0.points[2]);
                    if(edgeBuffer.contains(em2) == true)
                        edgeBuffer.remove(em2);
                    else edgeBuffer.add(em2);

                    EdgeMesh em3 = new EdgeMesh(t0.points[1], t0.points[2]);
                    if(edgeBuffer.contains(em3) == true)
                        edgeBuffer.remove(em3);
                    else edgeBuffer.add(em3);

                }
            }

            for(TriangleMesh t0: toRemove)
            {
                result.remove(getTriangleKey(t0));
            }

            for(EdgeMesh edge : edgeBuffer)
            {
                TriangleMesh triangle  = new TriangleMesh(edge.p0, edge.p1, i);
                result.put(getTriangleKey(triangle), triangle);
            }

        }

        LinkedList<TriangleMesh> toRemove = new LinkedList<>();

        for(Map.Entry<String, TriangleMesh> et: result.entrySet())
        {
            TriangleMesh t = et.getValue();
            for(int i = 0; i < 3; i++) {
                if (t.points[0] == superTriangle.points[i] || t.points[1] == superTriangle.points[i] ||
                        t.points[2] == superTriangle.points[i]) {
                    toRemove.add(t);
                    break;
                }
            }
        }

        for(TriangleMesh t: toRemove)
            result.remove(getTriangleKey(t));

        ArrayList<TriangleMesh> res =  new ArrayList<>();
        for( Map.Entry<String, TriangleMesh> et : result.entrySet())
            res.add(et.getValue());

        return res;
    }

    String getTriangleKey(TriangleMesh t0)
    {
        String s0 =  new String();
        Arrays.sort(t0.points);
        for(int i = 0; i < 3; i++)
        s0 = s0.concat(String.valueOf(t0.points[i]));
        return s0;
    }
}
