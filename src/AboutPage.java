package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import SwingUIs.FrameBP;

public class AboutPage extends JPanel{
    public AboutPage() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel logo = new JLabel("DevStack_");
        logo.setIcon(FrameBP.scaledIcon(new ImageIcon("icons/PM_Logo.png"), 120, 120));
        logo.setFont(Controller.regular.deriveFont(35f));
        logo.setForeground(new Color(190, 190, 230));
        logo.setIconTextGap(-20);

        JLabel version = new JLabel("2020.1.0");
        version.setFont(Controller.regular.deriveFont(18f));
        version.setForeground(new Color(120, 120, 150));
        version.setBorder(new EmptyBorder(14, 5, 0, 0));

        JTextArea desc = new JTextArea(
            "DevStack_ is an application developed by Someone, development started since may, 2024" +
            " It's created solely for the usage of the creator, and as a project meant for a better portfolio."
        );
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setForeground(new Color(180, 180, 220));
        desc.setBackground(new Color(40, 40, 50));
        desc.setBorder(new EmptyBorder(10, 10, 10, 10));
        desc.setFont(Controller.regular.deriveFont(14f));
        desc.setSelectionColor(new Color(160, 160, 255));


        JPanel p1 = new JPanel();
        p1.setOpaque(false);
        p1.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        p1.add(logo);
        p1.add(version);
        add(p1, BorderLayout.NORTH);
        add(desc, BorderLayout.SOUTH);
    }
}
