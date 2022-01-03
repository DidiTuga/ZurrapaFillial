import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame {
    private JTextField tfFirstName;
    private JButton Registar;
    private JPasswordField PasswordField;
    private JPanel mainFrame;
    private JLabel lbpassword;
    private JLabel lbutilizador;
    private JButton clearButton;
    public static int local;

    public login() {
        setContentPane(mainFrame);
        setTitle("Bem-Vindo à Zurrapa Fillial");
        setSize(350, 200);
        setResizable(false); // assim a janela fica com um só tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Registar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try { //Compara as primeiras duas colunas com os dois primeiros parametros
                    String sql = "Select Username, Palavra_passe, Nome, IDEmpregado, IDLocal from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
                    pst.setString(1, tfFirstName.getText());
                    pst.setString(2, String.valueOf(PasswordField.getPassword()));
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        setVisible(false); //meter o login invisivel e meter o hub
                        Empregado emp = new Empregado(rs.getInt(4), rs.getString(3));
                        local = rs.getInt(5);
                        Hub inicio = new Hub(emp);
                        inicio.setLocationRelativeTo(null);
                        inicio.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "Os dados que colocou estavam errados!");
                    }
                    Conectar.getCon().close();
                } catch (SQLException c) {
                    System.out.println("Oops, deu error!!");
                    c.printStackTrace();
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
    }
    public void clear(){
        tfFirstName.setText("");
        PasswordField.setText("");
    }

}
