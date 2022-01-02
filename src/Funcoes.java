import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Funcoes {
    public static void setDataorDelete(String msg, String Query){
        try {
            Connection con = Conectar.getCon();
            PreparedStatement pst = con.prepareStatement(Query);
            pst.executeQuery();
            if(!msg.equals("")){
                JOptionPane.showMessageDialog(null, msg);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
