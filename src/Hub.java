import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hub extends JFrame {
    private JButton bPedido;
    private JPanel Painel;
    private JButton bFCaixa;
    private JButton bSair;
    private JButton bTratarPedido;
    private JLabel nEmpregado;

    public Hub(Empregado emp, Local local) {
        setContentPane(Painel);
        setTitle("HUB");
        setSize(400, 300);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        nEmpregado.setText("Bem-vindo " + emp.getNome() + ".\n Encontra-se no " + local.getNome() + ".");
        setLocationRelativeTo(null);
        setVisible(true);


        //Criar Pedido
        bPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Pedido pedido = new Pedido(emp, local);
            }
        });

        //TratarPedido
        bTratarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                TratarPedido tp = new TratarPedido(emp, local);
            }
        });

        //Fechar caixa
        // Só o empregado com o idnolocal que pode fechar caixa
        bFCaixa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = -1;
                double totalganhos = 0;
                double totalgastos = 0;
                try {
                    ResultSet rs = Funcoes.getDataF("Select IDEmpregado From TblLocal WHere IDLocal = " + local.getIdLocal());
                    if (rs.next()) {
                        id = rs.getInt("IDEmpregado");
                    }

                    if (id == emp.getId()) {
                        int verifica = 0;
                        ResultSet rsd = Funcoes.getDataF("Select * From TblPedido");
                        while (rsd.next()) {
                            if (rsd.getInt("Estado") != 1) {
                                verifica = 1;
                            }
                        }
                        if (verifica == 1) {
                            JOptionPane.showMessageDialog(null, "Tem que fechar todos os pedidos primeiro!", "Erro ao fechar caixa", JOptionPane.WARNING_MESSAGE);
                        } else {


                            if (JOptionPane.showConfirmDialog(null, "Tem a certeza que quer fechar caixa?", "Fechar Caixa",
                                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                int idpedido= 0;
                                // FEcho loja
                                ResultSet rip = Funcoes.getDataF("Select p.IDPedido, pr.IDProduto, Quantidade_Pedida, pr.Preco_Compra, pr.Preco_Venda \n" +
                                        "From TblPedido p, TblConteudoPedido cp, TblProduto pr\n" +
                                        "WHERE p.IDPedido = cp.IDPedido\n" +
                                        "and IDLocal = " + local.getIdLocal() +
                                        "\nand cp.IDProduto = pr.IDProduto\n");
                                while (rip.next()) { //Vai ver quando gastos e ganhos houve
                                    totalganhos += rip.getInt("Quantidade_Pedida") * rip.getDouble("Preco_Venda");
                                    totalgastos += rip.getInt("Quantidade_Pedida") * rip.getDouble("Preco_Compra");
                                    idpedido = rip.getInt("IDPedido");
                                    Funcoes.setDataorDelete("", "DELETE From TblConteudoPedido WHERE IDPedido = " + idpedido +
                                            "\nDELETE FROM TblPedido WHERE IDPedido =" + idpedido);
                                }
                                //Manda os dados para a tabela
                                String query = "INSERT INTO TblFilialDiaBar(DataDia, Lucro, Despesa, IDBar, IDFilial)\n Values(GETDATE(), " + totalganhos + ", " + totalgastos + ", " + local.getIdLocal() + ", "+login.FilialIdentification+");";
                                Funcoes.setDataorDeleteS("Caixa Fechada com Sucesso!", query);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tem de ser o responsável para fechar caixa", "Fechar Caixa", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException x) {
                    JOptionPane.showMessageDialog(null, x, "Deu Erro ao ler no Fechar Caixa", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //sair
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                login inicio = new login(login.FilialIdentification);
            }
        });


    }

}
