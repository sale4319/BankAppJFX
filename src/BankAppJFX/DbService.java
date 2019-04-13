/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import java.sql.*;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aco PC
 */
public class DbService {

    Connection connection;

    public DbService() {
        connection = DbService.getConnection();
        if (connection == null) {
            System.exit(1);

        }
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConnection() {
        String connect_string = "jdbc:sqlite:bankDb.sqlite";

        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connect_string);

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean isLogin(String user, String pass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from users where username = ? and password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    public boolean isTaken(String user, String email) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from users where username = ? or email = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, email);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    public boolean checkCardNr(int cardNr) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from accounts where cardNr = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, cardNr);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    //CRUD
    //Create account
    int AddAccount(String user, int cardNr, CardType cardType, double balance) {
        int accountId = -1;
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("insert into accounts (userId, cardNr, cardType, balance) values ('" + user + "','" + cardNr + "','" + cardType + "','" + balance + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountId;

    }

    public void deleteAccount(int id) {
        String deleteSql = "delete from accounts where id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(deleteSql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
