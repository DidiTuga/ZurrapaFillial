import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Random;

public class Funcoes {
    public static void setDataorDelete(String msg, String Query) {
        try {
            Connection con = Conectar.getCon();
            PreparedStatement pst = con.prepareStatement(Query);
            int rs = pst.executeUpdate();
            if (msg.equals("")) {
                JOptionPane.showMessageDialog(null, msg);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Message0", JOptionPane.ERROR_MESSAGE);
        }

    }
    public static ResultSet getDataF(String query){
        try{
            Connection con = Conectar.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            return  rs;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
            return  null;
        }
    }

    public static int tamanho() {
        int tam = 0;
        try {

            PreparedStatement pst = Conectar.getCon().prepareStatement("Select * from TblConteudoPedido");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tam++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message1", JOptionPane.ERROR_MESSAGE);
        }
        return tam;
    }

    public static boolean verStock(int quantidade, Produto p) {
        boolean valor = false;
        try {
            //ir buscar os produtos para os adicionar no combobox
            String sql = "SELECT * From TblStock WHERE IDLocal =2  and IDProduto = " + p.getId(); // ALTERAR O LOCALLLLLL
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int stock = 0;
            int armazem = 0;
            while (rs.next()) {
                if (p.getId() == rs.getInt("IDProduto")) {
                    stock = rs.getInt("Quantidade");
                }
            }
            if (stock < quantidade) { // nao existe stock na loja // ve no armazem
                sql = "Select S.IDProduto, S.Quantidade , C.ConversaoAPB\n" +
                        "From TblStock S, TblConversao C, TblMedida M\n" +
                        "Where S.IDProduto = " + p.getId() + " \n" +
                        "and S.IDLocal = 1 \n" +
                        "and S.IDMedida = M.IDMedida \n" +
                        "and C.IDMedidaA = S.IDMedida ";
                pst = Conectar.getCon().prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    armazem += rs.getInt("Quantidade") * rs.getInt("ConversaoAPB"); //+= pois pode ter diferente medidas
                }
                if (armazem > quantidade) { // existe no armazem
                    valor = true;
                    Random rand = new Random();
                    int minutos = rand.nextInt(59) + 1;
                    JOptionPane.showMessageDialog(null, "A entrega do stock está previsto chegar em " + minutos + " minutos.");
                    armazem -= quantidade;
                    if (armazem % 12 == 0) { //tentar meter em pacotes de 12
                        armazem /= 12;
                        Funcoes.setDataorDelete("A sair do armazém!!",
                                "Update TblStock\n"
                                + "SET Quantidade=" + armazem + ","
                                + "AND IDMedida = " + 2
                                + "WHERE IDProduto ="+ p.getId()
                                +"AND IDLocal = 1;"
                                +"UPDATE TblPedido"
                                +"SET Estado = 1"
                                +"WHERE IDProduto ="+p.getId());
                    } else { // se nao der deixa ficar em minis
                        Funcoes.setDataorDelete("A sair do armazém!!",
                                "Update TblStock\n"
                                + "SET Quantidade=" + armazem + ", "
                                + "IDMedida = " + 1
                                + "\nWHERE IDProduto ="+ p.getId()
                                + "\nAND IDLocal = 1");
                    }
                } else { //stock produto

                    JOptionPane.showMessageDialog(null, "Não existe mais stock para esse produto.");

                }
            } else { // existe na loja
                valor = true;
                stock -=quantidade;
                Funcoes.setDataorDelete("Está servido!!",
                        "Update TblStock\n"
                                + "SET Quantidade=" + stock + ", "
                                + "IDMedida = " + 1
                                + "\nWHERE IDProduto ="+ p.getId()
                                + "\nAND IDLocal = "+ login.local);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message3", JOptionPane.ERROR_MESSAGE);
        }
        return valor;
    }

}
