import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * PPZ
 * Created by PD on 05.04.2017.
 */
class DatabaseCommunicator {

    private SettingsProvider settingsProvider;

    //Connection constants
    private String HOST;
    private String UNAME;
    private String UPASSWD;
    private Connection connection;

    /**
     * Class constructor
     */
    DatabaseCommunicator(SettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
        setParams();
        try {
            //Driver setup - JDBC MYSQL
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception e) {
                Debug.Log("Exception 1 in DatabaseCommunicator/DatabaseCommunicator:" + e);
            }

            //Connection
            this.connection = DriverManager.getConnection(HOST, UNAME, UPASSWD);
        } catch (Exception e) {
            Debug.Log("Exception 2 in DatabaseCommunicator/DatabaseCommunicator: " + e);
        }
    }

    private void setParams() {
        HOST = "jdbc:mysql://localhost:3306/"+settingsProvider.get("dbname")+"?autoReconnect=true&useSSL=false";
        UNAME = settingsProvider.get("username");
        UPASSWD = settingsProvider.get("password");
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
            Debug.Log("Exception in DatabaseCommunicator/executeQuery: " + e);
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
        } catch (Exception e) {
            Debug.Log("Exception in DatabaseCommunicator/executeUpdate: " + e);
        }
    }
}
