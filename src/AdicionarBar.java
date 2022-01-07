import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdicionarBar extends JFrame {
    private JPanel Painel;
    private JTable Tabela;
    private JLabel lbBar;
    private JComboBox cbProdutos;
    private JComboBox cbLocal;
    private JTextField tfQtdD;
    private JLabel lbArmazem;
    private JButton bTransferir;
    private JPanel painelS;
    private JLabel lbQtdD;
    private JLabel lbQtdS;
    private JLabel lbQtd;
    private JLabel lbProduto;
    private JButton sairButton;
    private JScrollPane JSPTabela;
    private DefaultTableModel model;

    public AdicionarBar(Empregado emp) {
        //Definir janela
        setContentPane(Painel);
        setTitle("Retocar stock dos bares");
        setSize(650, 450);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Dar valores as variaveis
        tfQtdD.setText("0");
        ArrayList<Produto> produtos = veProduto();
        ArrayList<Local> locais = veLocal();
        //vai buscar os produtos do bar selecionado
        ArrayList<Stock> stocks = veStock(verificaLocal(locais).getIdLocal());

        //STOCKS DO ARMAZEM
        final ArrayList<Stock>[] stocksA = new ArrayList[]{veStock(1)};
        //cria tabela com o stock que esta acontecer
        criaTabela(stocks);
        atualizaQtd(stocksA[0]);
        setVisible(true);

        //botao de sair
        sairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                HubArmazem diz = new HubArmazem(emp);
                diz.setLocationRelativeTo(null);
            }
        });
        // quando mudam os produtos atualiza a qtd dos produtos
        cbProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stocksA[0] = veStock(1);
                atualizaQtd(stocksA[0]);
            }
        });
        //Quando mudam o local é preciso atualizar a tabela
        cbLocal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                criaTabela(stocks);
            }
        });

        //Botao transferir
        //Verifica se qtd >0 e <= qtdA E
        bTransferir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int qtd = Integer.parseInt(tfQtdD.getText());
                int qtdA = atualizaQtd(stocksA[0]);
                if(qtd>0 && qtd <= qtdA){
                    int local = verificaLocal(locais).getIdLocal();
                    int index = 0;
                    //ve qual é o produto
                    for (int i = 0; i < stocks.size(); i++) {
                        if (cbProdutos.getSelectedItem().equals(stocks.get(i).getDesignacao())) {
                            index = i;
                        }
                    }
                    //nova quantidade do armazem
                    int qtdNovaArm = qtdA - qtd;
                    //nova quantidade do local
                    int qtdNovaLoc = stocks.get(index).getQtd() + qtd;
                    //atualiza tabela do armazem
                    Funcoes.setDataorDelete("", "UPDATE TblStock\n" +
                            "SET Quantidade = " + qtdNovaArm + ",IDMedida = 1" +
                            "\nWHERE IDlocal = 1\n" +
                            "and IDProduto = "+ stocks.get(index).getIDProduto());
                    //Atualiza tabela do local
                    Funcoes.setDataorDelete("Transfêrencia feita com sucesso!", "UPDATE TblStock\n" +
                            "SET Quantidade = " + qtdNovaLoc +
                            "\nWHERE IDlocal = " + local +
                            "\nand IDProduto = "+ stocks.get(index).getIDProduto());
                    criaTabela(veStock(local));
                    stocksA[0] =veStock(1);
                    atualizaQtd(stocksA[0]);
                }
                else{
                    JOptionPane.showMessageDialog(null , "A quantidade tem que ser maior que 0 e menor ou igual ao valor do armazém!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    //          --- Funcôes ---

    //Ve quantos produtos há e colca as numa array e no combobox
    public ArrayList<Produto> veProduto() {
        ArrayList<Produto> produtos = new ArrayList<>();
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
                cbProdutos.addItem(produto.getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Produto, adicionar a Combo", JOptionPane.ERROR_MESSAGE);
        }
        return produtos;
    }

    public ArrayList<Medida> Medida() {
        ArrayList<Medida> medidas = new ArrayList<>();
        try {
            ResultSet rs = Funcoes.getDataF("Select * From TblMedida, TblConversao\n" +
                                                "WHere IDMedida = IDMedidaB");
            while (rs.next()) {
                Medida m = new Medida(rs.getInt("IDMedidaA"), rs.getString("Designacao"));
                m.setConversao(rs.getInt("ConversaoAPB"));
                medidas.add(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Erro ao ler medidas", JOptionPane.ERROR_MESSAGE);
        }
        return medidas;
    }

    //Ve quantos produtos há e colca as numa array e no combobox
    public ArrayList<Local> veLocal() {
        ArrayList<Local> locais = new ArrayList<>();
        //Colocar os produtos que existem no combobox
        try {
            //ir buscar os produtos para os adicionar no combobox
            ResultSet rs = Funcoes.getDataF("SELECT * From TblLocal WHERE IDLocal > 1");

            while (rs.next()) {
                Local p = new Local(rs.getInt("IDLocal"), rs.getString("Designacao"));
                locais.add(p);
            }
            for (Local p : locais) {
                cbLocal.addItem(p.getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Locais, adicionar a Combo", JOptionPane.ERROR_MESSAGE);
        }
        return locais;
    }

    // Ve o stock existente e coloca o no array
    public ArrayList<Stock> veStock(int local) {
        ArrayList<Stock> stocks = new ArrayList<>();
        try {
            ResultSet rs = Funcoes.getDataF("Select s.IDStock, s.IDProduto, p.Designacao, s.Quantidade, s.IDMedida\n" +
                    "From TblStock s, TblProduto p\n" +
                    "WHERE IDLocal =" + local +
                    "\nand s.IDProduto = p.IDProduto");
            while (rs.next()) {
                Stock m = new Stock(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5));
                stocks.add(m);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Erro ao ler stock", JOptionPane.ERROR_MESSAGE);
        }
        return stocks;
    }

    //Verifica o local que esta selecionado
    public Local verificaLocal(ArrayList<Local> locals) {
        for (Local l : locals) {
            if (cbLocal.getSelectedItem().equals(l.getNome())) {
                return l;
            }
        }
        return null;
    }

    //Atualiza o produto que foi selecionado
    public int atualizaQtd(ArrayList<Stock> stocks) {
        int i = 0;
        int qtd = 0;
        for (Stock p : stocks) {
            if (cbProdutos.getSelectedItem().equals(p.getDesignacao())) {
                for ( Medida m : Medida()){
                    if(m.getIdMedida() == p.getIDMedida()){
                       qtd = p.getQtd() * m.getConversao();
                        lbQtd.setText(String.valueOf(qtd));
                        i = 1;
                    }

                }

            }
        }
        if(i == 0){
            lbQtd.setText("Não existe stock");
        }
        return qtd;
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
}
