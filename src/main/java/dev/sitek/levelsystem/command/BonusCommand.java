package dev.sitek.levelsystem.command;

import dev.sitek.levelsystem.instance.bonus.ServerBonus;
import dev.sitek.levelsystem.manager.LevelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BonusCommand implements CommandExecutor {

    private LevelManager levelManager;

    public BonusCommand(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) return true;
        if (args.length != 2) return false;

        Player player = (Player) sender;

        if (!player.hasPermission("try.core")) {
            player.sendMessage("&8[&c✖&8] &cKomenda nie istnieje lub nie masz do niej dostepu!");
            return false;
        }

        int amount = Integer.parseInt(args[0]);
        int minutes = Integer.parseInt(args[1]);

        new ServerBonus(levelManager, amount, minutes);
        return false;
    }

}
