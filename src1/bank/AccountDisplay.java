package bank;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class AccountDisplay extends JFrame {

    private JTable table;
    String filePath = "D:\\bank5.7\\bank\\ChildAccount.txt";
    private String username;
    public AccountDisplay(String username) {
        this.username = username;
        setTitle("Current Deposit-"+username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Date", "Amount", "Agree"};

        String[][] data = readDataFromFile(filePath);

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private String[][] readDataFromFile(String filePath) {
        String[][] data = new String[0][0];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    sb.append(line).append("\n");
                }
            }
            reader.close();

            String[] lines = sb.toString().split("\n");
            data = new String[lines.length][4];
            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].split(" ");
                data[i][0] = values[0];
                data[i][1] = values[1];
                data[i][2] = values[2];
                data[i][3] = values[3];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccountDisplay("cone").setVisible(true));
    }
}
