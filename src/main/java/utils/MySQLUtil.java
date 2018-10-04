package utils;

import com.mysql.jdbc.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Class MySQLUtil
 * @Description: MySQL工具类，主要获取MySQL连接和关闭连接
 * @Author: luozhen
 * @Create: 2018/10/02 20:37
 */
public class MySQLUtil {

    private static String jdbcConfig;

    static {
        jdbcConfig = "jdbc.properties";
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        Connection connection = null;
        try {
            inputStream = MySQLUtil.class.getClassLoader().getResourceAsStream(jdbcConfig);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("load jdbc.properties file failed!");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");
        try {
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Can not find driver!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("close MySQL connection failed!");
        }
    }

}
