
package bank;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChildTaskPage extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable[] tables;
    private DefaultTableModel[] tableModels;
    private String[] tabTitles = {"Daily Tasks", "Special Tasks", "Completed Tasks without Grade","Completed Tasks with Grade"};
    private JComboBox<String> ratingComboBox;
    private DefaultCellEditor ratingEditor;
    private JButton submitButton;
    private JButton submitButton2;

    private String username;

    public ChildTaskPage(String username) {
        this.username = username;
        setTitle("Task Page");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tables = new JTable[4];
        tableModels = new DefaultTableModel[4];
        for (int i = 0; i < 4; i++) {
            tableModels[i] = new DefaultTableModel();
            tableModels[i].addColumn("Name of Task");
            tableModels[i].addColumn("Award Amount");
            tableModels[i].addColumn("Start Dates");
            tableModels[i].addColumn("FrequencyORDeadline");
            tableModels[i].addColumn("Detail Information");
            if (i == 2 || i==3) { // Add rating column to "Published Finished Tasks" and "Completed and Rated Tasks" tables
                tableModels[i].addColumn("Rating");
            }
            
            tables[i] = new JTable(tableModels[i]);
            JScrollPane scrollPane = new JScrollPane(tables[i]);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            if (i == 0) { // Only for "Published Finished Tasks"
                submitButton = new JButton("Submit");
                submitButton.addActionListener(new ChildTaskPage.SubmitButtonActionListener());
                panel.add(submitButton, BorderLayout.SOUTH);
            }
            if (i == 1) { // Only for "Published Finished Tasks"
                submitButton2 = new JButton("Submit");
                submitButton2.addActionListener(new ChildTaskPage.SubmitButtonActionListener2());
                panel.add(submitButton2, BorderLayout.SOUTH);
            }
            tabbedPane.addTab(tabTitles[i], panel);
        }

        add(tabbedPane, BorderLayout.CENTER);
        
        
        displayTasks();
        displaySpecialTasks();

        setVisible(true);
    }
    
    private void displayTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:/bank5.7/bank/task.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String pName = AccountManager.getPNameByCName(username);
                String[] split = parts[0].split("-");
                if (parts.length >= 7 && pName.equals(split[1])) {
                    String status = parts[parts.length - 2];
                    String nameOfTask = parts[0];
                    String awardAmount = parts[1];
                    String startDate = parts[2];
                    String frequencyORdata = parts[3];
                    StringBuilder detailInfo = new StringBuilder();
                    for (int i = 4; i < parts.length - 2; i++) {
                        detailInfo.append(parts[i]);
                        if (i < parts.length - 3) {
                            detailInfo.append(" ");
                        }
                    }
                    int tabIndex = Integer.parseInt(status) - 1; // Calculate tab index based on status

                    
                    if (tabIndex == 0 || tabIndex == 1 || tabIndex == 2|| tabIndex == 3) {
                        // Check if the task already exists in the table of the corresponding tab
                        boolean taskExists = false;
                        for (int row = 0; row < tableModels[tabIndex].getRowCount(); row++) {
                            if (nameOfTask.equals(tableModels[tabIndex].getValueAt(row, 0))) {
                                taskExists = true;
                                break;
                            }
                        }

                        if (!taskExists) {
                            Object[] rowData;
                            if (tabIndex == 1 || tabIndex == 2) { // For 3rd and 4th tab, add rating column
                                String rating = parts[parts.length - 1];
                                rowData = new Object[]{nameOfTask, awardAmount, startDate, frequencyORdata, detailInfo.toString(), rating};
                                tableModels[tabIndex+1].addRow(rowData);
                            } else {
                                rowData = new Object[]{nameOfTask, awardAmount, startDate, frequencyORdata, detailInfo.toString()};
                                tableModels[tabIndex].addRow(rowData);
                            }
                            
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ChildTaskPage.this, "Error occurred while reading tasks!");
        }
    }

    private void displaySpecialTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:/bank5.7/bank/taskspecial.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String pName = AccountManager.getPNameByCName(username);
                String[] split = parts[0].split("-");
                if (parts.length >= 7 && pName.equals(split[1])) {
                    String status = parts[parts.length - 2];
                    String nameOfTask = parts[0];
                    String awardAmount = parts[1];
                    String startDate = parts[2];
                    String deadline = parts[3];
                    String detailInfo = parts[4];
                    int tabIndex = Integer.parseInt(status) ;
                    int tabIndex2=0;
                    if(tabIndex>1) {
                    	tabIndex2=tabIndex+1 ;
                    }
                    
                    if (tabIndex == 1 || tabIndex == 2 || tabIndex == 3) { // Check if data should be displayed in 2nd, 3rd, or 4th tab
                        // Add data to the corresponding table (tables[tabIndex])
                        boolean taskExists = false;
                        for (int row = 0; row < tableModels[tabIndex].getRowCount(); row++) {
                            if (nameOfTask.equals(tableModels[tabIndex].getValueAt(row, 0))) {
                                taskExists = true;
                                break;
                            }
                        }
                        if (!taskExists) {
                            Object[] rowData;
                            if (tabIndex == 2 || tabIndex == 3) {
                                String rating = parts[parts.length - 1];
                                rowData = new Object[]{nameOfTask, awardAmount, startDate, deadline, detailInfo, rating};
                            } else {
                                rowData = new Object[]{nameOfTask, awardAmount, startDate, deadline, detailInfo};
                            }
                            tableModels[tabIndex].addRow(rowData);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ChildTaskPage.this, "Error occurred while reading special tasks!");
        }
    }
    private void updateSpTaskStatusAndRating(String taskName, String newStatus) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:/bank5.7/bank/taskspecial.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 7 && parts[0].equals(taskName) && parts[parts.length - 2].equals("1")) {
                    parts[parts.length - 2] = newStatus;
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            FileWriter writer = new FileWriter("D:/bank5.7/bank/taskspecial.txt");
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ChildTaskPage.this, "Error occurred while updating task status and rating!");
        }
    }

    private void updateTaskStatusAndRating(String taskName, String newStatus) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:/bank5.7/bank/task.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 7 && parts[0].equals(taskName) && parts[parts.length - 2].equals("1")) {
                    parts[parts.length - 2] = newStatus;
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            FileWriter writer = new FileWriter("D:/bank5.7/bank/task.txt");
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(ChildTaskPage.this, "Error occurred while updating task status and rating!");
        }
    }

    private boolean isTaskValid(String startDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:M:d");
            Date taskStartDate = dateFormat.parse(startDate);
            Date currentDate = new Date();
            if (taskStartDate.after(currentDate)) {
                JOptionPane.showMessageDialog(ChildTaskPage.this, "The mission has not begun!");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean isEndTaskValid(String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:M:d");
            Date taskStartDate = dateFormat.parse(endDate);
            Date currentDate = new Date();
            if (taskStartDate.before(currentDate)) {
                JOptionPane.showMessageDialog(ChildTaskPage.this, "The mission is over!");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    private class SubmitButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = 0;
            // Update the task status and rating for selected rows
            for (int i = 0; i < tables[index].getRowCount(); i++) {
                String taskName = (String) tables[index].getValueAt(i, 0);
                String startDate = (String) tables[index].getValueAt(i, 2); 
                if (tables[index].isRowSelected(i) && isTaskValid(startDate)) {
                    updateTaskStatusAndRating(taskName, "2");
                }
            }
            // Clear the table
            while (tableModels[index].getRowCount() > 0) {
                tableModels[index].removeRow(0);
            }
            // Display tasks again to refresh the table
            new ChildTaskPage(username).setVisible(true);
            setVisible(false);
        }
    }

    private class SubmitButtonActionListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = 1;
            for (int i = 0; i < tables[index].getRowCount(); i++) {
                String taskName = (String) tables[index].getValueAt(i, 0);
                String startDate = (String) tables[index].getValueAt(i, 2); 
                String endDate = (String) tables[index].getValueAt(i, 3); 
                if (tables[index].isRowSelected(i) && isTaskValid(startDate) && isEndTaskValid(endDate)) {
                    updateSpTaskStatusAndRating(taskName, "2");
                }
            }
            // Clear the table
            while (tableModels[index].getRowCount() > 0) {
                tableModels[index].removeRow(0);
            }
            // Display tasks again to refresh the table
            new ChildTaskPage(username).setVisible(true);
            setVisible(false);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ChildTaskPage("cone");
            }
        });
    }
}

