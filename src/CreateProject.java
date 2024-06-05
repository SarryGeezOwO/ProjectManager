package src;

import SwingUIs.Vector2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import SwingUIs.FrameBP;

public class CreateProject extends FrameBP {

    JPanel mp;
    int pageSelected;
    JTextField nameField;
    JTextField pathField;

    String[] projectTypes = {"java", "html", "csharp"};
    int selectedType = 0;
    boolean readMeBool = false;
    boolean isUndecorated = false;

    public CreateProject() {
        super("New project", "< DevStack > New project", new Vector2(800, 600), FrameState.HIDE, false);
        requestFocus();
        setAlwaysOnTop(true);

        ProjectPage.create.setEnabled(false);
        ProjectPage.open.setEnabled(false);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Nothing
            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                toFront();
                requestFocus();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ProjectPage.create.setEnabled(true);
                ProjectPage.open.setEnabled(true);
            }
        });

        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 2, 2, 2, primaryCol),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        mp = new JPanel();
        mp.setLayout(new BorderLayout());
        mp.setOpaque(false);

        updateUI();

        contentPanel.add(createSidebar(), BorderLayout.WEST);
        contentPanel.add(mp, BorderLayout.CENTER);
        setVisible(true);
    }

    JRadioButton sidebarButtons(String txt, ImageIcon icn, int page) {
        JRadioButton b = new JRadioButton(txt);
        Font f = Controller.regular.deriveFont(14f);
        b.setIcon(icn);
        b.setIconTextGap(10);
        b.setFont(f);
        b.setFocusable(false);
        b.setBackground(contentPanel.getBackground());
        b.setPreferredSize(new Dimension(188, 50));
        b.setBorderPainted(false);
        b.setForeground(new Color(200, 200, 220));
        b.setHorizontalAlignment(JLabel.LEFT);
        b.setMargin(new Insets(0, 20, 0, 20));
        b.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                b.setBackground((b.isSelected()) ? new Color(40, 40, 70) : contentPanel.getBackground());
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
                if(!b.isSelected())b.setBackground(contentPanel.getBackground());
            }
        });
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change Page
                pageSelected = page;
                updateUI();
            }
        });
        return b;
    }

    private JPanel createSidebar() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        p.setPreferredSize(new Dimension(200, 69));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(40, 40, 50)));

        ButtonGroup group = new ButtonGroup();
        JRadioButton b1 = sidebarButtons("Empty project", null, 0);
        JRadioButton b2 = sidebarButtons("Swing project", null, 1);
        group.add(b1);
        group.add(b2);

        b1.setSelected(true);
        p.add(b1);
        p.add(b2);

        return p;
    }

    private void updateUI() {
        mp.removeAll();
        mp.add(getSelectedPage(), BorderLayout.CENTER);
        mp.add(createToolbar(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel createEmptyPage() { // ================= EMPTY =======================
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        p.setOpaque(false);

        Font f = Controller.regular.deriveFont(16f);
        JLabel nameLabel = new JLabel("Name:       ");
        nameLabel.setForeground(new Color(200, 200, 220));
        nameLabel.setFont(f);
        JLabel pathLabel = new JLabel("Location:   ");
        pathLabel.setForeground(new Color(200, 200, 220));
        pathLabel.setFont(f);

        nameField = createField(300, 30);
        pathField = createField(405, 300);
        pathField.setEditable(false);

        JButton browse = new JButton();
        browse.setIcon(FrameBP.scaledIcon(new ImageIcon("icons/folder.png"), 20, 20));
        browse.setPreferredSize(new Dimension(30,30));
        browse.setBackground(new Color(40, 40, 50));
        browse.setFocusPainted(false);
        browse.setBorderPainted(false);
        browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(SettingsPage.defaultPath != null) chooser.setSelectedFile(SettingsPage.defaultPath);

                JDialog d = new JDialog(null, "Select project", Dialog.ModalityType.APPLICATION_MODAL);
                d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                d.setAlwaysOnTop(true);

                chooser.addActionListener(e1 -> {
                    if(JFileChooser.APPROVE_SELECTION.equals(e1.getActionCommand())) {
                        File f = chooser.getSelectedFile();
                        pathField.setText(f.getAbsolutePath());
                    }
                    d.dispose();
                });

                d.getContentPane().add(chooser, BorderLayout.CENTER);
                d.setSize(600, 600);
                d.setLocationRelativeTo(null);
                d.setVisible(true);
            }
        });

        Font f2 = Controller.regular.deriveFont(12f);
        JTextArea notice = new JTextArea("> The location text field is not editable. Use the browse button to assign a directory");
        notice.setPreferredSize(new Dimension(600, 40));
        notice.setWrapStyleWord(true);
        notice.setLineWrap(true);
        notice.setForeground(new Color(150, 150, 170));
        notice.setFont(f2);
        notice.setEditable(false);
        notice.setCaretColor(new Color(0, 0, 0, 1));
        notice.setOpaque(false);

        JLabel typeLabel = new JLabel("Language:   ");
        typeLabel.setForeground(new Color(200, 200, 220));
        typeLabel.setFont(f);
        JPanel typeChooser = createChoosePanel("JAVA", "HTML", "CS");

        JCheckBox readMe = createCheckBox();
        readMe.addActionListener(e -> readMeBool = readMe.isSelected());
        JLabel readMeLabel = new JLabel("Add a README");
        readMeLabel.setFont(Controller.semiBold.deriveFont(14f));
        readMeLabel.setForeground(new Color(200, 200, 220));

        p.add(Box.createRigidArea(new Dimension(650, 20)));
        p.add(nameLabel);
        p.add(nameField);
        p.add(Box.createHorizontalStrut(120));
        p.add(pathLabel);
        p.add(pathField);
        p.add(browse);
        p.add(typeLabel);
        p.add(typeChooser);
        p.add(Box.createHorizontalStrut(120));
        p.add(readMe);
        p.add(readMeLabel);
        p.add(notice);

        if(SettingsPage.defaultPath != null) pathField.setText(SettingsPage.defaultPath.getAbsolutePath());
        return p;
    }

    private JPanel createSwingPage() { // ==================== SWING =====================
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        p.setOpaque(false);

        Font f = Controller.regular.deriveFont(16f);
        JLabel nameLabel = new JLabel("Name:       ");
        nameLabel.setForeground(new Color(200, 200, 220));
        nameLabel.setFont(f);
        JLabel pathLabel = new JLabel("Location:   ");
        pathLabel.setForeground(new Color(200, 200, 220));
        pathLabel.setFont(f);

        nameField = createField(300, 30);
        pathField = createField(405, 300);
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
                }
                d.dispose();
            });

            d.getContentPane().add(chooser, BorderLayout.CENTER);
            d.setSize(600, 600);
            d.setLocationRelativeTo(null);
            d.setVisible(true);
        });

        JLabel sizeX = new JLabel("Width:      ");
        sizeX.setFont(f);
        sizeX.setForeground(new Color(200, 200, 220));
        JTextField frameX = createNumberField(50, 4);
        frameX.setText("500");

        JLabel sizeY = new JLabel("Height:     ");
        sizeY.setFont(f);
        sizeY.setForeground(new Color(200, 200, 220));
        JTextField frameY = createNumberField(50, 4);
        frameY.setText("500");

        JCheckBox readMe = createCheckBox();
        readMe.addActionListener(e -> readMeBool = readMe.isSelected());
        JLabel readMeLabel = new JLabel("Add a README");
        readMeLabel.setFont(Controller.semiBold.deriveFont(14f));
        readMeLabel.setForeground(new Color(200, 200, 220));

        JCheckBox type = createCheckBox();
        type.addActionListener(e -> isUndecorated = type.isSelected());
        JLabel typeLabel = new JLabel("Undecorated frame");
        typeLabel.setFont(Controller.semiBold.deriveFont(14f));
        typeLabel.setForeground(new Color(200, 200, 220));
        
        Font f2 = Controller.regular.deriveFont(12f);
        JTextArea notice = new JTextArea("> The location text field is not editable. Use the browse button to assign a directory");
        notice.setPreferredSize(new Dimension(600, 40));
        notice.setWrapStyleWord(true);
        notice.setLineWrap(true);
        notice.setForeground(new Color(150, 150, 170));
        notice.setFont(f2);
        notice.setEditable(false);
        notice.setCaretColor(new Color(0, 0, 0, 1));
        notice.setOpaque(false);

        p.add(Box.createRigidArea(new Dimension(650, 20)));
        p.add(nameLabel);
        p.add(nameField);
        p.add(Box.createHorizontalStrut(120));
        p.add(pathLabel);
        p.add(pathField);
        p.add(browse);
        p.add(sizeX);
        p.add(frameX);
        p.add(Box.createHorizontalStrut(350));
        p.add(sizeY);
        p.add(frameY);
        p.add(Box.createHorizontalStrut(350));
        p.add(type);
        p.add(typeLabel);
        p.add(Box.createHorizontalStrut(350));
        p.add(readMe);
        p.add(readMeLabel);
        p.add(notice);

        if(SettingsPage.defaultPath != null) pathField.setText(SettingsPage.defaultPath.getAbsolutePath());
        return p;
    }

    private JCheckBox createCheckBox() {
        JCheckBox cb = new JCheckBox();
        cb.setMargin(new Insets(0, -5, 0, 0));
        cb.setOpaque(false);
        cb.setIcon(FrameBP.scaledIcon(new ImageIcon("icons/unchecked.png"), 30, 30));
        cb.setSelectedIcon(FrameBP.scaledIcon(new ImageIcon("icons/checked.png"), 30, 30));
        cb.setPreferredSize(new Dimension(30, 30));
        return cb;
    }

    private JPanel getSelectedPage() {
        return switch(pageSelected) {
            case 0 -> createEmptyPage();
            case 1 -> createSwingPage();
            default -> null;
        };
    }

    private void createFolder(String path, int type) {
        File folder = new File(path);
        if(type == 0) {
            // Empty
            File rootSource = new File("materials/empty/" + projectTypes[selectedType]);
            for(File f : rootSource.listFiles()) {
                Path source = Paths.get(f.getAbsolutePath());
                Path targetDirectory = Paths.get(folder.getAbsolutePath());
                folder.mkdir();
                if(folder.exists()) {
                    try {
                        Path target = targetDirectory.resolve(source.getFileName());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println("Failed to create folder");
                    }
                }
                ProjectPage.openProject_VScode(folder.getAbsolutePath());
            }
        }else {
            // Swing
            File rootSource = new File("materials/swingUndecorated");
            Path source = Paths.get(rootSource.getAbsolutePath());
            Path targetDirectory = Paths.get(folder.getAbsolutePath());
            try {
                copyDirectory(source, targetDirectory);
            } catch (IOException ignore) {}
            ProjectPage.openProject_VScode(folder.getAbsolutePath());
        }

        if(readMeBool) {
            File readMeSource = new File("materials/README.txt");
            Path src = Paths.get(readMeSource.getAbsolutePath());
            Path targetDirectory = Paths.get(folder.getAbsolutePath());
            try {
                Path target = targetDirectory.resolve(src.getFileName());
                Files.copy(src, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("Failed to create folder");
            }
        }
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setOpaque(false);
        toolbar.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 17));
        toolbar.setPreferredSize(new Dimension(69, 65));
        toolbar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(40,40,50)));

        JButton confirm = createButton("Finish");
        confirm.addActionListener(e -> {
            try {
                if(!nameField.getText().isEmpty() && !pathField.getText().isEmpty()) {
                    Launcher.database.insertData(nameField.getText(), pathField.getText() + "\\"+ nameField.getText());
                    Launcher.controller.updateProjectPage();
                    createFolder(pathField.getText() + "\\"+ nameField.getText(), pageSelected);
                    dispose();
                }
            } catch (SQLException e1) {
                //System.out.println("Insert went wrong");
                e1.printStackTrace();
            }
        });

        JButton cancel = createButton("Cancel");
        cancel.addActionListener(e -> dispose());

        toolbar.add(confirm);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(cancel);
        return toolbar;
    }

    private JButton createButton(String str) {
        JButton b = new JButton(str);
        b.setFocusPainted(false);
        b.setBackground(new Color(40, 40, 50));
        b.setForeground(new Color(200, 200, 220));
        b.setBorder(new EmptyBorder(10, 25, 10, 25));
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

    private JPanel createChoosePanel(String... str) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout(FlowLayout.TRAILING, 0,0));

        ButtonGroup group = new ButtonGroup();
        int counter = 0;
        for(String s : str) {
            JRadioButton b = createTypeChooserButton(s, counter);
            if(counter == 0) b.setSelected(true);
            p.add(b);
            group.add(b);
            counter++;
        }
        return p;
    }

    private JRadioButton createTypeChooserButton(String txt, int page) {
        JRadioButton b = new JRadioButton(txt);
        Font f = Controller.regular.deriveFont(14f);
        Icon transparentIcon = new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        b.setIcon(transparentIcon);
        b.setFont(f);
        b.setFocusable(false);
        b.setBackground(new Color(15, 15, 20));
        b.setPreferredSize(new Dimension(100, 30));
        b.setBorderPainted(false);
        b.setForeground(new Color(200, 200, 220));
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setMargin(new Insets(0, 20, 0, 20));
        b.addChangeListener(e -> b.setBackground((b.isSelected()) ? new Color(40, 40, 70) : new Color(15, 15, 20)));
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
        b.addActionListener(e -> selectedType = page);
        return b;
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

    private JTextField createNumberField(int length, int maxDigits) {
        JTextField f = createField(length, maxDigits);
        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (f.getText().length() >= maxDigits)
                    e.consume();
                if (Character.isLetter(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        return f;
    }

    public static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                if(!Files.exists(targetDir)) {
                    Files.createDirectory(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
