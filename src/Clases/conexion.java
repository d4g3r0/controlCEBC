package Clases;

import java.sql.*;
import java.io.FileReader;
import java.util.Properties;

public class conexion {
    Connection puente=null;
    String url="";
    String usuario="";
    String clave="";
    public Connection conectar() throws ClassNotFoundException{
        //propiedades del archivo de configuración
        try (FileReader lector = new FileReader("config")){
            Properties propiedades =  new Properties();
            propiedades.load(lector);
            url="jdbc:mysql://"+propiedades.getProperty("ip")+"/"+propiedades.getProperty("bd");
            usuario=propiedades.getProperty("user");
            clave=propiedades.getProperty("password");  
        } catch (Exception e) {
            System.out.println("error, revisar archivo de configuración"+e);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            puente = DriverManager.getConnection(url,usuario,clave);
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
