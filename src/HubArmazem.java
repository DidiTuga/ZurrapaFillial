import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HubArmazem extends JFrame {
    private JPanel painelP;
    private JButton btASArmazem;
    private JButton btCTSBar;
    private JButton btSair;
    private JLabel lbTexto;
    private JButton btEstatisticas;

    public HubArmazem(Empregado emp) {
        setContentPane(painelP);
        setTitle("Hub do Armazem");
        setSize(420, 350);
        setResizable(false); //Assim nao se pode mudar o tamanho
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //clicar no x para fechar
        lbTexto.setText("Bem-vindo " + emp.getNome() + ".\n");
        setLocationRelativeTo(null);
        setVisible(true);

        //Atualizar Stock no Armazem
        btASArmazem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                AtualizarArmazem aa = new AtualizarArmazem(emp);
            }
        });
        //Adicionar stock do bar e tirar do armazem
        btCTSBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdicionarBar bb = new AdicionarBar(emp);
            }
        });

        //Estatisticas abre uma janela
        btEstatisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Estatistica esta = new Estatistica();
            }
        });


        //sair
        btSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                login inicio = new login(login.FilialIdentification);
            }
        });


    }
}
