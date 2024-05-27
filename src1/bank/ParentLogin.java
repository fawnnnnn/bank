package bank;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ParentLogin extends JFrame {
    private JTextField userIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AccountManager accountManager;

    public ParentLogin() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        accountManager = new AccountManager();

        JPanel loginPanel = createPanel("Login");

        add(loginPanel);
    }

    private JPanel createPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        userIDField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton(title);
        loginButton.setBackground(new Color(0, 128, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        gbc.gridy++;
        panel.add(titleLabel, gbc);
        gbc.gridy++;
        panel.add(new JLabel("User ID:"), gbc);
        gbc.gridx++;
        panel.add(userIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx++;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        return panel;
    }

    private void performLogin() {
        String userID = userIDField.getText();
        String password = new String(passwordField.getPassword());

        if (accountManager.checkLogin(userID, password)) {
            JOptionPane.showMessageDialog(null, "Login Successful!");
            this.setVisible(false);
            new IndexP(userID).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid UserID or Password!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ParentLogin().setVisible(true);
            }
        });
    }
}
