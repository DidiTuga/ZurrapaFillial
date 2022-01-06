import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HubArmazem extends JFrame{
    private JPanel painelP;
    private JButton btASArmazem;
    private JButton btCTSBar;
    private JButton btSair;
    private JLabel lbTexto;

    public HubArmazem(Empregado emp){
        setContentPane(painelP);
        setTitle("Hub do Armazem");
        setSize(400, 300);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        lbTexto.setText("Bem-vindo " + emp.getNome() + ".\n");
        setVisible(true);

        //Atualizar Stock no Armazem
        btASArmazem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                AtualizarArmazem aa = new AtualizarArmazem(emp);
                aa.setLocationRelativeTo(null);
            }
        });

        //sair
        btSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                login login = new login();
                login.setLocationRelativeTo(null);

            }
        });

    }
}
