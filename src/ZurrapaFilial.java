import javax.swing.*;

public class ZurrapaFilial {

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {}

        JFrame.setDefaultLookAndFeelDecorated(true);
        Identification identificationWindow = new Identification();
        identificationWindow.setLocationRelativeTo(null);

    }

}