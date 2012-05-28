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


    // ALTER TABLE `data_a` ADD INDEX `Index 1` (`id`), ADD INDEX `Index 2` (`type`), ADD INDEX `Index 3` (`tag_id`), ADD INDEX `Index 4` (`tag_name`);
    public static void createIndex(String name) {
        try {
            if (connection == null)
                openConnection();
            String query = "ALTER TABLE  " + name + " ADD INDEX `Index 1` (`id`), ADD INDEX `Index 2` (`type`), ADD INDEX `Index 3` (`tag_id`), ADD INDEX `Index 4` (`tag_name`);";
            Statement stmt = connection.createStatement();
            stmt.execute(query);
            stmt.close();
            debug("index created in:"+ name);
        } catch (SQLException e) {
            //e.printStackTrace();
            debug("not exist " + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Could not find the database driver
        }
    }

    public static void createTable(String name) {

        try {
            if (connection == null)
                openConnection();
            String query = "DROP TABLE " + name + ";";
            Statement stmt = connection.createStatement();
            stmt.execute(query);
            stmt.close();
            debug("droped "+ name);
        } catch (SQLException e) {
            //e.printStackTrace();
            debug("not exist " + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Could not find the database driver
        }

        try {
            if (connection == null)
                openConnection();
            String query = "CREATE TABLE " + name + " (\n" +
                    "\t`id` INT(10) NOT NULL,\n" +
                    "\t`type` VARCHAR(50) NOT NULL,\n" +
                    "\t`tag_id` INT(10) NOT NULL,\n" +
                    "\t`tag_name` VARCHAR(250) NOT NULL,\n" +
                    "\t`value` VARCHAR(5000) NOT NULL\n" +
                    ")\n" +
                    "COLLATE='utf8_general_ci'\n" +
                    "ENGINE=MyISAM;";
            Statement stmt = connection.createStatement();
            stmt.execute(query);
            stmt.close();
            debug("created " + name);
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

    public static void insertLine(List<Element> rows, String table) {
        //debug("insert size:" + rows.size());
        try {
            if (connection == null)
                openConnection();

            for (Element item : rows) {
                if (item.value.length() > 4998) debug("large value in:" + item.id + " " + item.tag_name);
                if  ( item.value.trim().equals("") ) continue;
                String query = "INSERT INTO " + table + " VALUES (?,?,?,?,?); ";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, item.id);
                ps.setString(2, item.type);
                ps.setInt(3, item.tag_id);
                ps.setString(4, item.tag_name);
                ps.setString(5, (item.value.length() > 4998) ? item.value.substring(0, 4998) : item.value);
                ps.executeUpdate();
                ps.close();
            }
            rows.clear();
            connection.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bigInsertLine(List<Element> rows, String table) {

        try {
            if (connection == null)
                openConnection();

            String query = "INSERT INTO " + table + " VALUES (";
            for (Element item : rows) {
                if  ( item.value.trim().equals("") ) continue;
                query += "?,?,?,?,?),(";
            }
            query = query.substring(0, query.length() - 2) + ";";
            int i = 1;
            PreparedStatement ps = connection.prepareStatement(query);
            for (Element item : rows) {
                if (item.value.length() > 4998) debug("large value in:" + item.id + " " + item.tag_name);
                if  ( item.value.trim().equals("") ) continue;
                ps.setInt(i, item.id);
                i++;
                ps.setString(i, item.type);
                i++;
                ps.setInt(i, item.tag_id);
                i++;
                ps.setString(i, item.tag_name);
                i++;
                ps.setString(i, (item.value.length() > 4998) ? item.value.substring(0, 4998) : item.value);
                i++;
            }
            ps.executeUpdate();
            ps.close();
            rows.clear();
            connection.commit();
        } catch (ClassNotFoundException e) {
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
        connection.setAutoCommit(false);
        System.out.println("is connect to DB" + connection);
    }

    static void debug(String msg) {
        System.out.println(msg);
    }
}
