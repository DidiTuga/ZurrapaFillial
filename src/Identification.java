import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.util.Scanner;

public class Identification extends JFrame {

    private JPanel window;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton clearButton;
    private JButton confirmButton;


    public Identification() {
        File fileExistence = new File("filialIdentification.txt");
        if(fileExistence.exists() && !fileExistence.isDirectory()) {
            try {
                File fileObject = new File("filialIdentification.txt");
                Scanner file = new Scanner(fileObject);
                String filialIdentification = file.nextLine();
                login employeeLogin = new login(filialIdentification);
                file.close();
            } catch (FileNotFoundException e) {
                System.out.println("Ocorreu um erro na leitura de ficheiro.");
                e.printStackTrace();
            }
        } else {
            //----- Definicoes da Janela -----
            this.setContentPane(window);
            this.setTitle("Janela de Identificação");
            this.setSize(350, 200);
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            this.setVisible(true);

            //----- Acoes -----
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try {
                        String query = "SELECT IDFilial, Designacao, Email, Palavra_passe FROM TblFilial WHERE Email = ? AND Palavra_passe = ?";
                        Connection connection = Conectar.getCon("ZurrapaSede");
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, emailField.getText());
                        preparedStatement.setString(2, String.valueOf(passwordField.getPassword()));
                        ResultSet result = preparedStatement.executeQuery();

                        if (result.next()) {
                            try {
                                File fileObject = new File("filialIdentification.txt");
                                if (fileObject.createNewFile()) {
                                    System.out.println("Ficheiro de Identificacao Criado: " + fileObject.getName());
                                } else {
                                    System.out.println("File already exists.");
                                }
                                try {
                                    FileWriter fileObjectWriter = new FileWriter("filialIdentification.txt");
                                    fileObjectWriter.write(result.getString(1));
                                    fileObjectWriter.close();
                                } catch (IOException exception) {
                                    System.out.println("Ocorreu um erro ao gravar o ficheiro");
                                    exception.printStackTrace();
                                }
                            } catch (IOException exception) {
                                System.out.println("Ocorreu um erro ao gravar a identificacao");
                                exception.printStackTrace();
                            }
                            dispose();
                            login employeeLogin = new login(result.getString(1));
                            connection.close();
                        } else {
                            JOptionPane.showMessageDialog(null, "Os dados estão incorrectos!");
                            connection.close();
                        }

                    } catch (SQLException exception) {
                        System.out.println("Ups, deu erro na query");
                        exception.printStackTrace();
                    }

                }
            });

            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    emailField.setText("");
                    passwordField.setText("");
                }
            });
        }
    }

}
