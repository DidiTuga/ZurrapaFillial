import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pedido extends  JFrame{
    private JPanel iPainel;
    private JTable Pedidos;
    private JLabel Quantidade;
    private JLabel Produto;
    private JTextField textField1;
    private JComboBox Produtos;
    private JButton bAdicionar;
    private JLabel Preço;
    private JLabel nEmpregado;
    public Pedido(String nome) throws SQLException {
        double preco = 0.00;
        setContentPane(iPainel);
        setTitle("Criar Pedido");
        setSize(600,350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        nEmpregado.setText("Olá " + nome + ".");
        Quantidade.setText("Quantidade");
        Produto.setText("Produto");
        Preço.setText("Preço " + String.valueOf(preco));
        //adicionar pedidos a tabela
        Object[][] data = null;
        try {
            String sql = "Select * from TblConteudoPedido";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int i = 0;
                data[i][0] = rs.getInt("IDPedido");
                data[i][1] = rs.getInt("IDProduto");
                data[i][2] = rs.getInt("Quantidade_Pedida");
                data[i][3] = rs.getInt("Quantidade_Servida");
                i++;
            }
        }catch (SQLException c){
            System.out.println("Oops, deu error!!");
            c.printStackTrace();
        }
        String[] Colunas = {"IDPedido","IDProduto","Quantidade Pedida","Quantidade Servida"};
        Pedidos =  new JTable(data,Colunas);

        //meter os produtos na combobox
        setVisible(true);
    }
}
