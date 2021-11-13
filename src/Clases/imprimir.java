package Clases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.io.FileReader;
import java.util.Properties;
public class imprimir {

    Connection conexx;
    Statement newst;

    public imprimir() {
        String url="";
        String usuario="";
        String clave="";
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
        //realizando la conexión
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conexx = DriverManager.getConnection(url,usuario,clave);
            this.newst = this.conexx.createStatement();
        } catch (ClassNotFoundException | SQLException eq) {
            JOptionPane.showMessageDialog(null, eq, "ADVERTENCIA", 2);
        }
    }

    public void mostrar_reporte(String nReporte) throws Exception {
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/" + nReporte));
        JasperPrint print = JasperFillManager.fillReport(reporte, null, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Reportes");
        view.setExtendedState(0);
    }

    public void mostrar_reporte_param(String z, String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("code", x);
        parametros.put("mes", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/" + z));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Reportes");
        view.setExtendedState(0);
    }//sirve para mostrar las fichas de inscripcion

    public void mostarRecibo(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("code", x);
        parametros.put("serv", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/recibo_cjm.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        //JasperPrintManager.printReport(print, true);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Recibo de Juan Marcos");
        view.setExtendedState(0);
    }

    public void mostarJosue(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("code", x);
        parametros.put("serv", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/recibo_roca.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Recibo");
        view.setExtendedState(0);
    }
    
    public void mostarSimple(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("code", x);
        parametros.put("serv", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/recibo_simple.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Recibo");
        view.setExtendedState(0);
    }
    
    public void mostarAcademia(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("numero", x);
        parametros.put("centro", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/RepuestoReciboAcademia.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Recibo de Academia");
        view.setExtendedState(0);
    }
    
    public void mostarMorosos(String sql) throws Exception {
        System.out.println(sql);
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/MOROSOS.jrxml"));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, null, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("LIBRO DE CUOTAS");
        view.setExtendedState(0);
    }
    
    public void mostarPeriodos(String sql) throws Exception {
        System.out.println(sql);
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/periodos.jrxml"));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, null, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Reporte Por Periodos");
        view.setExtendedState(0);
    }
    
    public void notaCobro(String sql) throws Exception {
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/notaCobro.jrxml"));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, null, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Notas de Cobro");
        view.setExtendedState(0);
    }
   
    public void solvenciaNew(String sql,String x) throws Exception {
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/solvencia.jrxml"));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        Map parametros = new HashMap();
        parametros.put("mes",x);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Solvencia");
        view.setExtendedState(0);
    }
    public void reportediario(String sql) throws Exception {
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/inglesos.jrxml"));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, null, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Solvencia");
        view.setExtendedState(0);
    }
    public void imprimirFicha(String sql,String x,String ficha) throws Exception {
        JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream("/reportes/"+ficha));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jd.setQuery(newQuery);
        Map parametros = new HashMap();
        parametros.put("mes",x);
        JasperReport jr = JasperCompileManager.compileReport(jd);
        JasperPrint print = JasperFillManager.fillReport(jr, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Solvencia");
        view.setExtendedState(0);
    }
}
