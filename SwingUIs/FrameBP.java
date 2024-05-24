package SwingUIs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.geom.RoundRectangle2D;

public class FrameBP extends JFrame {

    public enum FrameState {
        CLOSE, HIDE
    }

    public JPanel header, contentPanel;
    public static Color primaryCol = new Color(15, 15, 20);
    public static Color accentCol = new Color(25, 25, 35);

    private JButton minBtn;
    private JLabel titleLabel;
    int xMouse = 0;
    int yMouse = 0;
    boolean isFullScreen = false;
    boolean resizable;

    public static ImageIcon scaledIcon(ImageIcon icon, int width, int height) {
        Image root = icon.getImage();
        Image newImage = root.getScaledInstance(width, width, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
    
    public FrameBP(String title, String display, Vector2 size, FrameState closeOperation, boolean resizable) {
        
        this.resizable = resizable;
        setSize(size.x, size.y);
        setTitle(title);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // SetShape() is compatible with ComponentResizer()
        // Uncomment SetShape if you want rounded frame, but unable to resize
        //setShape(new RoundRectangle2D.Double(0, 0, size.x, size.y, 20, 20));

        ImageIcon closeIcon = scaledIcon(new ImageIcon("icons/close.png"), 15, 15);
        ImageIcon minIcon = scaledIcon(new ImageIcon("icons/minus.png"), 15, 15);
        ImageIcon frameIcon = scaledIcon(new ImageIcon("icons/PM_Logo.png"), 30, 30);
        ImageIcon layerIcon = scaledIcon(new ImageIcon("icons/layer.png"), 10, 10);
        ImageIcon fullScreenIcon = scaledIcon(new ImageIcon("icons/max.png"), 10, 10);

        ImageIcon tmp = new ImageIcon("icons/PM.png");
        Image logo = tmp.getImage();
        setIconImage(logo);

        header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
        header.setBackground(primaryCol);
        header.setPreferredSize(new Dimension(404, 23));
        header.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - xMouse, y - yMouse);
            };
        });
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                xMouse = e.getX();
                yMouse = e.getY();
            }
        });

    

        JButton closeBtn = createButton(closeIcon, new Color(220, 55, 75));
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(closeOperation.equals(FrameState.CLOSE)) System.exit(0);
                else dispose();
            }
        });
        minBtn = createButton(minIcon, new Color(80, 80, 95));
        minBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                setExtendedState(JFrame.ICONIFIED);
            }
        });
        JButton toggleFullScreen = createButton(fullScreenIcon, new Color(80, 80, 95));
        toggleFullScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isFullScreen = !isFullScreen;
                setExtendedState((isFullScreen) ? JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
                toggleFullScreen.setIcon((isFullScreen) ? layerIcon : fullScreenIcon);
            }
        });

        titleLabel = new JLabel(display); // Change this to something or getTitle()
        titleLabel.setIcon(frameIcon);
        titleLabel.setHorizontalTextPosition(JLabel.RIGHT);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        titleLabel.setForeground(new Color(215, 215, 215));
        int n = (resizable) ? 3 : 2;
        titleLabel.setPreferredSize(new Dimension((getWidth()) - (35*n), 23));

        header.add(titleLabel);
        header.add(minBtn);
        if (resizable)header.add(toggleFullScreen);
        header.add(closeBtn);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(accentCol);
        contentPanel.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, primaryCol));

        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);   

        if(resizable) {
            ComponentResizer cr = new ComponentResizer();  
            cr.registerComponent(this);
            cr.setSnapSize(new Dimension(10, 10));
            cr.setMinimumSize(new Dimension(size.x, size.y));
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                header.remove(titleLabel);
                header.remove(minBtn);
                if (resizable) header.remove(toggleFullScreen);
                header.remove(closeBtn);
                
                int n = (resizable) ? 3 : 2;
                titleLabel.setPreferredSize(new Dimension((getWidth()-6) - (35*n), 23));
                header.add(titleLabel);
                header.add(minBtn);
                if (resizable) header.add(toggleFullScreen);
                header.add(closeBtn);
                revalidate();
            }
        });
    }

    private JButton createButton(ImageIcon icon, Color selectedColor) {
        JButton btn = new JButton();
        btn.setIcon(icon);
        btn.setPreferredSize(new Dimension(35, 23));
        btn.setFocusable(false);
        btn.setBackground(header.getBackground());
        btn.setHorizontalAlignment(JLabel.CENTER);
        btn.setBorder(null);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                btn.setBackground(selectedColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                btn.setBackground(header.getBackground());
            }
        });

        return btn;
    }

    public void disableMinimize() {
        header.remove(minBtn);
        int n = (resizable) ? 2 : 1;
        titleLabel.setPreferredSize(new Dimension((getWidth()) - (35*n), 23));
    }
}
