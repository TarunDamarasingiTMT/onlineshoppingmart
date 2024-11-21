import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;


public class ConnectorFactory {


    public static String url = "jdbc:mysql://localhost:3306/onlineshopping";
    public static String user = "root";
    public static String password  ="root";

    public static DataSource getDataSource(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }




}