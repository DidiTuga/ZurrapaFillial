import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Pedido extends JFrame {
    private JPanel iPainel;
    private JTable Pedidos;
    private JLabel Quantidade;
    private JLabel Produto;
    private JComboBox Produtos;
    private JButton bAdicionar;
    private JLabel Preço;
    private JLabel nEmpregado;
    private JScrollPane Tabela;
    private JFormattedTextField tfQuantidade;

    public Pedido(Empregado emp) {
        final double[] preco = {0};
        int ultimoId = 0;
        ArrayList<Produto> pr = new ArrayList<>();
        setContentPane(iPainel);
        setTitle("Criar Pedido");
        setSize(700, 450);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Escrever  nas labels
        nEmpregado.setText("Olá " + emp.getNome() + ".");
        Quantidade.setText("Quantidade");
        Produto.setText("Produto");
        atualizarTabela(ultimoId);
        try{
            //ir buscar os produtos para os adicionar no combobox
            String sql = "SELECT * From TblProduto";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("IDProduto"));
                p.setNome(rs.getString("Designacao"));
                p.setPreco_venda(rs.getDouble("Preco_venda"));
                p.setPreco_compra(rs.getDouble("Preco_Compra"));
                pr.add(p);
            }
            for (int i = 0; i < pr.size(); i++) {
                Produtos.addItem(pr.get(i).getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
        }
        atualizaPreco(pr, preco);
        setVisible(true);
        //quando alteram o valor da quantidade
        tfQuantidade.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               atualizaPreco(pr, preco);
                                           }
                                       }
        );
        //quando escolhem um produto
        Produtos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaPreco(pr, preco);
            }
        });
        //quando clicam no adicionar pedido
        bAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (Produto p : pr) {

                    if (Produtos.getSelectedItem().equals(p.getNome())) {
                        Funcoes.setDataorDelete("", "INSERT INTO TblPedido(IDPedido, Estado, IDEmpregado, IDLocal) " +
                                "VALUES(" + ultimoId+", " + 0 + ", " + emp.getId() + ", "+ 2 + ")" );
                        Funcoes.setDataorDelete("", "INSERT INTO TblConteudoPedido(IDPedido, IDProduto, Quantidade_Pedida, Quantidade_Servida) " +
                                "VALUES(" + ultimoId+", " + p.getId() + ", " + Integer.parseInt(tfQuantidade.getText()) + ", "+ 0 + ")");
                        atualizarTabela(ultimoId);
                    }
                }

            }
        });
    }


    public void atualizaPreco(ArrayList<Produto> pr, double[] preco) {
        for (Produto p : pr) {

            if (Produtos.getSelectedItem().equals(p.getNome())) {
                preco[0] = p.getPreco_venda() * Integer.parseInt(tfQuantidade.getText());
                Preço.setText("Preço " + String.valueOf(preco[0]) + "€");
            }
        }
    }
    public void atualizarTabela(int ultimoId){
        try {
            //adicionar pedidos a tabela
            Object[][] data = null;
            String sql = "Select * from TblConteudoPedido";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int i = 0;
                data[0][i] = rs.getInt("IDPedido");
                data[1][i] = rs.getInt("IDProduto");
                data[2][i] = rs.getInt("Quantidade_Pedida");
                data[3][i] = rs.getInt("Quantidade_Servida");
                ultimoId = i + 1;
                i++;
            }

            String[] Colunas = {"IDPedido", "IDProduto", "Quantidade Pedida", "Quantidade Servida"};
            Pedidos.setModel(new DefaultTableModel(data, Colunas));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
