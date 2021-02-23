package task1;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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

    @DataProvider
    public Object[][] data2() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select region_id, count(*) from countries group by region_id;");

        resultSet.last(); // point to the last row
        int numberOfRows = resultSet.getRow();// this returns the number of the row
        Object[][] data = new Object[numberOfRows][2];  // set the number of rows in data provider

        resultSet.beforeFirst(); // reset position of my pointer

        int index = 0;
        while(resultSet.next()){
            data[index][0] = resultSet.getInt(1);
            data[index][1] = resultSet.getInt(2);
            index++;
        }
        return data;
    }

    @Test(dataProvider = "data2")
    public void task5v2(Integer actualRegionId, Integer actualNumberOfCountries) {
        Map<Integer, Integer> countryCountMap = new HashMap<>();
        countryCountMap.put(1, 8);
        countryCountMap.put(2, 5);
        countryCountMap.put(3, 6);
        countryCountMap.put(4, 6);

        Assert.assertEquals(actualNumberOfCountries, countryCountMap.get(actualRegionId));
        System.out.println(actualRegionId + " " + actualNumberOfCountries);
    }

    @Test
    public void task6v1() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, SALARY, MIN_SALARY, MAX_SALARY from employees join jobs on employees.JOB_ID = jobs.JOB_ID;");
        SoftAssert softAssert = new SoftAssert();
        while(resultSet.next()){
            BigDecimal salary = resultSet.getBigDecimal("SALARY");
            BigDecimal minSalary = resultSet.getBigDecimal("MIN_SALARY");
            BigDecimal maxSalary = resultSet.getBigDecimal("MAX_SALARY");
            softAssert.assertTrue(salary.compareTo(minSalary) > 0, getErrorMessage(resultSet, "lower", "min"));
            softAssert.assertTrue(salary.compareTo(maxSalary) < 0, getErrorMessage(resultSet, "larger", "max"));
        }

        softAssert.assertAll();
    }

    private String getErrorMessage(ResultSet resultSet, String s, String type) throws SQLException {
        return "Salary is " + s + " the "+type+" salary for employee ID: " + resultSet.getString("EMPLOYEE_ID") +" name: " + resultSet.getString("FIRST_NAME")+ " "+  resultSet.getString("LAST_NAME");
    }

    @DataProvider
    public Object[][] salaryData() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select EMPLOYEE_ID, FIRST_NAME, LAST_NAME, SALARY, MIN_SALARY, MAX_SALARY from employees join jobs on employees.JOB_ID = jobs.JOB_ID;");
        resultSet.last();
        int numberOfRows = resultSet.getRow();
        Object[][] salaryData = new Object[numberOfRows][5];

        resultSet.beforeFirst();

        int index = 0;
        while(resultSet.next()){
            BigDecimal salary = resultSet.getBigDecimal("SALARY");
            BigDecimal minSalary = resultSet.getBigDecimal("MIN_SALARY");
            BigDecimal maxSalary = resultSet.getBigDecimal("MAX_SALARY");
            String id = resultSet.getString("EMPLOYEE_ID");
            String name = resultSet.getString("FIRST_NAME") + " " + resultSet.getString("LAST_NAME");
            salaryData[index][0] = salary;
            salaryData[index][1] = minSalary;
            salaryData[index][2] = maxSalary;
            salaryData[index][3] = id;
            salaryData[index][4] = name;
            index++;
        }
        return salaryData;
    }

    @Test(dataProvider = "salaryData")
    public void task6v2(BigDecimal salary, BigDecimal minSalary, BigDecimal maxSalary, String id, String name){
        Assert.assertTrue(salary.compareTo(minSalary) > 0, "Employee id " + id + " has lower salary. Name: " + name);
        Assert.assertTrue(salary.compareTo(maxSalary) < 0, "Employee id " + id + " has larger salary. Name: " + name);
    }

    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }
}
