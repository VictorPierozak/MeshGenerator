package com.example.projekt_geo.Model;

import javafx.scene.image.PixelReader;

import java.util.ArrayList;
import java.util.LinkedList;

public class RegularMesh {
    private ArrayList<PointINT> nodes;
    private ArrayList<Polygon> elements;

    //

    private ArrayList<Integer> xPos;
    private ArrayList<Integer> yPos;

    //

    private int densityX;
    private int densityY;

    private int background;
    private int phase;
    //


    public RegularMesh()
    {
        xPos = new ArrayList<>();
        yPos = new ArrayList<>();
        nodes = new ArrayList<>();
        elements = new ArrayList<>();
    }

    public RegularMesh(int xdensity, int ydensity)
    {
        this.xPos = new ArrayList<>();
        this.yPos = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.densityX = xdensity;
        this.densityY = ydensity;
    }

    //

    public int getDensityX() {
        return densityX;
    }

    public void setDensityX(int densityX) {
        this.densityX = densityX;
    }

    public int getDensityY() {
        return densityY;
    }

    public void setDensityY(int densityY) {
        this.densityY = densityY;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    //
    public boolean construct(MyImage myImage)
    {
        int height = (int) myImage.getImage().getHeight();
        int width = (int) myImage.getImage().getWidth();
        if( initializeNodes(height, width) == false)
            return false;
        createPoints();
        createElements();
        return true;
    }
    public boolean constructEV(MyImage myImage)
    {
        int height = (int) myImage.getImage().getHeight();
        int width = (int) myImage.getImage().getWidth();
        if( initializeNodes(height, width) == false)
            return false;

        createPoints();
        createElements();

        elementsValidation(myImage);
        return true;
    }

    public boolean constructQV(MyImage myImage)
    {
        int height = (int) myImage.getImage().getHeight();
        int width = (int) myImage.getImage().getWidth();
        if( initializeNodes(height, width) == false)
            return false;
        quickValidation(myImage);
        createPoints();
        createElements();
        return true;
    }

    public ArrayList<PointINT> selectedPoints(MyImage myImage)
    {
        int height = (int) myImage.getImage().getHeight();
        int width = (int) myImage.getImage().getWidth();
        if( initializeNodes(height, width) == false)
            return null;
        createPoints();
        pointValidation(myImage);
        return nodes;
    }

    private boolean initializeNodes(int height, int width)
    {
        if(densityY == 0 || densityX == 0) return false;
        //Obliczenie liczby podziałów na osi 0X i 0Y
        int xNum = width / densityX;
        int yNum = height / densityY;

        //Generacja tablic zawierających współrzędne podziałów na osi 0X i 0Y
        for(int i = 0; i < xNum; i++)
            xPos.add(i*densityX);
        for(int i = 0; i < yNum; i++)
            yPos.add(i*densityY);

        return true;
    }

    private void quickValidation(MyImage image)
    {
        LinkedList<Integer> toRemove = new LinkedList<>();
        int backgroundCounter = 0;
        int phaseCounter = 0;
        PixelReader pixels = image.getImage().getPixelReader();

        for(int x = 1; x < xPos.size() - 1; x++)
        {
            Integer pos = xPos.get(x);
            backgroundCounter = 0;
            phaseCounter = 0;

            for(int y = 1; y < yPos.size()-1; y++)
            {
                Integer perPos = yPos.get(y);
                int c = pixels.getArgb(pos.intValue(),perPos.intValue());
                if(c == background)
                    backgroundCounter++;
                else phaseCounter++;
            }

            if( phaseCounter == 0)
                toRemove.add(pos);
        }

        xPos.removeAll(toRemove);

        //

        toRemove.clear();


        for(int y = 1; y < yPos.size()-1; y++)
        {
            Integer perPos = yPos.get(y);
            backgroundCounter = 0;
            phaseCounter = 0;

            for(int x = 1; x < xPos.size() - 1; x++)
            {
                Integer pos = xPos.get(x);
                int c = pixels.getArgb(pos.intValue(),perPos.intValue());
                if(c == background)
                    backgroundCounter++;
                else phaseCounter++;
            }

            if(phaseCounter == 0)
                toRemove.add(perPos);
        }

        yPos.removeAll(toRemove);
    }

    private void elementsValidation(MyImage myImage)
    {
        PixelReader pixels = myImage.getImage().getPixelReader();
        LinkedList<Polygon> toRemove = new LinkedList<>();
        boolean flag = true;
        for(Polygon element: elements)
        {
            flag = true;
            ArrayList<Integer> vertecies = element.getVertecies();
            for(Integer i: vertecies)
                if( pixels.getArgb(nodes.get(i).getX(), nodes.get(i).getY()) != background )
                    flag = false;
            if(flag) toRemove.add(element);
        }

        elements.removeAll(toRemove);
    }

    private void pointValidation(MyImage myImage)
    {
        PixelReader pixels = myImage.getImage().getPixelReader();
        LinkedList<PointINT> toRemove = new LinkedList<>();
        for(PointINT p0: nodes)
        {
            if(myImage.getImage().getWidth() - p0.getX() < 50 ||
            myImage.getImage().getHeight() - p0.getY() < 50 || p0.getX() < 50 || p0.getY() < 50) continue;
            if(pixels.getArgb(p0.getX(), p0.getY()) == background)
                toRemove.add(p0);
        }
        nodes.removeAll(toRemove);
    }

    private void createPoints()
    {
        int xNum = xPos.size();
        int yNum = yPos.size();
        for(int x = 0; x < xNum; x++)
            for(int y = 0; y < yNum ; y++)
                nodes.add(new PointINT(xPos.get(x), yPos.get(y)));
    }

    private void createElements()
    {
        int xNum = xPos.size();
        int yNum = yPos.size();
        for(int column = 0; column < xNum - 1 ; column++)
            for(int row = 0; row < yNum - 1 ; row++)
            {
                Polygon element = new Polygon();
                element.addVertex(column * yNum + row );
                element.addVertex((column+1) * (yNum) + row );
                element.addVertex((column+1) * (yNum) + row + 1);
                element.addVertex(column * yNum + row + 1);
                elements.add(element);
            }
    }

    public void reset()
    {
        xPos.clear();
        yPos.clear();
        elements.clear();
        nodes.clear();
    }

    //

    public ArrayList<PointINT> getNodes() {
        return nodes;
    }

    public ArrayList<Polygon> getElements() {
        return elements;
    }
}
