package Clases;
import Clases.conexion;
import java.sql.*;


public class estadoAlumno {
    public static void estado(String year,String code){
        String nombre,apellido,grado,jornada;
        double inscripcion,mensualidad;
        conexion nuevaConexion = new conexion();
        try {
            Connection nc = nuevaConexion.conectar();
            Statement st0 = nc.createStatement();
            ResultSet rs0 = st0.executeQuery("select cod,nom,ape,grado,jornada, ins,mens from inscripciones_"+year+" where cod="+"'"+code+"'");
            
        } catch (Exception e) {
            System.out.println("error al concectar a la base de datos:"+e);
        }
        
    }
}
