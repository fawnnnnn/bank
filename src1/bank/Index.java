package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Index extends JFrame {
    private String username;
    public Index(String username) {
        this.username = username;
        setTitle("Index-"+username);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("username:"+this.username+" welcome!"));

        JPanel panel1 = new JPanel();
        add(panel, BorderLayout.NORTH);
        add(panel1, BorderLayout.CENTER);
        panel1.setLayout(new GridLayout(3, 2));
        JButton taskView = new JButton("Task View");
        taskView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChildTaskPage(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton goal = new JButton("Goal");
        goal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CurrentDeposite(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton deposite = new JButton("Deposite");
        deposite.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Deposite(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton withDrawRecord = new JButton("With Draw Record");
        withDrawRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AccountDisplay(username).setVisible(true);
                setVisible(false);
            }
        });
        panel1.add(taskView);
        panel1.add(goal);
        panel1.add(deposite);
        panel1.add(withDrawRecord);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Index("cone").setVisible(true);
            }
        });
    }
}
