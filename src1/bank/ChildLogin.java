package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChildLogin extends JFrame {
    private JTextField childrenIDField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public ChildLogin() {
        setTitle("Child Login");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        childrenIDField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        panel.add(new JLabel("Children ID:"));
        panel.add(childrenIDField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String childrenID = childrenIDField.getText();
                String password = new String(passwordField.getPassword());
                boolean loginSuccessful = checkLogin(childrenID, password);
                if (loginSuccessful) {
                    JOptionPane.showMessageDialog(ChildLogin.this, "Login Successful!");
                    new Index(childrenID).setVisible(true);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(ChildLogin.this, "Invalid Children ID or Password!");
                }
            }
        });

        add(panel);
    }

    private boolean checkLogin(String childrenID, String password) {
        try {
            File file = new File("D:/bank5.7/bank/accountC.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3 && parts[0].equals(childrenID) && parts[1].equals(password)) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChildLogin().setVisible(true);
            }
        });
    }
}
