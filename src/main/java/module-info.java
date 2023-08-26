module com.example.projekt_geo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.projekt_geo to javafx.fxml;
    exports com.example.projekt_geo;
}