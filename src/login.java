import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame{
    private JTextField tfFirstName;
    private JButton Registar;
    private JPasswordField PasswordField;
    private JPanel mainFrame;
    private JLabel lbpassword;
    private JLabel lbutilizador;


    public login(){
        setContentPane(mainFrame);
        setTitle("Bem-Vindo ao Zurrapa Fillial");
        setSize(300,200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Registar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = "jdbc:sqlserver://DESKTOP-UJ26N4k\\SQLEXPRESS;databaseName=ZurrapaFilial;integratedSecurity=true";
                try {
                    Connection connection = DriverManager.getConnection(url);
                    String sql = "Select Username, Palavra_passe from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, tfFirstName.getText());
                    pst.setString(2,String.valueOf(PasswordField.getPassword()));
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null, "Acertou na password!");
                        Hub inicio = new Hub();
                        setVisible(false);


                    }else{
                        JOptionPane.showMessageDialog(null, "Errou ou no nome de utilizador ou na password!");
                    }
                    connection.close();
                } catch (SQLException c){
                    System.out.println("Oops, deu error!!");
                    c.printStackTrace();
                }
            }
        });
    }


}
