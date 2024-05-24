import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import SwingUIs.FrameBP;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

public class Controller {

    public JPanel cp;
    InputStream stream;
    InputStream stream2;

    public static Font regular;
    public static Font semiBold;
    int selectedPage = 0; // 0 = project page

    public Controller(JPanel cp) {
        stream = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/RedditMono-SemiBold.ttf");
        stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/RedditMono-Regular.ttf");
        try {
            regular = Font.createFont(Font.TRUETYPE_FONT, stream);
            semiBold = Font.createFont(Font.TRUETYPE_FONT, stream2);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // FrontEnd Controlss
        this.cp = cp;
        cp.add(createSidebar(), BorderLayout.WEST);
        cp.add(mainPanel(), BorderLayout.CENTER);
    }

    public void updateProjectPage() {
        cp.remove(cp.getComponentCount()-1);
        cp.add(mainPanel(), BorderLayout.CENTER);
    }

    JPanel mainPanel() {
        JPanel p = new JPanel();
        switch (selectedPage) {
            case 0:
                p = new ProjectPage();
                break;
            case 1: 
                p = new GithubPage();
                break;
            case 2:
                p = new SettingsPage();
                break;
            case 3:
                p = new AboutPage();
                break;
            default:
                break;
        }
        p.setBorder(new EmptyBorder(0,10,0,0));
        cp.revalidate();
        cp.repaint();
        return p;
    }

    JPanel createSidebar() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(250, 404));
        p.setBackground(new Color(15, 15, 20));
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        ImageIcon icn1 = FrameBP.scaledIcon(new ImageIcon("icons/folder.png"), 20, 20);
        ImageIcon icn2 = FrameBP.scaledIcon(new ImageIcon("icons/github.png"), 20, 20);
        ImageIcon icn3 = FrameBP.scaledIcon(new ImageIcon("icons/settings.png"), 20, 20);
        ImageIcon icn4 = FrameBP.scaledIcon(new ImageIcon("icons/about.png"), 20, 20);

        ButtonGroup group = new ButtonGroup();
        p.add(createLogo());

        JRadioButton b1 = sidebarButtons("Projects", icn1, 0);
        b1.setSelected(true);
        
        JRadioButton b2 = sidebarButtons("Github", icn2, 1);
        JRadioButton b3 = sidebarButtons("Settings", icn3, 2);
        JRadioButton b4 = sidebarButtons("About", icn4, 3);
        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        // ============ //
        group.add(b1);
        group.add(b2);
        group.add(b3);
        group.add(b4);

        return p;
    }

    JRadioButton sidebarButtons(String txt, ImageIcon icn, int page) {
        JRadioButton b = new JRadioButton(txt);
        Font f = regular.deriveFont(14f);
        b.setIcon(icn);
        b.setIconTextGap(10);
        b.setFont(f);
        b.setFocusable(false);
        b.setBackground(new Color(15, 15, 20));
        b.setPreferredSize(new Dimension(250, 50));
        b.setBorderPainted(false);
        b.setForeground(new Color(200, 200, 220));
        b.setHorizontalAlignment(JLabel.LEFT);
        b.setMargin(new Insets(0, 20, 0, 20));
        b.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                b.setBackground((b.isSelected()) ? new Color(40, 40, 70) : new Color(15, 15, 20));
            }
        });
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(!b.isSelected())b.setBackground(new Color(40, 40, 70));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(!b.isSelected())b.setBackground(new Color(15, 15, 20));
            }
        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedPage = page;
                cp.remove(cp.getComponentCount()-1);
                cp.add(mainPanel(), BorderLayout.CENTER);
            }
        });
        return b;
    }

    JPanel createLogo() {
        ImageIcon logo = FrameBP.scaledIcon(new ImageIcon("icons/PM_Logo.png"), 70, 70);
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(260, 100));
        p.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 20));

        JLabel img = new JLabel();
        img.setIcon(logo);

        JPanel p2 = new JPanel();
        p2.setOpaque(false);
        p2.setPreferredSize(new Dimension(150, 60));
        p2.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));

        JLabel t = new JLabel("DevStack_");
        Font font = semiBold.deriveFont(16f);
        t.setFont(font);
        t.setForeground(Color.WHITE);
        t.setBorder(new EmptyBorder(8, 0, 0, 60));

        JLabel version = new JLabel("2020.1.0");
        Font font2 = regular.deriveFont(12f);
        version.setFont(font2);
        version.setForeground(new Color(80, 80, 90));

        p.add(img);
        p2.add(t);
        p2.add(version);
        p.add(p2);
        return p;
    }
}
