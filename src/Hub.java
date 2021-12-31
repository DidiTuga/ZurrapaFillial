import javax.swing.*;

public class Hub extends JFrame{
    private JButton Armazem;
    private JPanel Painel;
    private JButton Pedido;
    private JButton Stock;

    public Hub(){
        setContentPane(Painel);
        setTitle("HUB");
        setSize(600,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
