package bank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AccountDisplayP extends JFrame {

    private JTable table;
    String filePath = "D:\\bank5.7\\bank\\ChildAccount.txt";
    private String username;

    private JButton submitButton = new JButton("Agree");
    public AccountDisplayP(String username) {
        this.username = username;
        setTitle("Current Deposit-"+username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Date", "Amount", "Agree", "Timestamp"};

        String[][] data = readDataFromFile(filePath);

        table = new JTable(data, columnNames);
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double sum = 0D;
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.isRowSelected(i)) {
                        String timestamp = (String) table.getValueAt(i, 4);
                        sum += agree(timestamp);
                    }
                }
                JOptionPane.showMessageDialog(null, "agree with draw "+sum+" successfully!");
                setVisible(false);
                new AccountDisplayP(username).setVisible(true);
            }
        });

        add(panel);
    }
    Double agree(String timestamp) {
        String childName = AccountManager.getChildNameByPName(this.username);
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\ChildAccount.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            Double money = 0D;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if ( parts[parts.length - 2].equals("No") && parts[parts.length - 1].equals(timestamp) && parts[0].equals(childName)) {
                    parts[parts.length - 2] = "Yes";
                    money += Double.parseDouble(parts[2]);
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            FileWriter writer = new FileWriter("D:\\bank5.7\\bank\\ChildAccount.txt");
            writer.write(fileContent.toString());
            writer.close();
            Double money1 = Double.parseDouble(AccountManager.getMoney(childName));

            if (money1 - money < 0) {
                JOptionPane.showMessageDialog(null, "error, 账户余额不足!");
                throw new RuntimeException();
            }
            AccountManager.updateChildMoney(childName, -money);
            return money;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0D;
    }
    private String[][] readDataFromFile(String filePath) {
        String[][] data = new String[0][0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            StringBuilder sb = new StringBuilder();
            String line;
            String childName = AccountManager.getChildNameByPName(this.username);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(childName)) {
                    sb.append(line).append("\n");
                }
            }
            reader.close();

            String[] lines = sb.toString().split("\n");
            data = new String[lines.length][5];
            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].split(" ");
                data[i][0] = values[0];
                data[i][1] = values[1];
                data[i][2] = values[2];
                data[i][3] = values[3];
                data[i][4] = values[4];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountDisplayP("pone").setVisible(true));
    }
}
