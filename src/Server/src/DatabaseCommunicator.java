import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
class DatabaseCommunicator {

    //Connection constants
    //TODO
    private String HOST = "jdbc:mysql://localhost:3306/ppz?autoReconnect=true&useSSL=false";
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

    /**
     * Executes given query
     *
     * @param query String query to be executed on the database
     * @return ResultSet of results in case of success, null otherwise
     */

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    /**
     * Executes given update
     *
     * @param query String query to be executed on the database
     */
    public void executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            System.out.println(statement.executeUpdate(query));
            return;
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }
}
