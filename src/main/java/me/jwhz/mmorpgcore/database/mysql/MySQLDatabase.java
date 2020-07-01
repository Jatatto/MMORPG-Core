package me.jwhz.mmorpgcore.database.mysql;

import me.jwhz.mmorpgcore.config.ConfigFile;
import me.jwhz.mmorpgcore.database.IDatabase;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class MySQLDatabase extends ConfigFile implements IDatabase {

    private Connection connection;

    public MySQLDatabase() {

        super("mysql");

    }

    @Override
    public Object retrieve(UUID id, String key) {

        return null;

    }

    @Override
    public void store(UUID id, String key, Object value) {



    }

    /*
     *  MySQL Connection
     */


    public void closeConnection(){

        try {
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection openConnection() {

        synchronized (this) {

            try {

                if (connection != null && !connection.isClosed())
                    return connection;

                Class.forName("com.mysql.jdbc.Driver");

                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + getYamlConfiguration().getString("host") + ":" + getYamlConfiguration().getString("port")
                                + "/" + getYamlConfiguration().getString("database"),
                        getYamlConfiguration().getString("username"),
                        getYamlConfiguration().getString("password")
                );

                return connection;

            } catch (Exception e) {

                Bukkit.getLogger().log(Level.SEVERE, "Incorrect database information given, please try again and reload the plugin.");
                Bukkit.getLogger().log(Level.SEVERE, "Disabling MMORPGCore!");
                Bukkit.getPluginManager().disablePlugin(core);

            }

        }

        return connection;

    }

    public Connection getConnection() {

        return connection;

    }


}
