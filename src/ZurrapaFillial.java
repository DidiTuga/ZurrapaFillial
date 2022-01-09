import javax.swing.*;

public class ZurrapaFillial {
    public static boolean valor = false;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()); //Para definir como windows para aprsentar uma interface mais clean.
        } catch (Exception e) {
        }
        JFrame.setDefaultLookAndFeelDecorated(true);

        login _Frame = new login();
    }


}
