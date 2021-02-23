package task1;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
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

    @Test
    public void testTask3Test() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from countries where COUNTRY_NAME = 'Canada'");
        resultSet.first(); // important
        String region = resultSet.getString("REGION_ID");
        Assert.assertEquals(region, "2");
    }

    @Test
    public void testTask4Test() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM countries join regions on countries.REGION_ID = regions.REGION_ID where COUNTRY_NAME = ?;");
        preparedStatement.setString(1, "Singapore");
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.first(); // important
        String region = resultSet.getString("REGION_NAME").trim();
        Assert.assertEquals(region, "Asia");
    }

    @DataProvider
    public Object[][] data1(){
        Object[][] data = {
                {1, 8}, // region 1 has 8 countries
                {2, 5},
                {3, 6},
                {4, 6}
        };
        return data;
    }

    @Test(dataProvider="data1")
    public void task5(int regionId, int numberOfCountries) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(*) from countries where region_id = ? group by region_id;");
        statement.setInt(1, regionId);
        ResultSet resultSet = statement.executeQuery();

//        resultSet.next();
        resultSet.first();

        int actualCountries = resultSet.getInt(1);
        Assert.assertEquals(actualCountries, numberOfCountries);
    }


    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
