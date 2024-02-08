package safa.ddi_visor;

// Importaciones necesarias
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.sql.Connection;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Clase para probar el controlador de cuentas
public class ControllerCuentaTest {

    private ControllerCuenta controller;
    private Connection con;

    // Método para configurar el entorno antes de cada prueba
    @BeforeEach
    public void setUp() throws SQLException {
        controller = new ControllerCuenta();
        con = ConexionBD.conectar();
        controller.cargarDatosDeBaseDeDatos();
    }

    // Prueba para verificar el número de cuentas
    @Test
    public void numCuentas() {
        // Probar que el número de cuentas es el esperado
        assertEquals(2, controller.numCuentas(), "El número de cuentas debería ser 2");
    }

    // Prueba para verificar el número de cuentas con saldo mayor o igual a 50k
    @Test
    public void numCuentas50k() {
        // Probar que el número de cuentas con saldo mayor o igual a 50k es el esperado
        assertEquals(1, controller.numCuentas50k(), "El número de cuentas con saldo mayor o igual a 50k debería ser 2");
    }

    // Método para limpiar el entorno después de cada prueba
    @AfterEach
    public void tearDown() throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}
