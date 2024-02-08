module safa.ddi_visor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires jasperreports;
    requires javafx.base;


    opens safa.ddi_visor to javafx.fxml;
    exports safa.ddi_visor;
}