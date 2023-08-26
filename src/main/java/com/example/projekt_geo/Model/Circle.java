package com.example.projekt_geo.Model;

public class Circle {
    public double radius;
    public Point center;

    public Circle(double radius, Point center) {
        this.radius = radius;
        this.center = new Point(center.getX(), center.getY());
    }

    static public Circle findCircle(Point p1, Point p2, Point p3) {

        double x12 = p1.getX() - p2.getX();
        double x13 = p1.getX() - p3.getX();
        double y12 = p1.getY() - p2.getY();
        double y13 = p1.getY() - p3.getY();
        double y31 = p3.getY() - p1.getY();
        double y21 = p2.getY() - p1.getY();
        double x31 = p3.getX() - p1.getX();
        double x21 = p2.getX() - p1.getX();
// x1^2 - x3^2
        double sx13 = (Math.pow(p1.getX(), 2) -
                Math.pow(p3.getX(), 2));
// y1^2 - y3^2
        double sy13 = (Math.pow(p1.getY(), 2) -
                Math.pow(p3.getY(), 2));
        double sx21 = (Math.pow(p2.getX(), 2) -
                Math.pow(p1.getX(), 2));
        double sy21 = (Math.pow(p2.getY(), 2) -
                Math.pow(p1.getY(), 2));
        double f = ((sx13) * (x12)
                + (sy13) * (x12)
                + (sx21) * (x13)
                + (sy21) * (x13))
                / (2 * ((y31) * (x12) - (y21) * (x13)));
        double g = ((sx13) * (y12)
                + (sy13) * (y12)
                + (sx21) * (y13)
                + (sy21) * (y13))
                / (2 * ((x31) * (y12) - (x21) * (y13)));
        double c = -Math.pow(p1.getX(), 2) - Math.pow(p1.getY(), 2) -
                2 * g * p1.getX() - 2 * f * p1.getY();
        double h = -g;
        double k = -f;
        double sqr_of_r = h * h + k * k - c;
        double r = Math.sqrt(sqr_of_r);
        return new Circle(r, new Point(h, k));
    }
}
