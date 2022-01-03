import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame {
    private JTextField usernameField;
    private JButton connectButton;
    private JPasswordField passwordField;
    private JPanel window;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton clearButton;


    public login() {
        //----- Definicoes da Janela -----
        setContentPane(window);                                  // Coloca a janela, como ativa
        setTitle("Bem-Vindo à Zurrapa Filial");                  // Define titulo
        setSize(350, 200);                           // Define tamanho
        setResizable(false);                                     // Define alteracao de tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Define fechar tudo ao fechar a janela
        setVisible(true);
        //----- Acoes -----
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { // Validar utilizador e palavrapasse

                    String query = "Select Username, Palavra_passe, Nome, IDEmpregado from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    Connection connection = Conectar.getCon(); // Cria conecao com a base de dados
                    PreparedStatement pst = connection.prepareStatement(query);
                    pst.setString(1, usernameField.getText());
                    pst.setString(2, String.valueOf(passwordField.getPassword()));
                    ResultSet result = pst.executeQuery();

                    if (result.next()) {
                        Empregado empregadoAtual = new Empregado(result.getInt(4), result.getString(3));
                        Hub hub_Gestao = new Hub(empregadoAtual);

                        dispose(); // Fecha Janela Atual
                        hub_Gestao.setLocationRelativeTo(null);
                        hub_Gestao.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "Os dados que colocou estavam errados!");
                    }

                    connection.close(); // Fecha conecao com a base de dados
                } catch (SQLException c) {
                    System.out.println("Oops, deu error!!");
                    c.printStackTrace();
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