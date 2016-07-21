package uai_biblioteca;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    static String base = "";//Base de Datos
    static String usuario = "";//usuario
    static String pass = "";//contraseña
    static String ruta = "";//host
    Connection conexion = null;
    static int cont = 0;

    public void config() throws Exception {
        Properties propiedades = new Properties();
        InputStream entrada = null;

        try {
            entrada = new FileInputStream("./src/ArchivosConfiguracion/ConfiguracionBD.properties");
            // cargamos el archivo de propiedades
            propiedades.load(entrada);
            // obtenemos las propiedades y las imprimimos

            try {
                base = Descifrar.Desencriptar(propiedades.getProperty("BD"));
                usuario = Descifrar.Desencriptar(propiedades.getProperty("User"));
                pass = Descifrar.Desencriptar(propiedades.getProperty("Psw"));
                ruta = Descifrar.Desencriptar(propiedades.getProperty("Servidor")) + base;
            } catch (IOException ex) {
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Conexion() throws Exception {
        // Si es la primera vez, que se ejecuta, desencriptar
        if (cont == 0) {
            config();
        }
        cont++;

        try {
            //obtenemos el driver para mysql
            Class.forName("com.mysql.jdbc.Driver");
            //obtenemos la conexión
            conexion = DriverManager.getConnection(ruta, usuario, pass);
            if (conexion != null) {
                System.out.println("Conexión a base de datos " + base + ". listo");
            }
        } catch (SQLException e) {
            System.out.println("Error en conexion" + e);

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    public Connection getConnection() {
        return conexion;
    }

    /**
     * Permite desconectar conexión
     */
    public void desconectar() {
        try {
            conexion.close();
            System.out.println("La conexion a la  base de datos ha terminado");
        } catch (SQLException ex) {
            Logger.getLogger("Error al desconectar" + Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
