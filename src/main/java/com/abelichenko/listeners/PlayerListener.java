package com.abelichenko.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.abelichenko.TestCPlugin;
import com.abelichenko.managers.PlayerManager;
import com.abelichenko.managers.RankManager;
import com.abelichenko.models.Rank;

public class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private final RankManager rankManager;

    public PlayerListener(PlayerManager playerManager, RankManager rankManager) {
        this.playerManager = playerManager;
        this.rankManager = rankManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();

        if (!playerManager.userExists(playerName)) {
            playerManager.addUser(playerName);
        }

        Rank playerRank = playerManager.getUserRank(playerName);
        String rankName = playerRank != null ? playerRank.getName() : "Неизвестный";

        TestCPlugin.getInstance().getLogger().info(
                "Игрок " + playerName + " зашёл на сервер с рангом " + rankName
        );
    }
}