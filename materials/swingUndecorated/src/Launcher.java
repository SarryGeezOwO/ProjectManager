import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Launcher extends FrameBP {

    static int xSize;
    static int ySize;

    public Launcher() {
        super("Custom Swing", "New application", new Vector2(xSize, ySize), FrameState.CLOSE, true);

        JLabel label = new JLabel("Hello World!", JLabel.CENTER);
        label.setForeground(new Color(180, 180, 255));
        label.setFont(FrameBP.redditMono.deriveFont(25f));

        contentPanel.add(label, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            getSettingsData();
        } catch (IOException ignored) {}
        SwingUtilities.invokeLater(Launcher::new);
    }

    private static void getSettingsData() throws IOException {
        File settings = new File("./Settings.txt");
        if(!settings.exists()) {
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(settings));

        xSize = Integer.parseInt(getSettingsValue(reader.readLine()));
        ySize = Integer.parseInt(getSettingsValue(reader.readLine()));
        System.out.println(xSize + "\n" + ySize);

        reader.close();
    }

    private static String getSettingsValue(String data) {
        char[] widthValueChar = data.toCharArray();

        StringBuilder finalValue = new StringBuilder();
        for(char c : widthValueChar) {
            if(Character.isDigit(c)) {
                finalValue.append(c);
            }
        }
        return finalValue.toString();
    }
}
