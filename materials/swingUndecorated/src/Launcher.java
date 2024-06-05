import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
public class Launcher extends FrameBP {

    public Launcher() {
        super("Custom Swing", "New application", new Vector2(600, 600), FrameState.CLOSE, true);

        JLabel label = new JLabel("Hello World!", JLabel.CENTER);
        label.setForeground(new Color(180, 180, 255));
        label.setFont(FrameBP.redditMono.deriveFont(25f));

        contentPanel.add(label, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Launcher::new);
    }
    
}
