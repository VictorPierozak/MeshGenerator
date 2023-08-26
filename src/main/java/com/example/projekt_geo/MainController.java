package com.example.projekt_geo;

import com.example.projekt_geo.Model.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainController implements EventHandler {

    // FXML //

    @FXML
    Canvas mainCanvas;
    @FXML
    ChoiceBox<String> meshOptions;
    @FXML
    TextField phaseOneFrac;
    @FXML
    TextField phaseTwoFrac;
    @FXML
    TextField imagePathField;
    @FXML
    Button loadButton;
    @FXML
    Button saveMeshButton;
    @FXML
    Button loadMeshButton;
    @FXML
    TextField meshFilePath;
    @FXML
    ChoiceBox<String> advancedOptions;
    @FXML
    TextField advancedField;
    @FXML
    Button advancedApply;

    // OPTIONS MESH ///
    final String STRUCTURED_ELEMENTS = "Structured - elements validation";
    final String QUAD_TREE = "Unstructured - quad tree";
    final String DELAUNAY = "Unstructured - Delaunay";

    // ADVANCED OPTIONS //
    final String OPTION_X_DENISTY = "X_DENISTY";
    final String OPTION_Y_DENISTY = "Y_DENISTY";
    final String OPTION_DELAUNAY_RESOLUTION = "DELAUNAY_RESOLUTION";

    // IMAGE //
    Image image;

    // MESH CONTAINTERS //
    List<Polygon> mesh;
    List<PointINT> nodes;

    // DRAWING //
    GraphicsContext graphicsContext;
    Color meshColor = Color.BLUE;

    // PHASE //

    int phase_one = -1;
    int phase_two = 0;

    // MESH FILES PARAMETERS //
    String pathMesh = "C:\\Users\\piero\\Desktop\\Projekt_Geo\\DataFiles\\exampleMesh.txt";
    String pathImage = "C:\\Users\\piero\\Desktop\\Projekt_Geo\\DataFiles\\test_1.bmp";

    final String VERTECIES_HEADER = new String("nodes");
    final String ELEMENTS_HEADER = new String("elements");
    final String SEPARATOR = new String(" ");

    //  REGULAR MESH PARAMETERS //

    int X_DENSITY = 10;
    int Y_DENSITY = 10;
    int DELAUNAY_RESOLUTION = 15;
    // KONSTRUKTOR //

    public MainController()
    {
    }

    // INICJALIZACJA //

    @FXML
    void initialize()
    {
        nodes = new ArrayList<>();
        mesh = new ArrayList<>();
        initializeMeshOptions();
        initializeAdvancedOptions();
        initializeCanvas();
    }

    void initializeAdvancedOptions()
    {
        advancedOptions.getItems().add(OPTION_X_DENISTY);
        advancedOptions.getItems().add(OPTION_Y_DENISTY);
        advancedOptions.getItems().add(OPTION_DELAUNAY_RESOLUTION);
        advancedOptions.setOnAction(this);
    }

    void initializeMeshOptions()
    {
        meshOptions.getItems().add(STRUCTURED_ELEMENTS);
        meshOptions.getItems().add(QUAD_TREE);
        meshOptions.getItems().add(DELAUNAY);
        meshOptions.setOnAction(this);
    }

    void initailizePhaseText()
    {
        phaseOneFrac.setEditable(false);
        phaseTwoFrac.setEditable(false);
    }

    void  initializeCanvas()
    {
        graphicsContext = mainCanvas.getGraphicsContext2D();
        graphicsContext.drawImage(image,0,0);
    }

    // WCZYTYWANIE SIATKI Z PLIKU //

    public void loadMeshFromFile() throws IOException {

        File file = new File(pathMesh);
        BufferedReader fileReader = new BufferedReader(new FileReader(file));

        String row;
        row = fileReader.readLine();
        row = fileReader.readLine();
        nodes = new ArrayList<>();
        while( row.compareTo(ELEMENTS_HEADER) != 0)
        {
            nodes.add(stringToPoint(row));
            row = fileReader.readLine();
        }

        mesh = new ArrayList<>();
        while(  (row = fileReader.readLine()) != null )
        {
            mesh.add(stringToPolygon(row));
        }

        fileReader.close();

        drawMesh();
    }

    public PointINT stringToPoint(String row)
    {
        // Row - "ID X Y"
        // Seperated - { "ID", "X", "Y" }
        String[] seperated = row.split(SEPARATOR);
        int x = Integer.parseInt(seperated[1]);
        int y = Integer.parseInt(seperated[2]);

        return new PointINT(x,y);
    }

    public Polygon stringToPolygon(String row)
    {
        String[] seperated = row.split(SEPARATOR);
        Polygon result = new Polygon();
        for(int i = 1; i < seperated.length; i++)
            result.addVertex(Integer.parseInt(seperated[i]));
        return result;
    }

    // ZAPIS SIATKI DO PLIKU //

    public void saveMeshToFile() throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(pathMesh);

        printWriter.println(VERTECIES_HEADER);
        for(int idx = 0; idx < nodes.size(); idx++)
        {
            printWriter.println(String.valueOf(idx) + String.valueOf(SEPARATOR) + String.valueOf(nodes.get(idx).getX()) +
                    SEPARATOR + String.valueOf(nodes.get(idx).getY()));
        }
        printWriter.println(ELEMENTS_HEADER);
        for(int idx = 0; idx < mesh.size(); idx++)
        {
            String row = new String();
            row += String.valueOf(idx) + SEPARATOR;
            for(Integer i: mesh.get(idx).getVertecies())
                row += String.valueOf(i) + SEPARATOR;
            printWriter.println(row);
        }

        printWriter.close();
    }

    // RYSOWANIE SIATKI //


    public void drawMesh()
    {
        graphicsContext.clearRect(0,0, mainCanvas.getHeight(), mainCanvas.getWidth());
        graphicsContext.setStroke(meshColor);
        graphicsContext.drawImage(image, 0,0);
        for(Polygon polygon: mesh)
        {
            ArrayList<PointINT> vertices = new ArrayList<>();
            for(Integer i: polygon.getVertecies()) {
                if (i == 9099) {
                    System.out.println("OK");
                }
                vertices.add(nodes.get(i));
            }
            double[] x = new double[vertices.size()];
            double[] y = new double[vertices.size()];
            for(int i = 0; i < vertices.size(); i++)
            {
                x[i] = (double) vertices.get(i).getX();
                y[i] = (double) vertices.get(i).getY();
            }
            graphicsContext.strokePolygon(x, y, vertices.size());
        }
    }

    public void redrawMesh()
    {
        String selected = meshOptions.getSelectionModel().getSelectedItem();
        meshOptions.getSelectionModel().clearSelection();
        meshOptions.getSelectionModel().select(selected);
    }

    // WCZYTYWANIE OBRAZU //

    public void loadImage()
    {
        image = new Image(pathImage);
        graphicsContext.drawImage(image, 0, 0);
        calcPhaseFraction();
    }

    // QUAD TREE //

    public void meshQuadTree()
    {
        QuadTree quadTree = new QuadTree();
        MyImage myImage = new MyImage(pathImage);
        quadTree.construct(myImage,1);
        ArrayList<QuadTree.QuadTreeNode> elements = quadTree.toArrayList();

        nodes = new ArrayList<>();
        mesh = new ArrayList<>();

        PointINT[] vert = new PointINT[4];
        // 0 --- 1
        // |     |
        // 3 --- 2
        for(QuadTree.QuadTreeNode qn: elements)
        {
            vert[0] = new PointINT(qn._Xrange[0], qn._Yrange[0]);
            vert[1] = new PointINT(qn._Xrange[1], qn._Yrange[0]);
            vert[2] = new PointINT(qn._Xrange[1], qn._Yrange[1]);
            vert[3] = new PointINT(qn._Xrange[0], qn._Yrange[1]);

            for(int i = 0; i < 4; i++)
                if(nodes.contains(vert[i]) == false)
                    nodes.add(vert[i]);
            Polygon polygon = new Polygon();
            for(int i = 0; i < 4; i++)
                polygon.addVertex(nodes.indexOf(vert[i]));
            mesh.add(polygon);
        }

    }

    public void regularMeshEV()
    {
        RegularMesh meshGenertor = new RegularMesh(X_DENSITY, Y_DENSITY);
        meshGenertor.setBackground(phase_one);
        meshGenertor.constructEV(new MyImage(pathImage));
        nodes = new ArrayList<>(meshGenertor.getNodes());
        mesh = new ArrayList<>(meshGenertor.getElements());
    }
    public void regularMeshQV()
    {
        RegularMesh meshGenertor = new RegularMesh(X_DENSITY, Y_DENSITY);
        meshGenertor.setBackground(phase_one);
        meshGenertor.constructQV(new MyImage(pathImage));
        nodes = new ArrayList<>(meshGenertor.getNodes());
        mesh = new ArrayList<>(meshGenertor.getElements());
    }

    public void delaunayMesh()
    {
        QuadTree quadTree = new QuadTree();
        quadTree.construct(new MyImage(image), DELAUNAY_RESOLUTION);
        ArrayList<QuadTree.QuadTreeNode> qnNodes = quadTree.toArrayList();

        PointINT[] vert = new PointINT[4];
        // 0 --- 1
        // |     |
        // 3 --- 2
        for(QuadTree.QuadTreeNode qn: qnNodes)
        {
            vert[0] = new PointINT(qn._Xrange[0], qn._Yrange[0]);
            vert[1] = new PointINT(qn._Xrange[1], qn._Yrange[0]);
            vert[2] = new PointINT(qn._Xrange[1], qn._Yrange[1]);
            vert[3] = new PointINT(qn._Xrange[0], qn._Yrange[1]);

            for(int i = 0; i < 4; i++)
                if(nodes.contains(vert[i]) == false)
                    nodes.add(vert[i]);
        }
       LinkedList<Point> converted = new LinkedList<>();
        for(PointINT pointINT: nodes)
            converted.add(new Point(pointINT.getX(), pointINT.getY()));
        Delaunay delaunay = new Delaunay(converted);
        mesh = TriangleMeshToPolygon.convert(delaunay.generateMesh());
    }

    // ADDITIONAL //

    void calcPhaseFraction()
    {
        PixelReader pixels = image.getPixelReader();
        int phaseOneCounter = 0;
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++)
                if(pixels.getArgb(x,y) == phase_one)
                    phaseOneCounter++;

        double phOneFrac = ((double) phaseOneCounter)/( (double) height*width);
        double phTwoFrac = 1 - phOneFrac;
        phaseOneFrac.setText(String.valueOf(phOneFrac));
        phaseTwoFrac.setText(String.valueOf(phTwoFrac));

    }

    //
    //
    //

    @Override
    public void handle(Event event) {
        if(event.getSource().getClass() == ChoiceBox.class)
        {
            ChoiceBox<String> ch = (ChoiceBox<String>) event.getSource();
            if(ch.getId().compareTo("meshOptions") == 0) {
                mesh.clear();
                nodes.clear();
                String selected = meshOptions.getSelectionModel().getSelectedItem();
                if(selected == null) return;
                if (selected.compareTo(QUAD_TREE) == 0)
                    meshQuadTree();
                else if (selected.compareTo(STRUCTURED_ELEMENTS) == 0)
                    regularMeshEV();
                else if (selected.compareTo(DELAUNAY) == 0)
                    delaunayMesh();

                drawMesh();
            }
            else if(ch.getId().compareTo("advancedOptions") == 0)
            {

            }
        }
    }

    @FXML
    public void loadButtonClick()
    {
        pathImage = new String(imagePathField.getText());
        loadImage();
    }

    @FXML
    public void saveMeshClick()
    {
        String path = meshFilePath.getText();
        if(path.compareTo("") == 0) return;
        pathMesh = path;
        try {
            saveMeshToFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void loadMeshClick()
    {
        String path = meshFilePath.getText();
        if(path.compareTo("") == 0) return;
        pathMesh = path;
        try {
           loadMeshFromFile();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void advancedApplyClick()
    {
        String input = advancedField.getText();
        String selected = advancedOptions.getSelectionModel().getSelectedItem();
        if(selected == null) return;
        if(selected.compareTo(OPTION_X_DENISTY) == 0)
        {
            X_DENSITY = Integer.parseInt(input);
        }
        else if(selected.compareTo(OPTION_Y_DENISTY) == 0)
        {
            Y_DENSITY = Integer.parseInt(input);
        }
        else if(selected.compareTo(OPTION_DELAUNAY_RESOLUTION) == 0)
        {
            DELAUNAY_RESOLUTION = Integer.parseInt(input);
        }
        redrawMesh();
    }
}