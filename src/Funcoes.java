import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class Funcoes {
    public static void setDataorDelete(String msg, String Query) {
        try {
            Connection con = Conectar.getCon("ZurrapaFilial"+ login.FilialIdentification);;
            PreparedStatement pst = con.prepareStatement(Query);
            int rs = pst.executeUpdate();
            if (!msg.equals("")) {
                JOptionPane.showMessageDialog(null, msg);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "SetDataORDelete", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void setDataorDeleteS(String msg, String Query) {
        try {
            Connection con = Conectar.getConSede();
            PreparedStatement pst = con.prepareStatement(Query);
            int rs = pst.executeUpdate();
            if (!msg.equals("")) {
                JOptionPane.showMessageDialog(null, msg);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não pode fechar duas vezes no mesmo dia", "Erro ao fechar caixa", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static ResultSet getDataF(String query) {
        try {
            Connection con = Conectar.getCon("ZurrapaFilial"+ login.FilialIdentification);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "getDataF", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static ResultSet getDataS(String query) {
        try {
            Connection con = Conectar.getConSede();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "getDataS", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static boolean verStock(int quantidade, Produto p, int idlocal) {
        boolean valor = false;
        int stock = 0;
        int armazem = 0;
        try {
            //ir buscar os produtos para os adicionar no combobox
            ResultSet rs = getDataF("SELECT * From TblStock WHERE IDLocal =" + idlocal + "and IDProduto = " + p.getId());
            while (rs.next()) {
                if (p.getId() == rs.getInt("IDProduto")) {
                    stock = rs.getInt("Quantidade");
                }
            }
            if (stock < quantidade) { // nao existe stock na loja ve no armazem
                rs = getDataF("Select S.Quantidade , C.ConversaoAPB\n" +
                        "From TblStock S, TblConversao C, TblMedida M\n" +
                        "Where S.IDProduto = " + p.getId() + " \n" +
                        "and S.IDLocal = 1 \n" +
                        "and S.IDMedida = M.IDMedida \n" +
                        "and C.IDMedidaA = S.IDMedida ");
                while (rs.next()) {
                    armazem += rs.getInt("Quantidade") * rs.getInt("ConversaoAPB"); //+= pois pode ter diferente medidas
                }
                if (armazem >= quantidade) { // existe no armazem
                    valor = true;
                    Random rand = new Random();
                    int minutos = rand.nextInt(59) + 1;// PERGUNTA AO UTILIZADOR SE QUER ESPERA
                    JOptionPane.showMessageDialog(null, "A entrega do stock está previsto chegar em " + minutos + " minutos.");
                    // POSSO PERGUNTAR SE QUER MESMO
                    if (JOptionPane.showConfirmDialog(null, "Quer mesmo assim este produto?", "Stock",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        valor = true;
                    } else {
                        valor = false;
                    }

                } else { //stock produto

                    JOptionPane.showMessageDialog(null, "Não existe mais stock para esse produto.");

                }
            } else { // existe na loja
                valor = true;

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message3", JOptionPane.ERROR_MESSAGE);
        }
        return valor;
    }

    //ve quais produtos existem
    public static ArrayList<Produto> verProdutos() {
        ArrayList<Produto> produtos = new ArrayList<>();
        try {
            ResultSet rip = Funcoes.getDataF("SELECT * From TblProduto");
            while (rip.next()) {
                Produto x = new Produto(rip.getInt("IDProduto"), rip.getString("Designacao"));
                produtos.add(x);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Importar Pedidos a tratar", JOptionPane.ERROR_MESSAGE);
        }

        return produtos;
    }
}
