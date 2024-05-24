package SwingUIs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
