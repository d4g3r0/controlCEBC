
package Formularios;

import Clases.imprimir;
import javax.swing.JOptionPane;

public class menuMorosos extends javax.swing.JFrame {

    public menuMorosos() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        op = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        cMes = new javax.swing.JComboBox();
        cGrado = new javax.swing.JComboBox();
        cano = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Libro de cuotas");

        jButton1.setText("Aceptar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre" }));

        cGrado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pre-Primaria", "Primaria", "Basico", "Diversificado", "Todos" }));

        cano.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2017", "2018", "2019", "2020" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cMes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cano, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cGrado, 0, 225, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cGrado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        imprimir nuevo  = new imprimir();
        String mes="";
        int ano;
        String query="";
        if(String.valueOf(cMes.getSelectedItem()).equals("Enero")){
            mes="jan";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Febrero")){
            mes="feb";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Marzo")){
            mes="mar";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Abril")){
            mes="apr";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Mayo")){
            mes="may";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Junio")){
            mes="jun";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Julio")){
            mes="jul";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Agosto")){
            mes="ago";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Septiembre")){
            mes="sep";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Octubre")){
            mes="oct";
        }else if(String.valueOf(cMes.getSelectedItem()).equals("Noviembre")){
            mes="nov";
        }
        
        //año de la busquda
        ano=Integer.parseInt(String.valueOf(cano.getSelectedItem()));
        if (String.valueOf(cGrado.getSelectedItem()).equals("Pre-Primaria")) {
            try {
                query="SELECT inscripciones_"+ano+".cod,nom,ape,grado,jornada,inscripciones_"+ano+".ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM inscripciones_"+ano+" INNER JOIN cuotas_"+ano+" ON inscripciones_"+ano+".cod=cuotas_"+ano+".cod WHERE grado='Etapa 4' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Etapa 5' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Etapa 6' AND "+mes+" IS NULL AND estado='inscrito' ORDER BY ape ";
                nuevo.mostarMorosos(query);
                dispose();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }else if (String.valueOf(cGrado.getSelectedItem()).equals("Primaria")){
            try {
                query="SELECT inscripciones_"+ano+".cod,nom,ape,grado,jornada,inscripciones_"+ano+".ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM inscripciones_"+ano+" INNER JOIN cuotas_"+ano+" ON inscripciones_"+ano+".cod=cuotas_"+ano+".cod WHERE grado='Primero Primaria' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Segundo Primaria' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Tercero Primaria' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Cuarto Primaria' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Quinto Primaria' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Sexto Primaria' AND "+mes+" IS NULL AND estado='inscrito' ORDER BY grado,ape ";
                nuevo.mostarMorosos(query);
                dispose();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }else if (String.valueOf(cGrado.getSelectedItem()).equals("Basico")){
            try {
                query="SELECT inscripciones_"+ano+".cod,nom,ape,grado,jornada,inscripciones_"+ano+".ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM inscripciones_"+ano+" INNER JOIN cuotas_"+ano+" ON inscripciones_"+ano+".cod=cuotas_"+ano+".cod WHERE grado='Primero Basico' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Segundo Basico' AND "+mes+" IS NULL AND estado='inscrito' OR grado='Tercero Basico' AND "+mes+" IS NULL AND estado='inscrito' ORDER BY grado,ape ";
                nuevo.mostarMorosos(query);
                dispose();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }else if (String.valueOf(cGrado.getSelectedItem()).equals("Diversificado")){
            try {
                query="SELECT inscripciones_"+ano+".cod,nom,ape,grado,jornada,inscripciones_"+ano+".ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM inscripciones_"+ano+" INNER JOIN cuotas_"+ano+" ON inscripciones_"+ano+".cod=cuotas_"+ano+".cod WHERE grado LIKE '%Bachillerato%' AND "+mes+" IS NULL AND estado='inscrito' OR grado LIKE '%Perito%' AND "+mes+" IS NULL AND estado='inscrito' OR grado LIKE '%Secretariado%' AND "+mes+" IS NULL AND estado='inscrito' ORDER BY grado,ape ";
                nuevo.mostarMorosos(query);
                dispose();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }else if (String.valueOf(cGrado.getSelectedItem()).equals("Todos")){
            try {
                query="SELECT inscripciones_"+ano+".cod,nom,ape,grado,jornada,inscripciones_"+ano+".ins,jan,feb,mar,apr,may,jun,jul,ago,sep,oct,nov FROM inscripciones_"+ano+" INNER JOIN cuotas_"+ano+" ON inscripciones_"+ano+".cod=cuotas_"+ano+".cod WHERE "+mes+" IS NULL AND estado='inscrito' ORDER BY grado,ape";
                nuevo.mostarMorosos(query);
                dispose();
            } catch (Exception e) {
                System.out.println("Error:" + e);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Intentelo otra vez.");
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
            java.util.logging.Logger.getLogger(menuMorosos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuMorosos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuMorosos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuMorosos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuMorosos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cGrado;
    private javax.swing.JComboBox cMes;
    private javax.swing.JComboBox<String> cano;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.ButtonGroup op;
    // End of variables declaration//GEN-END:variables
}
