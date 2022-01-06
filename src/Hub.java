import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Hub extends JFrame {
    private JButton bPedido;
    private JPanel Painel;
    private JButton bStock;
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
