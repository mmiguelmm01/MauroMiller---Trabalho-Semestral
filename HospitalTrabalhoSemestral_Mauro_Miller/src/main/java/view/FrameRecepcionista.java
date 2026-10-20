/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Miller Matsimbe
 */
public class FrameRecepcionista extends javax.swing.JFrame {
    private int proximoId = 1;
    private int linhaSelecionada = -1;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameRecepcionista.class.getName());

    /**
     * Creates new form FrameRecepcionista
     */
    //htjg

    public FrameRecepcionista() {
        
         initComponents();
         setLocationRelativeTo(null);
         setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

    configurarTabela();
    configurarEventos();
    configurarTeclado();
    configurarSaida();

    limparCampos();
    }
    private void configurarTabela() {
    DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{
            "ID", "Nome", "BI", "Data de Nascimento",
            "Contacto", "Endereco", "Descricao"
        }, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    tblPacienteRecepcionista.setModel(modelo);
    tblPacienteRecepcionista.setSelectionMode(
        javax.swing.ListSelectionModel.SINGLE_SELECTION
    );

    tblPacienteRecepcionista.setRowHeight(25);

    tblPacienteRecepcionista.getTableHeader()
        .setReorderingAllowed(false);

    ((javax.swing.table.DefaultTableCellRenderer)
        tblPacienteRecepcionista.getTableHeader()
            .getDefaultRenderer())
        .setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    javax.swing.table.DefaultTableCellRenderer centro =
        new javax.swing.table.DefaultTableCellRenderer();

    centro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    for (int i = 0; i < tblPacienteRecepcionista.getColumnCount(); i++) {
        tblPacienteRecepcionista.getColumnModel()
            .getColumn(i).setCellRenderer(centro);
        }
    }
    
    private void cadastrarPaciente() {
    String nome = txtNomePacienteRecep.getText().trim();
    String bi = txtBIPacienteRecep.getText().trim();
    String dataTexto = txtDataNascPacienteRecep.getText().trim();
    String contacto = txtContactoPacienteRecep.getText().trim();
    String endereco = txtEnderecoPacienteRecep.getText().trim();
    String descricao = txtDescricaoPacienteRecep.getText().trim();
    String sexo = cmbSexoPaciente.getSelectedItem().toString();

    if (nome.isEmpty() || bi.isEmpty() || dataTexto.isEmpty()
            || contacto.isEmpty() || endereco.isEmpty()
            || descricao.isEmpty()) {

        JOptionPane.showMessageDialog(
            this,
            "Preencha todos os campos.",
            "Campos obrigatórios",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (!nome.matches("[\\p{L}][\\p{L} '\\-]*")) {
        JOptionPane.showMessageDialog(
            this, "Introduza um nome válido."
        );
        txtNomePacienteRecep.requestFocusInWindow();
        return;
    }

    DefaultTableModel modelo =
        (DefaultTableModel) tblPacienteRecepcionista.getModel();

    for (int i = 0; i < modelo.getRowCount(); i++) {
        String biExistente = modelo.getValueAt(i, 2).toString();

        if (biExistente.equalsIgnoreCase(bi)) {
            JOptionPane.showMessageDialog(
                this,
                "Já existe um paciente com este BI.",
                "BI duplicado",
                JOptionPane.WARNING_MESSAGE
            );
            txtBIPacienteRecep.requestFocusInWindow();
            return;
        }
    }

    LocalDate dataNascimento;

    try {
        DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                .withResolverStyle(
                    java.time.format.ResolverStyle.STRICT
                );

        dataNascimento = LocalDate.parse(dataTexto, formato);

    } catch (DateTimeParseException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Data inválida. Use o formato dd/MM/aaaa.",
            "Erro de data",
            JOptionPane.ERROR_MESSAGE
        );
        txtDataNascPacienteRecep.requestFocusInWindow();
        return;
    }

    if (dataNascimento.isAfter(LocalDate.now())) {
        JOptionPane.showMessageDialog(
            this, "A data de nascimento não pode ser futura."
        );
        txtDataNascPacienteRecep.requestFocusInWindow();
        return;
    }

    modelo.addRow(new Object[]{
        proximoId++,
        nome,
        bi,
        dataNascimento.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
        ),
        contacto,
        endereco,
        descricao
    });

    JOptionPane.showMessageDialog(
        this,
        "Paciente cadastrado com sucesso!",
        "Cadastro",
        JOptionPane.INFORMATION_MESSAGE
    );

      limparCampos();
   }
    
    private void limparCampos() {
    txtNomePacienteRecep.setText("");
    txtBIPacienteRecep.setText("");
    txtDataNascPacienteRecep.setText("");
    txtContactoPacienteRecep.setText("");
    txtEnderecoPacienteRecep.setText("");
    txtDescricaoPacienteRecep.setText("");

    cmbSexoPaciente.setSelectedIndex(0);

    tblPacienteRecepcionista.clearSelection();
    linhaSelecionada = -1;

    txtNomePacienteRecep.requestFocusInWindow();
    }
    
    private void configurarEventos() {

    btnCadastrarPacienteRecep.addActionListener(e -> {
        cadastrarPaciente();
    });

    btnCancelarPacienteRecep.addActionListener(e -> {
        limparCampos();
    });

    tblPacienteRecepcionista.getSelectionModel()
        .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                carregarPacienteSelecionado();
            }
        });
    }
    
    private void carregarPacienteSelecionado() {
    int linha = tblPacienteRecepcionista.getSelectedRow();

    if (linha == -1) {
        linhaSelecionada = -1;
        return;
    }

    linhaSelecionada = tblPacienteRecepcionista
        .convertRowIndexToModel(linha);

    DefaultTableModel modelo =
        (DefaultTableModel) tblPacienteRecepcionista.getModel();

    txtNomePacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 1).toString()
    );

    txtBIPacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 2).toString()
    );

    txtDataNascPacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 3).toString()
    );

    txtContactoPacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 4).toString()
    );

    txtEnderecoPacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 5).toString()
    );

    txtDescricaoPacienteRecep.setText(
        modelo.getValueAt(linhaSelecionada, 6).toString()
    );
    }
    
    private void configurarTeclado() {
    javax.swing.JRootPane raiz = getRootPane();

    InputMap input = raiz.getInputMap(
        JComponent.WHEN_IN_FOCUSED_WINDOW
    );

    ActionMap actions = raiz.getActionMap();

    input.put(KeyStroke.getKeyStroke("ENTER"), "cadastrar");
    actions.put("cadastrar", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            cadastrarPaciente();
        }
    });

    input.put(KeyStroke.getKeyStroke("ESCAPE"), "cancelar");
    actions.put("cancelar", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            limparCampos();
        }
    });

    input.put(KeyStroke.getKeyStroke("control S"), "guardar");
    actions.put("guardar", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            cadastrarPaciente();
        }
    });

    input.put(KeyStroke.getKeyStroke("control L"), "limpar");
    actions.put("limpar", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            limparCampos();
        }
    });

    input.put(KeyStroke.getKeyStroke("control N"), "novo");
    actions.put("novo", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            limparCampos();
            txtNomePacienteRecep.requestFocusInWindow();
        }
    });

    input.put(KeyStroke.getKeyStroke("DELETE"), "eliminar");
    actions.put("eliminar", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            eliminarPacienteSelecionado();
        }
    });

    input.put(KeyStroke.getKeyStroke("control 1"), "tabCadastro");
    actions.put("tabCadastro", new AbstractAction() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            jTabbedPane1.setSelectedIndex(0);
        }
    });
    }
    
    private void configurarSaida() {
    setDefaultCloseOperation(
        javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE
    );

    addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent evt) {
            sairDaTela();
        }
    });
    }
    
    private void sairDaTela() {
    int resposta = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Deseja realmente sair da tela do recepcionista?",
        "Confirmar saída",
        javax.swing.JOptionPane.YES_NO_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    if (resposta == javax.swing.JOptionPane.YES_OPTION) {
        dispose();
    }
    }
    
    private void eliminarPacienteSelecionado() {

    int linha = tblPacienteRecepcionista.getSelectedRow();

    if (linha == -1) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Selecione um paciente na tabela primeiro.",
            "Aviso",
            javax.swing.JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    int resposta = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Tem certeza de que deseja eliminar este paciente?",
        "Confirmar eliminação",
        javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (resposta == javax.swing.JOptionPane.YES_OPTION) {

        int linhaModelo =
            tblPacienteRecepcionista.convertRowIndexToModel(linha);

        javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel)
                tblPacienteRecepcionista.getModel();

        modelo.removeRow(linhaModelo);

        limparCampos();

        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Paciente eliminado com sucesso!"
        );
    }
    }
    
    public void definirPerfil(String perfil) {

    if (perfil.equalsIgnoreCase("Enfermeiro")) {

        setTitle("Tela do Enfermeiro");

        // Substitui lblTituloRecepcionista
        // pelo nome real da tua JLabel do topo.
        lblTituloRecepcionista.setText("ENFERMEIRO");

    } else if (perfil.equalsIgnoreCase("Recepcionista")) {

        setTitle("Tela do Recepcionista");

        lblTituloRecepcionista.setText("RECEPCIONISTA");
    }
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
        jDesktopPane2 = new javax.swing.JDesktopPane();
        lblTituloRecepcionista = new javax.swing.JLabel();
        btnSairRecepcionista = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jPanel2 = new javax.swing.JPanel();
        lblNomePacienteRecep = new javax.swing.JLabel();
        lblDescricaoPacienteRecep = new javax.swing.JLabel();
        txtNomePacienteRecep = new javax.swing.JTextField();
        txtDescricaoPacienteRecep = new javax.swing.JTextField();
        lblBIPacienteRecep = new javax.swing.JLabel();
        txtBIPacienteRecep = new javax.swing.JTextField();
        lblContactoPacienteRecep = new javax.swing.JLabel();
        txtContactoPacienteRecep = new javax.swing.JTextField();
        lblEnderecoPacienteRecep = new javax.swing.JLabel();
        txtEnderecoPacienteRecep = new javax.swing.JTextField();
        lblDataNascPacienteRecep = new javax.swing.JLabel();
        txtDataNascPacienteRecep = new javax.swing.JTextField();
        lblSexoPaciente = new javax.swing.JLabel();
        cmbSexoPaciente = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPacienteRecepcionista = new javax.swing.JTable();
        btnCadastrarPacienteRecep = new javax.swing.JButton();
        btnCancelarPacienteRecep = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tela do Recepcionista");
        setResizable(false);

        lblTituloRecepcionista.setText("Recepcionista");

        btnSairRecepcionista.setBackground(new java.awt.Color(255, 0, 0));
        btnSairRecepcionista.setText("Sair");
        btnSairRecepcionista.addActionListener(this::btnSairRecepcionistaActionPerformed);

        jDesktopPane2.setLayer(lblTituloRecepcionista, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(btnSairRecepcionista, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblTituloRecepcionista, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSairRecepcionista, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloRecepcionista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSairRecepcionista)
                .addContainerGap())
        );

        jDesktopPane1.setLayer(jDesktopPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lblNomePacienteRecep.setText("Nome:");

        lblDescricaoPacienteRecep.setText("Descricao do Doente:");

        lblBIPacienteRecep.setText("BI:");

        lblContactoPacienteRecep.setText("Contacto");

        lblEnderecoPacienteRecep.setText("Endereco:");

        lblDataNascPacienteRecep.setText("Data de Nascimento:");

        lblSexoPaciente.setText("Sexo:");

        cmbSexoPaciente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masculino", "Feminino" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNomePacienteRecep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNomePacienteRecep)
                    .addComponent(lblDescricaoPacienteRecep, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                    .addComponent(txtDescricaoPacienteRecep))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtBIPacienteRecep, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(lblBIPacienteRecep, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblSexoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSexoPaciente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(22, 22, 22)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContactoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtContactoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblEnderecoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEnderecoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDataNascPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDataNascPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)))
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNomePacienteRecep)
                    .addComponent(lblBIPacienteRecep)
                    .addComponent(lblContactoPacienteRecep)
                    .addComponent(lblSexoPaciente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomePacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBIPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContactoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSexoPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescricaoPacienteRecep)
                    .addComponent(lblEnderecoPacienteRecep)
                    .addComponent(lblDataNascPacienteRecep))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDescricaoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEnderecoPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataNascPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jDesktopPane3.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane3Layout = new javax.swing.GroupLayout(jDesktopPane3);
        jDesktopPane3.setLayout(jDesktopPane3Layout);
        jDesktopPane3Layout.setHorizontalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane3Layout.createSequentialGroup()
                .addContainerGap(184, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        jDesktopPane3Layout.setVerticalGroup(
            jDesktopPane3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tblPacienteRecepcionista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "BI", "Data de Nascimento", "Contacto", "Endereco", "Descriacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblPacienteRecepcionista);

        btnCadastrarPacienteRecep.setBackground(new java.awt.Color(51, 255, 0));
        btnCadastrarPacienteRecep.setText("Cadastrar");
        btnCadastrarPacienteRecep.addActionListener(this::btnCadastrarPacienteRecepActionPerformed);

        btnCancelarPacienteRecep.setBackground(new java.awt.Color(255, 0, 0));
        btnCancelarPacienteRecep.setText("Cancelar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane3)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCadastrarPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelarPacienteRecep, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(234, 234, 234))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jDesktopPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadastrarPacienteRecep)
                    .addComponent(btnCancelarPacienteRecep))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cadastrar Paciente", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarPacienteRecepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarPacienteRecepActionPerformed
        // TODO add your handling code here:
          cadastrarPaciente();
    }//GEN-LAST:event_btnCadastrarPacienteRecepActionPerformed

    private void btnSairRecepcionistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairRecepcionistaActionPerformed
        // TODO add your handling code here:
           int opcao = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Deseja terminar a sessão?",
            "Terminar sessão",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE
    );

    if (opcao == javax.swing.JOptionPane.YES_OPTION) {
        FrameLogin login = new FrameLogin();
        login.setLocationRelativeTo(null);
        login.setVisible(true);

        this.dispose();
    }
    }//GEN-LAST:event_btnSairRecepcionistaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrameRecepcionista().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrarPacienteRecep;
    private javax.swing.JButton btnCancelarPacienteRecep;
    private javax.swing.JButton btnSairRecepcionista;
    private javax.swing.JComboBox<String> cmbSexoPaciente;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblBIPacienteRecep;
    private javax.swing.JLabel lblContactoPacienteRecep;
    private javax.swing.JLabel lblDataNascPacienteRecep;
    private javax.swing.JLabel lblDescricaoPacienteRecep;
    private javax.swing.JLabel lblEnderecoPacienteRecep;
    private javax.swing.JLabel lblNomePacienteRecep;
    private javax.swing.JLabel lblSexoPaciente;
    private javax.swing.JLabel lblTituloRecepcionista;
    private javax.swing.JTable tblPacienteRecepcionista;
    private javax.swing.JTextField txtBIPacienteRecep;
    private javax.swing.JTextField txtContactoPacienteRecep;
    private javax.swing.JTextField txtDataNascPacienteRecep;
    private javax.swing.JTextField txtDescricaoPacienteRecep;
    private javax.swing.JTextField txtEnderecoPacienteRecep;
    private javax.swing.JTextField txtNomePacienteRecep;
    // End of variables declaration//GEN-END:variables
}
