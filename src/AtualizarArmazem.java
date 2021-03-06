import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AtualizarArmazem extends JFrame {
    private JTable Tabela;
    private JButton bEliminar;
    private JComboBox cbMedidas;
    private JButton bAtualizar;
    private JTextField tfQtd;
    private JLabel lbProduto;
    private JButton bSair;
    private JLabel lbIDMedida;
    private JScrollPane TabelaSP;
    private JPanel painel;
    private JComboBox cbProdutos;
    private JButton btAdicionar;
    private DefaultTableModel model;

    public AtualizarArmazem(Empregado emp) {
        //Janela configurações
        setContentPane(painel);
        setTitle("Atualizar Stock do Armazem");
        setSize(500, 400);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        setLocationRelativeTo(null);
        //Inicialiar variaveis/texto
        ArrayList<Medida> medidas = veMedida();
        tfQtd.setText("0");
        ArrayList<Stock> stocks = veStock();
        ArrayList<Produto> produtos = veProdutos();
        criaTabela(stocks);
        //meter janela visivel
        setVisible(true);

        //Atualiza com os dados que estao la a linha selecionada
        bAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Tabela.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Tem que selecionar uma linha!", "Eliminar Stock", JOptionPane.WARNING_MESSAGE);
                } else {
                    int qtd = Integer.parseInt(tfQtd.getText());
                    for (Medida m : medidas) {
                        if (m.getDesignacao().equals(cbMedidas.getSelectedItem())) {
                            stocks.get(index).setIDMedida(m.getIdMedida());
                        }
                    }
                    stocks.get(index).setQtd(qtd);
                    Funcoes.setDataorDelete("Atualizei com sucesso a linha", "Update TblStock\n" +
                            "Set Quantidade = " + stocks.get(index).getQtd() + ", IDMedida = " + stocks.get(index).getIDMedida() +
                            "\nWHERE IDStock = " + stocks.get(index).getIDStock());
                    criaTabela(stocks);
                }
            }
        });


        //Quando seleciona uma linha vai buscar a informações e mete
        Tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = Tabela.getSelectedRow();
                lbProduto.setText("Produto:");
                for (Produto p : produtos) {
                    if (p.getNome().equals(stocks.get(index).getDesignacao())) {
                        cbProdutos.getModel().setSelectedItem(p.getNome());
                    }
                }
                tfQtd.setText(String.valueOf(stocks.get(index).getQtd()));
                for (Medida m : medidas) {
                    if (m.getIdMedida() == stocks.get(index).getIDMedida()) {
                        cbMedidas.getModel().setSelectedItem(m.getDesignacao());
                    }
                }
            }
        });
        //Adicionar se nao tiver la o produto
        btAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = 0;
                int ultimoid = 0;
                for (Stock s : stocks) {
                    if (cbProdutos.getSelectedItem().equals(s.getDesignacao())) {
                        i = 1;
                    }
                    ultimoid = s.getIDStock();
                }

                if (i == 1) {
                    JOptionPane.showMessageDialog(null, "Já existe esse produto no stock!", "Erro ao adicionar produto", JOptionPane.ERROR_MESSAGE);
                } else {
                    ultimoid++;
                    for (Produto p : produtos) {
                        if (cbProdutos.getSelectedItem().equals(p.getNome())) {
                            for (Medida m : medidas) {
                                if (cbMedidas.getSelectedItem().equals(m.getDesignacao())) {
                                    Stock s = new Stock(ultimoid, p.getId(), p.getNome(), Integer.parseInt(tfQtd.getText()), m.getIdMedida());
                                    Funcoes.setDataorDelete("Adicionou com sucesso!", "INSERT INTO TblStock\n" +
                                            "VALUES (" + ultimoid + ", " + Integer.parseInt(tfQtd.getText()) + ", " + p.getId() + "," + m.getIdMedida() + ", " + 1 + ");");
                                    stocks.add(s);
                                }
                            }
                        }
                    }

                    criaTabela(stocks);
                }
            }
        });
        //Eliminar vai apagar a linha
        // Vai buscar a linha ver se não é -1 e se nao for -1 apaga
        bEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Tabela.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Tem que selecionar uma linha!", "Eliminar Stock", JOptionPane.WARNING_MESSAGE);
                } else {
                    Funcoes.setDataorDelete("Apaguei com sucesso a linha!", "DELETE FROM TblStock WHERE IDSTock =" + stocks.get(index).getIDStock());
                    stocks.remove(index);
                    criaTabela(stocks);
                }
            }
        });

        //sair
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                HubArmazem diz = new HubArmazem(emp);
                diz.setLocationRelativeTo(null);
            }
        });


    }

    //Ve quantas medidas á e colca as numa array e no combobox
    public ArrayList<Medida> veMedida() {
        ArrayList<Medida> medidas = new ArrayList<>();
        try {
            ResultSet rs = Funcoes.getDataF("Select * From TblMedida");
            while (rs.next()) {
                Medida m = new Medida(rs.getInt("IDMedida"), rs.getString("Designacao"));
                medidas.add(m);
                cbMedidas.addItem(m.getDesignacao());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Erro ao ler medidas", JOptionPane.ERROR_MESSAGE);
        }
        return medidas;
    }

    // Ve o stock existente e coloca o no array
    public ArrayList<Stock> veStock() {
        ArrayList<Stock> medidas = new ArrayList<>();
        try {
            ResultSet rs = Funcoes.getDataF("Select s.IDStock, s.IDProduto, p.Designacao, s.Quantidade, s.IDMedida\n" +
                    "From TblStock s, TblProduto p\n" +
                    "WHERE IDLocal = 1\n" +
                    "and s.IDProduto = p.IDProduto");
            while (rs.next()) {
                Stock m = new Stock(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
                medidas.add(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Erro ao ler stock", JOptionPane.ERROR_MESSAGE);
        }
        return medidas;
    }

    //Cria a tabela
    public void criaTabela(ArrayList<Stock> stocks) {
        Object[][] data = new Object[stocks.size()][5];
        String[] colunas = {"IDStock", "IDProduto", "Designacao", "Qtd_Stock", "IDMedida"};
        for (int i = 0; i < stocks.size(); i++) {
            data[i][0] = stocks.get(i).getIDStock();
            data[i][1] = stocks.get(i).getIDProduto();
            data[i][2] = stocks.get(i).getDesignacao();
            data[i][3] = stocks.get(i).getQtd();
            data[i][4] = stocks.get(i).getIDMedida();
        }
        model = new DefaultTableModel(data, colunas) {

            public boolean isCellEditable(int row, int column) {
                //mete tudo a que nao pode editar
                return false;
            }
        };

        Tabela.setModel(model);

    }

    public ArrayList<Produto> veProdutos() {
        ArrayList<Produto> produtos = new ArrayList<>();
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
                cbProdutos.addItem(produto.getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Pedido, adicionar a Combo", JOptionPane.ERROR_MESSAGE);
        }

        return produtos;
    }
}
