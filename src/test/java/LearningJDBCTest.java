import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.*;

public class LearningJDBCTest {

    private Connection connection;

    @BeforeClass
    public void connection() throws SQLException {
        String url = "jdbc:mysql://test.medis.mersys.io:33306/ts_dauke";
        String user = "technostudy";
        String password = "zhTPis0l9#$&";
        connection = DriverManager.getConnection(url, user, password);
    }

    @Test
    public void gettingCustomers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users;");

        while(resultSet.next()) {
            String username = resultSet.getString("username");
            boolean active = resultSet.getBoolean("active");
            Timestamp created = resultSet.getTimestamp("created");

            System.out.println(username + "\t\t\t\t" + active + "\t\t\t\t" + created);
        }
    }

    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }

}
