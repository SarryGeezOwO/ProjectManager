package src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ProjectPage extends JPanel{

    public static JPanel recycler = new JPanel();
    public JScrollPane scrollPane;
    public static JButton create;
    public static JButton open;

    public ProjectPage() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 10, 0, 0));

        recycler.setLayout(new BoxLayout(recycler, BoxLayout.Y_AXIS));
        recycler.setOpaque(false);
        recycler.setBorder(new EmptyBorder(0, 0, 0, 8));
        
        scrollPane = new JScrollPane(recycler);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(new Color(25, 25, 35));
        scrollPane.setBorder(null);

        customizeScrollBarUI(scrollPane);

        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 50));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 0, 13),
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(40, 40, 50))
        ));
        header.setPreferredSize(new Dimension(69, 100));

        create = createButton("New project");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateProject();
            }
        });
        open = createButton("Open existing project");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showOpenDialog(null);
                if(chooser.getSelectedFile() != null) {
                    File path = chooser.getSelectedFile();
                    try {
                        Launcher.database.insertData(path.getName(), path.getAbsolutePath());
                        openProject_VScode(path.getAbsolutePath());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    Launcher.controller.updateProjectPage();
                }
            }
        });

        header.add(create);
        header.add(Box.createHorizontalStrut(10));
        header.add(open);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateUI();
            }
        });
    }

    public void updateUI() {
        showProjects();
        revalidate();
        repaint();
    }

    private JButton createButton(String str) {
        JButton b = new JButton(str);
        b.setFocusPainted(false);
        b.setBackground(new Color(40, 40, 50));
        b.setForeground(new Color(200, 200, 220));
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(100, 100, 120));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(40, 40, 50));
            }
        });

        Font f = Controller.regular.deriveFont(14f);
        b.setFont(f);
        return b;
    }

    public void showProjects() {
        recycler.removeAll();
        recycler.setLayout(new BoxLayout(recycler, BoxLayout.Y_AXIS));
        int counter = 0;
        if(!Database.storage.isEmpty()) { // If src.Database is not empty
            recycler.add(Box.createVerticalStrut(10));
            for(Project p : Database.storage) {
                int x = 0;
                if(scrollPane != null) x = (int)scrollPane.getViewport().getSize().getWidth() - 10;

                JPanel view = p.show(x, 60, counter);
                File folder = new File(p.path);
                if(!folder.exists()) {
                    p.disableUI(view, x);
                }

                recycler.add(view);
                recycler.add(Box.createVerticalStrut(10));
                counter++;
            }
        }else { // Display a single Label showing, that there is no projects created / found
            recycler.setLayout(new BorderLayout());
            JLabel feedback = new JLabel("No Projects...", JLabel.CENTER);
            feedback.setForeground(new Color(100, 100, 130));
            feedback.setFont(Controller.regular.deriveFont(20f));
            recycler.add(feedback, BorderLayout.CENTER);
        }
    }

    public static void openProject_VScode(String folderPath) {
        ProcessBuilder p = new ProcessBuilder();
        p.command("cmd.exe", "/c", "code", folderPath);
        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }











    private static void customizeScrollBarUI(JScrollPane scrollPane) {
        // Get the vertical scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();

        // Remove the default UI for the vertical scrollbar
        verticalScrollBar.setUI(new CustomScrollBarUI());

        // Set other properties of the scrollbar as needed
        verticalScrollBar.setPreferredSize(new Dimension(5, Integer.MAX_VALUE)); // Example width
        verticalScrollBar.setUnitIncrement(10); // Example increment
    }

    static class CustomScrollBarUI extends BasicScrollBarUI {

        // The size of the buttons
        private final Dimension zeroSize = new Dimension(0, 0);

        @Override
        protected void configureScrollBarColors() {
            // Do nothing, to retain default colors
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(zeroSize);
            return button;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = new JButton();
            button.setPreferredSize(zeroSize);
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            // Paint the track background
            g.setColor(new Color(25, 25, 35));
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            // Paint the thumb
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            g.setColor(new Color(15, 15, 20));
            g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 5, 5);
        }
    }
}
