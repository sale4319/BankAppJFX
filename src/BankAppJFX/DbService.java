/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankAppJFX;

import java.sql.*;
import java.sql.DriverManager;

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

    //CRUD
    //Create account
    int addCreditCard(String user, int cardNr, CardType cardType, double balance) {
        int id = -1;
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO accounts (userId, cardNr, cardType, balance) VALUES ('" + user + "','" + cardNr + "','" + cardType + "','" + balance + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;

    }

    //Read 
    public boolean checkLogin(String user, String pass) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String readSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            pstmt = connection.prepareStatement(readSQL);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public boolean chekcIfTaken(String user, String email) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String readSQL = "SELECT * FROM users WHERE username = ? OR email = ?";
        try {
            pstmt = connection.prepareStatement(readSQL);
            pstmt.setString(1, user);
            pstmt.setString(2, email);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public boolean checkCardNr(int cardNr) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String readSQL = "SELECT * FROM accounts WHERE cardNr = ?";
        try {
            pstmt = connection.prepareStatement(readSQL);
            pstmt.setInt(1, cardNr);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public void getDeposit(int id, double amount) throws Exception {//
        //Here we fetch deposit amount and add it to current balance and send to updateBalance and
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String readSQL = "SELECT balance FROM accounts WHERE id = ?";
        try {
            pstmt = connection.prepareStatement(readSQL);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                double newBalance = balance + amount;

                updateBalance(id, newBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    public void getWithdrawal(int id, double amount) throws Exception {//
        //Here we fetch withdrawal amount and add it to current balance and send to updateBalance and
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String readSQL = "SELECT balance FROM accounts WHERE id = ?";
        try {
            pstmt = connection.prepareStatement(readSQL);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                double newBalance = balance - amount;

                updateBalance(id, newBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pstmt.close();
            rs.close();
        }
    }

    //Update
    boolean updateBalance(int id, double balance) {
        //Update new balance to DB
        PreparedStatement pstmt = null;
        boolean success = false;
        try {
            String updateSQL = "UPDATE accounts SET balance = ? WHERE id = ?";
            pstmt = connection.prepareStatement(updateSQL);
            pstmt.setDouble(1, balance);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return success;
    }

    //Delete
    public void deleteCard(int id) {
        //Delte selected credit card
        PreparedStatement pstmt = null;
        String deleteSQL = "DELETE FROM accounts WHERE id = ?";
        try {
            pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
