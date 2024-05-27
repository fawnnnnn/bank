package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.Date;

public class Deposite extends JFrame {
    private String username;
    private JLabel accumulatedIncome = new JLabel("accumulated income: 0");

    private JLabel accumulatedIncome2 = new JLabel("accumulated income: 0");
    private JLabel balance = new JLabel("balance: 0");
    private JLabel balance2 = new JLabel("balance: 0");
    private JTextField jTextField = new JFormattedTextField();
    private JTextField jTextField2 = new JFormattedTextField();
    public Deposite(String username) {
        this.username = username;
        setTitle("Deposite:"+this.username);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1, 1));
        JPanel panel1_1 = new JPanel();
        panel1_1.setLayout(new GridLayout(7, 1));
        panel1_1.add(new JLabel("interest(Week) 1.5%"));
        panel1_1.add(accumulatedIncome);
        JButton rollIn = new JButton("Roll in");
        rollIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(jTextField.getText());
                    rollInDemand(username, v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                jTextField.setText("");
            }
        });
        JButton rollOut= new JButton("Roll out");
        rollOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(jTextField.getText());
                    rollOutDemand(username, v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                jTextField.setText("");
            }
        });
        panel1_1.add(balance);
        panel1_1.add(new JLabel("Demand Deposite"));
        panel1_1.add(jTextField);
        panel1_1.add(rollIn);
        panel1_1.add(rollOut);
        panel1.add(panel1_1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 1));
        JPanel panel2_1 = new JPanel();
        panel2_1.setLayout(new GridLayout(7, 1));
        panel2_1.add(new JLabel("interest(Month) 5%"));
        panel2_1.add(accumulatedIncome2);
        JButton rollIn2 = new JButton("Roll in");
        rollIn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(jTextField2.getText());
                    rollInTime(username, v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                jTextField2.setText("");
            }
        });
        JButton rollOut2= new JButton("Roll out");
        rollOut2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Double v=0D;
                try {
                    v = Double.parseDouble(jTextField2.getText());
                    rollOutTime(v);
                }catch (Exception exception) {
                    System.out.println("异常："+exception.getMessage());
                }
                jTextField2.setText("");
            }
        });
        panel2_1.add(balance2);
        panel2_1.add(new JLabel("Time Deposite"));
        panel2_1.add(jTextField2);
        panel2_1.add(rollIn2);
        panel2_1.add(rollOut2);
        panel2.add(panel2_1);

        add(panel1);
        add(panel2);

        init();

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Deposite("cone").setVisible(true);
            }
        });
    }

    /**
     * 活期存款
     * @param username
     */
    void rollInDemand(String username, Double money) {
        Double cMoney = Double.parseDouble(AccountManager.getMoney(username));
        if (cMoney < money) {
            // 余额不足
            JOptionPane.showMessageDialog(null, username+"账户余额不足!");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            boolean save = true;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    save = false;
                    parts[1] = Double.parseDouble(parts[1]) + money +"";
                    parts[parts.length - 1] = Double.parseDouble(parts[parts.length - 1]) + money +"";
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\bankDemand.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            if (save) {
                writer.write(username+" "+money+ " "+ new Date().getTime() + " 0 "+ money+"\n"+fileContent);
                writer.newLine();
            }else {
                writer.write(fileContent.toString());
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AccountManager.updateChildMoney(username, 0D - money);
        JOptionPane.showMessageDialog(null, "roll in Demand "+money+" successfully");
        init();
    }
    /**
     * 活期取款
     * @param username
     */
    void rollOutDemand(String username, Double money) {
        Double demand = getDemand();
        if (demand < money) {
            // 余额不足
            JOptionPane.showMessageDialog(null, username+"活期账户余额不足!");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    parts[1] = Double.parseDouble(parts[1]) - money +"";
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\bankDemand.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AccountManager.updateChildMoney(username, money);
        JOptionPane.showMessageDialog(null, "roll out Demand "+money+" successfully");
        init();
    }

    /**
     * 定期存款
     * @param username
     */
    void rollInTime(String username, Double money) {
        Double cMoney = Double.parseDouble(AccountManager.getMoney(username));
        if (cMoney < money) {
            // 余额不足
            JOptionPane.showMessageDialog(null, username+"账户余额不足!");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            Double max = 0D;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (max==0&&parts[0].equals(username)) {
                    max = Double.parseDouble(parts[parts.length - 1]);
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\bankTime.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(username+" "+money+ " "+ new Date().getTime() + " 0 "+ (max+money)+"\n"+fileContent);
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AccountManager.updateChildMoney(username, 0D - money);
        JOptionPane.showMessageDialog(null, "roll in Time "+money+" successfully");
        init();
    }
    /**
     * 定期取款
     */
    void rollOutTime(Double money) {
        Double timeOutMoney = getTimeOutMoney();
        if (timeOutMoney < money) {
            JOptionPane.showMessageDialog(null, username+"定期账户到期余额不足!");
            return;
        }
        Double outM = money.doubleValue();
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                if (money>0&&parts[0].equals(username) && !"0".equals(parts[parts.length - 2])) {
                    if (Double.parseDouble(parts[1]) < money) {
                        parts[1] = "0";
                        money = money - Double.parseDouble(parts[1]);
                    }else {
                        parts[1] = Double.parseDouble(parts[1]) - money +"";
                        money = 0D;
                    }
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");


            }
            File file = new File("D:\\bank5.7\\bank\\bankTime.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AccountManager.updateChildMoney(username, outM);
        JOptionPane.showMessageDialog(null, "roll out Time "+outM+" successfully");
        init();
    }

    /**
     * 获取定期到期余额
     * @return
     */
    public Double getTimeOutMoney() {
        Double sum = 0D;
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username) && !"0".equals(parts[parts.length - 2])) {
                    sum += Double.parseDouble(parts[1]);
                }
            }
        } catch (Exception e) {

        }
        return sum;
    }



    /**
     * 获取活期余额
     * @return
     */
    public Double getDemand() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
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

    /**
     * 获取活期总存款
     * @return
     */
    public Double getDemandSum() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    return Double.parseDouble(parts[parts.length - 1]);
                }
            }
        } catch (Exception e) {

        }
        return 0D;
    }

    /**
     * 获取定期余额
     * @return
     */
    public Double getTimeMoney() {
        Double sum=0D;
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    sum += Double.parseDouble(parts[1]);
                }
            }
        } catch (Exception e) {

        }
        return sum;
    }

    /**
     * 获取定期总存款
     * @return
     */
    public Double getDTimeMoneySum() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    return Double.parseDouble(parts[parts.length - 1]);
                }
            }
        } catch (Exception e) {

        }
        return 0D;
    }

    /**
     * 计算活期、定期利息
     */
    void calcInterest() {
        // 计算活期利息
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 5 && Double.parseDouble(parts[parts.length - 2]) == 0D) {
                    // 判断时间
                    long current = new Date().getTime();
                    if (current > (Long.parseLong(parts[2]) + 30 * 24 * 60 * 60 * 1000L)) {
                        Double interest = Math.round(100D*Double.parseDouble(parts[1]) * 0.015D)/100D;
                        parts[parts.length - 2] = interest + "";
                        parts[1] = Double.parseDouble(parts[1]) + interest + "";
                        line = String.join(" ", parts);
                    }
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\bankDemand.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // 计算定期利息
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 5 && Double.parseDouble(parts[parts.length - 2]) == 0D) {
                    // 判断时间
                    long current = new Date().getTime();
                    if (current > ((Long.parseLong(parts[2]) + 30 * 24 * 60 * 60 * 1000L))) {
                        Double interest = Math.round(100D*Double.parseDouble(parts[1]) * 0.05D)/100D;
                        parts[parts.length - 2] = interest + "";
                        parts[1] = Double.parseDouble(parts[1]) + interest + "";
                        line = String.join(" ", parts);
                    }
                }
                fileContent.append(line).append("\n");
            }
            File file = new File("D:\\bank5.7\\bank\\bankTime.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取总利息
     * @return
     */
    Double getInterest() {
        Double sum=0D;
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankTime.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    sum += Double.parseDouble(parts[parts.length - 2]);
                }
            }
        } catch (Exception e) {

        }
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\bankDemand.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    sum += Double.parseDouble(parts[parts.length - 2]);
                }
            }
        } catch (Exception e) {

        }
        return sum;
    }

    /**
     * 初始化页面数据加载
     */
    void init() {
        // 计算活期、定期利息
        calcInterest();
        // 加载活期余额
        Double demand = getDemand();
        balance.setText("balance: "+demand);
        // 加载活期总存款
        Double demandSum = getDemandSum();
        accumulatedIncome.setText("accumulated income: "+demandSum);
        // 加载定期余额
        Double timeMoney = getTimeMoney();
        balance2.setText("balance: "+timeMoney);
        // 加载定期总存款
        Double dTimeMoneySum = getDTimeMoneySum();
        accumulatedIncome2.setText("accumulated income: "+dTimeMoneySum);

    }

}
