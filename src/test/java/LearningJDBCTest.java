import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.*;

public class LearningJDBCTest {

    private Connection connection;
    private String jdbc;
    private PreparedStatement insertUser;

    @BeforeClass
    public void connection() throws SQLException {
        String url = "jdbc:mysql://test.medis.mersys.io:33306/ts_dauke";
        String user = "technostudy";
        String password = "zhTPis0l9#$&";
        connection = DriverManager.getConnection(url, user, password);
        jdbc = "testString";
        insertUser = connection.prepareStatement("insert into users(username, fullName) values (?, ?)");
    }

    @Test
    public void gettingUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from users;");

        while(resultSet.next()) {
            String username = resultSet.getString("username");
            String fullName = resultSet.getString("fullName");
            boolean active = resultSet.getBoolean("active");
            Timestamp created = resultSet.getTimestamp("created");

            System.out.println(username + "\t\t\t\t" + fullName + "\t\t\t\t" + active + "\t\t\t\t" + created);
        }
    }

    @Test
    public void updateUsers() throws SQLException {
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate("update users set active = true where active = false");
        System.out.println(rowsAffected + " rowsAffected");
    }

    @Test
    public void insertUsers() throws SQLException {
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate("insert into users(username, fullName) values ('" + jdbc + "', 'Java DataBase Connectivity')");
        System.out.println(rowsAffected + " rowsAffected");
    }

    @Test
    public void deleteUsers() throws SQLException {
        Statement statement = connection.createStatement();
        int rowsAffected = statement.executeUpdate("delete from users where username = '" + jdbc + "'");
        System.out.println(rowsAffected + " rowsAffected");
    }

    @Test
    public void insertUsersWithPreparedStatement() throws SQLException {
        insertUser.setString(1, jdbc);
        insertUser.setString(2, "Java DataBase Connectivity");
        int rowsAffected = insertUser.executeUpdate();
        System.out.println(rowsAffected + " rowsAffected");
    }

    @Test
    public void insertUsersWithPreparedStatement2() throws SQLException {
        insertUser.setString(1, "Dake");
        insertUser.setString(2, "SDK");
        int rowsAffected = insertUser.executeUpdate();
        System.out.println(rowsAffected + " rowsAffected");
    }

    @Test
    public void updateUsersWithPreparedStatement() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("update users set active = ? where active = ?");
        statement.setBoolean(1, true);
        statement.setBoolean(2, false);
        int rowsAffected = statement.executeUpdate();
        System.out.println(rowsAffected + " rowsAffected");

        gettingUsers();
    }

    @Test
    public void gettingNullValue() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from users where id = ?");
        statement.setInt(1, 3);
        ResultSet resultSet = statement.executeQuery();
        resultSet.first();

        Boolean active = resultSet.getBoolean("active");
        if(resultSet.wasNull()) {
            active = null;
        }
        System.out.println(active);

    }


    @AfterClass
    public void closeConnection() throws SQLException {
        connection.close();
    }

}
