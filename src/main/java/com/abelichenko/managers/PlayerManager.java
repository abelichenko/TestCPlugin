package com.abelichenko.managers;

import java.util.HashMap;
import java.util.Map;

import com.abelichenko.database.DatabaseManager;
import com.abelichenko.models.User;
import com.abelichenko.models.Rank;

public class PlayerManager {

    private final DatabaseManager databaseManager;
    private final RankManager rankManager;
    private final Map<String, User> users;

    public PlayerManager(DatabaseManager databaseManager, RankManager rankManager) {
        this.databaseManager = databaseManager;
        this.rankManager = rankManager;
        this.users = new HashMap<>();
    }

    public void loadPlayers() {
        users.clear();
        users.putAll(databaseManager.loadUsers());
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void addUser(String username) {
        if (!users.containsKey(username)) {
            User newUser = new User(username, 0); // Стандартный ранг 0
            users.put(username, newUser);
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public Rank getUserRank(String username) {
        User user = users.get(username);
        if (user != null) {
            return rankManager.getRank(user.getIdRank());
        }
        return null;
    }

    public Map<String, User> getUsers() {
        return users;
    }

}