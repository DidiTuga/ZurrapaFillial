import java.sql.Connection;
import java.sql.DriverManager;

public class Conectar {
    public static Connection getCon() {
        try {
            String url = "jdbc:sqlserver://DESKTOP-UJ26N4k\\SQLEXPRESS;databaseName=ZurrapaFilial;integratedSecurity=true";
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (Exception e) {
            return null;
        }
    }
    //ainda nao foi utilizada
        public static Connection getConSede() {
            try {
                String url = "jdbc:sqlserver://DESKTOP-UJ26N4k\\SQLEXPRESS;databaseName=ZurrapaSede;integratedSecurity=true";
                Connection connection = DriverManager.getConnection(url);
                return connection;
            } catch (Exception e) {
                return null;
            }
    }

}
