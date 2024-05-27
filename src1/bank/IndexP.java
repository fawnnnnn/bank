package bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IndexP extends JFrame {
    private String username;
    public IndexP(String username) {
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
                new TaskDisplay(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton spTaskView = new JButton("Special Task View");
        spTaskView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SpecialTaskDisplay(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton taskSetup = new JButton("Task setup");
        taskSetup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TaskSetUp(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton spTaskSetup = new JButton("Special task setup");
        spTaskSetup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TasksSetUp(username).setVisible(true);
                setVisible(false);
            }
        });
        JButton withDrawApply = new JButton("With Draw Apply");
        withDrawApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AccountDisplayP(username).setVisible(true);
                setVisible(false);
            }
        });
        panel1.add(taskView);
        panel1.add(spTaskView);
        panel1.add(taskSetup);
        panel1.add(spTaskSetup);
        panel1.add(withDrawApply);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new IndexP("pone").setVisible(true);
            }
        });
    }
}
