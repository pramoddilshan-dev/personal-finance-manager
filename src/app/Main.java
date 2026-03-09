package app;

import database.DBConnection;
import java.sql.Connection;

public class Main {

    public static void main(String[] args) {

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("Database connected successfully!");
        } else {
            System.out.println("Database connection failed.");
        }

    }
}