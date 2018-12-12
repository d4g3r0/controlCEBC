
package Formularios;

import Clases.conexion;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Isaias Daniel Ramirez Juarez
 * 8va Avenida 3-20 col. Vasquez Zona 21
 * Telefono: 24493691
 */
public class estadoCuenta extends javax.swing.JFrame {
//variables globales
    String yearwork,codework;
    public estadoCuenta(String year, String code) {
        initComponents();
        this.setLocationRelativeTo(null);
        yearwork=year;
        codework=code;
        llenarTablas();
    }
    
    public void llenarTablas(){
        //Limpiar tablas
        try {
            DefaultTableModel tab1 = (DefaultTableModel)colegiaturas.getModel();
            DefaultTableModel tab2 = (DefaultTableModel)compras.getModel();
            int filesTab1 = colegiaturas.getRowCount();
            int filesTab2 = compras.getRowCount();
            for (int i = 0; i < filesTab1; i++) {
                tab1.removeRow(0);
            }
            for (int i = 0; i < filesTab2; i++) {
                tab2.removeRow(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //inicializar variables de suma
        double sumColegiaturas, sumCompras;
        sumColegiaturas=0;
        sumCompras=0;
        //llenando Tabla de colegiaturas
        try {
            conexion cn = new conexion();
            Connection newConection = cn.conectar();
            PreparedStatement stColegiaturas = newConection.prepareStatement("SELECT ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM cuotas_"+yearwork+" WHERE cod='"+codework+"'");
            ResultSet rsColegiaturas = stColegiaturas.executeQuery();
            String[] conceptos = new String[12];
            String[] registroColegiaturas=new String[12];
            ArrayList<String> data = new ArrayList<String>();
            while(rsColegiaturas.next()){
                data.add(rsColegiaturas.getString("ins"));
                data.add(rsColegiaturas.getString("jan"));
                data.add(rsColegiaturas.getString("feb"));
                data.add(rsColegiaturas.getString("mar"));
                data.add(rsColegiaturas.getString("apr"));
                data.add(rsColegiaturas.getString("may"));
                data.add(rsColegiaturas.getString("jun"));
                data.add(rsColegiaturas.getString("jul"));
                data.add(rsColegiaturas.getString("ago"));
                data.add(rsColegiaturas.getString("sep"));
                data.add(rsColegiaturas.getString("oct"));
                data.add(rsColegiaturas.getString("nov"));
            }
            cn.cerrarConexion();
            //valores de concepto
            conceptos[0]="Inscripción";
            conceptos[1]="Enero";
            conceptos[2]="Febrero";
            conceptos[3]="Marzo";
            conceptos[4]="Abril";
            conceptos[5]="Mayo";
            conceptos[6]="Junio";
            conceptos[7]="Julio";
            conceptos[8]="Agosto";
            conceptos[9]="Septiembre";
            conceptos[10]="Octubre";
            conceptos[11]="Noviembre";
            String[] registro = new String[2];
            for (int i = 0; i < data.size(); i++) {
                registro[0]=conceptos[i];
                registro[1]=data.get(i);
                ((DefaultTableModel)colegiaturas.getModel()).addRow(registro);
            }
            //suma de colegiaturas pagadas
            double x=0;
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i) == null){
                    x=0;
                }else{
                    x = Double.parseDouble(String.valueOf(data.get(i)));
                }
                sumColegiaturas=sumColegiaturas+x;
            }
                
        } catch (Exception e) {
            System.out.println(e);
        }//fin del llenado de la tabla colegiaturas
        //llenado tabla compras
        try {
            conexion cn = new conexion();
            Connection newConection = cn.conectar();
            PreparedStatement stCompras = newConection.prepareStatement("SELECT concepto,saldo FROM ventas_"+yearwork+" WHERE cod='"+codework+"' ");
            ResultSet rsCompras = stCompras.executeQuery();
            String[] registro = new String[2];
            while (rsCompras.next()){
                registro[0] = rsCompras.getString("concepto");
                registro[1] = rsCompras.getString("saldo");
                ((DefaultTableModel)compras.getModel()).addRow(registro);
                sumCompras = sumCompras+Double.parseDouble(rsCompras.getString("saldo"));
            }
            cn.cerrarConexion();
        } catch (Exception e) {
            System.out.println(e);
        }//fin del llenado de la tabla compras
        //calculo de saldos
        int nocuotas;
        double ins,mens,tColegiaturas;
        String nota="";
        nocuotas=0;
        ins=0;
        mens=0;
        try {
            conexion cn = new conexion();
            Connection newConection = cn.conectar();
            PreparedStatement st = newConection.prepareStatement("SELECT nom,ape,ins,mens,ncuotas FROM inscripciones_" + yearwork + " WHERE cod='" + codework + "' ");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                nocuotas = rs.getInt("ncuotas");
                ins = rs.getDouble("ins");
                mens = rs.getDouble("mens");
                labelNota.setText("<html>El alumno (a) "+rs.getString("nom")+" "+rs.getString("ape")+" segun su ficha de estudiante <br>del ciclo "+yearwork+", paga una inscripción unica de Q."+rs.getString("ins")+" y una mensualidad <br>de Q."+rs.getString("mens")+" durante "+rs.getString("ncuotas")+" meses.<br>Este estado de cuenta no incluye moras. </html>");
            }
            cn.cerrarConexion();
            tColegiaturas = ((nocuotas * mens) + ins);
            DecimalFormat dc = new DecimalFormat("0.00");
            labelColegiaturasTotales.setText("Servicios Educativos Totales Q."+String.valueOf(dc.format(tColegiaturas)));
            labelSumCompras.setText("Saldo Compras y Otros Q." + String.valueOf(dc.format(sumCompras)));
            labelSumColegiaturas.setText("Servicios Educativos Pagados Q." + String.valueOf(dc.format(sumColegiaturas)));
            labelSaldoTotal.setText("Saldo Total Q."+String.valueOf(dc.format(((tColegiaturas + sumCompras)-sumColegiaturas))));
        } catch (Exception e) {
            System.out.println(e);
        }//fin calculo de saldos
        
    }

    private estadoCuenta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        datos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        colegiaturas = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        compras = new javax.swing.JTable();
        labelSumColegiaturas = new javax.swing.JLabel();
        labelSumCompras = new javax.swing.JLabel();
        labelSaldoTotal = new javax.swing.JLabel();
        labelColegiaturasTotales = new javax.swing.JLabel();
        labelNota = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Estado de cuenta");
        setResizable(false);

        datos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Inscripción y Colegiaturas");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Compras y Otros");

        colegiaturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Cantidad"
            }
        ));
        jScrollPane1.setViewportView(colegiaturas);

        compras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Concepto", "Saldo"
            }
        ));
        jScrollPane2.setViewportView(compras);
        if (compras.getColumnModel().getColumnCount() > 0) {
            compras.getColumnModel().getColumn(0).setMinWidth(400);
            compras.getColumnModel().getColumn(0).setMaxWidth(400);
        }

        labelSumColegiaturas.setForeground(new java.awt.Color(255, 0, 0));
        labelSumColegiaturas.setText("Inscripción y Colegiaturas Pagadas");

        labelSumCompras.setForeground(new java.awt.Color(0, 0, 204));
        labelSumCompras.setText("Saldos de Compras y Otros");

        labelSaldoTotal.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        labelSaldoTotal.setForeground(new java.awt.Color(0, 153, 0));
        labelSaldoTotal.setText("Saldo Total");

        labelColegiaturasTotales.setForeground(new java.awt.Color(0, 0, 204));
        labelColegiaturasTotales.setText("Inscripciones y Colegiaturas Totales");

        labelNota.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNota.setText("Nota");
        labelNota.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/reportes/banner_blanco_cebc.jpg"))); // NOI18N

        javax.swing.GroupLayout datosLayout = new javax.swing.GroupLayout(datos);
        datos.setLayout(datosLayout);
        datosLayout.setHorizontalGroup(
            datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                    .addGroup(datosLayout.createSequentialGroup()
                        .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelSaldoTotal)
                            .addComponent(labelSumColegiaturas)
                            .addComponent(labelSumCompras)
                            .addComponent(labelColegiaturasTotales)
                            .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                            .addComponent(labelNota, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        datosLayout.setVerticalGroup(
            datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(datosLayout.createSequentialGroup()
                        .addComponent(labelColegiaturasTotales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSumCompras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSumColegiaturas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSaldoTotal))
                    .addComponent(labelNota, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/printRecibo.png"))); // NOI18N
        jButton1.setText("IMPRIMIR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(354, 354, 354)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Imprimir Estado");
        job.setPrintable(new Printable(){
            public int print(Graphics pg, PageFormat pf, int pageNum){
                if(pageNum>0){
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D)pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.7, 0.7);
                datos.paint(g2);
                return Printable.PAGE_EXISTS;
            }
        });
        boolean ok = job.printDialog();
        if(ok){
            try {
                job.print();
            } catch (PrinterException e) {
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(estadoCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(estadoCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(estadoCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(estadoCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new estadoCuenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable colegiaturas;
    private javax.swing.JTable compras;
    private javax.swing.JPanel datos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelColegiaturasTotales;
    private javax.swing.JLabel labelNota;
    private javax.swing.JLabel labelSaldoTotal;
    private javax.swing.JLabel labelSumColegiaturas;
    private javax.swing.JLabel labelSumCompras;
    // End of variables declaration//GEN-END:variables
}
