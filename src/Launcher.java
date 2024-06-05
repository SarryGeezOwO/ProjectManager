package src;

import SwingUIs.*;
import javax.swing.SwingUtilities;

public class Launcher extends FrameBP{

    public static Database database;
    public static Controller controller;
    public Launcher() {
        super("DevStack", "< DevTool >", new Vector2(1000, 700), FrameState.CLOSE, true);

        database = new Database();
        controller = new Controller(contentPanel);

        SettingsPage.getSettingsData();
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Launcher::new);
    }
}