package dev.sitek.levelsystem.command;

import dev.sitek.levelsystem.manager.LevelManager;
import dev.sitek.levelsystem.database.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class TopCommand implements CommandExecutor {

    private final LevelManager levelManager;
    private final DatabaseManager databaseManager;

    public TopCommand(LevelManager levelManager, DatabaseManager databaseManager) {
        this.levelManager = levelManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage("");
        player.sendMessage(colorize("       &8[&6Top 10: &eLevel&8]       "));
        player.sendMessage("");

        List<String> topPlayers = levelManager.getTopPlayers();

        if (topPlayers.isEmpty()) {
            player.sendMessage(colorize("&7Brak dostępnych graczy w rankingu."));
        } else {
            for (int i = 0; i < topPlayers.size(); i++) {
                String playerName = topPlayers.get(i);
                int playerLevel = databaseManager.getLevel(playerName);

                player.sendMessage(colorize(String.format("   &8• &7%d. &f%s &6Lvl.&e %d", i + 1, topPlayers.get(i), playerLevel)));
            }
        }
        player.sendMessage("");

        return false;
    }

}
