import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class main extends JFrame{
    private JTextField tfFirstName;
    private JButton Registar;
    private JPasswordField PasswordField;
    private JPanel mainFrame;
    private JLabel lbpassword;
    private JLabel lbutilizador;


    public main (){
        setContentPane(mainFrame);
        boolean valor = false;
        setTitle("Bem-Vindo ao Zurrapa Fillial");
        setSize(300,200);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Registar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String utilizador =  tfFirstName.getText();
               String password = String.valueOf(PasswordField.getPassword());
                String url = "jdbc:sqlserver://DESKTOP-UJ26N4K\\SQLEXPRESS;databaseName=ZurrapaFilial;integratedSecurity=true";
                try {
                    Connection connection = DriverManager.getConnection(url);
                    String sql = "Select Username, Palavra_passe from TblEmpregado Where Username = ? and Palavra_Passe= ?";
                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, utilizador);
                    pst.setString(2,password);
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(null, "Username and Password Matched");

                    }else{
                        JOptionPane.showMessageDialog(null, "Username and password not correct");
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
