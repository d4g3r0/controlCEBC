/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Formularios;

import Clases.conexion;
import Clases.imprimir;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author d4g3r0
 */
public class nVenta extends javax.swing.JDialog {

    /**
     * Creates new form nVenta
     */
    String[] registros = new String[13];
    public boolean vf;
    public nVenta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Nueva venta "+guiPrincipal.year+" del cliente "+guiPrincipal.name);
        panelVentas(guiPrincipal.code, guiPrincipal.year);
    }

    public void panelVentas(String c,String y){
        /*
         Primero procedere a limpiar todos los elementos del panel de ventas, de esta
         manera cada vez que alguien presione el voton de ventas, este iniciara un 
         nuevo procedimiento, espero que con eso minimize los herrores
         */
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
            ResultSet r = st.executeQuery("SELECT cod,ape,nom FROM inscripciones_" + guiPrincipal.year + " WHERE cod='" + guiPrincipal.code + "'");
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
        //name = registros[2] + " " + registros[1];
        ven_searchbox.requestFocus();
        btnDialogVentas_select.setEnabled(false);
        
    }
    
    public void buscarTabVentas(String query){
        TableRowSorter tablaOrdenada = new TableRowSorter((DefaultTableModel)ven_tabla1.getModel());
        ven_tabla1.setRowSorter(tablaOrdenada);
        tablaOrdenada.setRowFilter(RowFilter.regexFilter("(?i)" + query, new int[0]));
    }
    
    public void addCesta(){
        String[] venta = new String[4];
            int fila = ven_tabla1.getSelectedRow();
            Object[] numero = {"1","2","3","4","5"};
            JComboBox x = new JComboBox(numero);
            x.setEditable(true);
            String cantidad;
            cantidad = String.valueOf(JOptionPane.showInputDialog(null, "ingrese cantidad", "Cantidad", JOptionPane.PLAIN_MESSAGE, null, numero, "1"));
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
            //limpiar la tabla antes de salir
            ven_searchbox.setText("");
            buscarTabVentas(ven_searchbox.getText());
            ven_searchbox.requestFocus();
    }
    
    public int autoNumerarRecibos(String servicio){
        int z = 0;
        String x=null;
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
                z = Integer.valueOf(x) + 1;
            } else {
                z = Integer.valueOf(x) + 1;
            }
            objConexion.cerrarConexion();
        } catch (Exception e) {
            System.out.println("Error:\n" + e);
        }
        //retorno
            return(z);
    }
    
    public boolean getmark(){
        return vf;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        marquesina = new javax.swing.JPanel();
        ven_total = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        inventario = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ven_tabla1 = new javax.swing.JTable();
        jLabel63 = new javax.swing.JLabel();
        ven_searchbox = new javax.swing.JTextField();
        btnDialogVentas_select = new javax.swing.JButton();
        controles = new javax.swing.JPanel();
        btnDialogVentas_aceptar = new javax.swing.JButton();
        btnDialogVentas_cerrar = new javax.swing.JButton();
        canasta = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ven_tabla2 = new javax.swing.JTable();
        ven_info = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        marquesina.setLayout(new java.awt.BorderLayout());

        ven_total.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        ven_total.setForeground(new java.awt.Color(51, 153, 0));
        ven_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ven_total.setText("MONTO");
        marquesina.add(ven_total, java.awt.BorderLayout.PAGE_END);

        jLabel33.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 51));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("TOTAL");
        marquesina.add(jLabel33, java.awt.BorderLayout.PAGE_START);

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
        ven_tabla1.setSize(new java.awt.Dimension(300, 0));
        ven_tabla1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ven_tabla1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(ven_tabla1);
        if (ven_tabla1.getColumnModel().getColumnCount() > 0) {
            ven_tabla1.getColumnModel().getColumn(0).setPreferredWidth(50);
            ven_tabla1.getColumnModel().getColumn(0).setMaxWidth(60);
            ven_tabla1.getColumnModel().getColumn(2).setPreferredWidth(50);
            ven_tabla1.getColumnModel().getColumn(2).setMaxWidth(50);
            ven_tabla1.getColumnModel().getColumn(3).setPreferredWidth(50);
            ven_tabla1.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        jLabel63.setText("Buscar:");

        ven_searchbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ven_searchboxKeyTyped(evt);
            }
        });

        btnDialogVentas_select.setText("Seleccionar");
        btnDialogVentas_select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogVentas_selectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inventarioLayout = new javax.swing.GroupLayout(inventario);
        inventario.setLayout(inventarioLayout);
        inventarioLayout.setHorizontalGroup(
            inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inventarioLayout.createSequentialGroup()
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ven_searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDialogVentas_select))
                    .addGroup(inventarioLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inventarioLayout.setVerticalGroup(
            inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(inventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDialogVentas_select)
                    .addComponent(ven_searchbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63))
                .addContainerGap())
        );

        controles.setLayout(new java.awt.BorderLayout());

        btnDialogVentas_aceptar.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnDialogVentas_aceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        btnDialogVentas_aceptar.setText("ACEPTAR");
        btnDialogVentas_aceptar.setMaximumSize(new java.awt.Dimension(99, 36));
        btnDialogVentas_aceptar.setMinimumSize(new java.awt.Dimension(99, 36));
        btnDialogVentas_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogVentas_aceptarActionPerformed(evt);
            }
        });
        controles.add(btnDialogVentas_aceptar, java.awt.BorderLayout.PAGE_START);

        btnDialogVentas_cerrar.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        btnDialogVentas_cerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        btnDialogVentas_cerrar.setText("CANCELAR");
        btnDialogVentas_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDialogVentas_cerrarActionPerformed(evt);
            }
        });
        controles.add(btnDialogVentas_cerrar, java.awt.BorderLayout.PAGE_END);

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
        jScrollPane4.setViewportView(ven_tabla2);
        if (ven_tabla2.getColumnModel().getColumnCount() > 0) {
            ven_tabla2.getColumnModel().getColumn(0).setPreferredWidth(50);
            ven_tabla2.getColumnModel().getColumn(0).setMaxWidth(50);
            ven_tabla2.getColumnModel().getColumn(2).setPreferredWidth(60);
            ven_tabla2.getColumnModel().getColumn(2).setMaxWidth(60);
            ven_tabla2.getColumnModel().getColumn(3).setPreferredWidth(50);
            ven_tabla2.getColumnModel().getColumn(3).setMaxWidth(50);
        }

        javax.swing.GroupLayout canastaLayout = new javax.swing.GroupLayout(canasta);
        canasta.setLayout(canastaLayout);
        canastaLayout.setHorizontalGroup(
            canastaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canastaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        canastaLayout.setVerticalGroup(
            canastaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canastaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ven_info.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        ven_info.setForeground(new java.awt.Color(0, 102, 102));
        ven_info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ven_info.setText("info");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(marquesina, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(controles, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(canasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(ven_info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(canasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(marquesina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(controles, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ven_info, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ven_searchboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ven_searchboxKeyTyped
        buscarTabVentas(ven_searchbox.getText());
    }//GEN-LAST:event_ven_searchboxKeyTyped

    private void btnDialogVentas_selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogVentas_selectActionPerformed
        addCesta();
    }//GEN-LAST:event_btnDialogVentas_selectActionPerformed

    private void ven_tabla1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ven_tabla1MouseClicked
        if(evt.getClickCount() == 1){
            btnDialogVentas_select.setEnabled(true);
        }else if(evt.getClickCount() == 2){
            addCesta();
        }
    }//GEN-LAST:event_ven_tabla1MouseClicked

    private void btnDialogVentas_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogVentas_cerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnDialogVentas_cerrarActionPerformed

    private void btnDialogVentas_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDialogVentas_aceptarActionPerformed
        try {
            //declaracion de variables y valores a utilizar.
            String concepto = "";
            Date fecha = new Date();
            String formato = "yyyy-MM-dd HH:mm:ss";
            DateFormat formato_fecha = new SimpleDateFormat(formato);
            Double total = 0.0;
            Double saldo;
            Double abono = 0.0;
            //llenar la variable concepto.
            int noFila = ven_tabla2.getRowCount();
            for (int i = 0; i < noFila; i++) {
                concepto = concepto + String.valueOf(ven_tabla2.getValueAt(i, 2)) + " " + String.valueOf(ven_tabla2.getValueAt(i, 1)) + ";";
            }
            //comprobar que la variable concepto no esta vacia.
            if (concepto.equals("") || concepto.equals(" ")) {
                JOptionPane.showMessageDialog(null, "No hay ningun articulo en la lista de compras, CANCELANDO... Vuelva a intentarlo.");
                //volviendo al panel principal
                //escogerpanel("x");
            } else {
                for (int i = 0; i < ven_tabla2.getRowCount(); i++) {
                    total = total + Double.parseDouble((String) ven_tabla2.getValueAt(i, 3));
                }
                /**
                * A continuación se le pide al usuario que ingrese la cantidad
                * de dinero que el comprador deja y se comprueba si es el total
                * de la compra o si se trata de un abono a la misma.
                */

                abono = Double.valueOf(JOptionPane.showInputDialog("INGRESE EL MONTO"));

                //Comprobar si el monto esta vacio
                if (abono.equals("")) {
                    JOptionPane.showMessageDialog(null, "Debe ingresar un monto");
                    abono = Double.valueOf(JOptionPane.showInputDialog("INGRESE EL MONTO"));
                } else {
                    //Calculo de Saldo (si lo hay)
                    if (total == abono) {
                        saldo = 0.0;
                    } else {
                        saldo = total - abono;
                    }//fin de la comprobación del saldo
                    String estado = JOptionPane.showInputDialog("Agregar alguna anotación");
                    //Registrar Operación, nueva autonumeracion
                    int norecibo = autoNumerarRecibos("Otro");
                    conexion nc = new conexion();
                    Connection n = nc.conectar();
                    //ingresa movimiento a libro de ventas
                    PreparedStatement st = n.prepareStatement("INSERT INTO ventas_" + guiPrincipal.year + " (cod,concepto,monto,saldo,fecha,estado)VALUES (?,?,?,?,?,?)");
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
                    //Actualizar Stock
                    for (int i = 0; i < ven_tabla2.getRowCount(); i++) {
                        PreparedStatement stupdate = n.prepareStatement("UPDATE inventario set stock = stock-"+(ven_tabla2.getValueAt(i, 2))+" WHERE codigo="+ven_tabla2.getValueAt(i, 0));
                        stupdate.executeUpdate();
                    }
                    //imprimir recibo
                    try {
                        imprimir nuevo = new imprimir();
                        nuevo.mostarSimple(String.valueOf(norecibo), "Otro");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    nc.cerrarConexion();
                }//fin de la comprobación del saldo
            }//fin de la comprobación del concepto
        } catch (HeadlessException | ClassNotFoundException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error:"+e);
        }
        vf=true;
        dispose();
    }//GEN-LAST:event_btnDialogVentas_aceptarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(nVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(nVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(nVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(nVenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                nVenta dialog = new nVenta(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDialogVentas_aceptar;
    private javax.swing.JButton btnDialogVentas_cerrar;
    private javax.swing.JButton btnDialogVentas_select;
    private javax.swing.JPanel canasta;
    private javax.swing.JPanel controles;
    private javax.swing.JPanel inventario;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel marquesina;
    private javax.swing.JLabel ven_info;
    private javax.swing.JTextField ven_searchbox;
    private javax.swing.JTable ven_tabla1;
    private javax.swing.JTable ven_tabla2;
    private javax.swing.JLabel ven_total;
    // End of variables declaration//GEN-END:variables
}
