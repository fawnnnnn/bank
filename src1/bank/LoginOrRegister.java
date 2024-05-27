package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginOrRegister extends JFrame {
    private JButton loginButton;
    private JButton registerButton;
    private JButton childrenRegisterButton;
    private JButton childrenLoginButton;

    public LoginOrRegister() {
        setTitle("Choose Login or Register");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        childrenRegisterButton = new JButton("Children Register");
        childrenLoginButton = new JButton("Children Login");

        JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(childrenRegisterButton);
        panel.add(childrenLoginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ParentLogin().setVisible(true);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ParentRegister().setVisible(true);
            }
        });

        childrenRegisterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ChildRegistration().setVisible(true);
            }
        });

        childrenLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ChildLogin().setVisible(true);
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginOrRegister().setVisible(true);
            }
        });
    }
}
