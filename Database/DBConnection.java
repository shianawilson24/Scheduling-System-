package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author shiana
 */
public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07p0t";

    private static final String jdbcUrl = protocol + vendorName + ipAddress + dbName;

    // driver interface reference
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    static Connection conn = null;
    // my username
    private static final String userName = "U07p0t";
    private static final String password = "53689094193";


    public static Connection startConnection() throws SQLException, ClassNotFoundException {
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection sucessful!");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void closeConnection(){
        try{
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        return conn;
    }
}