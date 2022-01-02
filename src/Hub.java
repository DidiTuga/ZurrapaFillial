import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class Hub extends JFrame{
    private JButton bPedido;
    private JPanel Painel;
    private JButton bStock;
    private JButton bSair;
    private JButton bArmazem;
    private JLabel nEmpregado;

    public Hub(String nomeEmpregado){
        setContentPane(Painel);
        setTitle("HUB");
        setSize(400,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        nEmpregado.setText("Olá " + nomeEmpregado + ".");

        bPedido.addActionListener(new ActionListener() { //Criar Pedido
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    Pedido pedido = new Pedido(nomeEmpregado);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        bSair.addActionListener(new ActionListener() { //sair
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                login inicio = new login();
            }
        });
    }
}
