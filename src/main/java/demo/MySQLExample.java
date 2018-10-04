package demo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import utils.MySQLUtil;

import java.sql.SQLException;

/**
 * @Class MySQLExample
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/10/03 10:41
 */
public class MySQLExample {

    public static void testMySQL() {
        Connection connection = null;
        String sql = "update user set user_age = user_age + 1 where user_id = 2";

        try {
            connection = MySQLUtil.getConnection();
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            for (int i = 0; i < 100; i++) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeConnection(connection);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(MySQLExample::testMySQL).start();
        }
    }
}
