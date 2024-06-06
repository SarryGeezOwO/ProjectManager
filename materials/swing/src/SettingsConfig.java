import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SettingsConfig {

    public static int xSize;
    public static int ySize;

    public static void getSettingsData() throws IOException {
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
