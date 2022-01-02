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


    public login() {
        setContentPane(mainFrame);
        setTitle("Bem-Vindo ao Zurrapa Fillial");
        setSize(350, 200);
        setResizable(false); // assim a janela fica com um só tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Registar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String sql = "Select Username, Palavra_passe, Nome, IDEmpregado from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
                    pst.setString(1, tfFirstName.getText());
                    pst.setString(2, String.valueOf(PasswordField.getPassword()));
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Acertou na password!");
                        setVisible(false);
                        Empregado emp = new Empregado(rs.getInt(4), rs.getString(3));
                        Hub inicio = new Hub(emp);
                        inicio.setLocationRelativeTo(null);
                        inicio.setVisible(true);


                    } else {
                        JOptionPane.showMessageDialog(null, "Colocou mal o utilizador ou a password!");
                    }
                    Conectar.getCon().close();
                } catch (SQLException c) {
                    System.out.println("Oops, deu error!!");
                    c.printStackTrace();
                }
            }
        });
    }


}
