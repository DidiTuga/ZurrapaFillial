import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Estatistica extends JFrame {
    private JComboBox<String> cbBar;
    private JComboBox<String> cbData;
    private JPanel painel;
    private JButton sairButton;
    private JLabel lbbar;
    private JLabel lbData;
    private JLabel lbLucro;
    private JLabel lbGastos;
    private JLabel lbGanhos;
    private JLabel lbGasto;

    public Estatistica() {
        setContentPane(painel);
        setTitle("Estatisticas");
        setSize(300, 300);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        //Inicialiar variaveis/texto
        ArrayList<Local> locais = veLocal();
        ArrayList<barEuros> datas = veData(locais);
        //meter janela visivel
        atualizaValores(datas, locais);
        setVisible(true);


        //Quando escolhem outro bar
        // tenho que atualizar a informação
        cbBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaValores(datas, locais);


            }
        });


        sairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        cbData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizaValores(datas, locais);
            }
        });
    }

    public void atualizaValores(ArrayList<barEuros> datas, ArrayList<Local> locais) {
        // ir buscar o local
        int i = -1;
        for (Local y : locais) {
            if (cbBar.getSelectedItem().equals(y.getNome())) {
                i = y.getIdLocal();
            }
        }
        if (i != -1) {
            int verifica = 0;
            for (barEuros l : datas) {
                if (cbData.getSelectedItem().equals("Situação Corrente")) {
                    if (l.getData().equals("Situação Corrente")) {
                        if (l.getIdBar() == i) {
                            lbGanhos.setText(String.valueOf(l.getGanhos()) + "€");
                            lbGastos.setText("Preço de Custo:");
                            lbGasto.setText(String.valueOf(l.getGasto()) + "€");
                            verifica = 0;
                        }

                    }
                } else if (cbData.getSelectedItem().equals(l.getData())) {
                    if (l.getIdBar() == i) {
                        lbGastos.setText("Gasto:");
                        lbGanhos.setText(String.valueOf(l.getGanhos()) + "€");
                        lbGasto.setText(String.valueOf(l.getGasto()) + "€");
                        verifica = 0;
                    }
                    if (l.getIdBar() != i) {
                        lbGanhos.setText("Não temos informações!");
                        lbGasto.setText("Não temos informações!");
                    }
                }
            }
        }

    }

    public ArrayList<Local> veLocal() {
        ArrayList<Local> locais = new ArrayList<>();
        //Colocar os produtos que existem no combobox
        try {
            //ir buscar os produtos para os adicionar no combobox
            ResultSet rs = Funcoes.getDataF("SELECT * From TblLocal WHERE IDLocal >= 1");

            while (rs.next()) {
                Local p = new Local(rs.getInt("IDLocal"), rs.getString("Designacao"));
                locais.add(p);
            }
            for (Local p : locais) {
                cbBar.addItem(p.getNome());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Locais, adicionar a Combo", JOptionPane.ERROR_MESSAGE);
        }
        return locais;
    }

    public ArrayList<barEuros> veData(ArrayList<Local> locais) {
        ArrayList<barEuros> datas = new ArrayList<>();
        //Colocar as que existem no combobox
        try {
            //IR BUSCAR A SITUACAO CORRENTE
            cbData.addItem("Situação Corrente");
            for (Local l : locais) {
                ResultSet rsd = Funcoes.getDataF("Select s.Quantidade, p.IDProduto, P.Preco_Compra, c.ConversaoAPB\n" +
                        "From TblStock s, TblProduto p, TblMedida m, TblConversao c\n" +
                        "WHere s.IDProduto = p.IDProduto\n" +
                        "AND s.IDMedida = m.IDMedida\n" +
                        "and m.IDMedida = c.IDMedidaA\n"
                        + "and IDLocal = " + l.getIdLocal());
                while (rsd.next()) {
                    double preçocusto = rsd.getInt("Quantidade") * rsd.getInt("ConversaoAPB") * rsd.getDouble("Preco_Compra");
                    barEuros b = new barEuros("Situação Corrente", l.getIdLocal(), 0, preçocusto);
                    datas.add(b);
                }
            }


            //ir buscar as datas para as adicionar no combobox
            ResultSet rs = Funcoes.getDataS("SELECT * From TblFilialDiaBar WHERE IDFilial = 1"); //pois este bar é a fillial 1

            while (rs.next()) {
                barEuros x = new barEuros(rs.getString("DataDia"), rs.getInt("IDBar"), rs.getDouble("Lucro"), rs.getDouble("Despesa"));
                datas.add(x);

                cbData.addItem(x.getData());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "A ir buscar datas", JOptionPane.ERROR_MESSAGE);
        }
        return datas;
    }
}
