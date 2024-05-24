package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import SwingUIs.ConfirmationAction;
import SwingUIs.ConfirmationDialog;
import SwingUIs.FrameBP;
import SwingUIs.Vector2;

public class Project {
    public int id;
    public String name;
    public String path;

    public Project(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public JPanel show(int x, int y, int num) {

        JPanel main = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Dimension arcs = new Dimension(15,15);
                int width = x;
                int height = y;
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                //Draws the rounded opaque panel with borders.
                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint background
                graphics.setColor(getBackground());
                graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);//paint border
            }
        };
        main.setMaximumSize(new Dimension(x, y));
        main.setPreferredSize(new Dimension(x, y));
        main.setLayout(new BorderLayout());
        main.setOpaque(false);
        main.setBackground(new Color(30, 30, 45));

        main.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                main.setBackground(new Color(40, 40, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                main.setBackground(new Color(30, 30, 45));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                ProjectPage.openProject_VScode(path);
            }
        });

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 4));
        p.setOpaque(false);

        JLabel numLabel = new JLabel(String.valueOf(num));
        Font numFont = Controller.semiBold.deriveFont(25f);
        numLabel.setFont(numFont);
        numLabel.setPreferredSize(new Dimension(50, y));
        numLabel.setHorizontalAlignment(JLabel.CENTER);
        numLabel.setForeground(new Color(200, 200, 220));

        JLabel n = new JLabel(name);
        Font font = Controller.regular.deriveFont(18f);
        n.setPreferredSize(new Dimension(x-n.getWidth(), 20));
        n.setForeground(new Color(200, 200, 220));
        n.setFont(font);

        JLabel f = new JLabel(path);
        Font font2 = Controller.semiBold.deriveFont(14f);
        f.setPreferredSize(new Dimension(x-f.getWidth(), 15));
        f.setForeground(new Color(100, 100, 120));
        f.setFont(font2);

        JButton delete = new JButton();
        ImageIcon icn = FrameBP.scaledIcon(new ImageIcon("icons/delete.png"), 25, 25);
        delete.setIcon(icn);
        delete.setPreferredSize(new Dimension(50, y));
        delete.setBackground(new Color(35, 35, 50));
        delete.setMargin(new Insets(5, 10, 5, 10));
        delete.setFocusPainted(false);
        delete.setBorderPainted(false);
        delete.setToolTipText("Delete src.Project");
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                delete.setBackground(new Color(40, 40, 65));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                delete.setBackground(new Color(35, 35, 50));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                new ConfirmationDialog("Are you sure you want to delete " + name + "?", new Vector2(300, 150),
                new ConfirmationAction() {
                    @Override
                    public void callAction() {
                        deleteFolder(path);
                        try {
                            Launcher.database.removeData(id);
                            Launcher.controller.updateProjectPage();
                            
                        } catch (SQLException e1) {
                            System.out.println("Something went wrong again");
                        }
                    }
                    
                });
            }
        });

        main.add(numLabel, BorderLayout.WEST);
        main.add(delete, BorderLayout.EAST);
        p.add(Box.createRigidArea(new Dimension(x, 2)));
        p.add(n);
        p.add(f);
        main.add(p, BorderLayout.CENTER);
        return main;
    }

    private void deleteFolder(String p) {
        Path f = Paths.get(p);
        try {
            Files.walkFileTree(f, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
