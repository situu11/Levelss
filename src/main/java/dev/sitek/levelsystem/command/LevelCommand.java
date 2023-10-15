package dev.sitek.levelsystem.command;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.manager.LevelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class LevelCommand implements CommandExecutor {

    private final LevelSystem plugin;
    private final LevelManager levelManager;

    public LevelCommand(LevelSystem plugin, LevelManager levelManager) {
        this.plugin = plugin;
        this.levelManager = levelManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        final Player player = (Player) sender;

        if (args.length != 1) {
            levelManager.showInfo(player, player.getUniqueId());
            return true;
        }

        final String playerName = args[0];
        UUID uuid = plugin.getDatabaseManager().getPlayerUUID(playerName);

        if (uuid == null) {
            player.sendMessage(colorize(String.format("&8[&c✖&8] &cGracz &7%s &cnie jest zarejestrowany w bazie danych!", playerName)));
            return true;
        }

        levelManager.showInfo(player, uuid);
        return true;
    }

}
