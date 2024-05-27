package bank;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChildRegistration extends JFrame {
    private JTextField childrenIDField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private AccountManager accountManager;

    public ChildRegistration() {
        setTitle("Child Registration");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        accountManager = new AccountManager();

        JPanel registerPanel = createPanel("Register");

        add(registerPanel);
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

        childrenIDField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);

        registerButton = new JButton(title);
        registerButton.setBackground(new Color(0, 128, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        gbc.gridy++;
        panel.add(titleLabel, gbc);
        gbc.gridy++;
        panel.add(new JLabel("Children ID:"), gbc);
        gbc.gridx++;
        panel.add(childrenIDField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx++;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx++;
        panel.add(confirmPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        return panel;
    }

    private void performRegistration() {
        String childrenID = childrenIDField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!isValidID(childrenID)) {
            JOptionPane.showMessageDialog(null, "Children ID must be at least 4 characters and contain only letters.");
        } else if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters and contain at least one letter and one digit.");
        } else if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match. Please re-enter.");
        } else {
            if (accountManager.isChildrenIDExists(childrenID)) { // Check if children ID exists in account file
                try {
                    boolean validAccount = false;
                    File file = new File("D:/bank5.7/bank/account.txt");
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(" ");
                        if (parts.length == 4 && parts[2].equals(childrenID)) {
                            validAccount = true;
                            break;
                        }
                    }
                    br.close();

                    if (validAccount) {
                        FileWriter fw = new FileWriter("D:/bank5.7/bank/accountC.txt", true);
                        PrintWriter pw = new PrintWriter(fw);
                        pw.println(childrenID + " " + password + " " + "0");
                        pw.close();
                        JOptionPane.showMessageDialog(null, "Registration successful!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Children ID. Please enter a valid Children ID.");
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error occurred while writing to file.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Children ID does not exist. Please enter a valid Children ID.");
            }
        }
    }

    private boolean isValidID(String id) {
        return id.length() >= 4 && id.matches("[a-zA-Z]+");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChildRegistration().setVisible(true);
            }
        });
    }
}

