package safa.ddi_visor;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConexionBDTest {

    @Test
    void conectar() throws SQLException {
        System.out.println("openConnection");
        Connection result = ConexionBD.conectar();
        assertNotNull(result);
    }
}