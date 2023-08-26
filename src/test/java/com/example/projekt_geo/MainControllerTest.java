package com.example.projekt_geo;

import com.example.projekt_geo.Model.PointINT;
import com.example.projekt_geo.Model.Polygon;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class MainControllerTest {

    MainController mainController = new MainController();

    @Test
    void loadMeshFromFile() throws IOException {
        mainController.loadMeshFromFile();
        PointINT p0 = mainController.nodes.get(4);
        Polygon pol0 = mainController.mesh.get(1);

        assertEquals(new PointINT(0,1), p0);
        ArrayList<Integer> pointsId = new ArrayList<>();
        pointsId.add(1); pointsId.add(2); pointsId.add((3)); pointsId.add(4);

        for(int i = 0; i < pointsId.size(); i++)
            assertEquals(pointsId.get(i), pol0.getVertecies().get(i) );
    }

    @Test
    void stringToPoint() {
        String tmp = new String("0 10 20");
        assertEquals(new PointINT(10,20), mainController.stringToPoint(tmp));
    }

    @Test
    void stringToPolygon() {
        String tmp = new String("0 1 3 4 2 5");
        ArrayList<Integer> pointsId = new ArrayList<>();
        pointsId.add(1); pointsId.add(3); pointsId.add((4)); pointsId.add(2); pointsId.add(5);
        ArrayList<Integer> isGood = mainController.stringToPolygon(tmp).getVertecies();

        for(int i = 0; i < pointsId.size(); i++)
            assertEquals(pointsId.get(i), isGood.get(i) );
    }

    @Test
    void saveMeshToFile() {
    }
}