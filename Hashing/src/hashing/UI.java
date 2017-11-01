package hashing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User interface for password storage client
 * @author  Caroline Zeng
 * @version 0.1.1
 */

public class UI extends JFrame {
    JButton enter    = new JButton("Enter");
    JButton quit     = new JButton("Quit");
    String[] options = new String[] {"Add Login", "Check Password", "See Stored Usernames",
            "Get a Hash", "Delete Info", "Clear All"};
    JComboBox<String> optionsBox = new JComboBox<>(options);
    FileProcessing file;

    /**
     * Constructor for the password storage client UI
     */
    public UI()
    {
        file        = new FileProcessing();
        JPanel ops  = new JPanel();
        ops.setLayout(new BoxLayout(ops, BoxLayout.X_AXIS));
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String choice = (String) optionsBox.getSelectedItem();

                JFrame frame        = new JFrame();
                JTextField name     = new JTextField("Username", 10);
                JPasswordField pass = new JPasswordField(10);
                JButton enter2      = new JButton("Enter");
                JButton close       = new JButton("Close");

                frame.setTitle(choice);
                close.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                    }
                });
                pass.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        enter.doClick();
                    }
                });

                switch (choice)
                {
                    case "Add Login":
                        JPanel panel = new JPanel();

                        enter2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean in = file.putInfo(name.getText(), new String(pass.getPassword()));
                                if (in) JOptionPane.showMessageDialog(null, "Login Added Successfully");
                                else JOptionPane.showMessageDialog(null, "Unable to Add Login");
                            }
                        });

                        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                        panel.add(name);
                        panel.add(pass);
                        panel.add(enter2);
                        panel.add(close);

                        frame.getContentPane().add(panel, "North");
                        frame.pack();
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        break;

                    case "Check Password":
                        panel = new JPanel();
                        enter2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                boolean right = file.checkPassword(name.getText(), new String(pass.getPassword()));
                                if (right) JOptionPane.showMessageDialog(null, "Password is Correct");
                                else JOptionPane.showMessageDialog(null, "Password is Incorrect");
                            }
                        });
                        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                        panel.add(name);
                        panel.add(pass);
                        panel.add(enter2);
                        panel.add(close);

                        frame.getContentPane().add(panel, "North");
                        frame.pack();
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        break;

                    case "See Stored Usernames":
                        JTextArea names = new JTextArea(10, 10);
                        panel           = new JPanel();
                        names.setText(file.getUserNames());
                        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                        panel.add(close);
                        panel.add(new JScrollPane(names));

                        frame.getContentPane().add(panel, "North");
                        frame.pack();
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        break;

                    case "Get a Hash":
                        panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                        enter2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String[] hash = HashFunction.rollingHash2New(new String(pass.getPassword()));
                                JOptionPane.showMessageDialog(null,
                                        "Hash: " + hash[0] +
                                                "\nSalt: " + hash[1] +
                                                "\nOriginal Text: " + new String(pass.getPassword()));
                            }
                        });
                        panel.add(pass);
                        panel.add(enter2);
                        panel.add(close);

                        frame.getContentPane().add(panel, "North");
                        frame.pack();
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        break;

                    case "Delete Info":
                        panel = new JPanel();
                        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
                        enter2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (file.checkPassword(name.getText(), new String(pass.getPassword())))
                                {
                                    int b = JOptionPane.showConfirmDialog(null, "Are you sure?");
                                    if (b == JOptionPane.YES_OPTION) file.map.remove(name.getText());
                                }
                            }
                        });
                        panel.add(name);
                        panel.add(pass);
                        panel.add(enter2);
                        panel.add(close);
                        frame.getContentPane().add(panel, "North");
                        frame.pack();
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);
                        break;

                    case "Clear All":
                        int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear all stored data?");
                        if (a == JOptionPane.YES_OPTION)
                        {
                            file.map.clear();
                        }
                        break;

                    default:
                }
            }
        });
        ops.add(optionsBox);
        ops.add(enter);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file.close();
                System.exit(0);
            }
        });
        p.add(quit);

        this.setTitle("Password Client");
        this.getContentPane().add(ops, "North");
        this.getContentPane().add(p, "Center");
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        new UI();
    }
}