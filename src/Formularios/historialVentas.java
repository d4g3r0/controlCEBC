package Formularios;

import Clases.conexion;
import Clases.imprimir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class historialVentas extends javax.swing.JFrame {
//variables globales
    String codigo, ano, nombre;
    public historialVentas(String year, String code, String name) {
        initComponents();
        ano=year;
        codigo=code;
        nombre=name;
        this.setLocationRelativeTo(null);
        this.setTitle("Historial de Ventas "+ano+" del cliente "+nombre);
        llenarTabla(codigo, ano);
    }

    public void llenarTabla(String code,String year){
    //limpiar
        try {
            DefaultTableModel model = (DefaultTableModel)tabla.getModel();
            int filas = tabla.getRowCount();
            for(int i=0;i<filas;i++){
                model.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    //llenar
        try {
            conexion nc = new conexion();
            Connection n = nc.conectar();
            Statement st = n.createStatement();
            ResultSet rs = st.executeQuery("SELECT no,concepto,monto,saldo,fecha,estado FROM ventas_"+year+" WHERE cod='"+code+"'");
            String[] registros = new String[6];
            while(rs.next()){
                registros[0]=rs.getString("no");
                registros[1]=rs.getString("concepto");
                registros[2]=rs.getString("monto");
                registros[3]=rs.getString("saldo");
                registros[4]=rs.getString("fecha");
                registros[5]=rs.getString("estado");
                ((DefaultTableModel)tabla.getModel()).addRow(registros);
            }
            nc.cerrarConexion();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private historialVentas() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        btn_cobrarsaldoVentas = new javax.swing.JButton();
        btn_salirVentas = new javax.swing.JButton();
        btn_actualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Concepto", "Monto", "Saldo", "Fecha/hora", "estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);
        if (tabla.getColumnModel().getColumnCount() > 0) {
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(0).setMaxWidth(50);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(90);
            tabla.getColumnModel().getColumn(2).setMaxWidth(90);
            tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
            tabla.getColumnModel().getColumn(3).setMaxWidth(90);
            tabla.getColumnModel().getColumn(4).setPreferredWidth(190);
            tabla.getColumnModel().getColumn(4).setMaxWidth(190);
            tabla.getColumnModel().getColumn(5).setPreferredWidth(120);
            tabla.getColumnModel().getColumn(5).setMaxWidth(600);
        }

        btn_cobrarsaldoVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cobros_small.png"))); // NOI18N
        btn_cobrarsaldoVentas.setText("Cobrar");
        btn_cobrarsaldoVentas.setEnabled(false);
        btn_cobrarsaldoVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cobrarsaldoVentasActionPerformed(evt);
            }
        });

        btn_salirVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancel.png"))); // NOI18N
        btn_salirVentas.setText("Cancelar");
        btn_salirVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirVentasActionPerformed(evt);
            }
        });

        btn_actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ok.png"))); // NOI18N
        btn_actualizar.setText("Actualizar");
        btn_actualizar.setEnabled(false);
        btn_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 917, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_salirVentas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cobrarsaldoVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cobrarsaldoVentas)
                    .addComponent(btn_salirVentas)
                    .addComponent(btn_actualizar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_salirVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirVentasActionPerformed
        dispose();
    }//GEN-LAST:event_btn_salirVentasActionPerformed

    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMouseClicked
        int fila=tabla.getSelectedRow();
            if(tabla.getValueAt(fila,3).equals("0.00")){
                btn_cobrarsaldoVentas.setEnabled(false);
                btn_actualizar.setEnabled(true);
            }else{
                btn_cobrarsaldoVentas.setEnabled(true);   
                btn_actualizar.setEnabled(true);
            }
    }//GEN-LAST:event_tablaMouseClicked

    private void btn_cobrarsaldoVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cobrarsaldoVentasActionPerformed
        try {
            conexion nc = new conexion();
            Connection n = nc.conectar();            
            String abono = JOptionPane.showInputDialog("Ingrese Monto");
            int fila = tabla.getSelectedRow();
            String no = String.valueOf(tabla.getValueAt(fila, 0));
            double saldo = Double.parseDouble(String.valueOf(tabla.getValueAt(fila, 3)));
            double newsaldo = saldo - Double.parseDouble(abono);
            String estado = JOptionPane.showInputDialog("Estado");
            //autonumerar 
            String x = "";
            int norecibo = 0;
            Statement st0 = n.createStatement();
            ResultSet rs = st0.executeQuery("SELECT MAX(norecibo) FROM registro WHERE serv='Otro'");
            while (rs.next()) {
                x = rs.getString("MAX(norecibo)");
            }
            if (x == null) {
                x = "0";
            } else {
                norecibo = Integer.valueOf(x) + 1;
            }
            //obtener fecha
            Date hoy = new Date();
            SimpleDateFormat fechaSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //actualizar registro
            PreparedStatement st1 = n.prepareStatement("INSERT INTO registro (norecibo,nom,concepto,date,ingreso,serv,obs) VALUES (?,?,?,?,?,?,?)");
            st1.setString(1, String.valueOf(norecibo));
            st1.setString(2, nombre);
            st1.setString(3, String.valueOf(tabla.getValueAt(fila, 1))+"saldo Q."+newsaldo);
            st1.setString(4, fechaSimple.format(hoy));
            st1.setString(5, abono);
            st1.setString(6, "Otro");
            st1.setString(7, "");
            st1.execute();
            //actualizar libro de ventas
            PreparedStatement st = n.prepareStatement("UPDATE ventas_" + ano + " SET saldo="+newsaldo+", estado='"+estado+"' WHERE no='" +no+ "'");
            st.executeUpdate();
            //imprimir
            try {
                imprimir nuevo = new imprimir();
                nuevo.mostarSimple(String.valueOf(norecibo), "Otro");
            } catch (Exception e) {
                System.out.println(e);
            }
            nc.cerrarConexion();
            dispose();
        } catch (Exception e) {
            System.out.println(e);
        }
    }//GEN-LAST:event_btn_cobrarsaldoVentasActionPerformed

    private void btn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actualizarActionPerformed
        int fila = tabla.getSelectedRow();
        String no = String.valueOf(tabla.getValueAt(fila, 0));
        password nuevo = new password(this, rootPaneCheckingEnabled);
        nuevo.setVisible(true);
        if(nuevo.contrasena.equals("") || nuevo.contrasena == null){
            JOptionPane.showMessageDialog(null, "Debe introducir una contraseña");
        }else if(nuevo.contrasena.equals("Admin")){
            //obtener fecha
            Date hoy = new Date();
            SimpleDateFormat fechaSimple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String newestado;
            newestado=JOptionPane.showInputDialog(null, "Estado:","Actualizar Estado De La Compra",JOptionPane.QUESTION_MESSAGE);
            /**
             * Aqui va la programacion para que actualize el estado del alumno
             */
            try {
            conexion nc = new conexion();
            Connection n = nc.conectar();
            //actualizar libro de ventas
            PreparedStatement st = n.prepareStatement("UPDATE ventas_" + ano + " SET fecha='"+fechaSimple.format(hoy)+"', estado='"+newestado+"' WHERE no='"+no+"'");
            st.executeUpdate();
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error al actualizar:"+e);
            }
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Contaseña Incorrecta");
            dispose();
        }
    }//GEN-LAST:event_btn_actualizarActionPerformed

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
            java.util.logging.Logger.getLogger(historialVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(historialVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(historialVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(historialVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new historialVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_actualizar;
    private javax.swing.JButton btn_cobrarsaldoVentas;
    private javax.swing.JButton btn_salirVentas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
