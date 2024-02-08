package safa.ddi_visor;

import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.*;
import safa.model.Cuenta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ControllerCuenta implements Initializable {

    @FXML
    private ContextMenu ContextMenuAvanzar;

    @FXML
    private ContextMenu ContextMenuAvanzar1;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button botonAvance;

    @FXML
    private Button botonBorrar;

    @FXML
    private Button botonCancelar;

    @FXML
    private Button botonEditar;

    @FXML
    private Button botonHTML;

    @FXML
    private Button botonNueva;

    @FXML
    private Button botonPDF;

    @FXML
    private Button botonRetroceso;

    @FXML
    private ImageView logo1;

    @FXML
    private ImageView logo2;

    @FXML
    private Pane panelExistentes;

    @FXML
    private Pane panelNuevas;

    @FXML
    private Pane panelPrincipal;

    @FXML
    private TextField tfFechaNuevas;

    @FXML
    private TextField tfFechaVisor;

    @FXML
    private TextField tfMayores50;

    @FXML
    private TextField tfNacionalidadNuevas;

    @FXML
    private TextField tfNacionalidadVisor;

    @FXML
    private TextField tfNumeroCuentas;

    @FXML
    private TextField tfNumeroNuevas;

    @FXML
    private TextField tfNumeroVisor;

    @FXML
    private TextField tfSaldoNuevas;

    @FXML
    private TextField tfSaldoVisor;

    @FXML
    private TextField tfTitularNuevas;

    @FXML
    private TextField tfTitularVisor;

    private ArrayList<Cuenta> cuentas = new ArrayList<>();
    private Boolean editar;

    @FXML
    void generaPDF(ActionEvent event) throws JRException, SQLException {
        Connection con = ConexionBD.conectar();
        JasperReport archivo = JasperCompileManager.compileReport("src/main/resources/jasper/pdf/pdf.jrxml");
        JasperPrint print = JasperFillManager.fillReport(archivo, null, con);
        JasperExportManager.exportReportToPdfFile(print, "src/main/resources/jasper/pdf/Visor_Jaspersoft.pdf");
        System.out.println("PDF GENERADO");
        JasperViewer.viewReport(print, false);
    }

    @FXML
    void generarHTML(ActionEvent event) throws SQLException, JRException {
        Connection con = ConexionBD.conectar();
        JasperReport archivo = JasperCompileManager.compileReport("src/main/resources/jasper/visor.jrxml");
        JasperPrint print = JasperFillManager.fillReport(archivo, null, con);
        JasperExportManager.exportReportToHtmlFile(print, "src/main/resources/jasper/HTML/Visor_Jaspersoft.html");
        System.out.println("HTML GENERADO");
        JasperViewer.viewReport(print, false);

    }

    /**
     * Función que controla la aplicación cuando se pulsa el botón de aceptar
     *
     * @param event pulsación del botón
     */
    @FXML
    void aceptar(ActionEvent event) {
        Cuenta c = leeCampos();
        if (c != null) {
            if (!editar) {
                cuentas.add(c);
            } else {
                for (Cuenta aux : cuentas) {
                    if (aux.getNumCuenta().equals(c.getNumCuenta())) {
                        aux.setTitular(c.getTitular());
                        aux.setNacionalidad(c.getNacionalidad());
                        aux.setFechaApertura(c.getFechaApertura());
                        aux.setSaldo(c.getSaldo());
                        break;
                    }
                }
            }

            leer(c);

            try {
                Connection con = ConexionBD.conectar(); // Conectamos con la base de datos

                if (!editar) {
                    // Consulta para insertar una nueva cuenta
                    PreparedStatement nuevaCuenta = con.prepareStatement("INSERT INTO cuenta(numCuenta, titular," +
                            "nacionalidad, fechaApertura, saldo) VALUES (?,?,?,?,?)");
                    nuevaCuenta.setString(1, String.valueOf(c.getNumCuenta()));
                    nuevaCuenta.setString(2, c.getTitular());
                    nuevaCuenta.setString(3, c.getNacionalidad());
                    nuevaCuenta.setString(4, String.valueOf(c.getFechaApertura()));
                    nuevaCuenta.setString(5, String.valueOf(c.getSaldo()));

                    System.out.println(nuevaCuenta);
                    // Ejecutamos la consulta
                    nuevaCuenta.executeUpdate();
                } else {
                    // Consulta para actualizar una cuenta existente
                    PreparedStatement nuevaCuenta = con.prepareStatement("UPDATE cuenta SET numCuenta = ?, " +
                            "titular = ?, nacionalidad = ?, fechaApertura = ?, saldo = ? WHERE numCuenta = ?");
                    nuevaCuenta.setString(1, String.valueOf(c.getNumCuenta()));
                    nuevaCuenta.setString(2, c.getTitular());
                    nuevaCuenta.setString(3, c.getNacionalidad());
                    nuevaCuenta.setString(4, String.valueOf(c.getFechaApertura()));
                    nuevaCuenta.setString(5, String.valueOf(c.getSaldo()));
                    nuevaCuenta.setString(6, String.valueOf(c.getNumCuenta()));

                    System.out.println(nuevaCuenta);
                    // Ejecutamos la consulta
                    nuevaCuenta.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        tfNumeroCuentas.setText(String.valueOf(numCuentas()));
        tfMayores50.setText(String.valueOf(numCuentas50k()));
        panelPrincipal.getScene().getWindow().setWidth(574);
        panelPrincipal.getScene().getWindow().setHeight(484);
        panelExistentes.setVisible(true);
        panelNuevas.setVisible(false);
    }

    public Cuenta leeCampos() {
        Integer numCuenta = null;
        String titular = null;
        String nacionalidad = null;
        LocalDate fechaApertura = null;
        Double saldo = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        tfNumeroNuevas.setStyle("-fx-background-color: #f5ffff; -fx-border-color: #00796B; /* Verde oscuro */ -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        tfFechaNuevas.setStyle("-fx-background-color: #f5ffff; -fx-border-color: #00796B; /* Verde oscuro */ -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        tfSaldoNuevas.setStyle("-fx-background-color: #f5ffff; -fx-border-color: #00796B; /* Verde oscuro */ -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        try {

            numCuenta = Integer.valueOf(tfNumeroNuevas.getText());
            for (Cuenta c : cuentas) {
                if (!editar && Objects.equals(numCuenta, c.getNumCuenta())) {
                    numCuenta = null;
                    throw new Exception();

                }
            }
        } catch (Exception e) {
            tfNumeroNuevas.setStyle("-fx-background-color: #ff8888; -fx-border-color: #ff0000; /* Verde oscuro */ -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        }
        titular = tfTitularNuevas.getText();
        nacionalidad = tfNacionalidadNuevas.getText();
        try {
            System.out.println(String.valueOf(tfFechaNuevas.getText()));
            fechaApertura = LocalDate.parse(String.valueOf(tfFechaNuevas.getText()), formatter);
        } catch (Exception e) {
            tfFechaNuevas.setStyle("-fx-background-color: #ff8888; -fx-border-color: #ff0000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        }
        try {
            saldo = Double.valueOf(tfSaldoNuevas.getText());
        } catch (Exception e) {
            tfSaldoNuevas.setStyle("-fx-background-color: #ff8888; -fx-border-color: #ff0000; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 5px; -fx-font-family: 'Arial'; -fx-font-size: 14px;");
        }
        if (numCuenta != null && titular != null && fechaApertura != null && saldo != null) {
            return new Cuenta(numCuenta, titular, nacionalidad, fechaApertura, saldo);
        }
        return null;
    }

    /**
     * Controla la aplicación cuando se pulsa el botón de avance
     *
     * @param event pulsación del botón
     */
    @FXML
    void avance(ActionEvent event) {
        int index = 0;
        for (int i = 0; i < cuentas.size(); i++) {
            if (tfNumeroVisor.getText().equals(String.valueOf(cuentas.get(i).getNumCuenta()))) {
                index = i + 1;
            }
        }
        leer(cuentas.get(index));
        botonRetroceso.setVisible(true);
        if (index == cuentas.size() - 1) {
            botonAvance.setVisible(false);
        }
    }

    /**
     * Controla la aplicación cuando se pulsa el botón de cancelar
     *
     * @param event pulsación del botón
     */
    @FXML
    void cancelar(ActionEvent event) {
        panelPrincipal.getScene().getWindow().setWidth(574);
        panelPrincipal.getScene().getWindow().setHeight(484);
        tfNumeroNuevas.setText("");
        tfTitularNuevas.setText("");
        tfFechaNuevas.setText("");
        tfSaldoNuevas.setText("");
        panelExistentes.setVisible(true);
        panelNuevas.setVisible(false);
    }

    /**
     * Elimina una cuenta de la base de datos
     * @param event
     */
    @FXML
    void eliminar(ActionEvent event) {
        if(cuentas.size() == 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("ALERTA");
            alert.setContentText("No se puede eliminar la última cuenta");
            alert.showAndWait();
        } else {
            try {
                Connection con = ConexionBD.conectar(); // Conectamos con la base de datos

                // Consulta para insertar una nueva cuenta
                PreparedStatement nuevaCuenta = con.prepareStatement("DELETE FROM cuenta WHERE numCuenta = ?");
                nuevaCuenta.setString(1, tfNumeroVisor.getText());
                System.out.println(nuevaCuenta);
                // Ejecutamos la consulta
                nuevaCuenta.executeUpdate();
                for (Cuenta c : cuentas){
                    if (c.getNumCuenta().equals(Integer.valueOf(tfNumeroVisor.getText()))){
                        cuentas.remove(c);
                        break;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            leer(cuentas.get(0));
            botonRetroceso.setVisible(false);
        }
    }

    /**
     * Controla la aplicación cuando pulsamos el botón editar de nuestra interfaz
     * @param event
     */
    @FXML
    void editar(ActionEvent event){
        panelPrincipal.getScene().getWindow().setWidth(514);
        panelPrincipal.getScene().getWindow().setHeight(404);
        editar = true;
        tfNumeroNuevas.setText(tfNumeroVisor.getText());
        tfNumeroNuevas.setEditable(false);
        tfTitularNuevas.setText(tfTitularVisor.getText());
        tfNacionalidadNuevas.setText(tfNacionalidadVisor.getText());
        tfFechaNuevas.setText(tfFechaVisor.getText());
        tfSaldoNuevas.setText(tfSaldoVisor.getText());
        panelExistentes.setVisible(false);
        panelNuevas.setVisible(true);
    }

    /**
     * Controla la aplicación cuando se pulsa la opción de cuenta nueva
     *
     * @param event pulsación del botón
     */
    @FXML
    void nueva(ActionEvent event) {
        panelPrincipal.getScene().getWindow().setWidth(514);
        panelPrincipal.getScene().getWindow().setHeight(404);
        editar = false;
        tfNumeroNuevas.setText("");
        tfNumeroNuevas.setEditable(true);
        tfTitularNuevas.setText("");
        tfNacionalidadNuevas.setText("");
        tfFechaNuevas.setText("");
        tfSaldoNuevas.setText("");
        panelExistentes.setVisible(false);
        panelNuevas.setVisible(true);
    }

    /**
     * Controla la aplicación cuando se pulsa el botón de retroceder
     *
     * @param event pulsación del botón
     */
    @FXML
    void retroceso(ActionEvent event) {
        int index = 0;
        for (int i = 0; i < cuentas.size(); i++) {
            if (tfNumeroVisor.getText().equals(String.valueOf(cuentas.get(i).getNumCuenta()))) {
                index = i - 1;
            }
        }
        leer(cuentas.get(index));
        if (index == 0) {
            botonRetroceso.setVisible(false);
        }
        botonAvance.setVisible(true);

    }

    /**
     * Lee una cuenta de la lista y la escribe en los campos de texto de la aplicación
     *
     * @param c cuenta a leer
     */
    void leer(Cuenta c) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        tfNumeroVisor.setText(String.valueOf(c.getNumCuenta()));
        tfTitularVisor.setText(c.getTitular());
        tfNacionalidadVisor.setText(c.getNacionalidad());
        tfFechaVisor.setText(c.getFechaApertura().format(formatter));
        tfSaldoVisor.setText(String.valueOf(c.getSaldo()));
    }

    /**
     * Función que inicializa la aplicación cargando sus datos iniciales
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Image image = new Image(getClass().getResourceAsStream("/img/llave.png"));
        logo1.setImage(image);
        logo2.setImage(image);

        // Variables para la creación de nuevas cuentas
        Integer numCuenta;
        String titular;
        String nacionalidad;
        LocalDate fechaApertura;
        Double saldo;

        try {
            Connection con = ConexionBD.conectar();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM cuenta");
            while (rs.next()) {
                numCuenta = Integer.valueOf(rs.getString(1));
                titular = rs.getString(2);
                nacionalidad = rs.getString(3);
                fechaApertura = LocalDate.parse(rs.getString(4));
                saldo = Double.valueOf(rs.getString(5));
                cuentas.add(new Cuenta(numCuenta, titular, nacionalidad, fechaApertura, saldo));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (cuentas.size() == 1){
            botonNueva.setVisible(true);
        }
        tfNumeroCuentas.setText(String.valueOf(numCuentas()));
        tfMayores50.setText(String.valueOf(numCuentas50k()));

        leer(cuentas.get(0));


    }
    public List<Cuenta> cuentass;

    // Constructor
    public ControllerCuenta() {
        cuentas = new ArrayList<>();
    }

    // Método para cargar datos desde la base de datos
    public void cargarDatosDeBaseDeDatos() throws SQLException {
        Connection con = null;
        try {
            con = ConexionBD.conectar();
            String sql = "SELECT numCuenta, titular, nacionalidad, fechaApertura, saldo FROM cuenta";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int numCuenta = rs.getInt("numCuenta");
                String titular = rs.getString("titular");
                String nacionalidad = rs.getString("nacionalidad");
                LocalDate fechaApertura = rs.getDate("fechaApertura").toLocalDate();
                double saldo = rs.getDouble("saldo");

                Cuenta cuenta = new Cuenta(numCuenta, titular, nacionalidad, fechaApertura, saldo);
                cuentas.add(cuenta);
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
    public Integer numCuentas() {
        return cuentas.size();
    }

    public Integer numCuentas50k() {
        Integer res = 0;
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).getSaldo() >= 50000) {
                res++;
            }
        }
        return res;
    }
}

