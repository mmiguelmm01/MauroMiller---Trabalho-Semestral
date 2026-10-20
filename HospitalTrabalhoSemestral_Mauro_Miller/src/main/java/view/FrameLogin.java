/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 *
 * @author Miller Matsimbe
 */
public class FrameLogin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameLogin.class.getName());

    /**
     * Creates new form FrameLogin
     */
    public FrameLogin() {
        initComponents();
        setLocationRelativeTo(null);

    txtUsuarioLogin.setBorder(
        javax.swing.BorderFactory.createMatteBorder(
            0, 0, 1, 0, Color.GRAY
        )
    );

    pwSenhaLogin.setBorder(
        javax.swing.BorderFactory.createMatteBorder(
            0, 0, 1, 0, Color.GRAY
        )
    );

    txtUsuarioLogin.setBackground(getBackground());
    pwSenhaLogin.setBackground(getBackground());

    lblLedUsuario.setForeground(Color.GRAY);
    lblLedSenha.setForeground(Color.GRAY);

    btnEntrarLogin.addActionListener(e -> autenticar());
    btnCancelarLogin.addActionListener(e -> cancelarLogin());

    configurarValidacao();
    configurarTeclado();
    }
    
    private void autenticar() {
    String usuario = txtUsuarioLogin.getText()
            .trim()
            .toLowerCase();

    String senha = new String(
            pwSenhaLogin.getPassword()
    );

    if (usuario.isEmpty() || senha.isEmpty()) {
        JOptionPane.showMessageDialog(
                this,
                "Preencha o utilizador e a senha.",
                "Campos obrigatórios",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (usuario.equals("mauro@gmail.com")
            && senha.equals("manhica")) {

        abrirTela(new FrameAdmin());

    } else if (usuario.equals("miller@gmail.com")
            && senha.equals("matsimbe")) {

        abrirTela(new FrameMedico());

    } else if (usuario.equals("enfermeiro@gmail.com")
            && senha.equals("enfermeiro")) {

        FrameRecepcionista tela = new FrameRecepcionista();
        tela.definirPerfil("Enfermeiro");
        abrirTela(tela);

    } else if (usuario.equals("recepcionista@gmail.com")
            && senha.equals("recepcionista")) {

        
        FrameRecepcionista tela = new FrameRecepcionista();
        tela.definirPerfil("Recepcionista");
        abrirTela(tela);

    } else {
        lblLedUsuario.setForeground(Color.RED);
        lblLedSenha.setForeground(Color.RED);

        JOptionPane.showMessageDialog(
                this,
                "Utilizador ou senha incorrectos.",
                "Falha no login",
                JOptionPane.ERROR_MESSAGE
        );

        pwSenhaLogin.setText("");
        pwSenhaLogin.requestFocusInWindow();
    }
    }
    
    private void abrirTela(javax.swing.JFrame tela) {
    tela.setLocationRelativeTo(null);
    tela.setVisible(true);
    dispose();
    }
    
    private void configurarValidacao() {

    txtUsuarioLogin.getDocument().addDocumentListener(
        new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarLEDs();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarLEDs();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarLEDs();
            }
        }
    );

    pwSenhaLogin.getDocument().addDocumentListener(
        new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarLEDs();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarLEDs();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarLEDs();
            }
        }
    );
    }
    
    private void atualizarLEDs() {

    String usuario = txtUsuarioLogin.getText()
            .trim()
            .toLowerCase();

    String senha = new String(
            pwSenhaLogin.getPassword()
    );

    String[] usuarios = {
        "mauro@gmail.com",
        "miller@gmail.com",
        "enfermeiro@gmail.com",
        "recepcionista@gmail.com"
    };

    String[] senhas = {
        "manhica",
        "matsimbe",
        "enfermeiro",
        "recepcionista"
    };

    Color cinzento = Color.GRAY;
    Color verde = new Color(0, 170, 0);
    Color vermelho = Color.RED;

    // Validar o campo do utilizador
    int indice = -1;

    if (usuario.isEmpty()) {
        txtUsuarioLogin.setBorder(
                javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 2, 0, cinzento
                )
        );
    } else {
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].startsWith(usuario)) {
                indice = i;
                break;
            }
        }

        txtUsuarioLogin.setBorder(
                javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 2, 0,
                        indice >= 0 ? verde : vermelho
                )
        );
    }

    // Validar o campo da senha
    if (senha.isEmpty()) {
        pwSenhaLogin.setBorder(
                javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 2, 0, cinzento
                )
        );
    } else if (indice >= 0
            && senhas[indice].startsWith(senha)) {

        pwSenhaLogin.setBorder(
                javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 2, 0, verde
                )
        );

    } else {
        pwSenhaLogin.setBorder(
                javax.swing.BorderFactory.createMatteBorder(
                        0, 0, 2, 0, vermelho
                )
        );
    }
    }
    
    private void configurarTeclado() {

    // Enter no campo da senha: entrar no sistema
    pwSenhaLogin.addActionListener(e -> autenticar());

    // Esc: cancelar e limpar o formulário
    getRootPane().getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
    ).put(
            KeyStroke.getKeyStroke("ESCAPE"),
            "cancelarLogin"
    );

    getRootPane().getActionMap().put(
            "cancelarLogin",
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelarLogin();
                }
            }
    );

    // Ctrl + L: limpar os campos
    getRootPane().getInputMap(
            JComponent.WHEN_IN_FOCUSED_WINDOW
    ).put(
            KeyStroke.getKeyStroke("control L"),
            "limparLogin"
    );

    getRootPane().getActionMap().put(
            "limparLogin",
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelarLogin();
                }
            }
    );
    }
    
    private void cancelarLogin() {
    txtUsuarioLogin.setText("");
    pwSenhaLogin.setText("");

    lblLedUsuario.setForeground(Color.GRAY);
    lblLedSenha.setForeground(Color.GRAY);

    txtUsuarioLogin.requestFocusInWindow();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblLedUsuario = new javax.swing.JLabel();
        lblLedSenha = new javax.swing.JLabel();
        txtUsuarioLogin = new javax.swing.JTextField();
        pwSenhaLogin = new javax.swing.JPasswordField();
        btnEntrarLogin = new javax.swing.JButton();
        btnCancelarLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setResizable(false);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("HOSPITAL - MM");

        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(43, 43, 43))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("LOGIN");

        lblLedUsuario.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lblLedUsuario.setText("Usuario:");

        lblLedSenha.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        lblLedSenha.setText("Senha:");

        txtUsuarioLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        pwSenhaLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pwSenhaLogin.addActionListener(this::pwSenhaLoginActionPerformed);

        btnEntrarLogin.setBackground(new java.awt.Color(153, 255, 0));
        btnEntrarLogin.setForeground(new java.awt.Color(0, 0, 0));
        btnEntrarLogin.setText("Entrar");

        btnCancelarLogin.setBackground(new java.awt.Color(255, 0, 0));
        btnCancelarLogin.setForeground(new java.awt.Color(0, 0, 0));
        btnCancelarLogin.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblLedUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsuarioLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblLedSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwSenhaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEntrarLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelarLogin)
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(47, 47, 47)
                .addComponent(lblLedUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsuarioLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblLedSenha)
                .addGap(18, 18, 18)
                .addComponent(pwSenhaLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEntrarLogin)
                    .addComponent(btnCancelarLogin))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pwSenhaLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwSenhaLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pwSenhaLoginActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrameLogin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarLogin;
    private javax.swing.JButton btnEntrarLogin;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblLedSenha;
    private javax.swing.JLabel lblLedUsuario;
    private javax.swing.JPasswordField pwSenhaLogin;
    private javax.swing.JTextField txtUsuarioLogin;
    // End of variables declaration//GEN-END:variables
}
