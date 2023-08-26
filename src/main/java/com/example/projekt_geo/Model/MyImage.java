package com.example.projekt_geo.Model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class MyImage {

    javafx.scene.image.Image _img;

    //Konstruktor - przyjmuje jako argument ścieżkę do pliku z obrazem
    public MyImage(String path)
    {
        _img = new Image(path);
    }
    public MyImage(Image img){_img = img;}

    public Image getImage()
    {
        return _img;
    }

    //Metoda sprawdzająca, czy prostokąt opsiany za pomocą dwóch przeciwległych
    //wierzchołków zawiera jedynie jeden kolor
    public boolean isHomogenous(int x0, int x1, int y0, int y1)
    {
        boolean flag = true;
        PixelReader pixelReader = _img.getPixelReader();
        int color = pixelReader.getArgb(x0,y0);
        for(int y = y0; y <= y1; y++)
        {
            for(int x =x0 ; x <= x1; x++)
            {
                if(pixelReader.getArgb(x,y) != color)
                {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}
