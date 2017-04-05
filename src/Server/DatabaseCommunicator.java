package Server;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
class DatabaseCommunicator {

    //Connection constants
    //TODO
    private String HOST = "jdbc:mysql://host:3306/ppz?autoReconnect=true&useSSL=false";
    private String UNAME = "uname";
    private String UPASSWD = "upasswd";
    private Connection connection;
    //END

    /**
     * Class constructor
     */
    DatabaseCommunicator() {
        try {
            //Driver setup - JDBC MYSQL
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                System.out.println(e);
            }

            //Connection
            this.connection = DriverManager.getConnection(HOST, UNAME, UPASSWD);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }
}
