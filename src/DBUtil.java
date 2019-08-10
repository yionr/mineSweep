import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static final String NAME = "root";
    public static final String PASSWORD = "1999102lp";
    public static final String DATABASE = "mine";
    public static final String DBIP = "localhost";
    public static Connection getConnection(String DBIP,String NAME,String PASSWORD) {
        Connection conn = null;
        try {
            //初始化驱动类com.mysql.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + DBIP +":3306/?serverTimezone=UTC", NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
