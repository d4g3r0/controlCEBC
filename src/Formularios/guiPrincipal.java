package Formularios;

import Clases.conexion;
import Clases.imprimir;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class guiPrincipal extends javax.swing.JFrame {
//variables para cobros
public String conceptoRecibo="";
public String yearRecibo="";
public Double mora;
String[] registros = new String[13];
public String updatesql="";
//variables globales para ventas
public String year;
public String code;
public String name;
    public guiPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);       
    }
    public void iniciarPanelIncripcion() {
        //volores por defecto
        ins_spinerNoCuotas.setValue(10);
        //limpiando casillas
        ins_tCodigo.setText("");
        ins_tNombres.setText("");
        ins_tApellidos.setText("");
        ins_tDireccion.setText("");
        ins_tTelefono.setText("");
        ins_tMovil.setText("");
        ins_tFecha.setDate(null);
        ins_tObservaciones.setText("");
        ins_tMadre.setText("");
        ins_tDpiMadre.setText("");
        ins_tProfMadre.setText("");
        ins_tPadre.setText("");
        ins_tDpiPadre.setText("");
        ins_tProfPadre.setText("");
        ins_tInscripcion.setText("");
        ins_tColegiatura.setText("");
        ins_tOtrosDocumentos.setText("");
        ins_tObsc.setText("");
        //limpiando checkbox
        ins_checkRenap.setSelected(false);
        ins_checkMeca.setSelected(false);
        ins_checkCertAnterior.setSelected(false);
        ins_checkDPrepri.setSelected(false);
        ins_checkDSexto.setSelected(false);
        ins_checkDTercero.setSelected(false);
        //habilitando checkbox
        ins_checkRenap.setEnabled(true);
        ins_checkMeca.setEnabled(true);
        ins_checkCertAnterior.setEnabled(true);
        ins_checkDPrepri.setEnabled(true);
        ins_checkDSexto.setEnabled(true);
        ins_checkDTercero.setEnabled(true);
        //invocando llenado de comboboxes
        insComboboxes(String.valueOf(ins_servicio.getSelectedItem()));
        updatesql = "";
        for (int i = 0; i < registros.length; i++) {
            registros[i] = "";
        }
    }
    public void imprimirFichas(String servicio, String jornada, String codigo) {
        //impresion de ficha
        String x;
        String y = "";

        if ("Colegio Juan Marcos".equals(servicio)) {
            x = "ficha_cjm.jrxml";
        } else if ("Colegio Cristiano Josue".equals(servicio)) {
            x = "ficha_josue.jrxml";
        } else if ("Colegio El Buen Camino".equals(servicio)) {
            x = "ficha_cebc.jrxml";
        } else {
            x = "ficha_academia.jrxml";
        }
        if ("Sabatina".equals(jornada)) {
            y = "Noviembre";
        } else{
            y = "Octubre";
        }
        imprimir abrir = new imprimir();
        try {
            abrir.mostrar_reporte_param(x, codigo, y);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir la ficha de inscripcion\n" + e);
        }
    }
    public void limpiarPanelCobros() {
        //limpiando el panel
        cob_codigo.setText("");
        cob_nombres.setText("");
        cob_apellidos.setText("");
        cob_servicio.setText("");
        cob_grado.setText("");
        cob_jornada.setText("");
        cob_inscripcion.setText("");
        cob_colegiatura.setText("");
        cob_recibo.setText("");
        cob_monto.setText("");
        cob_observaciones.setText("");
        cob_concepto.setText("");

        cob_check_ins.setEnabled(true);
        cob_check_enero.setEnabled(true);
        cob_check_febrero.setEnabled(true);
        cob_check_marzo.setEnabled(true);
        cob_check_abril.setEnabled(true);
        cob_check_mayo.setEnabled(true);
        cob_check_junio.setEnabled(true);
        cob_check_julio.setEnabled(true);
        cob_check_agosto.setEnabled(true);
        cob_check_septiembre.setEnabled(true);
        cob_check_octubre.setEnabled(true);
        cob_check_noviembre.setEnabled(true);
        //cob_check_clausura.setEnabled(true);

        cob_check_ins.setSelected(false);
        cob_check_enero.setSelected(false);
        cob_check_febrero.setSelected(false);
        cob_check_marzo.setSelected(false);
        cob_check_abril.setSelected(false);
        cob_check_mayo.setSelected(false);
        cob_check_junio.setSelected(false);
        cob_check_julio.setSelected(false);
        cob_check_agosto.setSelected(false);
        cob_check_septiembre.setSelected(false);
        cob_check_octubre.setSelected(false);
        cob_check_noviembre.setSelected(false);
        cob_check_clausura.setSelected(false);
    }
    public void PanelCobros(String cod, String ano) throws ClassNotFoundException {
        //variable de la alerta bonbillo
        String bombillo="";
        String State="";
        limpiarPanelCobros();
        conceptoRecibo = "";
        mora = 0.0;
        yearRecibo = ano;
        updatesql = "";
        //limpiando tabla
        try {
            DefaultTableModel model = (DefaultTableModel) this.cob_tabla.getModel();
            int filas = this.cob_tabla.getRowCount();
            for (int i = 0; filas > i; i++) {
                model.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar tabla:" + e);
        }
        //llenando datos del alumno
        conexion cc = new conexion();
        Connection nc = cc.conectar();
        try {
            Statement st = nc.createStatement();
            ResultSet rs0 = st.executeQuery("SELECT cod,nom,ape,grado,jornada,ins,mens,serv,obsc,estado FROM inscripciones_" + ano + " WHERE cod='" + cod + "'");
            if (rs0.next() == true) {
                cob_codigo.setText(rs0.getString("cod"));
                cob_nombres.setText(rs0.getString("nom"));
                cob_grado.setText(rs0.getString("grado"));
                cob_inscripcion.setText(rs0.getString("ins"));
                cob_jornada.setText(rs0.getString("jornada"));
                cob_colegiatura.setText(rs0.getString("mens"));
                cob_apellidos.setText(rs0.getString("ape"));
                cob_servicio.setText(rs0.getString("serv"));
                bombillo=rs0.getString("obsc");
                State=rs0.getString("estado");
            }
            System.out.println(State);
        } catch (SQLException sqx) {
            System.out.println("Error:" + sqx);
        }
        //llenar tabla de cuotas
        try {
            Statement st = nc.createStatement();
            ResultSet rs0 = st.executeQuery("SELECT ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov,clausura FROM cuotas_" + ano + " WHERE cod='" + cod + "'");
            while (rs0.next()) {
                registros[0] = rs0.getString("ins");
                registros[1] = rs0.getString("jan");
                registros[2] = rs0.getString("feb");
                registros[3] = rs0.getString("mar");
                registros[4] = rs0.getString("apr");
                registros[5] = rs0.getString("may");
                registros[6] = rs0.getString("jun");
                registros[7] = rs0.getString("jul");
                registros[8] = rs0.getString("ago");
                registros[9] = rs0.getString("sep");
                registros[10] = rs0.getString("oct");
                registros[11] = rs0.getString("nov");
                registros[12] = rs0.getString("clausura");
                ((DefaultTableModel) cob_tabla.getModel()).addRow(registros);
            }
        } catch (SQLException sqx) {
            System.out.println("Error:" + sqx);
        }
        cc.cerrarConexion();
        //Numero de recibo Automatico
        int temp=autoNumerarRecibos(cob_servicio.getText());
        if(temp==0){
            cob_recibo.setEditable(true);
        }else{
            cob_recibo.setText(String.valueOf(temp));
            cob_recibo.setEditable(false);
        }
        //Ensender el bonbillo
        if(bombillo==null || "".equals(bombillo)){
            ImageIcon luz = new ImageIcon(getClass().getClassLoader().getResource("imagenes/off_128.png"));
            alerta_bonbillo.setIcon(luz);
        }else{
            ImageIcon luz = new ImageIcon(getClass().getClassLoader().getResource("imagenes/on_128.png"));
            alerta_bonbillo.setIcon(luz);
        }
        
        if("Inscrito".equals(State)){
            ImageIcon stamp = new ImageIcon(getClass().getClassLoader().getResource("imagenes/inscrito_80.png"));
            iconState.setIcon(stamp);
        }else{
            ImageIcon stamp = new ImageIcon(getClass().getClassLoader().getResource("imagenes/retirado_80.png"));
            iconState.setIcon(stamp);
        }   
    }
    public void panelVentas(String cod, String ano) {
        /*
         Primero procedere a limpiar todos los elementos del panel de ventas, de esta
         manera cada vez que alguien presione el voton de ventas, este iniciara un 
         nuevo procedimiento, espero que con eso minimize los herrores
         */
        year = ano;
        code = cod;
        //limpiando info y registros
        ven_info.setText("");
        ven_total.setText("MONTO");
        for (int i = 0; i < registros.length; i++) {
            registros[i] = "";
        }
        //limpiando tablas
        try {
            DefaultTableModel model1 = (DefaultTableModel) this.ven_tabla1.getModel();
            DefaultTableModel model2 = (DefaultTableModel) this.ven_tabla2.getModel();
            int filas1 = this.ven_tabla1.getRowCount();
            int filas2 = this.ven_tabla2.getRowCount();
            for (int i = 0; filas1 > i; i++) {
                model1.removeRow(0);
            }
            for (int i = 0; filas2 > i; i++) {
                model2.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar tabla:" + e);
        }

        /*
         ahora que todo esta limpio hay que cargar la información del estudiante
         desde el servidor, luego habra que mostrarla en el jlabel llamdo ven_info,
         tambien almacenare alguna información util para generar la factura de la 
         compra dentro del vector registros utilizado inicialmente en las inscripciones.
         asi podre reutilizarlo para otros menesteres XD
         */
        String[] stock = new String[4];
        try {
            conexion nc = new conexion();
            Connection n = nc.conectar();
            Statement st = n.createStatement();
            ResultSet r = st.executeQuery("SELECT cod,ape,nom FROM inscripciones_" + ano + " WHERE cod='" + cod + "'");
            while (r.next()) {
                registros[0] = r.getString("cod");
                registros[1] = r.getString("ape");
                registros[2] = r.getString("nom");
            }
            //datos para la tabla de articulos para la venta
            Statement st1 = n.createStatement();
            ResultSet r1 = st1.executeQuery("SELECT codigo,item,price,stock FROM inventario");
            while (r1.next()) {
                stock[0] = r1.getString("codigo");
                stock[1] = r1.getString("item");
                stock[2] = r1.getString("price");
                stock[3] = r1.getString("stock");
                ((DefaultTableModel) ven_tabla1.getModel()).addRow(stock);
            }
            nc.cerrarConexion();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error:\n" + e);
        }

        ven_info.setText("<html>Venta para el cliente <b>" + registros[2] + " " + registros[1] + "</b></html>");
        name = registros[2] + " " + registros[1];
        ven_searchbox.requestFocus();
        ven_btnSelectProduct.setEnabled(false);
    }
    public int autoNumerarRecibos(String servicio){
        String x = null;
        int z = 0;
        switch (servicio) {
            case "Colegio Cristiano Josue":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Colegio Cristiano Josue'");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
            case "Colegio El Buen Camino":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Colegio El Buen Camino'");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null || x.equals("")) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
            case "Liceo Roca De Los Siglos":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Liceo Roca De Los Siglos'");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
            case "Academia Juan Marcos":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Academia Juan Marcos'");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
            case "Otro":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Otro' AND gasto IS NULL");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
            case "Colegio Juan Marcos":
                try {
                    conexion objConexion = new conexion();
                    Connection conection = objConexion.conectar();
                    Statement st = conection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Colegio Juan Marcos' AND gasto IS NULL");
                    while (rs.next()) {
                        x = rs.getString("MAX(norecibo)");
                    }
                    if (x == null) {
                        x = "0";
                        z=Integer.valueOf(x)+1;
                    } else {
                        z=Integer.valueOf(x)+1;
                    }
                    objConexion.cerrarConexion();
                } catch (Exception e) {
                    System.out.println("Error:\n" + e);
                }
                break;
        }
        return(z);
    }
    public void insComboboxes(String x) {
        if (x.equals("Colegio Juan Marcos")) {
            ins_cGrado.removeAllItems();
            ins_cGrado.addItem("Primero Primaria");
            ins_cGrado.addItem("Segundo Primaria");
            ins_cGrado.addItem("Tercero Primaria");
            ins_cGrado.addItem("Cuarto Primaria");
            ins_cGrado.addItem("Quinto Primaria");
            ins_cGrado.addItem("Sexto Primaria");
            ins_cGrado.addItem("Primero Basico");
            ins_cGrado.addItem("Segundo Basico");
            ins_cGrado.addItem("Tercero Basico");
            ins_cJornada.removeAllItems();
            ins_cJornada.addItem("Matutina");
            ins_cJornada.addItem("Vespertina");
            ins_cJornada.addItem("Sabatina");
        } else if (x.equals("Colegio Cristiano Josue")) {
            ins_cGrado.removeAllItems();
            ins_cGrado.addItem("4to Bachillerato");
            ins_cGrado.addItem("5to Bachillerato");
            ins_cGrado.addItem("4to Perito");
            ins_cGrado.addItem("5to Perito");
            ins_cGrado.addItem("6to Perito");
            ins_cGrado.addItem("Ciclo I");
            ins_cGrado.addItem("Ciclo II");
            ins_cGrado.addItem("Bachillerato Por Madurez");
            ins_cJornada.removeAllItems();
            ins_cJornada.addItem("Vespertina");
            ins_cJornada.addItem("Sabatina");
        } else if (x.equals("Colegio El Buen Camino")) {
            ins_cGrado.removeAllItems();
            ins_cGrado.addItem("Etapa 4");
            ins_cGrado.addItem("Etapa 5");
            ins_cGrado.addItem("Etapa 6");
            ins_cGrado.addItem("Primero Primaria");
            ins_cGrado.addItem("Segundo Primaria");
            ins_cGrado.addItem("Tercero Primaria");
            ins_cGrado.addItem("Cuarto Primaria");
            ins_cGrado.addItem("Quinto Primaria");
            ins_cGrado.addItem("Sexto Primaria");
            ins_cGrado.addItem("Primero Basico");
            ins_cGrado.addItem("Segundo Basico");
            ins_cGrado.addItem("Tercero Basico");
            ins_cGrado.addItem("4to Bachillerato");
            ins_cGrado.addItem("5to Bachillerato");
            ins_cGrado.addItem("4to Perito");
            ins_cGrado.addItem("5to Perito");
            ins_cGrado.addItem("6to Perito");
            ins_cGrado.addItem("4to Secretariado");
            ins_cGrado.addItem("5to Secretariado");
            ins_cGrado.addItem("6to Secretariado");
            ins_cJornada.removeAllItems();
            ins_cJornada.addItem("Matutina");
            ins_cJornada.addItem("Vespertina");
            ins_cJornada.addItem("Sabatina");
        }else if (x.equals("Academia Juan Marcos")) {
            ins_cGrado.removeAllItems();
            ins_cGrado.addItem("Mecanografia Básica");
            ins_cGrado.addItem("Mecanografia Libre");
            ins_cGrado.addItem("Ingles");
            ins_cGrado.addItem("Computación");
            ins_cJornada.removeAllItems();
            ins_cJornada.addItem("Matutina");
            ins_cJornada.addItem("Vespertina");
            ins_cJornada.addItem("Sabatina");
        }else {
            ins_cGrado.removeAllItems();
            ins_cGrado.addItem("Primero Basico");
            ins_cGrado.addItem("Segundo Basico");
            ins_cGrado.addItem("Tercero Basico");
            ins_cJornada.removeAllItems();
            ins_cJornada.addItem("Sabatina");
        }
    }
    public void escogerpanel(String x) {
        switch (x) {
            case "1":
                PanelInfo.setVisible(false);
                panelCobros.setVisible(false);
                panelGastos.setVisible(false);
                panelVentas.setVisible(false);
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelOtrosCobros.setVisible(false);
                panelOculto.setVisible(false);
                panelInscripciones.setVisible(true);
                iniciarPanelIncripcion();
                break;
            case "2":
                panelGastos.setVisible(false);
                panelVentas.setVisible(false);
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelInscripciones.setVisible(false);
                PanelInfo.setVisible(false);
                panelOtrosCobros.setVisible(false);
                panelOculto.setVisible(false);
                panelCobros.setVisible(true);
                break;
            case "3":
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelInscripciones.setVisible(false);
                PanelInfo.setVisible(false);
                panelCobros.setVisible(false);
                panelGastos.setVisible(false);
                panelOtrosCobros.setVisible(false);
                panelOculto.setVisible(false);
                panelVentas.setVisible(true);
                break;
            case "4":
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelInscripciones.setVisible(false);
                PanelInfo.setVisible(false);
                panelCobros.setVisible(false);
                panelVentas.setVisible(false);
                panelOtrosCobros.setVisible(false);
                panelOculto.setVisible(false);
                panelGastos.setVisible(true);
                try {
                    panelGastos();
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case "5":
                panelInscripciones.setVisible(false);
                panelCobros.setVisible(false);
                panelVentas.setVisible(false);
                panelGastos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelOtrosCobros.setVisible(false);
                PanelInfo.setVisible(false);
                panelOculto.setVisible(false);
                panelAlumnos.setVisible(true);
                break;
            case "6":
                panelInscripciones.setVisible(false);
                panelCobros.setVisible(false);
                panelVentas.setVisible(false);
                panelGastos.setVisible(false);
                panelCarnet.setVisible(false);
                panelOtrosCobros.setVisible(false);
                PanelInfo.setVisible(false);
                panelAlumnos.setVisible(false);
                panelOculto.setVisible(false);
                panelHistorial.setVisible(true);
                break;
            case "7":
                break;
            case "8":
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelInscripciones.setVisible(false);
                PanelInfo.setVisible(false);
                panelCobros.setVisible(false);
                panelVentas.setVisible(false);
                panelGastos.setVisible(false);
                panelOculto.setVisible(false);
                panelOtrosCobros.setVisible(true);
                break;
            default:
                panelCobros.setVisible(false);
                panelGastos.setVisible(false);
                panelVentas.setVisible(false);
                panelAlumnos.setVisible(false);
                panelCarnet.setVisible(false);
                panelHistorial.setVisible(false);
                panelInscripciones.setVisible(false);
                panelOtrosCobros.setVisible(false);
                panelOculto.setVisible(false);
                PanelInfo.setVisible(true);
                break;
        }
    }
    public void cobrosCheckPressed(int m) {
        String montext = "";
        String v = "";
        //no permite que se vuelva a utilizar el checkbox
        switch (m) {
            case 0:
                cob_check_enero.setEnabled(false);
                montext = "enero";
                v = "jan=";
                break;
            case 1:
                cob_check_febrero.setEnabled(false);
                montext = "febrero";
                v = "feb=";
                break;
            case 2:
                cob_check_marzo.setEnabled(false);
                montext = "marzo";
                v = "mar=";
                break;
            case 3:
                cob_check_abril.setEnabled(false);
                montext = "abril";
                v = "apr=";
                break;
            case 4:
                cob_check_mayo.setEnabled(false);
                montext = "mayo";
                v = "may=";
                break;
            case 5:
                cob_check_junio.setEnabled(false);
                montext = "junio";
                v = "jun=";
                break;
            case 6:
                cob_check_julio.setEnabled(false);
                montext = "julio";
                v = "jul=";
                break;
            case 7:
                cob_check_agosto.setEnabled(false);
                montext = "agosto";
                v = "ago=";
                break;
            case 8:
                cob_check_septiembre.setEnabled(false);
                montext = "septiembre";
                v = "sep=";
                break;
            case 9:
                cob_check_octubre.setEnabled(false);
                montext = "octubre";
                v = "oct=";
                break;
            case 10:
                cob_check_noviembre.setEnabled(false);
                montext = "noviembre";
                v = "nov=";
                break;
            case 11:
                cob_check_clausura.setEnabled(false);
                break;
        }
        //calculo de mora
        Calendar c1 = Calendar.getInstance();
        Double montoActual;
        Double montoMensual = Double.parseDouble(JOptionPane.showInputDialog("Ingrese Monto"));
        int moraParcial = 0;
        conceptoRecibo = conceptoRecibo + "mes de " + montext + ";";
        cob_concepto.setText(conceptoRecibo);
        if (cob_monto.getText().equals("")) {
            montoActual = 0.0;
        } else {
            montoActual = Double.valueOf(cob_monto.getText());
        }

        if (c1.get(Calendar.MONTH) < m) {
            mora += 0;
            moraParcial = 0;
            System.out.println(String.valueOf(c1.get(Calendar.MONTH)) + " < " + String.valueOf(m));
        } else if (c1.get(Calendar.MONTH) == m && c1.get(Calendar.DAY_OF_MONTH) > 10) {
            mora += 5;
            moraParcial = 5;
            System.out.println(String.valueOf(c1.get(Calendar.MONTH)) + " == " + String.valueOf(m));
        } else if (c1.get(Calendar.MONTH) == m && c1.get(Calendar.DAY_OF_MONTH) <= 10) {
            mora += 0;
            moraParcial = 0;
            System.out.println(String.valueOf(c1.get(Calendar.MONTH)) + " = " + String.valueOf(m));
        } else if (c1.get(Calendar.MONTH) > m) {
            mora += 5;
            moraParcial = 5;
            System.out.println(String.valueOf(c1.get(Calendar.MONTH)) + " > " + String.valueOf(m));
        } else {
            mora += 0;
            moraParcial = 0;
        }//fin de la mora
        //revisando saldo
        if (registros[m + 1] == null || registros[m + 1].equals("")) {
            updatesql = updatesql + v + String.valueOf(montoMensual) + ",";
        } else {
            Double x = montoMensual + Double.parseDouble(registros[m + 1]);
            updatesql = updatesql + v + String.valueOf(x) + ",";
        }
        cob_monto.setText(String.valueOf(montoActual + montoMensual + moraParcial));
        cob_concepto.setText(cob_concepto.getText() + "mora:Q." + String.valueOf(mora));
    }
    public void printRecibo(String codigo, String servicio) {
        imprimir nuevo = new imprimir();
        if (servicio.equals("Colegio Juan Marcos")) {
            try {
                nuevo.mostarRecibo(codigo, servicio);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (servicio.equals("Colegio Cristiano Josue")) {
            try {
                nuevo.mostarJosue(codigo, servicio);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (servicio.equals("Colegio El Buen Camino")) {
            try {
                nuevo.mostarCEBC(codigo, servicio);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (servicio.equals("Otro")) {
            try {
                nuevo.mostarSimple(codigo, servicio);
            } catch (Exception e) {
                System.out.println(e);
            }
        }else {
            try {
                nuevo.mostarAcademia(codigo, servicio);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public void PanelOtrosCobros(){
        //limpiar todo
        otrosCobros_servicio.setSelectedIndex(0);
        otrosCobros_norecibo.setText("");
        otrosCobros_nombre.setText("");
        otrosCobros_monto.setText("");
        otrosCobros_detalle.setText("");
        //autonumerar recibo
        int temp = autoNumerarRecibos(String.valueOf(otrosCobros_servicio.getSelectedItem()));
        if (temp == 0) {
            otrosCobros_norecibo.setText("");
            //mando el foco a la casilla de numero de recibo 
            otrosCobros_norecibo.requestFocus();
        } else {
            otrosCobros_norecibo.setText(String.valueOf(temp));
            otrosCobros_nombre.requestFocus();
        }        
    }
    public void panelGastos() throws ClassNotFoundException {
        //limpiar
        fGastos_registro.setText("");
        fGastos_monto.setText("");
        fGastos_nombre.setText("");
        fGastos_concepto.setText("");
        fGastos_observacion.setText("");
        //limpiar tabla de gastos del dia
        try {
            DefaultTableModel tab = (DefaultTableModel)tablaGastos.getModel();
            int filasG = tablaGastos.getRowCount();
            
        } catch (Exception e) {
        }
        //autonumerar de nuevo
        try {
            conexion nc = new conexion();
            Connection n = nc.conectar();
            PreparedStatement stx = n.prepareStatement("SELECT MAX(norecibo) FROM registro WHERE gasto IS NOT NULL");
            ResultSet rs = stx.executeQuery();
            int x=0;
            while (rs.next()) {
                x=Integer.valueOf(rs.getString("MAX(norecibo)"));
            }
            fGastos_registro.setText(String.valueOf(x+1));
            fGastos_monto.requestFocus();
            nc.cerrarConexion();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void iniciarPanelAlumnos(String codigo,String ano){
        /***
         * limpiar todas las casillas cada vez que inicia el panel, incluyendo 
         * el arreglo registros, tambien el codigo y año para cuando se ejecute 
         * guardar e imprimir
         */
        code=codigo;
        year=ano;
        
        alumnosCodigo.setText("");
        alumnosNombres.setText("");
        alumnosApellido.setText("");
        alumnosMadre.setText("");
        alumnosPadre.setText("");
        alumnosDPImadre.setText("");
        alumnosDPIpadre.setText("");
        alumnosPROFmadre.setText("");
        alumnosPROFpadre.setText("");
        alumnosTelefono.setText("");
        alumnosMovil.setText("");
        alumnosDireccion.setText("");
        alumnosServicios.setSelectedItem(0);
        alumnosGrado.setSelectedItem(3);
        alumnosJornada.setSelectedItem(0);
        alumnosExpediente.setText("");
        alumnosInscripcion.setText("");
        alumnosMensualidad.setText("");
        alumnosNoCuotas.setText("");
        
        /***
         * obtener los datos del servidor y almacenarlos en el arreglo llamado
         * registros, luego imprimir los datos en pantalla para el usuario.
         */
        pestanas.setSelectedIndex(0);
        //obteniendo datos
        try {
            conexion nc = new conexion();
            Connection c = nc.conectar();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM inscripciones_"+ano+" WHERE cod='"+codigo+"'");
            while(rs.next()){
                //datos del alumno
                alumnosCodigo.setText(rs.getString("cod"));
                alumnosNombres.setText(rs.getString("nom"));
                alumnosApellido.setText(rs.getString("ape"));
                alumnosMadre.setText(rs.getString("m_nom"));
                alumnosPadre.setText(rs.getString("p_nom"));
                alumnosDPImadre.setText(rs.getString("m_dpi"));
                alumnosDPIpadre.setText(rs.getString("p_dpi"));
                alumnosPROFmadre.setText(rs.getString("m_prof"));
                alumnosPROFpadre.setText(rs.getString("p_prof"));
                //datos de contacto
                alumnosTelefono.setText(rs.getString("tel"));
                alumnosMovil.setText(rs.getString("mov"));
                alumnosDireccion.setText(rs.getString("addr"));
                //datos de la inscripcion
                alumnosServicios.setSelectedItem(rs.getString("serv"));
                alumnosGrado.setSelectedItem(rs.getString("grado"));
                alumnosJornada.setSelectedItem(rs.getString("jornada"));
                alumnosExpediente.setText(rs.getString("exped"));
                alumnosInscripcion.setText(rs.getString("ins"));
                alumnosMensualidad.setText(rs.getString("mens"));
                alumnosNoCuotas.setText(rs.getString("ncuotas"));
                alumnosObsc.setText(rs.getString("obsc"));
                alumnosEstado.setSelectedItem(rs.getString("estado"));
                //cerrando combos 
                btnAlumnosEditar.setEnabled(false);
                btnAlumnosEditar2.setEnabled(false);
                btnAlumnosEditar3.setEnabled(false);
                //determinar operadora para telefono 1
                
            }
            nc.cerrarConexion();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error:\n"+e);
        }
    }
    public void guardarPanelAlumnos(int op,String codigo,String ano){
        String sql;
        try {
            conexion nc = new conexion();
            Connection c = nc.conectar();
            switch (op) {
                case 1:
                    sql = "UPDATE inscripciones_" + ano + " SET nom=?,ape=?,m_nom=?,m_dpi=?,m_prof=?,p_nom=?,p_dpi=?,p_prof=? WHERE cod='" + codigo + "'";
                    try {
                        PreparedStatement st = c.prepareStatement(sql);
                        st.setString(1, alumnosNombres.getText());
                        st.setString(2, alumnosApellido.getText());
                        st.setString(3, alumnosMadre.getText());
                        st.setString(4, alumnosDPImadre.getText());
                        st.setString(5, alumnosPROFmadre.getText());
                        st.setString(6, alumnosPadre.getText());
                        st.setString(7, alumnosDPIpadre.getText());
                        st.setString(8, alumnosPROFpadre.getText());
                        st.executeUpdate();
                        nc.cerrarConexion();
                    } catch (Exception e) {
                        System.out.println("error:\n"+e);
                    }
                    break;
                case 2:
                    sql = "UPDATE inscripciones_" + ano + " SET tel=?,mov=?,addr=? WHERE cod='" + codigo + "'";
                    try {
                        PreparedStatement st = c.prepareStatement(sql);
                        st.setString(1, alumnosTelefono.getText());
                        st.setString(2, alumnosMovil.getText());
                        st.setString(3, alumnosDireccion.getText());
                        st.executeUpdate();
                        nc.cerrarConexion();
                    } catch (Exception e) {
                        System.out.println("error:\n"+e);
                    }
                    break;
                case 3:
                    sql = "UPDATE inscripciones_" + ano + " SET serv=?,grado=?,jornada=?,exped=?,ins=?,mens=?,ncuotas=?,obsc=?,estado=? WHERE cod='" + codigo + "'";
                    try {
                        PreparedStatement st = c.prepareStatement(sql);
                        st.setString(1, String.valueOf(alumnosServicios.getSelectedItem()));
                        st.setString(2, String.valueOf(alumnosGrado.getSelectedItem()));
                        st.setString(3, String.valueOf(alumnosJornada.getSelectedItem()));
                        st.setString(4, alumnosExpediente.getText());
                        st.setString(5, alumnosInscripcion.getText());
                        st.setString(6, alumnosMensualidad.getText());
                        st.setString(7, alumnosNoCuotas.getText());
                        st.setString(8, alumnosObsc.getText());
                        st.setString(9, String.valueOf(alumnosEstado.getSelectedItem()));
                        st.executeUpdate();
                        nc.cerrarConexion();
                    } catch (Exception e) {
                        System.out.println("error:\n"+e);
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        
        
        
    }
    public void iniciarHistorial(){
        /***
         * Este panel se encarga de mostrar todo el contenido de la tabla registros
         * con la capacidad de filtrar la información para poder entrar registros 
         * especificos sobre algunos filtros como No. de recibo, Nombre, Fecha
         */
        historialBuscar.setText("");
        filtrohistorialtabla1();
        limpiarhistorialtabla1();
        //llenar tabla
        String[] regis = new String[7];
        try {
            conexion nc = new conexion();
            Connection c = nc.conectar();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT norecibo, nom, concepto, date, ingreso, serv, obs FROM registro WHERE ingreso is not NULL");
            while (rs.next()){
                regis[0]=rs.getString("norecibo");
                regis[1]=rs.getString("nom");
                regis[2]=rs.getString("concepto");
                regis[3]=rs.getString("date");
                regis[4]=rs.getString("ingreso");
                regis[5]=rs.getString("serv");
                regis[6]=rs.getString("obs");
               ((DefaultTableModel)historialTable1.getModel()).addRow(regis);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error\n"+e);
        }
        historialBuscar.requestFocus();
    }
    public void limpiarhistorialtabla1(){
        try {
            DefaultTableModel modely = (DefaultTableModel)historialTable1.getModel();
            int nofila = historialTable1.getRowCount();
            for (int i=0; i<nofila; i++) {
                modely.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println("Error al limpiar la tabla:"+e);
        }
    }
    public void filtrohistorialtabla1(){
        TableRowSorter newrs = new TableRowSorter((DefaultTableModel)historialTable1.getModel());
        historialTable1.setRowSorter(newrs);
        newrs.setRowFilter(RowFilter.regexFilter("(?i)"+historialBuscar.getText(),new int[0]));
    }
    public void selecoperadora1(int num){
        try {
            if (num >= 40000000 && num <= 40130000) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 50000000 && num <= 50099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 50300000 && num <= 50699999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 51400000 && num <= 51449999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 51500000 && num <= 52099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 52000000 && num <= 52099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 53000000 && num <= 53099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 53140000 && num <= 53199999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 55210000 && num <= 55299999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 55500000 && num <= 55539999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 55800000 && num <= 55819999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57000000 && num <= 57099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57190000 && num <= 57199999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57300000 && num <= 57329999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57400000 && num <= 57899999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 58000000 && num <= 58099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 58190000 && num <= 58199999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 58800000 && num <= 59099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 58900000 && num <= 59099999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 59180000 && num <= 59199999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 59900000 && num <= 59999999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 58800000 && num <= 58899999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57200000 && num <= 57299999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 53200000 && num <= 53899999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 57330000 && num <= 57399999) {
                alumnosTelefono.setForeground(Color.blue);
            } else if (num >= 50100000 && num <= 50199999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 51100000 && num <= 51329999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 53100000 && num <= 53119999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 54100000 && num <= 54199999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 54700000 && num <= 54779999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 54800000 && num <= 54999999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 55100000 && num <= 55179999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 55310000 && num <= 55399999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 55430000 && num <= 55449999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 55540000 && num <= 55799999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 55820000 && num <= 55999999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 56100000 && num <= 56199999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 56900000 && num <= 56999999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 57100000 && num <= 57189999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 58100000 && num <= 58189999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 58100000 && num <= 58799999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 58200000 && num <= 58799999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 59100000 && num <= 59149999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 59200000 && num <= 59899999) {
                alumnosTelefono.setForeground(Color.red);
            } else if (num >= 50200000 && num <= 50299999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 50700000 && num <= 51099999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 52100000 && num <= 52209999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 52210000 && num <= 52899999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 52900000 && num <= 52999999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 53120000 && num <= 53139999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 53900000 && num <= 53999999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 54000000 && num <= 54099999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 55000000 && num <= 55099999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 55180000 && num <= 55199999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 55400000 && num <= 55429999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 55450000 && num <= 55499999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 56000000 && num <= 56099999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 56400000 && num <= 56899999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 57900000 && num <= 57999999) {
                alumnosTelefono.setForeground(Color.green);
            } else if (num >= 59150000 && num <= 59179999) {
                alumnosTelefono.setForeground(Color.green);
            } else{
                alumnosTelefono.setForeground(Color.black);
            }
            
            
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        groupOtrosCobros = new javax.swing.ButtonGroup();
        panelDeBotones = new javax.swing.JPanel();
        btnInscripciones = new javax.swing.JButton();
        btnColegiaturas = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnGastos = new javax.swing.JButton();
        btnAlumnos = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();
        btnOtrosCobros = new javax.swing.JButton();
        panelContenedor = new javax.swing.JPanel();
        PanelInfo = new javax.swing.JPanel();
        Marquesina = new javax.swing.JLabel();
        panelInscripciones = new javax.swing.JPanel();
        ins_servicio = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        ins_tCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ins_tNombres = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        ins_tApellidos = new javax.swing.JTextField();
        ins_tFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ins_tDireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ins_tTelefono = new javax.swing.JTextField();
        ins_tMovil = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        ins_cGrado = new javax.swing.JComboBox();
        ins_cJornada = new javax.swing.JComboBox();
        ins_tObservaciones = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        ins_tMadre = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ins_tDpiMadre = new javax.swing.JTextField();
        ins_tProfMadre = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ins_tPadre = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        ins_tDpiPadre = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        ins_tProfPadre = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        ins_checkRenap = new javax.swing.JCheckBox();
        ins_checkMeca = new javax.swing.JCheckBox();
        ins_checkCertAnterior = new javax.swing.JCheckBox();
        ins_checkDPrepri = new javax.swing.JCheckBox();
        ins_checkDSexto = new javax.swing.JCheckBox();
        ins_checkDTercero = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        ins_tOtrosDocumentos = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ins_tInscripcion = new javax.swing.JTextField();
        ins_tColegiatura = new javax.swing.JTextField();
        ins_btnInscribir = new javax.swing.JButton();
        ins_btnCancel = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        ins_btnImportar = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        ins_tObsc = new javax.swing.JTextField();
        ins_spinerNoCuotas = new javax.swing.JSpinner();
        panelCobros = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cob_tabla = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        cob_codigo = new javax.swing.JTextField();
        cob_nombres = new javax.swing.JTextField();
        cob_apellidos = new javax.swing.JTextField();
        cob_servicio = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cob_grado = new javax.swing.JTextField();
        cob_jornada = new javax.swing.JTextField();
        cob_colegiatura = new javax.swing.JTextField();
        cob_inscripcion = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        cob_recibo = new javax.swing.JTextField();
        cob_monto = new javax.swing.JTextField();
        cob_observaciones = new javax.swing.JTextField();
        cob_check_ins = new javax.swing.JCheckBox();
        cob_check_enero = new javax.swing.JCheckBox();
        cob_check_febrero = new javax.swing.JCheckBox();
        cob_check_marzo = new javax.swing.JCheckBox();
        cob_check_abril = new javax.swing.JCheckBox();
        cob_check_mayo = new javax.swing.JCheckBox();
        cob_check_junio = new javax.swing.JCheckBox();
        cob_check_julio = new javax.swing.JCheckBox();
        cob_check_agosto = new javax.swing.JCheckBox();
        cob_check_septiembre = new javax.swing.JCheckBox();
        cob_check_octubre = new javax.swing.JCheckBox();
        cob_check_noviembre = new javax.swing.JCheckBox();
        cob_check_clausura = new javax.swing.JCheckBox();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        cob_concepto = new javax.swing.JTextArea();
        jLabel32 = new javax.swing.JLabel();
        cob_btnAceptar = new javax.swing.JButton();
        cob_btnCancelar = new javax.swing.JButton();
        cob_btnOtronombre = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        alerta_bonbillo = new javax.swing.JLabel();
        iconState = new javax.swing.JLabel();
        panelVentas = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ven_tabla1 = new javax.swing.JTable();
        ven_btnSelectProduct = new javax.swing.JButton();
        ven_searchbox = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ven_tabla2 = new javax.swing.JTable();
        ven_info = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        ven_total = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        btn_historialVentas = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        panelGastos = new javax.swing.JPanel();
        panelInteriorGastos = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        fGastos_registro = new javax.swing.JTextField();
        fGastos_monto = new javax.swing.JTextField();
        fGastos_nombre = new javax.swing.JTextField();
        fGastos_concepto = new javax.swing.JTextField();
        fGastos_observacion = new javax.swing.JTextField();
        btn_GastosAceptar = new javax.swing.JButton();
        bnt_GastosCancelar = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaGastos = new javax.swing.JTable();
        panelAlumnos = new javax.swing.JPanel();
        pestanas = new javax.swing.JTabbedPane();
        alumnosPanel1 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        alumnosCodigo = new javax.swing.JTextField();
        alumnosNombres = new javax.swing.JTextField();
        alumnosApellido = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        alumnosMadre = new javax.swing.JTextField();
        alumnosPadre = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        alumnosDPImadre = new javax.swing.JTextField();
        alumnosDPIpadre = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        alumnosPROFmadre = new javax.swing.JTextField();
        alumnosPROFpadre = new javax.swing.JTextField();
        btnAlumnosEditar = new javax.swing.JButton();
        alumnosPanel2 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        alumnosTelefono = new javax.swing.JTextField();
        alumnosMovil = new javax.swing.JTextField();
        alumnosDireccion = new javax.swing.JTextField();
        btnAlumnosEditar2 = new javax.swing.JButton();
        alumnosPanel3 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        alumnosServicios = new javax.swing.JComboBox<>();
        alumnosGrado = new javax.swing.JComboBox<>();
        alumnosJornada = new javax.swing.JComboBox<>();
        alumnosExpediente = new javax.swing.JTextField();
        alumnosInscripcion = new javax.swing.JTextField();
        alumnosMensualidad = new javax.swing.JTextField();
        alumnosNoCuotas = new javax.swing.JTextField();
        btnAlumnosEditar3 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        alumnosObsc = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        alumnosEstado = new javax.swing.JComboBox<>();
        btnAlumnosCancelar = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        panelCarnet = new javax.swing.JPanel();
        panelHistorial = new javax.swing.JPanel();
        scrollPaneHistorial = new javax.swing.JScrollPane();
        historialTable1 = new javax.swing.JTable();
        historialBuscar = new javax.swing.JTextField();
        btnHistorialBuscar = new javax.swing.JButton();
        btnHistorialImprimir = new javax.swing.JButton();
        panelOtrosCobros = new javax.swing.JPanel();
        otrosCobros_servicio = new javax.swing.JComboBox();
        jLabel39 = new javax.swing.JLabel();
        detallesRecibo = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        otrosCobros_norecibo = new javax.swing.JTextField();
        otrosCobros_monto = new javax.swing.JTextField();
        otrosCobros_nombre = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        otrosCobros_detalle = new javax.swing.JTextArea();
        otrosCobros_btnAceptar = new javax.swing.JButton();
        otrosCobros_btnCancelar = new javax.swing.JButton();
        panelOculto = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        historial_tablaoculta = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control de Ingresos");
        setSize(new java.awt.Dimension(900, 600));

        panelDeBotones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnInscripciones.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnInscripciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inscribir3.png"))); // NOI18N
        btnInscripciones.setText("Inscripciones");
        btnInscripciones.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnInscripciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInscripcionesMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInscripcionesMouseEntered(evt);
            }
        });
        btnInscripciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInscripcionesActionPerformed(evt);
            }
        });

        btnColegiaturas.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnColegiaturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cobros.png"))); // NOI18N
        btnColegiaturas.setText("Colegiaturas");
        btnColegiaturas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnColegiaturas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnColegiaturasMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnColegiaturasMouseEntered(evt);
            }
        });
        btnColegiaturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColegiaturasActionPerformed(evt);
            }
        });

        btnVentas.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ventas2.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVentasMouseEntered(evt);
            }
        });
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnGastos.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnGastos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/gastos2.png"))); // NOI18N
        btnGastos.setText("Gastos");
        btnGastos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGastosMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGastosMouseEntered(evt);
            }
        });
        btnGastos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGastosActionPerformed(evt);
            }
        });

        btnAlumnos.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnAlumnos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/alumnos.png"))); // NOI18N
        btnAlumnos.setText("Alumnos");
        btnAlumnos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAlumnos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAlumnosMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAlumnosMouseEntered(evt);
            }
        });
        btnAlumnos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlumnosActionPerformed(evt);
            }
        });

        btnHistorial.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnHistorial.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/hisotiral2.png"))); // NOI18N
        btnHistorial.setText("Historial");
        btnHistorial.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHistorialMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHistorialMouseEntered(evt);
            }
        });
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        btnOtrosCobros.setFont(new java.awt.Font("Droid Sans", 1, 18)); // NOI18N
        btnOtrosCobros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cobros2.png"))); // NOI18N
        btnOtrosCobros.setText("Otros Cobros");
        btnOtrosCobros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOtrosCobros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOtrosCobrosMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOtrosCobrosMouseEntered(evt);
            }
        });
        btnOtrosCobros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtrosCobrosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDeBotonesLayout = new javax.swing.GroupLayout(panelDeBotones);
        panelDeBotones.setLayout(panelDeBotonesLayout);
        panelDeBotonesLayout.setHorizontalGroup(
            panelDeBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDeBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInscripciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnColegiaturas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGastos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlumnos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOtrosCobros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelDeBotonesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAlumnos, btnColegiaturas, btnGastos, btnHistorial, btnInscripciones, btnVentas});

        panelDeBotonesLayout.setVerticalGroup(
            panelDeBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDeBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnInscripciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnColegiaturas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVentas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGastos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlumnos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHistorial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOtrosCobros)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelContenedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelContenedor.setLayout(new java.awt.CardLayout());

        Marquesina.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        Marquesina.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Marquesina.setText("El Buen Camino");

        javax.swing.GroupLayout PanelInfoLayout = new javax.swing.GroupLayout(PanelInfo);
        PanelInfo.setLayout(PanelInfoLayout);
        PanelInfoLayout.setHorizontalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInfoLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(Marquesina, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PanelInfoLayout.setVerticalGroup(
            PanelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInfoLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(Marquesina, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        panelContenedor.add(PanelInfo, "card2");

        ins_servicio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Colegio El Buen Camino", "Colegio Cristiano Josue", "Academia Juan Marcos" }));
        ins_servicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ins_servicioItemStateChanged(evt);
            }
        });

        jLabel1.setText("Codigo");

        jLabel2.setText("Nombres");

        jLabel3.setText("Apellidos");

        jLabel4.setText("Nacimiento");

        jLabel5.setText("Dirección");

        jLabel6.setText("Telefóno");

        jLabel7.setText("Movil");

        jLabel8.setText("Grado");

        jLabel9.setText("Jornada");

        jLabel10.setText("Observaciones");

        jLabel11.setText("Nombre de la Madre");

        jLabel12.setText("DPI");

        jLabel13.setText("Profesión");

        jLabel14.setText("Nombre del Padre");

        jLabel15.setText("DPI");

        jLabel16.setText("Profesión");

        ins_checkRenap.setText("Certificado de RENAP");
        ins_checkRenap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkRenapActionPerformed(evt);
            }
        });

        ins_checkMeca.setText("Certificado de Mecanografia");
        ins_checkMeca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkMecaActionPerformed(evt);
            }
        });

        ins_checkCertAnterior.setText("Certificado Anterior");
        ins_checkCertAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkCertAnteriorActionPerformed(evt);
            }
        });

        ins_checkDPrepri.setText("DIP. Pre-Primaria");
        ins_checkDPrepri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkDPrepriActionPerformed(evt);
            }
        });

        ins_checkDSexto.setText("DIP. Sexto");
        ins_checkDSexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkDSextoActionPerformed(evt);
            }
        });

        ins_checkDTercero.setText("DIP. Tercero Basico");
        ins_checkDTercero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_checkDTerceroActionPerformed(evt);
            }
        });

        jLabel17.setText("Otros Documentos");

        jLabel18.setText("Monto de Inscripción");

        jLabel19.setText("Colegiatura");

        ins_btnInscribir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        ins_btnInscribir.setText("Inscribir");
        ins_btnInscribir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_btnInscribirActionPerformed(evt);
            }
        });

        ins_btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        ins_btnCancel.setText("Cancelar");
        ins_btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_btnCancelActionPerformed(evt);
            }
        });

        jLabel20.setText("Numero de Cuotas");

        ins_btnImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/download.png"))); // NOI18N
        ins_btnImportar.setText("Importar");
        ins_btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ins_btnImportarActionPerformed(evt);
            }
        });

        jLabel66.setText("Observaciones sobre los pagos");

        javax.swing.GroupLayout panelInscripcionesLayout = new javax.swing.GroupLayout(panelInscripciones);
        panelInscripciones.setLayout(panelInscripcionesLayout);
        panelInscripcionesLayout.setHorizontalGroup(
            panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ins_cGrado, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ins_cJornada, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(ins_tObservaciones)))
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(ins_tDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ins_tTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(ins_tMovil, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ins_tCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(ins_tNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addComponent(ins_tApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(ins_tFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(ins_tPadre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ins_tMadre, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(ins_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(ins_tDpiPadre, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                                .addComponent(ins_tDpiMadre)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addComponent(jLabel15))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel13)
                                            .addComponent(ins_tProfMadre, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                                            .addComponent(ins_tProfPadre)))
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ins_checkRenap)
                                            .addComponent(ins_checkDPrepri))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ins_checkDSexto)
                                            .addComponent(ins_checkMeca))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ins_checkDTercero)
                                            .addComponent(ins_checkCertAnterior)))
                                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ins_tInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(ins_tColegiatura, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel19))
                                        .addGap(30, 30, 30)
                                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel66)
                                            .addComponent(jLabel20)
                                            .addComponent(ins_tObsc, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(ins_spinerNoCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ins_tOtrosDocumentos))
                            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                                .addComponent(ins_btnImportar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ins_btnCancel)
                                .addGap(18, 18, 18)
                                .addComponent(ins_btnInscribir))))
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator3)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        panelInscripcionesLayout.setVerticalGroup(
            panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInscripcionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ins_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ins_tFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInscripcionesLayout.createSequentialGroup()
                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ins_tNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ins_tApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ins_tCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_tDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tMovil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_cGrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_cJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_tMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tDpiMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tProfMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_tPadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tDpiPadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tProfPadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_checkRenap)
                    .addComponent(ins_checkMeca)
                    .addComponent(ins_checkCertAnterior))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_checkDPrepri)
                    .addComponent(ins_checkDSexto)
                    .addComponent(ins_checkDTercero))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(ins_tOtrosDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_tInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_spinerNoCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel66))
                .addGap(6, 6, 6)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_tColegiatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ins_tObsc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addGroup(panelInscripcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ins_btnInscribir)
                    .addComponent(ins_btnCancel)
                    .addComponent(ins_btnImportar))
                .addContainerGap())
        );

        panelContenedor.add(panelInscripciones, "card3");

        cob_tabla.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        cob_tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Inscripcion", "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Clausura"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(cob_tabla);

        jLabel21.setText("Codigo");

        jLabel22.setText("Nombres");

        jLabel23.setText("Apellidos");

        cob_codigo.setEditable(false);

        cob_nombres.setEditable(false);

        cob_apellidos.setEditable(false);

        cob_servicio.setEditable(false);

        jLabel24.setText("Colegio");

        cob_grado.setEditable(false);

        cob_jornada.setEditable(false);

        cob_colegiatura.setEditable(false);

        cob_inscripcion.setEditable(false);

        jLabel25.setText("Grado");

        jLabel26.setText("Jornada");

        jLabel27.setText("Inscripción");

        jLabel28.setText("Colegiatura");

        jLabel29.setText("No.Recibo");

        jLabel30.setText("Monto");

        jLabel31.setText("Observaciones");

        cob_check_ins.setText("Inscripción");
        cob_check_ins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_insActionPerformed(evt);
            }
        });

        cob_check_enero.setText("Enero");
        cob_check_enero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_eneroActionPerformed(evt);
            }
        });

        cob_check_febrero.setText("Febrero");
        cob_check_febrero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_febreroActionPerformed(evt);
            }
        });

        cob_check_marzo.setText("Marzo");
        cob_check_marzo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_marzoActionPerformed(evt);
            }
        });

        cob_check_abril.setText("Abril");
        cob_check_abril.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_abrilActionPerformed(evt);
            }
        });

        cob_check_mayo.setText("Mayo");
        cob_check_mayo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_mayoActionPerformed(evt);
            }
        });

        cob_check_junio.setText("Junio");
        cob_check_junio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_junioActionPerformed(evt);
            }
        });

        cob_check_julio.setText("Julio");
        cob_check_julio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_julioActionPerformed(evt);
            }
        });

        cob_check_agosto.setText("Agosto");
        cob_check_agosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_agostoActionPerformed(evt);
            }
        });

        cob_check_septiembre.setText("Septiembre");
        cob_check_septiembre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_septiembreActionPerformed(evt);
            }
        });

        cob_check_octubre.setText("Octubre");
        cob_check_octubre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_octubreActionPerformed(evt);
            }
        });

        cob_check_noviembre.setText("Noviembre");
        cob_check_noviembre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_noviembreActionPerformed(evt);
            }
        });

        cob_check_clausura.setText("Clausura");
        cob_check_clausura.setEnabled(false);
        cob_check_clausura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_check_clausuraActionPerformed(evt);
            }
        });

        cob_concepto.setColumns(20);
        cob_concepto.setLineWrap(true);
        cob_concepto.setRows(5);
        jScrollPane2.setViewportView(cob_concepto);

        jLabel32.setText("Por concepto de:");

        cob_btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/printRecibo.png"))); // NOI18N
        cob_btnAceptar.setText("Registrar Cobro");
        cob_btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_btnAceptarActionPerformed(evt);
            }
        });

        cob_btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        cob_btnCancelar.setText("Cancelar");
        cob_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_btnCancelarActionPerformed(evt);
            }
        });

        cob_btnOtronombre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/print.png"))); // NOI18N
        cob_btnOtronombre.setText("Registrar Con Otro Nombre");
        cob_btnOtronombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cob_btnOtronombreActionPerformed(evt);
            }
        });

        alerta_bonbillo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alerta_bonbillo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/off_128.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alerta_bonbillo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(alerta_bonbillo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        iconState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout panelCobrosLayout = new javax.swing.GroupLayout(panelCobros);
        panelCobros.setLayout(panelCobrosLayout);
        panelCobrosLayout.setHorizontalGroup(
            panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCobrosLayout.createSequentialGroup()
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCobrosLayout.createSequentialGroup()
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelCobrosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelCobrosLayout.createSequentialGroup()
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cob_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel21))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cob_nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel22))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cob_apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel24)
                                            .addComponent(cob_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(panelCobrosLayout.createSequentialGroup()
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cob_check_marzo)
                                            .addComponent(cob_check_febrero)
                                            .addGroup(panelCobrosLayout.createSequentialGroup()
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cob_check_ins)
                                                    .addComponent(cob_check_enero))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cob_check_abril)
                                                    .addGroup(panelCobrosLayout.createSequentialGroup()
                                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(cob_check_mayo)
                                                            .addComponent(cob_check_junio)
                                                            .addComponent(cob_check_julio))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(cob_check_septiembre)
                                                            .addComponent(cob_check_octubre)
                                                            .addComponent(cob_check_agosto)
                                                            .addComponent(cob_check_noviembre))))
                                                .addGap(8, 8, 8)
                                                .addComponent(cob_check_clausura)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(iconState, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCobrosLayout.createSequentialGroup()
                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(panelCobrosLayout.createSequentialGroup()
                                                .addComponent(cob_btnOtronombre)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cob_btnCancelar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cob_btnAceptar))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCobrosLayout.createSequentialGroup()
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cob_grado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel25))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cob_jornada, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel26))
                                                .addGap(202, 202, 202)
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel27)
                                                    .addComponent(cob_inscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCobrosLayout.createSequentialGroup()
                                                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCobrosLayout.createSequentialGroup()
                                                        .addComponent(jLabel31)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(cob_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(panelCobrosLayout.createSequentialGroup()
                                                        .addGap(10, 10, 10)
                                                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(panelCobrosLayout.createSequentialGroup()
                                                                .addComponent(jLabel29)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cob_recibo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(172, 172, 172)
                                                                .addComponent(jLabel30)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(cob_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(jLabel32)
                                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGap(12, 12, 12))
                                    .addComponent(cob_colegiatura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCobrosLayout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelCobrosLayout.setVerticalGroup(
            panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCobrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_nombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_apellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cob_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCobrosLayout.createSequentialGroup()
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_grado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_jornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCobrosLayout.createSequentialGroup()
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_inscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_colegiatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCobrosLayout.createSequentialGroup()
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_check_ins)
                            .addComponent(cob_check_abril)
                            .addComponent(cob_check_agosto)
                            .addComponent(cob_check_clausura))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_check_enero)
                            .addComponent(cob_check_mayo)
                            .addComponent(cob_check_septiembre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_check_febrero)
                            .addComponent(cob_check_junio)
                            .addComponent(cob_check_octubre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cob_check_marzo)
                            .addComponent(cob_check_julio)
                            .addComponent(cob_check_noviembre)))
                    .addComponent(iconState, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCobrosLayout.createSequentialGroup()
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(cob_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cob_recibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(cob_observaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
                .addGroup(panelCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cob_btnAceptar)
                    .addComponent(cob_btnCancelar)
                    .addComponent(cob_btnOtronombre))
                .addContainerGap())
        );

        panelContenedor.add(panelCobros, "card4");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Para La Venta", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        ven_tabla1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Item", "Precio", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ven_tabla1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ven_tabla1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(ven_tabla1);
        if (ven_tabla1.getColumnModel().getColumnCount() > 0) {
            ven_tabla1.getColumnModel().getColumn(0).setPreferredWidth(60);
            ven_tabla1.getColumnModel().getColumn(0).setMaxWidth(60);
            ven_tabla1.getColumnModel().getColumn(2).setPreferredWidth(60);
            ven_tabla1.getColumnModel().getColumn(2).setMaxWidth(60);
            ven_tabla1.getColumnModel().getColumn(3).setPreferredWidth(60);
            ven_tabla1.getColumnModel().getColumn(3).setMaxWidth(60);
        }

        ven_btnSelectProduct.setText("Seleccionar");
        ven_btnSelectProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ven_btnSelectProductActionPerformed(evt);
            }
        });

        ven_searchbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ven_searchboxKeyTyped(evt);
            }
        });

        jLabel63.setText("Buscar:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ven_searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ven_btnSelectProduct))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ven_btnSelectProduct)
                    .addComponent(ven_searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Compras", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        ven_tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Item", "Cantidad", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ven_tabla2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ven_tabla2MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(ven_tabla2);
        if (ven_tabla2.getColumnModel().getColumnCount() > 0) {
            ven_tabla2.getColumnModel().getColumn(0).setPreferredWidth(60);
            ven_tabla2.getColumnModel().getColumn(0).setMaxWidth(60);
            ven_tabla2.getColumnModel().getColumn(2).setPreferredWidth(60);
            ven_tabla2.getColumnModel().getColumn(2).setMaxWidth(60);
            ven_tabla2.getColumnModel().getColumn(3).setPreferredWidth(60);
            ven_tabla2.getColumnModel().getColumn(3).setMaxWidth(60);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        ven_info.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ven_info.setForeground(new java.awt.Color(0, 102, 102));
        ven_info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ven_info.setText("info");

        jLabel33.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 51));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("TOTAL");

        ven_total.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        ven_total.setForeground(new java.awt.Color(51, 153, 0));
        ven_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ven_total.setText("MONTO");

        btn_historialVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/sumario.png"))); // NOI18N
        btn_historialVentas.setText("Historial");
        btn_historialVentas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btn_historialVentas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btn_historialVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_historialVentasActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        jButton1.setText("ACEPTAR");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        jButton2.setText("CANCELAR");
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_historialVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_historialVentas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelVentasLayout = new javax.swing.GroupLayout(panelVentas);
        panelVentas.setLayout(panelVentasLayout);
        panelVentasLayout.setHorizontalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVentasLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelVentasLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelVentasLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelVentasLayout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33)
                                    .addComponent(ven_total, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(ven_info, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelVentasLayout.setVerticalGroup(
            panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelVentasLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ven_total, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))
                    .addGroup(panelVentasLayout.createSequentialGroup()
                        .addComponent(ven_info, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelContenedor.add(panelVentas, "card5");

        panelInteriorGastos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel34.setText("Registro No.");

        jLabel35.setText("Monto:");

        jLabel36.setText("Nombre:");

        jLabel37.setText("Por motivo de:");

        jLabel38.setText("Observaciónes:");

        javax.swing.GroupLayout panelInteriorGastosLayout = new javax.swing.GroupLayout(panelInteriorGastos);
        panelInteriorGastos.setLayout(panelInteriorGastosLayout);
        panelInteriorGastosLayout.setHorizontalGroup(
            panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInteriorGastosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fGastos_concepto, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .addComponent(fGastos_observacion)
                    .addComponent(fGastos_nombre)
                    .addComponent(fGastos_monto, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(fGastos_registro))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelInteriorGastosLayout.setVerticalGroup(
            panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInteriorGastosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(fGastos_registro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(fGastos_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(fGastos_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(fGastos_concepto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInteriorGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(fGastos_observacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_GastosAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        btn_GastosAceptar.setText("Aceptar");
        btn_GastosAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GastosAceptarActionPerformed(evt);
            }
        });

        bnt_GastosCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        bnt_GastosCancelar.setText("Cancelar");
        bnt_GastosCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnt_GastosCancelarActionPerformed(evt);
            }
        });

        tablaGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Registro", "Nombre", "Descripcion", "Monto"
            }
        ));
        jScrollPane7.setViewportView(tablaGastos);

        javax.swing.GroupLayout panelGastosLayout = new javax.swing.GroupLayout(panelGastos);
        panelGastos.setLayout(panelGastosLayout);
        panelGastosLayout.setHorizontalGroup(
            panelGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGastosLayout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addGroup(panelGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelGastosLayout.createSequentialGroup()
                            .addComponent(bnt_GastosCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn_GastosAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(panelInteriorGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(154, Short.MAX_VALUE))
        );
        panelGastosLayout.setVerticalGroup(
            panelGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGastosLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(panelInteriorGastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_GastosAceptar)
                    .addComponent(bnt_GastosCancelar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelContenedor.add(panelGastos, "card6");

        jLabel44.setText("Codigo:");

        jLabel45.setText("Nombres:");

        jLabel46.setText("Apellidos:");

        alumnosCodigo.setEditable(false);

        alumnosNombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosNombresKeyTyped(evt);
            }
        });

        alumnosApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosApellidoKeyTyped(evt);
            }
        });

        jLabel57.setText("Madre:");

        jLabel58.setText("Padre:");

        alumnosMadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosMadreKeyTyped(evt);
            }
        });

        alumnosPadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosPadreKeyTyped(evt);
            }
        });

        jLabel59.setText("DPI:");

        jLabel60.setText("DPI:");

        alumnosDPImadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosDPImadreKeyTyped(evt);
            }
        });

        alumnosDPIpadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosDPIpadreKeyTyped(evt);
            }
        });

        jLabel61.setText("Profesión:");

        jLabel62.setText("Profesión:");

        alumnosPROFmadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosPROFmadreKeyTyped(evt);
            }
        });

        alumnosPROFpadre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosPROFpadreKeyTyped(evt);
            }
        });

        btnAlumnosEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reload.png"))); // NOI18N
        btnAlumnosEditar.setText("Actualizar");
        btnAlumnosEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAlumnosEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAlumnosEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlumnosEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout alumnosPanel1Layout = new javax.swing.GroupLayout(alumnosPanel1);
        alumnosPanel1.setLayout(alumnosPanel1Layout);
        alumnosPanel1Layout.setHorizontalGroup(
            alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel58)
                    .addComponent(jLabel57)
                    .addComponent(jLabel45)
                    .addComponent(jLabel44)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(alumnosApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(alumnosNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addComponent(alumnosCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alumnosPanel1Layout.createSequentialGroup()
                        .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(alumnosPadre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(alumnosMadre, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel59)
                            .addComponent(jLabel60))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alumnosDPImadre, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(alumnosDPIpadre, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(alumnosPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alumnosPROFpadre))
                    .addGroup(alumnosPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alumnosPROFmadre, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alumnosPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        alumnosPanel1Layout.setVerticalGroup(
            alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(alumnosCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(alumnosNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(alumnosApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alumnosPanel1Layout.createSequentialGroup()
                        .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel57)
                            .addComponent(alumnosMadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(alumnosPadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnosDPIpadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel62)
                            .addComponent(alumnosPROFpadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60)))
                    .addGroup(alumnosPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel59)
                        .addComponent(alumnosDPImadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel61)
                        .addComponent(alumnosPROFmadre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar)
                .addContainerGap())
        );

        pestanas.addTab("Datos Del Alumno", alumnosPanel1);

        jLabel47.setText("Telefono:");

        jLabel48.setText("Movil:");

        jLabel49.setText("Dirección:");

        alumnosTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosTelefonoKeyTyped(evt);
            }
        });

        alumnosMovil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosMovilKeyTyped(evt);
            }
        });

        alumnosDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosDireccionKeyTyped(evt);
            }
        });

        btnAlumnosEditar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reload.png"))); // NOI18N
        btnAlumnosEditar2.setText("Actualizar");
        btnAlumnosEditar2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAlumnosEditar2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAlumnosEditar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlumnosEditar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout alumnosPanel2Layout = new javax.swing.GroupLayout(alumnosPanel2);
        alumnosPanel2.setLayout(alumnosPanel2Layout);
        alumnosPanel2Layout.setHorizontalGroup(
            alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(alumnosTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addComponent(alumnosMovil))
                    .addComponent(alumnosDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(125, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alumnosPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        alumnosPanel2Layout.setVerticalGroup(
            alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alumnosTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alumnosMovil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alumnosDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar2)
                .addContainerGap())
        );

        pestanas.addTab("Contactos", alumnosPanel2);

        jLabel50.setText("Expediente:");

        jLabel51.setText("Inscripción:");

        jLabel52.setText("Mensualidad:");

        jLabel53.setText("No. Cuotas:");

        jLabel54.setText("Servicio:");

        jLabel55.setText("Grado:");

        jLabel56.setText("Jornada:");

        alumnosServicios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Colegio Juan Marcos", "Colegio Cristiano Josue", "Colegio El Buen Camino", "Academia Juan Marcos", "Liceo Roca De Los Siglos" }));
        alumnosServicios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                alumnosServiciosItemStateChanged(evt);
            }
        });

        alumnosGrado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Etapa 4", "Etapa 5", "Etapa 6", "Primero Primaria", "Segundo Primaria", "Tercero Primaria", "Cuarto Primaria", "Quinto Primaria", "Sexto Primaria", "Primero Basico", "Segundo Basico", "Tercero Basico", "4to Bachillerato", "5to Bachillerato", "4to Perito", "5to Perito", "6to Perito", "4to Secretariado", "5to Secretariado", "6to Secretariado", "Ciclo I", "Ciclo II", "Bachillerato Por Madurez" }));
        alumnosGrado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                alumnosGradoItemStateChanged(evt);
            }
        });

        alumnosJornada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matutina", "Vespertina", "Sabatina", "En Linea" }));
        alumnosJornada.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                alumnosJornadaItemStateChanged(evt);
            }
        });

        alumnosExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosExpedienteKeyTyped(evt);
            }
        });

        alumnosInscripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosInscripcionKeyTyped(evt);
            }
        });

        alumnosMensualidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosMensualidadKeyTyped(evt);
            }
        });

        alumnosNoCuotas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosNoCuotasKeyTyped(evt);
            }
        });

        btnAlumnosEditar3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reload.png"))); // NOI18N
        btnAlumnosEditar3.setText("Actualizar");
        btnAlumnosEditar3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAlumnosEditar3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAlumnosEditar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlumnosEditar3ActionPerformed(evt);
            }
        });

        jLabel64.setText("Observaciones sobre pagos:");

        alumnosObsc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                alumnosObscKeyTyped(evt);
            }
        });

        jLabel65.setText("Estado:");

        alumnosEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Inscrito", "Retirado", "Online", "Otro" }));
        alumnosEstado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                alumnosEstadoItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout alumnosPanel3Layout = new javax.swing.GroupLayout(alumnosPanel3);
        alumnosPanel3.setLayout(alumnosPanel3Layout);
        alumnosPanel3Layout.setHorizontalGroup(
            alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alumnosPanel3Layout.createSequentialGroup()
                        .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel54)
                            .addComponent(jLabel55)
                            .addComponent(jLabel56)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51))
                        .addGap(45, 45, 45)
                        .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alumnosJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnosGrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnosServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(alumnosExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(alumnosPanel3Layout.createSequentialGroup()
                                .addComponent(alumnosInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel52)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(alumnosMensualidad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(alumnosNoCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(alumnosPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alumnosObsc, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(alumnosPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alumnosEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(111, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, alumnosPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        alumnosPanel3Layout.setVerticalGroup(
            alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alumnosPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(alumnosServicios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(alumnosGrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(alumnosJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(alumnosExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(alumnosInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52)
                    .addComponent(alumnosMensualidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(alumnosNoCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(alumnosObsc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(alumnosPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(alumnosEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAlumnosEditar3)
                .addContainerGap())
        );

        pestanas.addTab("Sobre la inscripción", alumnosPanel3);

        btnAlumnosCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelBig.png"))); // NOI18N
        btnAlumnosCancelar.setText("Cancelar");
        btnAlumnosCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAlumnosCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAlumnosCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlumnosCancelarActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/printBig.png"))); // NOI18N
        jButton3.setText("Ficha");
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAlumnosLayout = new javax.swing.GroupLayout(panelAlumnos);
        panelAlumnos.setLayout(panelAlumnosLayout);
        panelAlumnosLayout.setHorizontalGroup(
            panelAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlumnosLayout.createSequentialGroup()
                .addComponent(pestanas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAlumnosLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(panelAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlumnosCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );
        panelAlumnosLayout.setVerticalGroup(
            panelAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAlumnosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pestanas, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlumnosCancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        panelContenedor.add(panelAlumnos, "card7");

        javax.swing.GroupLayout panelCarnetLayout = new javax.swing.GroupLayout(panelCarnet);
        panelCarnet.setLayout(panelCarnetLayout);
        panelCarnetLayout.setHorizontalGroup(
            panelCarnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        panelCarnetLayout.setVerticalGroup(
            panelCarnetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 626, Short.MAX_VALUE)
        );

        panelContenedor.add(panelCarnet, "card8");

        scrollPaneHistorial.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneHistorial.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        historialTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "recibo", "nombre", "concepto", "fecha", "monto", "servicio", "observaciones"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        historialTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrollPaneHistorial.setViewportView(historialTable1);
        if (historialTable1.getColumnModel().getColumnCount() > 0) {
            historialTable1.getColumnModel().getColumn(0).setMinWidth(80);
            historialTable1.getColumnModel().getColumn(0).setMaxWidth(80);
            historialTable1.getColumnModel().getColumn(1).setMinWidth(200);
            historialTable1.getColumnModel().getColumn(1).setMaxWidth(200);
            historialTable1.getColumnModel().getColumn(2).setMinWidth(400);
            historialTable1.getColumnModel().getColumn(2).setMaxWidth(400);
            historialTable1.getColumnModel().getColumn(3).setMinWidth(200);
            historialTable1.getColumnModel().getColumn(3).setMaxWidth(200);
            historialTable1.getColumnModel().getColumn(4).setMinWidth(80);
            historialTable1.getColumnModel().getColumn(4).setMaxWidth(80);
            historialTable1.getColumnModel().getColumn(5).setMinWidth(150);
            historialTable1.getColumnModel().getColumn(5).setMaxWidth(150);
            historialTable1.getColumnModel().getColumn(6).setMinWidth(400);
            historialTable1.getColumnModel().getColumn(6).setMaxWidth(400);
        }

        historialBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                historialBuscarKeyTyped(evt);
            }
        });

        btnHistorialBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/search16.png"))); // NOI18N
        btnHistorialBuscar.setText("Buscar");
        btnHistorialBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialBuscarActionPerformed(evt);
            }
        });

        btnHistorialImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/print16.png"))); // NOI18N
        btnHistorialImprimir.setText("Imprimir");
        btnHistorialImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelHistorialLayout = new javax.swing.GroupLayout(panelHistorial);
        panelHistorial.setLayout(panelHistorialLayout);
        panelHistorialLayout.setHorizontalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelHistorialLayout.createSequentialGroup()
                        .addComponent(scrollPaneHistorial)
                        .addContainerGap())
                    .addGroup(panelHistorialLayout.createSequentialGroup()
                        .addComponent(historialBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHistorialBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHistorialImprimir)
                        .addGap(200, 224, Short.MAX_VALUE))))
        );
        panelHistorialLayout.setVerticalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelHistorialLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(historialBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHistorialBuscar)
                    .addComponent(btnHistorialImprimir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPaneHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        panelContenedor.add(panelHistorial, "card9");

        otrosCobros_servicio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Colegio El Buen Camino", "Colegio Cristiano Josue", "Colegio Juan Marcos", "Academia Juan Marcos", "Otro" }));
        otrosCobros_servicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otrosCobros_servicioActionPerformed(evt);
            }
        });

        jLabel39.setText("Servicio Prestado Por:");

        detallesRecibo.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Recibo"));

        jLabel40.setText("No. Recibo:");

        jLabel41.setText("Monto:");

        jLabel42.setText("Nombre:");

        jLabel43.setText("Detalle:");

        otrosCobros_detalle.setColumns(20);
        otrosCobros_detalle.setRows(5);
        jScrollPane5.setViewportView(otrosCobros_detalle);

        javax.swing.GroupLayout detallesReciboLayout = new javax.swing.GroupLayout(detallesRecibo);
        detallesRecibo.setLayout(detallesReciboLayout);
        detallesReciboLayout.setHorizontalGroup(
            detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detallesReciboLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(otrosCobros_monto)
                    .addComponent(otrosCobros_norecibo, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(otrosCobros_nombre)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        detallesReciboLayout.setVerticalGroup(
            detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detallesReciboLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(otrosCobros_norecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(otrosCobros_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(otrosCobros_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detallesReciboLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        otrosCobros_btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        otrosCobros_btnAceptar.setText("Cobrar");
        otrosCobros_btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otrosCobros_btnAceptarActionPerformed(evt);
            }
        });

        otrosCobros_btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        otrosCobros_btnCancelar.setText("Cancelar");
        otrosCobros_btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otrosCobros_btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelOtrosCobrosLayout = new javax.swing.GroupLayout(panelOtrosCobros);
        panelOtrosCobros.setLayout(panelOtrosCobrosLayout);
        panelOtrosCobrosLayout.setHorizontalGroup(
            panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOtrosCobrosLayout.createSequentialGroup()
                .addGroup(panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelOtrosCobrosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(otrosCobros_btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otrosCobros_btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelOtrosCobrosLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel39)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(otrosCobros_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelOtrosCobrosLayout.createSequentialGroup()
                            .addGap(111, 111, 111)
                            .addComponent(detallesRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        panelOtrosCobrosLayout.setVerticalGroup(
            panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOtrosCobrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(otrosCobros_servicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58)
                .addComponent(detallesRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelOtrosCobrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otrosCobros_btnAceptar)
                    .addComponent(otrosCobros_btnCancelar))
                .addContainerGap(293, Short.MAX_VALUE))
        );

        panelContenedor.add(panelOtrosCobros, "card10");

        historial_tablaoculta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "recibo", "nombre", "concepto", "fecha", "monto", "servicio", "observaciones"
            }
        ));
        jScrollPane6.setViewportView(historial_tablaoculta);

        javax.swing.GroupLayout panelOcultoLayout = new javax.swing.GroupLayout(panelOculto);
        panelOculto.setLayout(panelOcultoLayout);
        panelOcultoLayout.setHorizontalGroup(
            panelOcultoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOcultoLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(240, Short.MAX_VALUE))
        );
        panelOcultoLayout.setVerticalGroup(
            panelOcultoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelOcultoLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        panelContenedor.add(panelOculto, "card11");

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuHambuerger.png"))); // NOI18N
        jMenu1.setText("Menu");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuPrinter.png"))); // NOI18N
        jMenuItem2.setText("Reponer Recibo");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator8);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuBasquet.png"))); // NOI18N
        jMenuItem3.setText("Inventario");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator7);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuShutdown.png"))); // NOI18N
        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuReports.png"))); // NOI18N
        jMenu2.setText("Reportes");
        jMenu2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuReport.png"))); // NOI18N
        jMenuItem4.setText("Reporte Diario");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);
        jMenu2.add(jSeparator9);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuReport.png"))); // NOI18N
        jMenuItem5.setText("Morosos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);
        jMenu2.add(jSeparator10);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuReport.png"))); // NOI18N
        jMenuItem7.setText("Notas De Cobro Por Jornada");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);
        jMenu2.add(jSeparator11);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/menuReport.png"))); // NOI18N
        jMenuItem6.setText("Periodos");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(panelDeBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 630, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelDeBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnInscripcionesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInscripcionesMouseEntered
        Marquesina.setText("<html><CENTER>Inscripción de alumnos para el siguiente ciclo escolar.</CENTER></html>");
    }//GEN-LAST:event_btnInscripcionesMouseEntered

    private void btnInscripcionesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnInscripcionesMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnInscripcionesMouseExited

    private void btnColegiaturasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnColegiaturasMouseEntered
        Marquesina.setText("<html><CENTER>Pago de colegiaturas y otros</CENTER></html>");
    }//GEN-LAST:event_btnColegiaturasMouseEntered

    private void btnColegiaturasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnColegiaturasMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnColegiaturasMouseExited

    private void btnVentasMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentasMouseEntered
        Marquesina.setText("<html><CENTER>Venta de uniformes y otros.</CENTER></html>");
    }//GEN-LAST:event_btnVentasMouseEntered

    private void btnGastosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGastosMouseEntered
        Marquesina.setText("<html><CENTER>Registro de gastos realizados</CENTER></html>");
    }//GEN-LAST:event_btnGastosMouseEntered

    private void btnGastosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGastosMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnGastosMouseExited

    private void btnAlumnosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlumnosMouseEntered
        Marquesina.setText("<html><CENTER>Información sobre los estudiantes inscritos, (Ficha de Inscripción)</CENTER></html>");
    }//GEN-LAST:event_btnAlumnosMouseEntered

    private void btnAlumnosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlumnosMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnAlumnosMouseExited

    private void btnHistorialMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistorialMouseEntered
        Marquesina.setText("<html><CENTER>Busqueda en registro de ingresos.</CENTER></html>");
    }//GEN-LAST:event_btnHistorialMouseEntered

    private void btnHistorialMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHistorialMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnHistorialMouseExited

    private void btnInscripcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInscripcionesActionPerformed
        escogerpanel("1");
    }//GEN-LAST:event_btnInscripcionesActionPerformed

    private void btnColegiaturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColegiaturasActionPerformed
        if (panelCobros.isVisible() != true) {
            buscar newdiagbus = new buscar(this, rootPaneCheckingEnabled);
            newdiagbus.panelchoose = "Cobros";
            if (newdiagbus.getCode()) {
                escogerpanel("2");
                try {
                    PanelCobros(newdiagbus.codigo, newdiagbus.year);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Error al llenar el panel de cobros");
                }
            } else {
                escogerpanel("0");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe Cancelar La Operación Anterior", "Atención!!!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnColegiaturasActionPerformed

    private void btnOtrosCobrosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtrosCobrosMouseExited
        Marquesina.setText("<html><CENTER>Bienvenido!...Aquí vera alguna información Importante</CENTER></html>");
    }//GEN-LAST:event_btnOtrosCobrosMouseExited

    private void btnOtrosCobrosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOtrosCobrosMouseEntered
        Marquesina.setText("<html><CENTER>Realizar Cobros que no son cuotas de 2014 a la fecha u Otro tipo de cobros.</CENTER></html>");
    }//GEN-LAST:event_btnOtrosCobrosMouseEntered

    private void btnOtrosCobrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtrosCobrosActionPerformed
        if(panelOtrosCobros.isVisible()==true){
            JOptionPane.showMessageDialog(null, "Debe cancelar la operacion en curso");
        }else{
            escogerpanel("8");
            PanelOtrosCobros();
        }
    }//GEN-LAST:event_btnOtrosCobrosActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        if (panelVentas.isVisible() != true) {
            buscar newdiagbus = new buscar(this, rootPaneCheckingEnabled);
            newdiagbus.panelchoose = "Ventas";
            if (newdiagbus.getCode()) {
                escogerpanel("3");
                try {
                    panelVentas(newdiagbus.codigo, newdiagbus.year);
                } catch (Exception ex) {
                    System.out.println("Error al llenar el panel de ventas");
                }
            } else {
                escogerpanel("0");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe Cancelar La Operación Anterior", "Atención!!!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnVentasActionPerformed

    private void ven_tabla1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ven_tabla1MouseClicked
        if (evt.getClickCount() == 1) {
            ven_btnSelectProduct.setEnabled(true);
        } else if (evt.getClickCount() == 2) {
            String[] venta = new String[4];
            int fila = ven_tabla1.getSelectedRow();
            Object[] numero = {"1","2","3","4","5"};
            JComboBox x = new JComboBox(numero);
            x.setEditable(true);
            String cantidad;
            cantidad = String.valueOf(JOptionPane.showInputDialog(null,"ingrese cantidad","Cantidad",JOptionPane.PLAIN_MESSAGE,null,numero,"1"));
            venta[0] = String.valueOf(ven_tabla1.getValueAt(fila, 0));
            venta[1] = String.valueOf(ven_tabla1.getValueAt(fila, 1));
            venta[2] = String.valueOf(cantidad);
            venta[3] = String.valueOf(Double.parseDouble((String) ven_tabla1.getValueAt(fila, 2)) * Integer.parseInt(cantidad));
            ((DefaultTableModel) ven_tabla2.getModel()).addRow(venta);
            Double acu = 0.0;
            for (int i = 0; i < ven_tabla2.getRowCount(); i++) {
                acu = acu + Double.parseDouble((String) ven_tabla2.getValueAt(i, 3));
            }

            DecimalFormat f = new DecimalFormat("¤#.##");
            ven_total.setText(String.valueOf(f.format(acu)));
        } else if (ven_tabla1.getSelectedRow() != 0) {
            ven_btnSelectProduct.setEnabled(true);
        }
    }//GEN-LAST:event_ven_tabla1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        escogerpanel("x");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ven_tabla2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ven_tabla2MouseClicked
        if (evt.getClickCount() == 2) {
            int fila = ven_tabla2.getSelectedRow();
            ((DefaultTableModel) ven_tabla2.getModel()).removeRow(fila);

            if (((DefaultTableModel) ven_tabla2.getModel()).getRowCount() == 0) {
                ven_total.setText("MONTO");
            } else {
                Double acu = 0.0;
                for (int i = 0; i < ven_tabla2.getRowCount(); i++) {
                    acu = acu + Double.parseDouble((String) ven_tabla2.getValueAt(i, 3));
                }
                DecimalFormat f = new DecimalFormat("¤#.##");
                ven_total.setText(String.valueOf(f.format(acu)));
            }
        }
    }//GEN-LAST:event_ven_tabla2MouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        reponerRecibo abrir = new reponerRecibo();
        abrir.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        inventario abrir = new inventario();
        abrir.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        imprimir abrirReporte = new imprimir();
        try {
            abrirReporte.mostrar_reporte("inglesos.jrxml");
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        menuMorosos abrir = new menuMorosos();
        abrir.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String concepto = "";
        int noFila = ven_tabla2.getRowCount();
        for (int i=0; i<noFila; i++) {
            concepto=concepto+ String.valueOf(ven_tabla2.getValueAt(i,2))+" "+String.valueOf(ven_tabla2.getValueAt(i, 1))+";";
        }
        Date fecha = new Date();
        String formato = "yyyy-MM-dd HH:mm:ss";
        DateFormat formato_fecha = new SimpleDateFormat(formato);
        Double total = 0.0;
        Double saldo;
        for(int i=0;i<ven_tabla2.getRowCount();i++){
                total=total+Double.parseDouble((String)ven_tabla2.getValueAt(i, 3));
            }
        Double abono= Double.valueOf(JOptionPane.showInputDialog("Ingrese Monto"));
        if(total == abono){
            saldo=0.0;
        }else{
            saldo= total-abono;
        }       
        String estado = JOptionPane.showInputDialog("Agregar alguna anotación");        
        //nueva autonumeracion
        int norecibo=autoNumerarRecibos("Otro");
        try {
            conexion nc = new conexion();
            Connection n = nc.conectar();
            //ingresa movimiento a libro de ventas 
            PreparedStatement st = n.prepareStatement("INSERT INTO ventas_" + year + " (cod,concepto,monto,saldo,fecha,estado)VALUES (?,?,?,?,?,?)");
            st.setString(1, registros[0]);
            st.setString(2, concepto);
            st.setString(3, String.valueOf(total));
            st.setString(4, String.valueOf(saldo));
            st.setString(5, formato_fecha.format(fecha));
            st.setString(6, estado);
            st.execute();

            //ingresa movimiento a registros
            PreparedStatement st1 = n.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,ingreso,serv,obs) VALUES (?,?,?,?,?,?,?)");
            st1.setString(1, String.valueOf(norecibo));
            st1.setString(2, registros[2] + " " + registros[1]);
            st1.setString(3, concepto + " Saldo:" + String.valueOf(saldo));
            st1.setString(4, formato_fecha.format(fecha));
            st1.setString(5, String.valueOf(abono));
            st1.setString(6, "Otro");
            st1.setString(7, "");
            st1.execute();

            //imprimir recibo
            printRecibo(String.valueOf(norecibo), "Otro");
            nc.cerrarConexion();
            escogerpanel("0");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_historialVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_historialVentasActionPerformed
        historialVentas abrir = new historialVentas(year,code,name);
        abrir.setVisible(true);
    }//GEN-LAST:event_btn_historialVentasActionPerformed

    private void btnGastosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGastosActionPerformed
        if(panelGastos.isVisible()==true){
            JOptionPane.showMessageDialog(null, "Debe cancelar la operacion en curso!!!");
        }else{
            escogerpanel("4");
        }
    }//GEN-LAST:event_btnGastosActionPerformed

    private void bnt_GastosCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnt_GastosCancelarActionPerformed
        escogerpanel("0");
    }//GEN-LAST:event_bnt_GastosCancelarActionPerformed

    private void btn_GastosAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GastosAceptarActionPerformed
        if (fGastos_monto.getText() != "") {
            Date hoy = new Date();
            String formato = "yyyy-MM-dd HH:mm:ss";
            DateFormat formato_fecha = new SimpleDateFormat(formato);
            try {
                conexion nc = new conexion();
                Connection n = nc.conectar();
                PreparedStatement st = n.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,obs,gasto) VALUES (?,?,?,?,?,?)");
                st.setString(1, fGastos_registro.getText());
                st.setString(2, fGastos_nombre.getText());
                st.setString(3, fGastos_concepto.getText());
                st.setString(4, String.valueOf(formato_fecha.format(hoy)));
                st.setString(5, fGastos_observacion.getText());
                st.setString(6, fGastos_monto.getText());
                st.execute();                
                nc.cerrarConexion();
                //recargando
                panelGastos();
            } catch (SQLException e) {
                System.out.println(e);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(guiPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un monto valido");
        }
    }//GEN-LAST:event_btn_GastosAceptarActionPerformed

    private void otrosCobros_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otrosCobros_btnCancelarActionPerformed
        escogerpanel("0");
    }//GEN-LAST:event_otrosCobros_btnCancelarActionPerformed

    private void otrosCobros_servicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otrosCobros_servicioActionPerformed
        int temp = autoNumerarRecibos(String.valueOf(otrosCobros_servicio.getSelectedItem()));
        if (temp == 0) {
            otrosCobros_norecibo.setText("");
            //mando el foco a la casilla de numero de recibo 
            otrosCobros_norecibo.requestFocus();
        } else {
            otrosCobros_norecibo.setText(String.valueOf(temp));
        }
    }//GEN-LAST:event_otrosCobros_servicioActionPerformed

    private void otrosCobros_btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otrosCobros_btnAceptarActionPerformed
        /**
         * antes de guardar cualquier registro en la BD hay que asegurarce que 
         * el campo de numero de recibo no esta en blanco, y acontinuacion 
         * hay que recolectar los datos, entre los cuales esta la fecha y hora
         */
        if (otrosCobros_norecibo.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe proporcionar un numero de recibo");
        } else {
            //fecha y hora
            Date fecha = new Date();
            String formato = "yyyy-MM-dd HH:mm:ss";
            DateFormat formato_fecha = new SimpleDateFormat(formato);
            //guardando el registro
            try {
                conexion nc = new conexion();
                Connection n = nc.conectar();
                //inicia guardado del registro
                PreparedStatement st = n.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,ingreso,serv,obs) VALUES (?,?,?,?,?,?,?)");
                st.setString(1, otrosCobros_norecibo.getText());
                st.setString(2, otrosCobros_nombre.getText());
                st.setString(3, otrosCobros_detalle.getText());
                st.setString(4, formato_fecha.format(fecha));
                st.setString(5, otrosCobros_monto.getText());
                st.setString(6, String.valueOf(otrosCobros_servicio.getSelectedItem()));
                st.setString(7, "");
                st.execute();
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e);
            }//fin del guardado del registro
            //inicia impresion de recibo
            try {
                printRecibo(otrosCobros_norecibo.getText(), String.valueOf(otrosCobros_servicio.getSelectedItem()));
            } catch (Exception e) {
                System.out.println(e);
            }//fin de la impresion de recibo
            //reiniciar panel de otros cobros
            PanelOtrosCobros();
        }
    }//GEN-LAST:event_otrosCobros_btnAceptarActionPerformed

    private void btnAlumnosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlumnosActionPerformed
       if (panelAlumnos.isVisible() != true) {
            buscar newdiagbus = new buscar(this, rootPaneCheckingEnabled);
            newdiagbus.panelchoose = "Alumnos";
            if (newdiagbus.getCode()) {
                escogerpanel("5");
                iniciarPanelAlumnos(newdiagbus.codigo, newdiagbus.year);
            } else {
                escogerpanel("0");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Debe Cancelar La Operación Anterior", "Atención!!!", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlumnosActionPerformed

    private void alumnosNombresKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosNombresKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosNombresKeyTyped

    private void alumnosApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosApellidoKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosApellidoKeyTyped

    private void alumnosMadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosMadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosMadreKeyTyped

    private void alumnosPadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosPadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosPadreKeyTyped

    private void alumnosDPImadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosDPImadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosDPImadreKeyTyped

    private void alumnosDPIpadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosDPIpadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosDPIpadreKeyTyped

    private void alumnosPROFmadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosPROFmadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosPROFmadreKeyTyped

    private void alumnosPROFpadreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosPROFpadreKeyTyped
        btnAlumnosEditar.setEnabled(true);
    }//GEN-LAST:event_alumnosPROFpadreKeyTyped

    private void alumnosTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosTelefonoKeyTyped
        btnAlumnosEditar2.setEnabled(true);
    }//GEN-LAST:event_alumnosTelefonoKeyTyped

    private void alumnosMovilKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosMovilKeyTyped
        btnAlumnosEditar2.setEnabled(true);
    }//GEN-LAST:event_alumnosMovilKeyTyped

    private void alumnosDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosDireccionKeyTyped
        btnAlumnosEditar2.setEnabled(true);
    }//GEN-LAST:event_alumnosDireccionKeyTyped

    private void alumnosExpedienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosExpedienteKeyTyped
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosExpedienteKeyTyped

    private void alumnosInscripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosInscripcionKeyTyped
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosInscripcionKeyTyped

    private void alumnosMensualidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosMensualidadKeyTyped
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosMensualidadKeyTyped

    private void alumnosNoCuotasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosNoCuotasKeyTyped
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosNoCuotasKeyTyped

    private void alumnosServiciosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_alumnosServiciosItemStateChanged
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosServiciosItemStateChanged

    private void btnAlumnosCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlumnosCancelarActionPerformed
        escogerpanel("0");
    }//GEN-LAST:event_btnAlumnosCancelarActionPerformed

    private void alumnosGradoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_alumnosGradoItemStateChanged
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosGradoItemStateChanged

    private void alumnosJornadaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_alumnosJornadaItemStateChanged
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosJornadaItemStateChanged

    private void btnAlumnosEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlumnosEditarActionPerformed
        guardarPanelAlumnos(1, code, year);
        iniciarPanelAlumnos(code, year);
    }//GEN-LAST:event_btnAlumnosEditarActionPerformed

    private void btnAlumnosEditar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlumnosEditar2ActionPerformed
        guardarPanelAlumnos(2, code, year);
        iniciarPanelAlumnos(code, year);
    }//GEN-LAST:event_btnAlumnosEditar2ActionPerformed

    private void btnAlumnosEditar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlumnosEditar3ActionPerformed
        guardarPanelAlumnos(3, code, year);
        iniciarPanelAlumnos(code, year);
    }//GEN-LAST:event_btnAlumnosEditar3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        imprimirFichas(String.valueOf(alumnosServicios.getSelectedItem()), String.valueOf(alumnosJornada.getSelectedItem()),alumnosCodigo.getText());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        if (panelHistorial.isVisible() != true) {
            escogerpanel("6");
            iniciarHistorial();
        }else{
            JOptionPane.showMessageDialog(null, "Se ha recargado la información", "Atención!!!", JOptionPane.INFORMATION_MESSAGE);
            iniciarHistorial();
        }
        
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void historialBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_historialBuscarKeyTyped
        filtrohistorialtabla1();
    }//GEN-LAST:event_historialBuscarKeyTyped

    private void btnHistorialBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialBuscarActionPerformed
        filtrohistorialtabla1();
    }//GEN-LAST:event_btnHistorialBuscarActionPerformed

    private void btnHistorialImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialImprimirActionPerformed
        //limpiar tabla oculta
        DefaultTableModel modelx = (DefaultTableModel)historial_tablaoculta.getModel();
        int nofila = historial_tablaoculta.getRowCount();
        for (int i=0;i<nofila;i++){
            modelx.removeRow(0);
        }
        //realizar copia a tabla oculta
        int fila = historialTable1.getRowCount();
        String[] data = new String[7];
        Double total=0.0;
        for (int i=0;i<fila;i++){
            //System.out.println(historialTable1.getValueAt(i, 0)+" "+historialTable1.getValueAt(i, 1)+" "+historialTable1.getValueAt(i, 2)+" "+historialTable1.getValueAt(i, 3)+" "+historialTable1.getValueAt(i, 4)+" "+historialTable1.getValueAt(i, 5)+" "+historialTable1.getValueAt(i, 6));
            data[0]=String.valueOf(historialTable1.getValueAt(i, 0));
            data[1]=String.valueOf(historialTable1.getValueAt(i, 1));
            data[2]=String.valueOf(historialTable1.getValueAt(i, 2));
            data[3]=String.valueOf(historialTable1.getValueAt(i, 3));
            data[4]=String.valueOf(historialTable1.getValueAt(i, 4));
            data[5]=String.valueOf(historialTable1.getValueAt(i, 5));
            if(String.valueOf(historialTable1.getValueAt(i, 6)).equals("null")){
                data[6]="";
            }else{
                data[6]=String.valueOf(historialTable1.getValueAt(i, 6));
            }
            total=total+Double.valueOf(String.valueOf(historialTable1.getValueAt(i, 4)));
            ((DefaultTableModel)historial_tablaoculta.getModel()).addRow(data);
        }
        DecimalFormat f = new DecimalFormat("¤#.##");
        Map parametros = new HashMap();
        parametros.put("titulo", "Historial De Ingresos");
        parametros.put("total", String.valueOf(f.format(total)));
        try {
            JasperReport reporte = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reportes/historial.jrxml"));
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros,new JRTableModelDataSource(historial_tablaoculta.getModel()));
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex);
        }
    }//GEN-LAST:event_btnHistorialImprimirActionPerformed

    private void ven_searchboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ven_searchboxKeyTyped
        TableRowSorter tablaOrdenada = new TableRowSorter((DefaultTableModel)ven_tabla1.getModel());
        ven_tabla1.setRowSorter(tablaOrdenada);
        tablaOrdenada.setRowFilter(RowFilter.regexFilter("(?i)" + ven_searchbox.getText(), new int[0]));
    }//GEN-LAST:event_ven_searchboxKeyTyped

    private void ven_btnSelectProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ven_btnSelectProductActionPerformed
        String[] venta=new String[4];
            int fila = ven_tabla1.getSelectedRow();
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("ingrese cantidad"));
            venta[0]=String.valueOf(ven_tabla1.getValueAt(fila, 0));
            venta[1]=String.valueOf(ven_tabla1.getValueAt(fila, 1));
            venta[2]=String.valueOf(cantidad);
            venta[3]=String.valueOf(Double.parseDouble((String)ven_tabla1.getValueAt(fila, 2))*cantidad);
            ((DefaultTableModel)ven_tabla2.getModel()).addRow(venta);
            Double acu=0.0;
            for(int i=0;i<ven_tabla2.getRowCount();i++){
                acu=acu+Double.parseDouble((String)ven_tabla2.getValueAt(i, 3));
            }
            DecimalFormat f = new DecimalFormat("¤#.##");
            ven_total.setText(String.valueOf(f.format(acu)));
    }//GEN-LAST:event_ven_btnSelectProductActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        imprimir nuevaNota = new imprimir();
        //obteniendo el parametro num
        Calendar x = Calendar.getInstance();
        int num=x.get(Calendar.MONTH)+1;
        //obenniendo el parametro fecha
        Format formatoMont = new SimpleDateFormat("MMMMM");
        String fechames = formatoMont.format(new Date());
        
        Format formatoDay = new SimpleDateFormat("d");
        String fechaday = formatoDay.format(new Date());
        
        Format formatoYear = new SimpleDateFormat("yyyy");
        String fechayear = formatoYear.format(new Date());
        
        String z="Guatemala,"+fechaday+" de "+fechames+" de "+fechayear;
        
        String mes="";
        //Obteniendo el parametro de jornada
        Object[] jornada = {"Matutina","Vespertina","Sabatina"};
        JComboBox combo = new JComboBox(jornada);
        combo.setEditable(true);
        String j=String.valueOf(JOptionPane.showInputDialog(null,"Ingrese la jornada","Jornada",JOptionPane.PLAIN_MESSAGE,null,jornada,"Matutina"));
        
        switch (num){
            case 1:
                mes="jan";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 2:
                mes="feb";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 3:
                mes="mar";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 4:
                mes="apr";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 5:
                mes="may";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 6:
                mes="jun";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 7:
                mes="jul";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 8:
                mes="ago";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 9:
                mes="sep";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 10:
                mes="oct";
                try {
                    nuevaNota.notasCobro2(String.valueOf(num), mes, z,j);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            case 11:
                JOptionPane.showMessageDialog(null, "Durante el mes de noviembre no se generan notas de cobro");
                break;
            case 12:
                JOptionPane.showMessageDialog(null, "Durante el mes de diciembre no se generan notas de cobro");
                break;        
        }//fin del case
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void alumnosObscKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_alumnosObscKeyTyped
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosObscKeyTyped

    private void alumnosEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_alumnosEstadoItemStateChanged
        btnAlumnosEditar3.setEnabled(true);
    }//GEN-LAST:event_alumnosEstadoItemStateChanged

    private void cob_btnOtronombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_btnOtronombreActionPerformed
        //obtener nuevo nombre para el recibo
        String nombrenuevo;
        nombrenuevo=JOptionPane.showInputDialog(null, "Ingrese Nombre","A que nombre desea el recibo", JOptionPane.QUESTION_MESSAGE);
        //obtener fecha
        Date hoy = new Date();
        SimpleDateFormat fechaSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //escribir los cambios en cuotas
        String sql="UPDATE cuotas_"+yearRecibo+" SET "+updatesql+" "+"obs=NULL"+" WHERE cod="+"'"+cob_codigo.getText()+"'";
        try {
            conexion objConexion = new conexion();
            Connection cn = objConexion.conectar();
            //ingreso a registro
            PreparedStatement st0 = cn.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,ingreso,serv,obs) VALUES (?,?,?,?,?,?,?)");
            st0.setString(1, cob_recibo.getText());
            st0.setString(2, nombrenuevo);
            st0.setString(3, cob_concepto.getText()+" del alumno (a):"+cob_nombres.getText()+" "+cob_apellidos.getText());
            st0.setString(4, fechaSimple.format(hoy));
            st0.setString(5, cob_monto.getText());
            st0.setString(6, cob_servicio.getText());
            st0.setString(7, cob_observaciones.getText());
            st0.execute();
            //ingreso a libro de cuotas
            PreparedStatement st = cn.prepareStatement(sql);
            st.executeUpdate();
            //cerrando conexion
            objConexion.cerrarConexion();
            JOptionPane.showMessageDialog(null, "Libro de cuotas ACTUALIZADO!!!");
            //imprimiendo recibo
            printRecibo(cob_recibo.getText(), cob_servicio.getText());
            PanelCobros(cob_codigo.getText(), yearRecibo);
        } catch (ClassNotFoundException | SQLException | HeadlessException e) {
            System.out.println(e);
            escogerpanel("x");
        }
    }//GEN-LAST:event_cob_btnOtronombreActionPerformed

    private void cob_btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_btnCancelarActionPerformed
        escogerpanel("x");
    }//GEN-LAST:event_cob_btnCancelarActionPerformed

    private void cob_btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_btnAceptarActionPerformed
        if (cob_recibo.getCaretPosition() > 0) {
            //obtener fecha
            Date hoy = new Date();
            SimpleDateFormat fechaSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //obtener numero de recibo
            int norecibox=autoNumerarRecibos(cob_servicio.getText());
            //escribir los cambios en cuotas
            String sql = "UPDATE cuotas_" + yearRecibo + " SET " + updatesql + " " + "obs=NULL" + " WHERE cod=" + "'" + cob_codigo.getText() + "'";
            try {
                conexion objConexion = new conexion();
                Connection cn = objConexion.conectar();
                //ingreso a registro
                PreparedStatement st0 = cn.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,ingreso,serv,obs) VALUES (?,?,?,?,?,?,?)");
                st0.setString(1, String.valueOf(norecibox));
                st0.setString(2, cob_nombres.getText() + " " + cob_apellidos.getText());
                st0.setString(3, cob_concepto.getText());
                st0.setString(4, fechaSimple.format(hoy));
                st0.setString(5, cob_monto.getText());
                st0.setString(6, cob_servicio.getText());
                st0.setString(7, cob_observaciones.getText());
                st0.execute();
                //ingreso a libro de cuotas
                PreparedStatement st = cn.prepareStatement(sql);
                st.executeUpdate();
                //cerrando conexion
                objConexion.cerrarConexion();
                JOptionPane.showMessageDialog(null, "Libro de cuotas ACTUALIZADO!!!");
                //imprimiendo recibo
                printRecibo(cob_recibo.getText(), cob_servicio.getText());
                PanelCobros(cob_codigo.getText(), yearRecibo);
            } catch (ClassNotFoundException | SQLException | HeadlessException e) {
                System.out.println(e);
                escogerpanel("x");
            }//fin del try
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar un numero de recibo");
        }//fin de la condicion
    }//GEN-LAST:event_cob_btnAceptarActionPerformed

    private void cob_check_clausuraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_clausuraActionPerformed
        cob_check_clausura.setEnabled(false);
        Double montoActual;
        Double montoMensual = Double.parseDouble(JOptionPane.showInputDialog("Ingrese Monto"));
        conceptoRecibo = conceptoRecibo + "Clausura de " + yearRecibo + ";";
        cob_concepto.setText(conceptoRecibo);
        //revisando saldo
        if(registros[0]==null || registros[0].equals("")){
            updatesql=updatesql+"clausura="+String.valueOf(montoMensual)+",";
        }else{
            Double x=montoMensual+Double.parseDouble(registros[0]);
            updatesql=updatesql+"clausura="+String.valueOf(x)+",";
        }
        if (cob_monto.getText().equals("")) {
            montoActual = 0.0;
        } else {
            montoActual = Double.valueOf(cob_monto.getText());
        }
        cob_monto.setText(String.valueOf(montoActual + montoMensual));
    }//GEN-LAST:event_cob_check_clausuraActionPerformed

    private void cob_check_noviembreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_noviembreActionPerformed
        cobrosCheckPressed(10);
    }//GEN-LAST:event_cob_check_noviembreActionPerformed

    private void cob_check_octubreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_octubreActionPerformed
        cobrosCheckPressed(9);
    }//GEN-LAST:event_cob_check_octubreActionPerformed

    private void cob_check_septiembreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_septiembreActionPerformed
        cobrosCheckPressed(8);
    }//GEN-LAST:event_cob_check_septiembreActionPerformed

    private void cob_check_agostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_agostoActionPerformed
        cobrosCheckPressed(7);
    }//GEN-LAST:event_cob_check_agostoActionPerformed

    private void cob_check_julioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_julioActionPerformed
        cobrosCheckPressed(6);
    }//GEN-LAST:event_cob_check_julioActionPerformed

    private void cob_check_junioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_junioActionPerformed
        cobrosCheckPressed(5);
    }//GEN-LAST:event_cob_check_junioActionPerformed

    private void cob_check_mayoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_mayoActionPerformed
        cobrosCheckPressed(4);
    }//GEN-LAST:event_cob_check_mayoActionPerformed

    private void cob_check_abrilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_abrilActionPerformed
        cobrosCheckPressed(3);
    }//GEN-LAST:event_cob_check_abrilActionPerformed

    private void cob_check_marzoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_marzoActionPerformed
        cobrosCheckPressed(2);
    }//GEN-LAST:event_cob_check_marzoActionPerformed

    private void cob_check_febreroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_febreroActionPerformed
        cobrosCheckPressed(1);
    }//GEN-LAST:event_cob_check_febreroActionPerformed

    private void cob_check_eneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_eneroActionPerformed
        cobrosCheckPressed(0);
    }//GEN-LAST:event_cob_check_eneroActionPerformed

    private void cob_check_insActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cob_check_insActionPerformed
        cob_check_ins.setEnabled(false);
        Double montoActual;
        Double montoMensual = Double.parseDouble(JOptionPane.showInputDialog("Ingrese Monto"));
        conceptoRecibo = conceptoRecibo + "inscripcion al ciclo " + yearRecibo + ";";
        cob_concepto.setText(conceptoRecibo);
        //revisando saldo
        if(registros[0]==null || registros[0].equals("")){
            updatesql=updatesql+"ins="+String.valueOf(montoMensual)+",";
        }else{
            Double x=montoMensual+Double.parseDouble(registros[0]);
            updatesql=updatesql+"ins="+String.valueOf(x)+",";
        }
        if (cob_monto.getText().equals("")) {
            montoActual = 0.0;
        } else {
            montoActual = Double.valueOf(cob_monto.getText());
        }
        cob_monto.setText(String.valueOf(montoActual + montoMensual));
    }//GEN-LAST:event_cob_check_insActionPerformed

    private void ins_btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_btnImportarActionPerformed
        /***
        * importar alumnos de años anteriores, abrira el menu de seleccion de
        * estudiantes y cargara la informacion de tal manera que llenara todos
        * los campos para realizar las modificaciones pertinentes antes de
        * guardar.
        */
        buscar newdiagbus = new buscar(this, rootPaneCheckingEnabled);
        if (newdiagbus.getCode()) {
            //iniciarPanelAlumnos(newdiagbus.codigo, newdiagbus.year);
            try {
                conexion nc = new conexion();
                Connection c = nc.conectar();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM inscripciones_"+newdiagbus.year+" WHERE cod='"+newdiagbus.codigo+"'");
                while(rs.next()){
                    ins_servicio.setSelectedItem(rs.getString("serv"));
                    ins_tCodigo.setText(rs.getString("cod"));
                    ins_tNombres.setText(rs.getString("nom"));
                    ins_tApellidos.setText(rs.getString("ape"));
                    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                    ins_tFecha.setDate(date.parse(rs.getString("fnac")));
                    ins_tDireccion.setText(rs.getString("addr"));
                    ins_tTelefono.setText(rs.getString("tel"));
                    ins_tMovil.setText(rs.getString("mov"));
                    ins_tObservaciones.setText(rs.getString("obs"));
                    ins_tMadre.setText(rs.getString("m_nom"));
                    ins_tDpiMadre.setText(rs.getString("m_dpi"));
                    ins_tProfMadre.setText(rs.getString("m_prof"));
                    ins_tPadre.setText(rs.getString("p_nom"));
                    ins_tDpiPadre.setText(rs.getString("p_dpi"));
                    ins_tProfPadre.setText(rs.getString("p_prof"));
                    DecimalFormat df = new DecimalFormat("#.##");
                    ins_tColegiatura.setText(String.valueOf(df.format(Double.parseDouble(rs.getString("mens"))+10)));
                    JOptionPane.showMessageDialog(null, "Ingrese Grado, Jornada, Observaciones e Inscripcion ANTES DE GUARDAR","Importante",  JOptionPane.WARNING_MESSAGE);
                }
                nc.cerrarConexion();
            } catch (Exception e) {
                System.out.println(e);
            }

        } else {
            escogerpanel("0");
        }
    }//GEN-LAST:event_ins_btnImportarActionPerformed

    private void ins_btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_btnCancelActionPerformed
        escogerpanel("x");
    }//GEN-LAST:event_ins_btnCancelActionPerformed

    private void ins_btnInscribirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_btnInscribirActionPerformed
        //inscripciones 2018
        //obtener fecha
        Date hoy = new Date();
        SimpleDateFormat fechaSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String codduplex = "";
        try {
            conexion objConexion = new conexion();
            Connection cn = objConexion.conectar();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT cod FROM inscripciones_2018 WHERE cod='" + ins_tCodigo.getText() + "'");
            while (rs.next()) {
                codduplex = rs.getString("cod");
            }
            objConexion.cerrarConexion();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        if (ins_tCodigo.getText().equals("")) {//seguro de duplicados
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo para el alumno");
        } else if (codduplex.equals(ins_tCodigo.getText())) {
            JOptionPane.showMessageDialog(null, "El alumno ya esta inscrito");
        } else {//inicia el proceso de inscripcion
            try {
                conexion objConexion = new conexion();
                Connection cn = objConexion.conectar();
                PreparedStatement st = cn.prepareStatement("INSERT INTO inscripciones_2018 (cod,nom,ape,fnac,addr,tel,mov,grado,jornada,obs,m_nom,m_dpi,m_prof,p_nom,p_dpi,p_prof,serv,ins,mens,ncuotas,exped,fins,obsc,estado) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                st.setString(1, ins_tCodigo.getText());
                st.setString(2, ins_tNombres.getText());
                st.setString(3, ins_tApellidos.getText());
                st.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(ins_tFecha.getDate()));//obtener fecha del chooser
                st.setString(5, ins_tDireccion.getText());
                st.setString(6, ins_tTelefono.getText());
                st.setString(7, ins_tMovil.getText());
                st.setString(8, String.valueOf(ins_cGrado.getSelectedItem()));//convertir el elemento seleccionado en string
                st.setString(9, String.valueOf(ins_cJornada.getSelectedItem()));
                st.setString(10, ins_tObservaciones.getText());
                st.setString(11, ins_tMadre.getText());
                st.setString(12, ins_tDpiMadre.getText());
                st.setString(13, ins_tProfMadre.getText());
                st.setString(14, ins_tPadre.getText());
                st.setString(15, ins_tDpiPadre.getText());
                st.setString(16, ins_tProfPadre.getText());
                st.setString(17, String.valueOf(ins_servicio.getSelectedItem()));
                st.setString(18, ins_tInscripcion.getText());
                st.setString(19, ins_tColegiatura.getText());
                st.setString(20, String.valueOf(ins_spinerNoCuotas.getValue()));
                st.setString(21, updatesql + ins_tOtrosDocumentos.getText());
                st.setString(22, fechaSimple.format(hoy));
                st.setString(23, ins_tObsc.getText());
                st.setString(24, "Inscrito");
                st.execute();
                //ingresamos el codigo al libro de cuotas
                PreparedStatement st0 = cn.prepareStatement("INSERT INTO cuotas_2018 (cod) VALUES ('" + ins_tCodigo.getText() + "')");
                st0.execute();
                JOptionPane.showMessageDialog(null, "Inscripcion realizada EXITOSAMENTE!");
                //impresion de ficha
                imprimirFichas(String.valueOf(ins_servicio.getSelectedItem()), String.valueOf(ins_cJornada.getSelectedItem()), ins_tCodigo.getText());
                objConexion.cerrarConexion();
                //limpiamos el panel de inscripcion
                iniciarPanelIncripcion();
            } catch (ClassNotFoundException | SQLException | HeadlessException e) {
                System.out.println(e);
                JOptionPane.showMessageDialog(null, "Error:\n"+e);
            }
        }//termina el seguro de duplicados

    }//GEN-LAST:event_ins_btnInscribirActionPerformed

    private void ins_checkDTerceroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkDTerceroActionPerformed
        ins_checkDTercero.setEnabled(false);
        updatesql=updatesql+"Diploma de Tercero Basico;";
    }//GEN-LAST:event_ins_checkDTerceroActionPerformed

    private void ins_checkDSextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkDSextoActionPerformed
        ins_checkDSexto.setEnabled(false);
        updatesql=updatesql+"Diploma de sexto;";
    }//GEN-LAST:event_ins_checkDSextoActionPerformed

    private void ins_checkDPrepriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkDPrepriActionPerformed
        ins_checkDPrepri.setEnabled(false);
        updatesql=updatesql+"Diploma de Pre-primaria;";
    }//GEN-LAST:event_ins_checkDPrepriActionPerformed

    private void ins_checkCertAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkCertAnteriorActionPerformed
        ins_checkCertAnterior.setEnabled(false);
        updatesql=updatesql+"Certificado Anterior;";
    }//GEN-LAST:event_ins_checkCertAnteriorActionPerformed

    private void ins_checkMecaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkMecaActionPerformed
        ins_checkMeca.setEnabled(false);
        updatesql=updatesql+"Certificado de mecanografia;";
    }//GEN-LAST:event_ins_checkMecaActionPerformed

    private void ins_checkRenapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ins_checkRenapActionPerformed
        ins_checkRenap.setEnabled(false);
        updatesql=updatesql+"Certificado de RENAP;";
    }//GEN-LAST:event_ins_checkRenapActionPerformed

    private void ins_servicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ins_servicioItemStateChanged
        insComboboxes(String.valueOf(ins_servicio.getSelectedItem()));
    }//GEN-LAST:event_ins_servicioItemStateChanged

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        formPeriodos nuevo = new formPeriodos();
        nuevo.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(guiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(guiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(guiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(guiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new guiPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Marquesina;
    private javax.swing.JPanel PanelInfo;
    private javax.swing.JLabel alerta_bonbillo;
    private javax.swing.JTextField alumnosApellido;
    private javax.swing.JTextField alumnosCodigo;
    private javax.swing.JTextField alumnosDPImadre;
    private javax.swing.JTextField alumnosDPIpadre;
    private javax.swing.JTextField alumnosDireccion;
    private javax.swing.JComboBox<String> alumnosEstado;
    private javax.swing.JTextField alumnosExpediente;
    private javax.swing.JComboBox<String> alumnosGrado;
    private javax.swing.JTextField alumnosInscripcion;
    private javax.swing.JComboBox<String> alumnosJornada;
    private javax.swing.JTextField alumnosMadre;
    private javax.swing.JTextField alumnosMensualidad;
    private javax.swing.JTextField alumnosMovil;
    private javax.swing.JTextField alumnosNoCuotas;
    private javax.swing.JTextField alumnosNombres;
    private javax.swing.JTextField alumnosObsc;
    private javax.swing.JTextField alumnosPROFmadre;
    private javax.swing.JTextField alumnosPROFpadre;
    private javax.swing.JTextField alumnosPadre;
    private javax.swing.JPanel alumnosPanel1;
    private javax.swing.JPanel alumnosPanel2;
    private javax.swing.JPanel alumnosPanel3;
    private javax.swing.JComboBox<String> alumnosServicios;
    private javax.swing.JTextField alumnosTelefono;
    private javax.swing.JButton bnt_GastosCancelar;
    private javax.swing.JButton btnAlumnos;
    private javax.swing.JButton btnAlumnosCancelar;
    private javax.swing.JButton btnAlumnosEditar;
    private javax.swing.JButton btnAlumnosEditar2;
    private javax.swing.JButton btnAlumnosEditar3;
    private javax.swing.JButton btnColegiaturas;
    private javax.swing.JButton btnGastos;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnHistorialBuscar;
    private javax.swing.JButton btnHistorialImprimir;
    private javax.swing.JButton btnInscripciones;
    private javax.swing.JButton btnOtrosCobros;
    private javax.swing.JButton btnVentas;
    private javax.swing.JButton btn_GastosAceptar;
    private javax.swing.JButton btn_historialVentas;
    private javax.swing.JTextField cob_apellidos;
    private javax.swing.JButton cob_btnAceptar;
    private javax.swing.JButton cob_btnCancelar;
    private javax.swing.JButton cob_btnOtronombre;
    private javax.swing.JCheckBox cob_check_abril;
    private javax.swing.JCheckBox cob_check_agosto;
    private javax.swing.JCheckBox cob_check_clausura;
    private javax.swing.JCheckBox cob_check_enero;
    private javax.swing.JCheckBox cob_check_febrero;
    private javax.swing.JCheckBox cob_check_ins;
    private javax.swing.JCheckBox cob_check_julio;
    private javax.swing.JCheckBox cob_check_junio;
    private javax.swing.JCheckBox cob_check_marzo;
    private javax.swing.JCheckBox cob_check_mayo;
    private javax.swing.JCheckBox cob_check_noviembre;
    private javax.swing.JCheckBox cob_check_octubre;
    private javax.swing.JCheckBox cob_check_septiembre;
    private javax.swing.JTextField cob_codigo;
    private javax.swing.JTextField cob_colegiatura;
    private javax.swing.JTextArea cob_concepto;
    private javax.swing.JTextField cob_grado;
    private javax.swing.JTextField cob_inscripcion;
    private javax.swing.JTextField cob_jornada;
    private javax.swing.JTextField cob_monto;
    private javax.swing.JTextField cob_nombres;
    private javax.swing.JTextField cob_observaciones;
    private javax.swing.JTextField cob_recibo;
    private javax.swing.JTextField cob_servicio;
    private javax.swing.JTable cob_tabla;
    private javax.swing.JPanel detallesRecibo;
    private javax.swing.JTextField fGastos_concepto;
    private javax.swing.JTextField fGastos_monto;
    private javax.swing.JTextField fGastos_nombre;
    private javax.swing.JTextField fGastos_observacion;
    private javax.swing.JTextField fGastos_registro;
    private javax.swing.ButtonGroup groupOtrosCobros;
    private javax.swing.JTextField historialBuscar;
    private javax.swing.JTable historialTable1;
    private javax.swing.JTable historial_tablaoculta;
    private javax.swing.JLabel iconState;
    private javax.swing.JButton ins_btnCancel;
    private javax.swing.JButton ins_btnImportar;
    private javax.swing.JButton ins_btnInscribir;
    private javax.swing.JComboBox ins_cGrado;
    private javax.swing.JComboBox ins_cJornada;
    private javax.swing.JCheckBox ins_checkCertAnterior;
    private javax.swing.JCheckBox ins_checkDPrepri;
    private javax.swing.JCheckBox ins_checkDSexto;
    private javax.swing.JCheckBox ins_checkDTercero;
    private javax.swing.JCheckBox ins_checkMeca;
    private javax.swing.JCheckBox ins_checkRenap;
    private javax.swing.JComboBox ins_servicio;
    private javax.swing.JSpinner ins_spinerNoCuotas;
    private javax.swing.JTextField ins_tApellidos;
    private javax.swing.JTextField ins_tCodigo;
    private javax.swing.JTextField ins_tColegiatura;
    private javax.swing.JTextField ins_tDireccion;
    private javax.swing.JTextField ins_tDpiMadre;
    private javax.swing.JTextField ins_tDpiPadre;
    private com.toedter.calendar.JDateChooser ins_tFecha;
    private javax.swing.JTextField ins_tInscripcion;
    private javax.swing.JTextField ins_tMadre;
    private javax.swing.JTextField ins_tMovil;
    private javax.swing.JTextField ins_tNombres;
    private javax.swing.JTextField ins_tObsc;
    private javax.swing.JTextField ins_tObservaciones;
    private javax.swing.JTextField ins_tOtrosDocumentos;
    private javax.swing.JTextField ins_tPadre;
    private javax.swing.JTextField ins_tProfMadre;
    private javax.swing.JTextField ins_tProfPadre;
    private javax.swing.JTextField ins_tTelefono;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JButton otrosCobros_btnAceptar;
    private javax.swing.JButton otrosCobros_btnCancelar;
    private javax.swing.JTextArea otrosCobros_detalle;
    private javax.swing.JTextField otrosCobros_monto;
    private javax.swing.JTextField otrosCobros_nombre;
    private javax.swing.JTextField otrosCobros_norecibo;
    private javax.swing.JComboBox otrosCobros_servicio;
    private javax.swing.JPanel panelAlumnos;
    private javax.swing.JPanel panelCarnet;
    private javax.swing.JPanel panelCobros;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JPanel panelDeBotones;
    private javax.swing.JPanel panelGastos;
    private javax.swing.JPanel panelHistorial;
    private javax.swing.JPanel panelInscripciones;
    private javax.swing.JPanel panelInteriorGastos;
    private javax.swing.JPanel panelOculto;
    private javax.swing.JPanel panelOtrosCobros;
    private javax.swing.JPanel panelVentas;
    private javax.swing.JTabbedPane pestanas;
    private javax.swing.JScrollPane scrollPaneHistorial;
    private javax.swing.JTable tablaGastos;
    private javax.swing.JButton ven_btnSelectProduct;
    private javax.swing.JLabel ven_info;
    private javax.swing.JTextField ven_searchbox;
    private javax.swing.JTable ven_tabla1;
    private javax.swing.JTable ven_tabla2;
    private javax.swing.JLabel ven_total;
    // End of variables declaration//GEN-END:variables
}
