

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
    private DefaultTableModel model;
    private JScrollPane JPedidos;
    private static int ultimoId = 1;

    public Pedido(Empregado emp, Local local) {
        final double[] preco = {0};
        ArrayList<Produto> Ppedidos = new ArrayList<>(); //que vao ser pedidos
        ArrayList<Produto> produtos = new ArrayList<>(); //que existem
        atualizaID(local.getIdLocal()); // para atualizar o id

        //Definir janela
        setContentPane(iPainel);
        setTitle("Criar Pedido");
        setSize(450, 450);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Escrever  nas labels
        nEmpregado.setText("Olá " + emp.getNome() + ". Está no "+ local.getNome() + ".");
        lbQuantidade.setText("Quantidade");
        lbProduto.setText("Produto");
        lbPedidos.setText("Pedidos");
        tfQuantidade.setText("0");
        //Cria Tabela

        criaTabela(Ppedidos);

        //Colocar os produtos que existem no combobox
        try {
            //ir buscar os produtos para os adicionar no combobox
            ResultSet rs = Funcoes.getDataF("SELECT * From TblProduto");

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
            JOptionPane.showMessageDialog(null, e, "Pedido, adicionar a Combo", JOptionPane.ERROR_MESSAGE);
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
                //Criar um maxqtd para ver se tem stock daquele produto que colocou juntamente se o colocou mais vezes
                int Maxqtd = qtd;
                for (Produto x : Ppedidos) {
                    if (CbProdutos.getSelectedItem().equals(x.getNome()))
                    {
                    Maxqtd = x.getQuantidade()+qtd;
                    }
                }
                tfQuantidade.setText("0");
                if (qtd <= 0) {
                    JOptionPane.showMessageDialog(null, "Não podes adcionar produtos com quantidade igual ou inferior a 0.", "Cuidado!", JOptionPane.WARNING_MESSAGE);
                } else {
                    for (Produto p : produtos) {
                        if (CbProdutos.getSelectedItem().equals(p.getNome())) {
                            if (Funcoes.verStock(Maxqtd, p, local.getIdLocal())) { // SE TIVER stock adiciona ao array de pedidos
                                Produto L = new Produto();
                                L = (Produto) p.clone();
                                L.setQuantidade(Maxqtd);
                                // SE JA EXISTIR UM PRODUTO IGUAL SOMA A QUANTIDADE A ESTE PRODUTO
                                for (int u = 0; u<Ppedidos.size(); u++){
                                    if(L.getNome().equals(Ppedidos.get(u).getNome())){
                                        preco[0] = preco[0] - (Ppedidos.get(u).getQuantidade() * Ppedidos.get(u).getPreco_venda());
                                        Ppedidos.remove(u);
                                    }
                                }
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
                Hub hub = new Hub(emp, local);
            }
        });

        //Quando clicamos no terminar pedido
        // Cria um pedido com o ultimoid e o iddo empregado
        //Submete tudo o que esta no array pPedidos para o conteudopedidos
        bTerminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // SUBMETER O PEDIDO COM O IDLOCAL E EMPREGADO E ESTADO 0 POIS DEPOIS VAI PARA O PROCESSAMENTO
                JOptionPane.showMessageDialog(null, "Tem de pagar: " + preco[0] + "€","Preço",JOptionPane.INFORMATION_MESSAGE );
                Funcoes.setDataorDelete("", "INSERT INTO TblPedido(IDPedido, Estado, IDEmpregado, IDLocal)\n" +
                        "VALUES(" + ultimoId + ", " + 0 + ", " + emp.getId() + ", " + local.getIdLocal() + ");"); //ONDE ESTA O 2 é para meter o do localZ


                    // SUBMETER PARA O CONTEUDOPEDIDO COM OS DIFERENTES PRODUTOS
                    for (Produto y : Ppedidos) {

                        try {
                        int quantidade=0;
                        Funcoes.setDataorDelete("", "INSERT INTO TblConteudoPedido(IDPedido, IDProduto, Quantidade_Pedida, Quantidade_Servida)\n" +
                                "VALUES(" + ultimoId + ", " + y.getId() + ", " + y.quantidade + ", " + 0 + ");");
                            //VER SE VOU RETIRAR AO ARMAZEM OU SE TIRO NA LOJA
                            // ATRAVES DAQUELE QUE TEM QUANTIDADE
                        ResultSet rs = Funcoes.getDataF("SELECT Quantidade FROM TblStock WHERE IDProduto=" + y.getId() + "AND IDLOCAL ="+ local.getIdLocal());
                        if (rs.next()) {
                            quantidade = rs.getInt("Quantidade");
                        }
                        if (quantidade>y.getQuantidade()){
                            quantidade -= y.getQuantidade();
                            Funcoes.setDataorDelete("",
                                    "Update TblStock\n"
                                            + "SET Quantidade=" + quantidade + ", "
                                            + "IDMedida = " + 1
                                            + "\nWHERE IDProduto ="+ y.getId()
                                            + "\nAND IDLocal = "+ local.getIdLocal());
                        }
                        else{
                            quantidade = 0;
                            //vai ver a quantidade que esta no armazem
                            ResultSet rip = Funcoes.getDataF("Select S.Quantidade , C.ConversaoAPB\n" +
                                    "From TblStock S, TblConversao C, TblMedida M\n" +
                                    "Where S.IDProduto = " + y.getId() + " \n" +
                                    "and S.IDLocal = 1 \n" +
                                    "and S.IDMedida = M.IDMedida \n" +
                                    "and C.IDMedidaA = S.IDMedida ");
                            if (rip.next()) { //Multiplica e assim retira do armazem
                                quantidade = (rip.getInt("Quantidade") * rip.getInt("ConversaoAPB"));
                            }
                            quantidade -= y.getQuantidade();
                            Funcoes.setDataorDelete("",
                                    "Update TblStock\n"
                                            + "SET Quantidade=" + quantidade + ", "
                                            + "IDMedida = 1"
                                            + "\nWHERE IDProduto ="+ y.getId()
                                            + "\nAND IDLocal = 1");
                        }
                    }                catch (SQLException x){
                            JOptionPane.showMessageDialog(null, x, "Terminar Pedido", JOptionPane.ERROR_MESSAGE);
                        }
                }
                dispose();
                Hub hub = new Hub(emp, local);
                hub.setLocationRelativeTo(null);
            }
        });

        Pedidos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = Pedidos.getSelectedRow();
                if (JOptionPane.showConfirmDialog(null, "Não consegues editar esta linha, queres eliminar esta linha?", "Editar Pedido",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    //Se sim
                    preco[0] = preco[0] - (Ppedidos.get(index).getQuantidade() * Ppedidos.get(index).getPreco_venda());
                    model.removeRow(index);
                    Ppedidos.remove(index);
                    atualizaPreco(produtos, preco[0]);
                    criaTabela(Ppedidos);

                }
            }
        });

    }

    public void atualizaID(int local) {
        try {
            ResultSet rs = Funcoes.getDataF("SELECT MAX(IDPedido) from TblPedido WHERE IDLocal = " + local);
            while (rs.next()) {
                ultimoId = rs.getInt("") + 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "PEdido - AtualizaID", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void atualizaPreco(ArrayList<Produto> pr, double preco) {
        for (Produto p : pr) {
            double tmp = 0;
            if (CbProdutos.getSelectedItem().equals(p.getNome())) {

                tmp = preco + p.getPreco_venda() * Integer.parseInt(tfQuantidade.getText());
                String resultado = String.format("Preço individual: %.2f€  Preço Total: %.2f€", p.getPreco_venda(), tmp);
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
        model = new DefaultTableModel(data, colunas) {

            public boolean isCellEditable(int row, int column) {
                //mete tudo a que nao pode editar
                return false;
            }
        };

        Pedidos.setModel(model);

    }


}