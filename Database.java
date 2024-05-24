import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    public static ArrayList<Project> storage;
    Connection conn = null;
    Statement stmt = null;

    public static String TABLE_NAME = "projects";
    public static String ID_COL = "id";
    public static String NAME_COL = "name";
    public static String PATH_COL = "path";
    int idCounter = 0;

    public Database() {
        storage = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:PMStorage.db");
            stmt = conn.createStatement();
            System.out.println("Database Open!");

            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID_COL + " INT PRIMARY KEY NOT NULL, " +
                            NAME_COL + " TEXT NOT NULL, " +
                            PATH_COL + " TEXT NOT NULL)";
            stmt.executeUpdate(query);
            readAllData();

            //stmt.executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE id > 0");

            stmt.close();
        }catch(Exception e) {
            //System.out.println("Something is wrong...");
            e.printStackTrace();
        }
    }

    public void insertData(String name, String path) throws SQLException {
        Statement s = conn.createStatement();
        int n = getHighestID() + 1;
        String query = "INSERT INTO " + TABLE_NAME + "(" + ID_COL + ", " + NAME_COL + ", " + PATH_COL + ") " + 
                        "VALUES (" + n + ", '" + name + "', '" + path + "')";
        s.executeUpdate(query);
        readAllData();
    }

    public void removeData(int id) throws SQLException {
        Statement s = conn.createStatement();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL + " = " + id;
        s.executeUpdate(query);
        readAllData();
    }

    private void readAllData() throws SQLException {
        Statement s = conn.createStatement();
        storage.clear();
        ResultSet rs = s.executeQuery("SELECT * FROM " + TABLE_NAME);
        while(rs.next()) {
            int id = rs.getInt(ID_COL);
            String name = rs.getString(NAME_COL);
            String path = rs.getString(PATH_COL);

            Project p = new Project(id, name, path);
            storage.add(p);
        }
    }

    private int getHighestID() {
        if(!storage.isEmpty()) {
            int winner = storage.getFirst().id;
            for(Project n : storage) {
                if(n.id > winner) winner = n.id;
            }
            System.out.println(winner);
            return winner;
        }

        return -1;
    }
}
