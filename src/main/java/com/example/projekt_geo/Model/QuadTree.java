package com.example.projekt_geo.Model;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class QuadTree {

    //Klasa węzła, reprezentuje pojedynczy obszar wyznaczone przez zakres na osiach 0X i 0Y
    public class QuadTreeNode {

        //Zakres na osi 0X
        public int _Xrange[];

        //Zakres na osi 0Y
        public int _Yrange[];

        //Synowie węzła
        QuadTreeNode _leftUp;
        QuadTreeNode _leftDown;
        QuadTreeNode _rightUp;
        QuadTreeNode _rightDown;

        //Rodzic węzła
        QuadTreeNode _parent;

        //Kolor obszaru - wartość jest istotna tylko dla liści drzewa, które
        //reprezentują podobszary w jednym kolorze
        public int _color;
        public QuadTreeNode(QuadTreeNode parent, int x0, int x1, int y0, int y1) {
            _Xrange = new int[2];
            _Xrange[0] = x0; _Xrange[1] = x1;
            _Yrange = new int[2];
            _Yrange[0] = y0; _Yrange[1] = y1;
            _leftUp = null;
            _leftDown = null;
            _rightDown = null;
            _rightUp = null;
            _parent = parent;

            _color = Integer.MIN_VALUE;
        }

        public QuadTreeNode(QuadTreeNode parent, int x0, int x1, int y0, int y1, int color) {
            _Xrange = new int[2];
            _Xrange[0] = x0; _Xrange[1] = x1;
            _Yrange = new int[2];
            _Yrange[0] = y0; _Yrange[1] = y1;

            _leftUp = null;
            _leftDown = null;
            _rightDown = null;
            _rightUp = null;

            _parent = parent;

            _color = color;
        }

        public void setLeftUp(QuadTreeNode l) {
            _leftUp = l;
        }
        public void setLeftDown(QuadTreeNode l) {
            _leftDown = l;
        }

        public void setRightUp(QuadTreeNode r) {
            _rightUp = r;
        }
        public void setRightDown(QuadTreeNode r) {
            _rightDown = r;
        }

        public QuadTreeNode getRightUp() {
            return _rightUp;
        }
        public QuadTreeNode getRightDown() {
            return _rightDown;
        }

        public QuadTreeNode getLeftUp() {
            return _leftUp;
        }
        public QuadTreeNode getLeftDown() {
            return _leftDown;
        }

    }

    //Korzeń drzewa
    QuadTreeNode _root;

    public QuadTree() {
        _root = null;
    }

    //Funkcja konstrująca drzewo na podstawie przekazanego obrazu
    public void construct(MyImage microImg, int stop)
    {
        _root = split(microImg, null, 0, (int) microImg.getImage().getWidth()-1, 0, (int) microImg.getImage().getHeight()-1, stop);
    }

    //Funkcja rekurencyjna, służąca do konstrukcji drzewa, dzieli zdjęcie na podobszary
    //dopóki nie będą one zawierały jednego koloru
    protected QuadTreeNode split(MyImage microImg, QuadTreeNode parent, int x0, int x1, int y0, int y1, int stop)
    {
        QuadTreeNode root = null;

        //Jeżeli obszar ma rozmiar jednego piksela lub zawiera wyłącznie jeden kolor
        //kończone jest wywoływanie rekurencyjne
        if( (Math.abs(x1-x0) <= stop && Math.abs(y1-y0) <= stop) ||
                microImg.isHomogenous(x0,x1,y0,y1))
        {
            root = new QuadTreeNode(parent, x0, x1, y0, y1, microImg.getImage().getPixelReader().getArgb(x0, y0));
            return  root;
        }

        //Utworzenie węzła
        root = new QuadTreeNode(parent, x0, x1, y0, y1);

        //Obliczenie środków boków obszaru wzdłuż obu osi
        int splitX = (x1+x0)/2;
        if( splitX == x0) splitX++;
        int splitY = (y1+y0)/2;
        if(splitY == y0) splitY++;

        // 3-4 (3+4)/2 = 3 3- 4-1 = 3

        //Podział na podobszary - utworzenie czterech synów węzła
        root.setLeftUp( split(microImg, root, x0, splitX-1, y0, splitY-1, stop));
        root.setRightUp( split(microImg, root, splitX, x1, y0, splitY-1, stop));
        root.setLeftDown( split(microImg, root, x0, splitX-1, splitY, y1, stop));
        root.setRightDown( split(microImg, root, splitX, x1, splitY, y1, stop));

        return root;
    }

    //Metoda zwracająca węzły zapisane w ArrayList
    public ArrayList<QuadTreeNode> toArrayList()
    {
        ArrayList<QuadTreeNode> al = new ArrayList<>();
        readElements(_root, al);
        return al;
    }

    //Metoda przechodząca po drzewie i zapisująca węzły do podanej w argumentach ArrayList
    private void readElements(QuadTreeNode node, ArrayList<QuadTreeNode> al)
    {
        if(node == null) return;
        if(node._leftDown == null && node._leftUp == null && node._rightDown == null && node._rightUp == null)
        {
            al.add(node);
        }
        else
        {
            readElements(node._leftDown, al);
            readElements(node._leftUp, al);
            readElements(node._rightDown, al);
            readElements(node._rightUp, al);
        }
    }
}