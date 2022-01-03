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

        //Criar Pedido
        bPedido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                try {
                    Pedido pedido = new Pedido(emp);
                    pedido.setLocationRelativeTo(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex, "MessageP", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        bSair.addActionListener(new ActionListener() { //sair
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                login inicio = new login();
                inicio.setLocationRelativeTo(null);
            }
        });
    }

}
