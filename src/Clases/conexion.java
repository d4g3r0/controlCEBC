package Clases;

import java.sql.*;

public class conexion {
    Connection puente=null;
    public Connection conectar() throws ClassNotFoundException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            puente = DriverManager.getConnection("jdbc:mysql://192.168.1.25/cebc", "guest", "guest");
            System.out.println("Conexion al servidor realizada exitosamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar al servidor:\n"+e);
        }
        return puente;
    }
    public void cerrarConexion(){
        try {
            puente.close();
        } catch (Exception e) {
            System.out.println("Error:\n"+e);
        }
    }
}
