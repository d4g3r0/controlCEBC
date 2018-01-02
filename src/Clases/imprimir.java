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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
public class imprimir {

    Connection conexx;
    Statement newst;

    public imprimir() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conexx = DriverManager.getConnection("jdbc:mysql://192.168.1.25/cebc", "guest", "guest");
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
    }

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
    
    public void mostarCEBC(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("code", x);
        parametros.put("serv", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/recibo_cebc.jrxml"));
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
    
    public void mostarMorosos(String x, int y, String z) throws Exception {
        Map parametros = new HashMap();
        parametros.put("mes", x);
        parametros.put("ano", Integer.valueOf(y));
        parametros.put("grado", z);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/MOROSOS2.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Listado de Morosos");
        view.setExtendedState(0);
    }
    
    public void notasCobro(String x, String y, String z) throws Exception {
        Map parametros = new HashMap();
        parametros.put("num", x);
        parametros.put("mes", y);
        parametros.put("fecha", z);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/notaCobro.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Notas de Cobro");
        view.setExtendedState(0);
    }
    
    public void notasCobro2(String x, String y, String z, String w) throws Exception {
        Map parametros = new HashMap();
        parametros.put("num", x);
        parametros.put("mes", y);
        parametros.put("fecha", z);
        parametros.put("jornada",w);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/notaCobro2.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Notas de Cobro");
        view.setExtendedState(0);
    }
    
    public void periodos(String x, String y) throws Exception {
        Map parametros = new HashMap();
        parametros.put("MIN", x);
        parametros.put("MAX", y);
        JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/periodos.jrxml"));
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conexx);
        JasperViewer view = new JasperViewer(print, false);
        view.setVisible(true);
        view.setTitle("Reporte Por Periodos");
        view.setExtendedState(0);
    }
}
