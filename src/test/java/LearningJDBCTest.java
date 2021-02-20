import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.*;

public class LearningJDBCTest {

    private Connection connection;

    @BeforeClass
    public void connection() throws SQLException {
        String url = "jdbc:mysql://test.medis.mersys.io:33306/classicmodels";
        String user = "technostudy";
        String password = "zhTPis0l9#$&";
        connection = DriverManager.getConnection(url, user, password);
    }

    @Test
    public void gettingCustomers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select customerName, phone, city from customers;");

        while(resultSet.next()) {
            String customerName = resultSet.getString("customerName");
            String phone = resultSet.getString("phone");
            String city = resultSet.getString("city");

            System.out.println(customerName + "\t\t\t\t" + phone + "\t\t\t\t" + city);
        }
    }

    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }

}
