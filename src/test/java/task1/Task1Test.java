package task1;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class Task1Test {
    private Connection connection;

    @BeforeClass
    public void connection() throws SQLException {
        String url = "jdbc:mysql://test.medis.mersys.io:33306/company";
        String user = "technostudy";
        String password = "zhTPis0l9#$&";
        connection = DriverManager.getConnection(url, user, password);
    }

    @Test
    public void testTask1Test() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from countries;");

        while(resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String region = resultSet.getString(3);
            System.out.println(id + "\t" + name + "\t" + region);
        }
    }

    @Test
    public void testTask2Test() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from countries;");

        while(resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String region = resultSet.getString(3);
            if(name.equals("Canada")) {
                Assert.assertEquals(region, "2");
            }
        }
    }

    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
