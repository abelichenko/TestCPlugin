package com.abelichenko;
import org.bukkit.plugin.java.JavaPlugin;
import com.abelichenko.database.DatabaseManager;
import com.abelichenko.managers.PlayerManager;
import com.abelichenko.managers.RankManager;
import com.abelichenko.commands.RankCommand;
import com.abelichenko.listeners.PlayerListener;

public class TestCPlugin extends JavaPlugin {

    private static TestCPlugin instance;
    private DatabaseManager databaseManager;
    private PlayerManager playerManager;
    private RankManager rankManager;

    @Override
    public void onEnable() {
        instance = this;
        initializeManagers();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.saveAllData();
            databaseManager.closeConnection();
        }
    }

    private void initializeManagers() {
        databaseManager = new DatabaseManager(this);
        databaseManager.initialize();
        rankManager = new RankManager(databaseManager);
        rankManager.loadRanks();
        playerManager = new PlayerManager(databaseManager, rankManager);
        playerManager.loadPlayers();
    }

    private void registerCommands() {
        getCommand("rank").setExecutor(new RankCommand(playerManager, rankManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager, rankManager), this);
    }

    public static TestCPlugin getInstance() {
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }
}