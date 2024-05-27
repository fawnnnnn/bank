package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.Date;

public class CurrentDeposite extends JFrame {
    private String username;
    JTextField goal = new JTextField();
    JTextField current = new JTextField();
    JTextField interest = new JTextField();
    JTextField input = new JTextField();

    public CurrentDeposite(String username) {
        this.username = username;
        setTitle("CurrentDeposite-"+username );
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(new JButton("goal:"));
        panel.add(goal);
        panel.add(new JButton("current:"));
        panel.add(current);
        panel.add(new JButton("cummualtive interest:"));
        panel.add(interest);
        panel.add(input);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 2));
        JButton setGoal = new JButton("set goal");
        setGoal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(input.getText());
                    setGoal(v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                input.setText("");
            }
        });
        JButton withDraw = new JButton("withdraw");
        withDraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(input.getText());
                    withDraw(v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                input.setText("");
            }
        });
        jPanel.add(setGoal);
        jPanel.add(withDraw);
        panel.add(jPanel);
        goal.setEnabled(false);
        current.setEnabled(false);
        interest.setEnabled(false);
        add(panel);
        init();
    }

    void withDraw(Double money) {
        double v = Double.parseDouble(AccountManager.getMoney(username));
        if (v<money) {
            JOptionPane.showMessageDialog(null, "余额不足");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\ChildAccount.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\ChildAccount.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            LocalDate date = LocalDate.now();
            writer.write(username+" "+date+" "+money+" No "+new Date().getTime()+"\n"+fileContent);

            writer.close();
            JOptionPane.showMessageDialog(null, "apply with draw "+money+" successfully");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //AccountManager.updateChildMoney(username, -money);
    }

    void setGoal(Double goal) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\goal.txt"))) {
            String line;
            boolean save = true;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    parts[1] = goal+"";
                    line = String.join(" ", parts);
                    save = false;
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\goal.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            if (save) {
                writer.write(username+" "+goal+"\n"+fileContent);
            }else {
                writer.write(fileContent.toString());
            }
            writer.close();
            JOptionPane.showMessageDialog(null, "set goal "+goal+" successfully");
            init();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Double getGoal() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\goal.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    return Double.parseDouble(parts[1]);
                }
            }
        } catch (Exception e) {

        }
        return 0D;
    }

    void init() {
        // get goal
        Double goal = getGoal();
        this.goal.setText(goal+"");
        // get current
        double cMoney = Double.parseDouble(AccountManager.getMoney(username));
        Deposite deposite = new Deposite(username);
        deposite.calcInterest();
        double bankSum = deposite.getDemand() + deposite.getTimeMoney();
        current.setText(cMoney+bankSum+"");
        // get interest
        Double totalInterest = deposite.getInterest();
        interest.setText(totalInterest+"");

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CurrentDeposite("ctwo").setVisible(true);
            }
        });
    }
}
