import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class login extends JFrame {
    private JTextField usernameField;
    private JButton connectButton;
    private JPasswordField passwordField;
    private JPanel window;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton clearButton;
    private JComboBox<String> cbLocal;
    private JLabel lbNome;
    private Local local;


    public login() {
        //----- Definicoes da Janela -----
        setContentPane(window);                                  // Coloca a janela, como ativa
        setTitle("Bem-Vindo à Zurrapa Filial");                  // Define titulo
        setSize(400, 250);                           // Define tamanho
        setResizable(false);                                     // Define alteracao de tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Define fechar tudo ao fechar a janela
        setLocationRelativeTo(null);

        ArrayList<Local> Locais = new ArrayList<>();

        //Colocar os produtos que existem no combobox
        try {
            //ir buscar os produtos para os adicionar no combobox
            String sql = "SELECT * From TblLocal";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Local l = new Local(rs.getInt("IDLocal"),rs.getString("Designacao"));
                Locais.add(l);

            }
            for (int i = 0; i<Locais.size(); i++) {
                cbLocal.addItem(Locais.get(i).getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Ler Locais", JOptionPane.ERROR_MESSAGE);
        }
        setVisible(true);


        //----- Acoes -----
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cbLocal.getSelectedItem();
                for (Local x : Locais) {
                    if (cbLocal.getSelectedItem().equals(x.getNome()))
                    {
                        local = new Local(x.getIdLocal(), x.getNome());
                    }
                }
                try { // Validar utilizador e palavrapasse

                    String query = "Select Username, Palavra_passe, Nome, IDEmpregado from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    Connection connection = Conectar.getCon(); // Cria conecao com a base de dados
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, usernameField.getText());
                    pst.setString(2, String.valueOf(passwordField.getPassword()));
                    ResultSet result = pst.executeQuery();

                    if (result.next()) {
                        Empregado empregadoAtual = new Empregado(result.getInt(4), result.getString(3));
                        if(local.getIdLocal()==1){ // se ele selecionar o armazem aparece o menu armazem
                            HubArmazem armazem = new HubArmazem(empregadoAtual);
                            dispose();
                        }else { // se ele selecionar outro sem ser o armazem vai o outro
                            Hub hub_Gestao = new Hub(empregadoAtual, local);
                            dispose(); // Fecha Janela Atual
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Os dados que colocou estavam errados!");
                    }

                    connection.close(); // Fecha conecao com a base de dados
                } catch (SQLException c) {
                    JOptionPane.showMessageDialog(null, c, "ERROR",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }
}