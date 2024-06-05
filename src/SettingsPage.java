package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import SwingUIs.FrameBP;

public class SettingsPage extends JPanel{

    public static File defaultPath;

    public SettingsPage() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        setLayout(new BorderLayout());
    
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(40, 40, 55)));
        header.setPreferredSize(new Dimension(69, 100));
        header.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 58));

        JLabel headLabel = createLabel("Settings", 25f);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5));
        content.setBorder(new EmptyBorder(10, 5, 5, 5));


        JLabel defPathLabel = createLabel("Default Location : ", 16f);
        JTextField pathField = createField(500, 300);
        if(SettingsPage.defaultPath != null) pathField.setText(SettingsPage.defaultPath.getAbsolutePath());
        pathField.setEditable(false);

        JButton browse = new JButton();
        browse.setIcon(FrameBP.scaledIcon(new ImageIcon("icons/folder.png"), 20, 20));
        browse.setPreferredSize(new Dimension(30,30));
        browse.setBackground(new Color(40, 40, 50));
        browse.setFocusPainted(false);
        browse.setBorderPainted(false);
        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(SettingsPage.defaultPath != null) chooser.setSelectedFile(SettingsPage.defaultPath);

            JDialog d = new JDialog(null, "Select project", Dialog.ModalityType.APPLICATION_MODAL);
            d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            d.setAlwaysOnTop(true);

            chooser.addActionListener(e1 -> {
                if(JFileChooser.APPROVE_SELECTION.equals(e1.getActionCommand())) {
                    File f1 = chooser.getSelectedFile();
                    pathField.setText(f1.getAbsolutePath());
                    SettingsPage.defaultPath = f1;

                    // TODO : Replace the first line in the SettingsData.txt to the given path (mini database)
                }
                d.dispose();
            });

            d.getContentPane().add(chooser, BorderLayout.CENTER);
            d.setSize(600, 600);
            d.setLocationRelativeTo(null);
            d.setVisible(true);
        });

        JTextArea notice = new JTextArea("> The location text field is not editable. Use the browse button to assign a directory");
        notice.setPreferredSize(new Dimension(600, 40));
        notice.setWrapStyleWord(true);
        notice.setLineWrap(true);
        notice.setForeground(new Color(80, 80, 100));
        notice.setFont(Controller.regular.deriveFont(14f));
        notice.setEditable(false);
        notice.setCaretColor(new Color(0, 0, 0, 1));
        notice.setOpaque(false);

        header.add(headLabel);
        content.add(defPathLabel);
        content.add(pathField);
        content.add(Box.createHorizontalStrut(10));
        content.add(browse);
        content.add(notice);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    private JLabel createLabel(String txt, float fontSize) {
        JLabel label = new JLabel(txt);
        label.setFont(Controller.regular.deriveFont(fontSize));
        label.setForeground(new Color(180, 180, 250));
        return label;
    }

    private JTextField createField(int length, int maxChar) {
        JTextField field = new JTextField();
        field.setCaretColor(new Color(200, 200, 220));
        field.setPreferredSize(new Dimension(length, 30));
        field.setBackground(new Color(40, 40, 50));
        field.setForeground(new Color(180, 180, 200));
        field.setBorder(new EmptyBorder(5,5,5,5));
        Font f = Controller.regular.deriveFont(16f);
        field.setFont(f);
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (field.getText().length() >= maxChar)
                    e.consume();
            }
        });
        return field;
    }
}
