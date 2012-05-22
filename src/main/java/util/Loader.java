package util;

import container.Element;

import java.sql.*;
import java.util.List;

/**
 * User: Andrey
 * Date: 21.05.12
 * Time: 17:27
 */
public class Loader {
    public static Connection connection;

    public static void createTable(List<String> rows,String name) {
        debug("items create:"+rows.size());
        try {
            if(connection == null)
                openConnection();
            ///String query = "Select 7 FROM dual";
//            CREATE TABLE `xml_1` (
//            `id_1` VARCHAR(255) NULL,
//            `id_2` VARCHAR(255) NULL
//            )
//            COLLATE='utf8_general_ci'
//            ENGINE=InnoDB;
            String query =  "CREATE TABLE xml_"+name+"( ";
            for (String item : rows) {
                query += item+" VARCHAR(100) NULL,";
            }
            query= query.subSequence(0,query.length()-1)+ " )\n" +
                    "            COLLATE='utf8_general_ci'\n" +
                    "            ENGINE=InnoDB;";
            debug(query);

            Statement stmt = connection.createStatement();

            stmt.execute(query);
            //closeConnection();
        } // end try
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Could not find the database driver
        } catch (SQLException e) {
            e.printStackTrace();
            // Could not connect to the database
        }
    }

    public static void insertLine(List<Element> rows) {

        try {
            if(connection == null)
                openConnection();

            for (Element item : rows) {
                String query =  "INSERT INTO data VALUES (?,?,?,?,?); ";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1,item.id);
                ps.setString(2,item.type);
                ps.setInt(3,item.tag_id);
                ps.setString(4,item.tag_name);
                ps.setString(5,item.value);
                ps.executeUpdate();
                ps.close();
            }
            rows.clear();
            //connection.commit();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }

    private static void openConnection() throws ClassNotFoundException, SQLException {
        // Название драйвера
        String driverName = "com.mysql.jdbc.Driver";

        Class.forName(driverName);

        // Create a connection to the database
        String url = "jdbc:mysql://hilton:3406/xml_data?useUnicode=yes&characterEncoding=UTF-8";
        String username = "root";
        String password = "123";

        connection = DriverManager.getConnection(url, username, password);
        //connection.setAutoCommit(false);
        System.out.println("is connect to DB" + connection);
    }

    static void debug(String msg){
        System.out.println(msg);
    }
}
