import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Estatistica extends JFrame{
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
        ArrayList<String> datas = veData();
        //meter janela visivel
        setVisible(true);





        //Quando escolhem outro bar
        // tenho que atualizar a informação
        cbBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



            }
        });


        sairButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

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
    public ArrayList<String> veData() {
        ArrayList<String> datas = new ArrayList<>();
        //Colocar as que existem no combobox
        try {
            //ir buscar as datas para as adicionar no combobox
            ResultSet rs = Funcoes.getDataS("SELECT * From TblFilialDiaBar WHERE IDFilial = 1");

            while (rs.next()) {
                String d = (rs.getString("DataDia"));
                datas.add(d);

                cbData.addItem(d);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "A ir buscar datas", JOptionPane.ERROR_MESSAGE);
        }
        return datas;
    }
}
