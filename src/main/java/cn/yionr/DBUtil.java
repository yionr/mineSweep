package cn.yionr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String driverClassName;
    private static String url;
    private static String username;
    private static String password;
    private static Connection conn;
    {
        Properties properties = new Properties();
        try {
            System.out.println("正在读取配置文件...");
            properties.load(DBUtil.class.getResourceAsStream("/db.properties"));
            driverClassName = properties.getProperty("jdbc.driverClassName");
            url = properties.getProperty("jdbc.url");
            username = properties.getProperty("jdbc.username");
            password = properties.getProperty("jdbc.password");
            System.out.println("读取成功!");
        } catch (IOException e) {
            System.err.println("读取数据库配置文件失败");
            System.exit(0);
        }
    }

    public Connection getConnection() {
        if (conn == null){
            try {
                System.out.println("正在获取数据库链接...");
                //初始化驱动类com.mysql.jdbc.Driver
                Class.forName(driverClassName);
                conn = DriverManager.getConnection(url, username, password);
                System.out.println("链接数据库成功!");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("登录数据库失败");
                System.exit(0);
            }
        }
        return conn;
    }
}
