package bank;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, String[]> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
        loadAccounts();
    }

    // 读取账户信息的方法
    private void loadAccounts() {
        try {
            File file = new File("D:\\bank5.7\\bank\\account.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 4) {
                        accounts.put(parts[0], new String[]{parts[1], parts[2], parts[3]});
                    } else {
                        System.err.println("Invalid account information: " + line);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 保存账户信息的方法
    private void saveAccounts() {
        try {
            File file = new File("D:\\bank5.7\\bank\\account.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String, String[]> entry : accounts.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue()[0] + " " + entry.getValue()[1] + " " + entry.getValue()[2]);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 // 检查登录的方法
    public boolean checkLogin(String parentID, String password) {
        try {
            File file = new File("D:\\bank5.7\\bank\\account.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 3 && parts[0].equals(parentID) && parts[1].equals(password)) {
                        reader.close();
                        return true;
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Login failed. Input: Username: " + parentID + ", Password: " + password);
        return false;
    }
    public boolean isChildrenIDExists(String childrenID) {
        try {
            File file = new File("D:\\bank5.7\\bank\\account.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 4 && parts[2].equals(childrenID)) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 检查父账户ID是否存在的方法
    public boolean isParentIDExists(String parentID) {
        return accounts.containsKey(parentID);
    }

    // 注册父账户的方法
    public void registerParent(String parentID, String password, String childrenID) {
        accounts.put(parentID, new String[]{password, childrenID, "0.0"});
        saveAccounts();
    }

    /**
     * 增加/减少孩子账户金额
     * @param childName
     * @param money 增加的金额 +增加 -减少
     */
    public static void updateChildMoney(String childName, Double money) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\accountC.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(childName)) {
                    parts[parts.length - 1] = Double.parseDouble(parts[parts.length - 1]) + money + "";
                    line = String.join(" ", parts);
                }
                fileContent.append(line).append("\n");
            }
            FileWriter writer = new FileWriter("D:\\bank5.7\\bank\\accountC.txt");
            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 根据父名字获取孩子名字
     * @param pName
     * @return
     */
    public static String getChildNameByPName(String pName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\account.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(pName)) {
                    return parts[2];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 根据孩子名字获取父名字
     * @param cName
     * @return
     */
    public static String getPNameByCName(String cName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\account.txt"))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[2].equals(cName)) {
                    return parts[0];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取孩子账户余额
     * @param cName
     * @return
     */
    public static String getMoney(String cName) {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\bank5.7\\bank\\accountC.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(cName)) {
                    return parts[2];
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args){
        String cone = getMoney("cone");
        System.out.println(cone);
    }
}
