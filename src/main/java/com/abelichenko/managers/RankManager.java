package com.abelichenko.managers;

import java.util.HashMap;
import java.util.Map;

import com.abelichenko.database.DatabaseManager;
import com.abelichenko.models.Rank;

public class RankManager {

    private final DatabaseManager databaseManager;
    private final Map<Integer, Rank> ranks;

    public RankManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.ranks = new HashMap<>();
    }

    public void loadRanks() {
        ranks.clear();
        ranks.putAll(databaseManager.loadRanks());
    }

    public Rank getRank(int id) {
        return ranks.get(id);
    }

    public Map<Integer, Rank> getRanks() {
        return ranks;
    }

    public void printRankInfo(int id) {
        Rank rank = ranks.get(id);
        if (rank != null) {
            System.out.println("Ранг ID: " + rank.getId() + ", Название: " + rank.getName() + ", Звание: " + rank.getPerms());
        } else {
            System.out.println("Ранг с ID " + id + " не найден");
        }
    }
}