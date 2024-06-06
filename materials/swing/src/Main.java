import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {

    public static String appName = "Sample Title";

    public Main() {

        try {
            SettingsConfig.getSettingsData();
        } catch (IOException ignored) {}

        setTitle(appName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(SettingsConfig.xSize, SettingsConfig.ySize);
        setLocationRelativeTo(null);


        JLabel label = new JLabel("Hello World!", JLabel.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.ITALIC, 30));
        

        add(label, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}