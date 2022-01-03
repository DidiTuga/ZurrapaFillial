

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Pedido extends JFrame {
    private JPanel iPainel;
    private JLabel Quantidade;
    private JLabel Produto;
    private JComboBox Produtos;
    private JButton bAdicionar;
    private JLabel Preço;
    private JLabel nEmpregado;
    private JFormattedTextField tfQuantidade;
    private JButton bSair;

    private static int ultimoId = 1;

    public Pedido(Empregado emp) {
        final double[] preco = {0};

        ArrayList<Produto> pr = new ArrayList<>();
        setContentPane(iPainel);
        setTitle("Criar Pedido");
        setSize(450, 450);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Escrever  nas labels
        nEmpregado.setText("Olá " + emp.getNome() + ".");
        Quantidade.setText("Quantidade");
        Produto.setText("Produto");

        try {
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
            for (Produto produto : pr) {
                Produtos.addItem(produto.getNome());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message3", JOptionPane.ERROR_MESSAGE);
        }
        atualizaPreco(pr, preco);
        setVisible(true);
        //quando alteram o valor da quantidade
        tfQuantidade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaPreco(pr, preco);
            }
        });
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

                        if (Funcoes.verStock(Integer.parseInt(tfQuantidade.getText()), p)) { //VERIFICA SE TEM EM STOCK
                            atualizaID();
                            System.out.println(ultimoId + ", " + emp.getId() + "," + login.local);
                            Funcoes.setDataorDelete("Pedido Colocado com sucesso!", "INSERT INTO TblPedido(IDPedido, Estado, IDEmpregado, IDLocal)\n" +
                                    "VALUES(" + ultimoId + ", " + 0 + ", " + emp.getId() + ", " + login.local + ");");
                            Funcoes.setDataorDelete("ContuedoPedido Colocado com sucesso!", "INSERT INTO TblConteudoPedido(IDPedido, IDProduto, Quantidade_Pedida, Quantidade_Servida)\n" +
                                    "VALUES(" + ultimoId + ", " + p.getId() + ", " + Integer.parseInt(tfQuantidade.getText()) + ", " + Integer.parseInt(tfQuantidade.getText()) + ");");

                        }

                        String pagar = String.format("São: %.2f€", preco[0]);
                        JOptionPane.showMessageDialog(null, pagar);
                        setVisible(false);
                        Hub inicio = new Hub(emp);
                        setVisible(false);
                        inicio.setLocationRelativeTo(null);
                        inicio.setVisible(true);

                    }
                }

            }
        });
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Hub inicio = new Hub(emp);
                setVisible(false);
                inicio.setLocationRelativeTo(null);
                inicio.setVisible(true);

            }
        });
    }


    public void atualizaPreco(ArrayList<Produto> pr, double[] preco) {
        for (Produto p : pr) {
            if (Produtos.getSelectedItem().equals(p.getNome())) {
                preco[0] = p.getPreco_venda() * Integer.parseInt(tfQuantidade.getText());
                String resultado = String.format("Preço individual: %.2f€  Preço Total: %.2f€", p.getPreco_venda(), preco[0]);
                Preço.setText(resultado);
            }
        }
    }

    public void atualizaID() {
        try {
            String sql = "Select * from TblConteudoPedido";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                ultimoId = rs.getInt("IDPedido") + 1;

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Message5", JOptionPane.ERROR_MESSAGE);
        }
    }


    /*    public void atualizarTabela() {
        try {
            //adicionar pedidos a tabela
            int i = 0;
            Object[][] data = new Object[Funcoes.tamanho()][4];
            String sql = "Select * from TblConteudoPedido";
            PreparedStatement pst = Conectar.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {

                data[i][0] = rs.getInt("IDPedido");
                data[i][1] = rs.getInt("IDProduto");
                data[i][2] = rs.getInt("Quantidade_Pedida");
                data[i][3] = rs.getInt("Quantidade_Servida");
                ultimoId = rs.getInt("IDPedido") + 1;
                i++;
            }
            String[] Colunas = {"IDPedido", "IDProduto", "Quantidade Pedida", "Quantidade Servida"};
            Pedidos.setModel(new DefaultTableModel(data, Colunas));
            Pedidos.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message2", JOptionPane.ERROR_MESSAGE);
        }
    }*/


}
