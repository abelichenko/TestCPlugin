package com.abelichenko.database;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.abelichenko.TestCPlugin;
import com.abelichenko.models.User;
import com.abelichenko.models.Rank;

public class DatabaseManager {

    private final TestCPlugin plugin;
    private Connection connection;
    private final String dbPath;

    public DatabaseManager(TestCPlugin plugin) {
        this.plugin = plugin;
        this.dbPath = plugin.getDataFolder().getAbsolutePath() + File.separator + "data.db";
    }

    public void initialize() {
        createDataFolder();
        createConnection();
        createTables();
        createDefaultRank();
    }

    private void createDataFolder() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
    }

    private void createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (ClassNotFoundException | SQLException e) {
        }
    }

    private void createTables() {
        createUsersTable();
        createRanksTable();
    }

    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "id_rank INTEGER DEFAULT 0" +
                ");";

        executeUpdate(sql, "Таблица users создана");
    }

    private void createRanksTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ranks (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "perms INTEGER DEFAULT 0" +
                ");";

        executeUpdate(sql, "Таблица ranks создана");
    }

    private void createDefaultRank() {
        String checkSql = "SELECT COUNT(*) FROM ranks WHERE id = 0";
        String insertSql = "INSERT OR IGNORE INTO ranks (id, name, perms) VALUES (0, 'Рядовой', 0)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                executeUpdate(insertSql, "Создан стандартный ранг 'Рядовой'");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка при создании стандартного ранга", e);
        }
    }

    public Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("id_rank")
                );
                users.put(user.getUsername(), user);
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка", e);
        }

        return users;
    }

    public Map<Integer, Rank> loadRanks() {
        Map<Integer, Rank> ranks = new HashMap<>();
        String sql = "SELECT * FROM ranks";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rank rank = new Rank(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("perms")
                );
                ranks.put(rank.getId(), rank);
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка", e);
        }

        return ranks;
    }

    public void saveAllData() {
        saveUsers();
        saveRanks();
    }

    private void saveUsers() {
        Map<String, User> users = plugin.getPlayerManager().getUsers();

        executeUpdate("DELETE FROM users", null);

        String sql = "INSERT INTO users (username, id_rank) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (User user : users.values()) {
                stmt.setString(1, user.getUsername());
                stmt.setInt(2, user.getIdRank());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            plugin.getLogger().warning("Чё-то с БД БРУХ");
        }
    }

    private void saveRanks() {
        Map<Integer, Rank> ranks = plugin.getRankManager().getRanks();

        executeUpdate("DELETE FROM ranks", null);

        String sql = "INSERT INTO ranks (id, name, perms) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Rank rank : ranks.values()) {
                stmt.setInt(1, rank.getId());
                stmt.setString(2, rank.getName());
                stmt.setInt(3, rank.getPerms());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (SQLException e) {
            plugin.getLogger().warning("Чё-то с БД БРУХ");
        }
    }

    private void executeUpdate(String sql, String successMessage) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            if (successMessage != null) {
                plugin.getLogger().info(successMessage);
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Чё-то с БД БРУХ");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Чё-то с БД БРУХ");
        }
    }
}