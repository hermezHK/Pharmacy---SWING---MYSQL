/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author akira
 */
public class ConnectionMySQL {

    private String database_name = "pharmacy_database";
    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://locahost:3306/" + database_name;
    Connection conn = null;

    public Connection getConnection() {
        try {
            // get value from driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // get the connection
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.err.println("has occurred ClassNotFoundException " + e.getMessage());
        }catch(SQLException e) {
            System.err.println("has occurred SQLException " + e.getMessage());
        }
        return conn;
    }
}
