package SwingUIs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ConfirmationDialog extends FrameBP {
    public ConfirmationDialog(String msg, Vector2 size, ConfirmationAction action) {
        super("Confirmation", "Confirmation", size, FrameState.HIDE, false);
        disableMinimize();

        JLabel alert = new JLabel(msg);
        alert.setForeground(Color.white);
        alert.setIcon(scaledIcon(new ImageIcon("./icons/about.png"), 30, 30));
        alert.setIconTextGap(10);
        alert.setHorizontalTextPosition(JLabel.RIGHT);
        alert.setHorizontalAlignment(JLabel.CENTER);

        File fontFile = new File("./fonts/RedditMono-Regular.ttf");
        if(fontFile.exists()) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                alert.setFont(font.deriveFont(14f));
            } catch (FontFormatException | IOException ignored) {}
        }

        requestFocus();
        setAlwaysOnTop(true);

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

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton confirm = button("Yes");
        JButton cancel = button("No");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.callAction();
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        p.add(confirm);
        p.add(cancel);

        contentPanel.add(Box.createRigidArea(new Dimension(size.x, 30)));
        contentPanel.add(alert, BorderLayout.CENTER);
        contentPanel.add(p, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JButton button(String s) {
        JButton b = new JButton(s);
        b.setFocusable(false);
        b.setPreferredSize(new Dimension(80, 30));
        b.setBackground(new Color(40, 40, 50));
        b.setForeground(Color.WHITE);
        return b;
    }
}
