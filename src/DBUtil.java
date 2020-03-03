import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String NAME = "root";
    private static final String PASSWORD = "";
    public static final String DATABASE = "mine";
    private static final String DBIP = "localhost";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            //初始化驱动类com.mysql.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + DBIP +":3306/?serverTimezone=UTC", NAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
