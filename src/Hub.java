import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Hub extends JFrame {
    private JButton bPedido;
    private JPanel Painel;
    private JButton bStock;
    private JButton bSair;
    private JButton bArmazem;
    private JLabel nEmpregado;

    public Hub(Empregado emp) {
        setContentPane(Painel);
        setTitle("HUB");
        setSize(400, 300);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        nEmpregado.setText("Bem-vindo " + emp.getNome() + ".");
        setVisible(true);
        //Criar Pedido
        bPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Pedido pedido = new Pedido(emp);
                pedido.setLocationRelativeTo(null);
                pedido.setVisible(true);
            }
        });

        bSair.addActionListener(new ActionListener() { //sair
            public void actionPerformed(ActionEvent e) {
                dispose();
                login inicio = new login();
                inicio.setLocationRelativeTo(null);
            }
        });
    }

}
