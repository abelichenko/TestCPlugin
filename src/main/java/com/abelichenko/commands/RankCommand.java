package com.abelichenko.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.abelichenko.managers.PlayerManager;
import com.abelichenko.managers.RankManager;
import com.abelichenko.models.Rank;

public class RankCommand implements CommandExecutor {

    private final PlayerManager playerManager;
    private final RankManager rankManager;

    public RankCommand(PlayerManager playerManager, RankManager rankManager) {
        this.playerManager = playerManager;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String targetPlayer = args[0];

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/rank <никнейм>");
            return true;
        }

        if (!playerManager.userExists(targetPlayer)) {
            sender.sendMessage(ChatColor.RED + "БРУХ такого игрока нит.");
            return true;
        }

        Rank playerRank = playerManager.getUserRank(targetPlayer);

        if (playerRank != null) {
            sender.sendMessage("Ранг игрока " + ChatColor.YELLOW + targetPlayer + ": " +
                    ChatColor.WHITE + playerRank.getName() + " (ID: " +
                    playerRank.getId() + ", Звание: " + playerRank.getPerms() + ")");
        } else {
            sender.sendMessage(ChatColor.RED + "ОШИБКО");
        }

        return true;
    }
}