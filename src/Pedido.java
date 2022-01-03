

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Pedido extends JFrame {
    private JPanel iPainel;
    private JLabel lbQuantidade;
    private JLabel lbProduto;
    private JComboBox CbProdutos;
    private JButton bAdicionar;
    private JLabel lbPreço;
    private JLabel nEmpregado;
    private JFormattedTextField tfQuantidade;
    private JButton bCancelar;
    private JButton bTerminar;
    private JLabel lbPedidos;
    private JTable Pedidos;
    private JScrollPane JPedidos;
    private JLabel lbPrecoT;
    private static int ultimoId = 1;

    public Pedido(Empregado emp) {
        final double[] preco = {0};
        ArrayList<Produto> Ppedidos = new ArrayList<>(); //que vao ser pedidos
        ArrayList<Produto> produtos = new ArrayList<>(); //que existem
        atualizaID(); // para atualizar o id

        //Definir janela
        setContentPane(iPainel);
        setTitle("Criar Pedido");
        setSize(450, 450);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Escrever  nas labels
        nEmpregado.setText("Olá " + emp.getNome() + ".");
        lbQuantidade.setText("Quantidade");
        lbProduto.setText("Produto");
        lbPedidos.setText("Pedidos");
        tfQuantidade.setText("0");
        //Cria Tabela

        criaTabela(Ppedidos);

        //Colocar os produtos que existem no combobox
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
                produtos.add(p);
            }
            for (Produto produto : produtos) {
                CbProdutos.addItem(produto.getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Message3", JOptionPane.ERROR_MESSAGE);
        }

        // atualiza o preço conforme o que está escolhido
        atualizaPreco(produtos, preco[0]);
        //Meter a janela visivel
        setVisible(true);


        //quando alteram o valor da quantidade
        tfQuantidade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaPreco(produtos, preco[0]);
            }
        });
        //quando escolhem um produto
        CbProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaPreco(produtos, preco[0]);
            }
        });

        //Quando clicamos no adicionar
        //Verificamos se tem stock e se tiver adicionamos
        bAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int qtd = Integer.parseInt(tfQuantidade.getText());
                if (qtd <= 0) {
                    JOptionPane.showMessageDialog(null, "Não podes adcionar produtos com quantidade igual ou inferior a 0.", "Cuidado!", JOptionPane.WARNING_MESSAGE);
                } else {
                    for (Produto p : produtos) {
                        if (CbProdutos.getSelectedItem().equals(p.getNome())) {
                            if (Funcoes.verStock(qtd, p)) { // SE TIVER stock adiciona ao array de pedidos
                                Produto L = new Produto();
                                L = (Produto) p.clone();
                                L.setQuantidade(qtd);
                                preco[0] += L.quantidade * L.getPreco_venda();
                                atualizaPreco(produtos, preco[0]);
                                Ppedidos.add(L);
                                criaTabela(Ppedidos);
                            }
                        }

                    }

                }
            }
        });

        //Quando clicamos no cancelar pedido
        //apagamos o array e saimos novamente para o hub
        bCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Hub hub = new Hub(emp);
                hub.setLocationRelativeTo(null);
            }
        });

        //Quando clicamos no terminar pedido
        // Cria um pedido com o ultimoid e o iddo empregado
        //Submete tudo o que esta no array pPedidos para o conteudopedidos
        bTerminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                dispose();
                Hub hub = new Hub(emp);
                hub.setLocationRelativeTo(null);
            }
        });
        Pedidos.addMouseListener(new MouseAdapter() { //////////////////////////////////// ESTA A DAR ERRO
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = Pedidos.getSelectedRow();
                System.out.println(index);
                if (JOptionPane.showConfirmDialog(null, "Não consegues editar esta linha, queres eliminar esta linha?", "Editar Pedido",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    //Se sim

                    Pedidos.remove(index);
                    preco[0] = preco[0] - (Ppedidos.get(index - 1).getQuantidade() * Ppedidos.get(index - 1).getPreco_venda());

                    Ppedidos.remove(index);
                    criaTabela(Ppedidos);

                }
            }
        });

    }

    public void atualizaID() {
        try {
            ResultSet rs = Funcoes.getDataF("SELECT MAX(IDPedido) from TblConteudoPedido");
            while (rs.next()) {
                ultimoId = rs.getInt("") + 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Message5", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void atualizaPreco(ArrayList<Produto> pr, double preco) {
        for (Produto p : pr) {
            double tmp = 0;
            if (CbProdutos.getSelectedItem().equals(p.getNome())) {

                tmp = preco + p.getPreco_venda() * Integer.parseInt(tfQuantidade.getText());
                String resultado = String.format("Preço individual: %.2f€  Preço Suposto: %.2f€", p.getPreco_venda(), tmp);
                lbPreço.setText(resultado);
            }
        }
    }

    public void criaTabela(ArrayList<Produto> Ppedidos) {
        Object[][] data = new Object[Ppedidos.size()][2];
        String[] colunas = {"Produto", "Quantidade" };
        for (int i = 0; i < Ppedidos.size(); i++) {
            data[i][0] = Ppedidos.get(i).getNome();
            data[i][1] = Ppedidos.get(i).getQuantidade();

        }
        DefaultTableModel tableModel = new DefaultTableModel(data, colunas) {

            @Override
            public boolean isCellEditable(int row, int column) {
                //mete tudo a que nao pode editar
                return false;
            }
        };

        Pedidos.setModel(tableModel);

    }


}