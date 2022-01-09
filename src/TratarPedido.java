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
    private JButton btCancelarPedido;
    //Definir variaveis
    private ArrayList<ConteudoPedido> pfechar = new ArrayList<>();
    private ArrayList<Produto> produtos = new ArrayList<>();

    public TratarPedido(Empregado emp, Local local) {
        //Definir janela
        setContentPane(painel);
        setTitle("Tratar de Pedidos");
        setSize(600, 500);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Editar as caixas de texto
        lbPedidosA.setText("Pedidos em Aberto");
        nEmpregado.setText("Olá, " + emp.getNome() + ". Encontra-se no " + local.getNome() + ".");

        //Mandar os dados para a tabela
        pfechar = atualizaDados(local.getIdLocal());
        produtos = Funcoes.verProdutos();
        criaTabela(pfechar);

        //meter visivel
        setVisible(true);


        //BOTAO DE SAIR
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Hub ola = new Hub(emp, local);
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
                btCancelarPedido.setEnabled(true);
            }
        });

        //Vai buscar o valor da quantidade, e o sitio do array onde esta o pedido
        //e altera a quantidade servida para a quantidade que lá está
        // e submete na base de dados
        balterarQtd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Pedidos.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Tem que selecionar uma linha da tabela.", "AVISO", JOptionPane.WARNING_MESSAGE);
                } else {
                    int valor = Integer.parseInt(tfQuantidadeS.getText());

                    ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
                    if (tmp.getQuantidade_pedida() > 0 && valor <= tmp.getQuantidade_pedida() && valor > 0) {
                        tmp.setQuantidade_servida(valor);
                        criaTabela(pfechar);
                        Funcoes.setDataorDelete("Alterado com sucesso!",
                                " UPDATE TblConteudoPedido\n" +
                                        "Set Quantidade_Servida =" + valor +
                                        "\nWHERE IDProduto = " + tmp.getIdProduto() +
                                        "\nand IDPedido = " + tmp.getIdPedido());
                    } else {
                        JOptionPane.showMessageDialog(null, "Não pode colocar um valor acima da quantidade pedida ou um número inferior a 0.", "AVISO na Quantidade Servida", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        //Fechar pedido
        // Vai ver se tmp.getquantida_pedida == tmp.getQuantida_servida
        // e se for mete o estado = 1 e fecha pedido;
        bfecharPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Pedidos.getSelectedRow();
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Tem que selecionar uma linha da tabela.", "AVISO", JOptionPane.WARNING_MESSAGE);
                } else {
                    ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
                    if (verificaPedido(pfechar, index)) {
                        String update = "UPDATE TblPedido\n" + "Set Estado = 1" + "\nWHERE IDPedido = " + tmp.getIdPedido() + "\nAND IDLocal =" + local.getIdLocal();
                        Funcoes.setDataorDelete("Pedido fechado com sucesso!", update);

                        //Voltar para o hub pois o pedido ja foi fechado

                        dispose();
                        Hub zamal = new Hub(emp, local);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Para fechar o pedido, as quantidades do(s) produto(s) têm de ser iguais.", "AVISO na Quantidade Servida", JOptionPane.WARNING_MESSAGE);
                    }

                }
            }
        });
        //cancelar pedido
        btCancelarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = Pedidos.getSelectedRow();
                //Verifica se esta alguma coisa selecionada
                if (index == -1) {
                    JOptionPane.showMessageDialog(null, "Tem que selecionar uma linha da tabela.", "AVISO", JOptionPane.WARNING_MESSAGE);
                } else {
                    //Guarda os conteudos pedidos com o id do selecionado
                    int eliminar = pfechar.get(index).getIdPedido();
                    ArrayList<ConteudoPedido> tmp= new ArrayList<>();
                    for(int i = 0; i<pfechar.size(); i++){
                        if(eliminar == pfechar.get(i).getIdPedido()){
                            tmp.add((ConteudoPedido) pfechar.get(i).clone());
                        }

                    }
                    //eliminada tudo do pedido na base de dados
                    Funcoes.setDataorDelete("Pedido Eliminado com sucesso!", "DELETE From TblConteudoPedido WHERE IDPedido =" + eliminar +
                            "\nDELETE FROM TblPedido WHERE IDPedido = "+eliminar);

                for (ConteudoPedido c : tmp){
                    int qtd = 0;
                    try{
                        //vai buscar a quantidade do produto que o stock ja tinha
                        ResultSet rs = Funcoes.getDataF("Select Quantidade\n" +
                                "From TblStock\n" +
                                "WHERE IDLocal = "+local.getIdLocal() +"\n" +
                                "AND IDProduto = "+ c.getIdProduto() );
                        if(rs.next()){
                            qtd = rs.getInt("Quantidade");
                        }
                    }catch(SQLException y){
                        JOptionPane.showMessageDialog(null, y, "Nao consegui ir buscar a quantidade", JOptionPane.ERROR_MESSAGE);
                    }
                    qtd += c.getQuantidade_pedida();
                    //soma a quantidade com a quantidade devolvida e da update na base de dados
                    Funcoes.setDataorDelete("", "UPDATE TblStock\n"+
                                                        "SET Quantidade = " + qtd
                                                        +"\n WHERE IDLocal = "+ local.getIdLocal()
                                                        +"AND IDProduto = " + c.getIdProduto());

                }
                //vai buscar novamente a informacao se esta algum pedido em aberto e atualiza a tabela
                pfechar = atualizaDados(local.getIdLocal());
                criaTabela(pfechar);
                }

            }
        });
    }

    //Verifica se existem mais pedidos com o mesmo id e ve se esses tem a quantidade servida no maximo
    public boolean verificaPedido(ArrayList<ConteudoPedido> pfechar, int index) {
        boolean valor = true;
        int t = 0;
        ConteudoPedido tmp = pfechar.get(index); //FUNCIONA COMO UM APONTADOR
        for (ConteudoPedido x : pfechar) {
            if (tmp.getIdPedido() == x.getIdPedido()) {
                int qtdp = x.getQuantidade_pedida();
                int qtds = x.getQuantidade_servida();
                if (x.getQuantidade_pedida() == x.getQuantidade_servida() && t == 0) {
                    t = 0;
                } else {
                    t = 1;
                }
            }
        }
        if (t == 1) {
            valor = false;
        }
        return valor;
    }

    //Atualiza os dados do array
    public ArrayList<ConteudoPedido> atualizaDados(int local) {
        ArrayList<ConteudoPedido> pfechar = new ArrayList<>();
        try {
            ResultSet rip = Funcoes.getDataF(
                    "SELECT p.IDPedido, cp.IDProduto, cp.Quantidade_Pedida, cp.Quantidade_Servida " +
                            "FROM TblPedido p, TblConteudoPedido cp " +
                            "WHERE p.IDPedido = cp.IDPedido and Estado = 0" +
                            "AND p.IDLocal = " + local);
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
