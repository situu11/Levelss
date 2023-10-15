package dev.sitek.levelsystem.instance.bonus;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.manager.LevelManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static dev.sitek.levelsystem.util.TextUtil.*;

public class Bonus implements Runnable {

    private final LevelManager levelManager;
    private final UUID uuid;

    public Bonus(UUID uuid, double bonus, long minutes) {
        LevelSystem plugin = LevelSystem.getInstance();

        this.levelManager = plugin.getLevelManager();
        this.uuid = uuid;

        Bukkit.getPlayer(uuid).sendTitle(colorize(String.format("&lPierscien Doswiadczenia &e+%s%%", bonus)),
                colorize(String.format("&7Aktywowano na czas: &3%d %s", minutes, minutes == 1 ? "minuty" : "minut")));

        levelManager.getLevel(uuid).setTemporaryBonusExp(bonus);
        plugin.getTaskScheduler().runLaterAsync(this, minutes * 60 * 20);
    }

    @Override
    public void run() {
        levelManager.getLevel(uuid).setTemporaryBonusExp(0);
        levelManager.removeBonusList(uuid);

        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            levelManager.removePlayer(uuid);
        } else {
            player.sendTitle(colorize("&lPierscien doswiadczenia"), colorize("&7Zakonczyl swoje dzialanie..."));
        }
    }

}
