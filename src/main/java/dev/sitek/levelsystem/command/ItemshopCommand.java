package dev.sitek.levelsystem.command;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.ui.ItemshopMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemshopCommand implements CommandExecutor {

    private final LevelSystem plugin;

    public ItemshopCommand(LevelSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        new ItemshopMenu(plugin, player);

        return false;
    }
}
