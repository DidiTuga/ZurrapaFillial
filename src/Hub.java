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
        setVisible(true);


        //Criar Pedido
        bPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Pedido pedido = new Pedido(emp, local);
                pedido.setLocationRelativeTo(null);
            }
        });

        //TratarPedido
        bTratarPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            dispose();
            TratarPedido tp = new TratarPedido(emp, local);
            tp.setLocationRelativeTo(null);
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
                        id=rs.getInt("IDEmpregado");
                    }

                if(id == emp.getId()){
                    if (JOptionPane.showConfirmDialog(null, "Tem a certeza que quer fechar caixa?", "Fechar Caixa",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        // FEcho loja
                        ResultSet rip = Funcoes.getDataF("Select pr.IDProduto, Quantidade_Pedida, pr.Preco_Compra, pr.Preco_Venda \n" +
                                "From TblPedido p, TblConteudoPedido cp, TblProduto pr\n" +
                                "WHERE p.IDPedido = cp.IDPedido\n" +
                                "and IDLocal = " + local.getIdLocal() +
                                "\nand cp.IDProduto = pr.IDProduto\n");
                        while(rip.next()){ //Vai ver quando gastos e ganhos houve
                            totalganhos += rip.getInt("Quantidade_Pedida") * rip.getDouble("Preco_Venda");
                            totalgastos += rip.getInt("Quantidade_Pedida") * rip.getDouble("Preco_Compra");
                        }
                        //Manda os dados para a tabela
                        String query = "INSERT INTO TblFilialDiaBar(DataDia, Lucro, Despesa, IDBar, IDFilial)\n Values(GETDATE(), "+ totalganhos + ", "+totalgastos + ", " + local.getIdLocal() + ", 1);";
                        Funcoes.setDataorDeleteS("Meter informacao da DiaBar", query);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Tem de ser o responsável para fechar caixa", "Fechar Caixa", JOptionPane.WARNING_MESSAGE);
                }
                }catch (SQLException x){
                    JOptionPane.showMessageDialog(null, x, "Deu Erro ao ler no Fechar Caixa", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //sair
        bSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                login inicio = new login();
                inicio.setLocationRelativeTo(null);
            }
        });


    }

}
