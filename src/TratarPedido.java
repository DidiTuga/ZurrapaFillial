import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static javax.swing.UIManager.getInt;

public class TratarPedido extends JFrame {
    private JScrollPane PedidosA;
    private DefaultTableModel model;
    private JTable Pedidos;
    private JButton balterarQtd;
    private JButton bfecharPedido;
    private JLabel nEmpregado;
    private JTextField tfQuantidadeS;
    private JPanel painel;
    private JLabel lbPedidosA;
    private JButton bSair;
    private JLabel lbProduto;
    //Definir variaveis
    private ArrayList<ConteudoPedido> pfechar = new ArrayList<>();
    private ArrayList<Produto> produtos = new ArrayList<>();

    public TratarPedido(Empregado emp) {
        //Definir janela
        setContentPane(painel);
        setTitle("Tratar de Pedidos");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Editar as caixas de texto
        lbPedidosA.setText("Pedidos em Aberto");
        nEmpregado.setText("Olá, " + emp.getNome() + ".");

        //Mandar os dados para a tabela
        pfechar = atualizaDados();
        produtos = Funcoes.verProdutos();
        criaTabela(pfechar);

        //meter visivel
        setVisible(true);


        //BOTAO DE SAIR
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Hub ola = new Hub(emp);
                ola.setLocationRelativeTo(null);
            }
        });


        //Quando clicao na linha da tabela da enable aos botoes
        // e coloca os valores / nome do produto do lado direito
        Pedidos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int index = Pedidos.getSelectedRow();
                tfQuantidadeS.setText(String.valueOf(pfechar.get(index).getQuantidade_servida()));
                for (Produto p : produtos) {
                    if (p.getId() == pfechar.get(index).getIdProduto()) {
                        lbProduto.setText("Produto: " + p.getNome());
                    }
                }
                balterarQtd.setEnabled(true);
                bfecharPedido.setEnabled(true);
            }
        });

        //Vai buscar o valor da quantidade, e o sitio do array onde esta o pedido
        //e altera a quantidade servida para a quantidade que lá está
        // e submete na base de dados
        balterarQtd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Pedidos.getSelectedRow();
                int valor = Integer.parseInt(tfQuantidadeS.getText());

                ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
                if (valor <= tmp.getQuantidade_pedida()) {
                    tmp.setQuantidade_servida(valor);
                    criaTabela(pfechar);
                    Funcoes.setDataorDelete("Mudar o valor da QTD do Pedido",
                            " UPDATE TblConteudoPedido\n" +
                                    "Set Quantidade_Servida =" + valor +
                                    "\nWHERE IDProduto = " + tmp.getIdProduto() +
                                    "\nand IDPedido = " + tmp.getIdPedido());
                } else {
                    JOptionPane.showMessageDialog(null, "Não pode colocar um valor acima da quantidade pedida.", "AVISO na Quantidade Servida", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        //Fechar pedido
        // Vai ver se tmp.getquantida_pedida == tmp.getQuantida_servida
        // e se for mete o estado = 1 e fecha pedido;
        bfecharPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Pedidos.getSelectedRow();
                ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
                if (verificaPedido(pfechar, index)) {
                    Funcoes.setDataorDelete("Meter o valor estado = 1",
                            " UPDATE TblPedido\n" +
                                    "Set Estado = "+1 +
                                    "\nWHERE IDPedido = " + tmp.getIdPedido());
                    JOptionPane.showMessageDialog(null, "Pedido fechado com sucesso!", "Ação feita com sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Para fechar o pedido, as quantidades do(s) produto(s) têm de ser iguais.", "AVISO na Quantidade Servida", JOptionPane.WARNING_MESSAGE);
                }
                dispose();
                Hub zamal = new Hub(emp);
                zamal.setLocationRelativeTo(null);
            }
        });
    }

    //Verifica se existem mais pedidos com o mesmo id e ve se esses tem a quantidade servida no maximo
    public boolean verificaPedido(ArrayList<ConteudoPedido> pfechar, int index){
        boolean valor = true;
        int t = 0;
        ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
        for (ConteudoPedido x : pfechar){
            if (tmp.getIdPedido()==x.getIdPedido()){
                int qtdp = x.getQuantidade_pedida();
                int qtds = x.getQuantidade_servida();
                if(x.getQuantidade_pedida()==x.getQuantidade_servida()  && t == 0){
                    t = 0;
                }
                else{
                    t = 1;
                }
            }
        }
        if(t==1){
            valor = false;
        }
        return valor;
    }

    //Atualiza os dados do array
    public ArrayList<ConteudoPedido> atualizaDados() {
        ArrayList<ConteudoPedido> pfechar = new ArrayList<>();
        try {
            ResultSet rip = Funcoes.getDataF(
                    "SELECT p.IDPedido, cp.IDProduto, cp.Quantidade_Pedida, cp.Quantidade_Servida " +
                            "FROM TblPedido p, TblConteudoPedido cp " +
                            "WHERE p.IDPedido = cp.IDPedido and Estado = 0");
            while (rip.next()) {
                ConteudoPedido x = new ConteudoPedido(rip.getInt("IDPedido"), rip.getInt("IDProduto"), rip.getInt("Quantidade_servida"), rip.getInt("Quantidade_Pedida"));
                pfechar.add(x);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Importar Pedidos a tratar", JOptionPane.ERROR_MESSAGE);
        }
        return pfechar;
    }



    //cria a tabela e coloca la tudo
    public void criaTabela(ArrayList<ConteudoPedido> Ppedidos) {
        Object[][] data = new Object[Ppedidos.size()][4];
        String[] colunas = {"IDPedido", "IDProduto", "Quantidade Pedida", "Quantidade Servida"};
        for (int i = 0; i < Ppedidos.size(); i++) {
            data[i][0] = Ppedidos.get(i).getIdPedido();
            data[i][1] = Ppedidos.get(i).getIdProduto();
            data[i][2] = Ppedidos.get(i).getQuantidade_pedida();
            data[i][3] = Ppedidos.get(i).getQuantidade_servida();
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
