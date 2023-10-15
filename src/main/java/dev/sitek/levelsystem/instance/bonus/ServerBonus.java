package dev.sitek.levelsystem.instance.bonus;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.manager.LevelManager;
import org.bukkit.Bukkit;

import static dev.sitek.levelsystem.util.TextUtil.*;

public class ServerBonus implements Runnable {

    private final LevelManager levelManager;

    private final double amount;

    public ServerBonus(LevelManager levelManager, double amount, long minutes) {
        this.levelManager = levelManager;

        this.amount = amount;

        levelManager.setServerBonus(amount);

        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(
                colorize(String.format("&lSerwerowy Pierscien &e+%s%%", this.amount)),
                colorize(String.format("&7Aktywowano na czas: &3%d %s", minutes, minutes == 1 ? "minuty" : "minut")))
        );

        LevelSystem.getInstance().getTaskScheduler().runLaterAsync(this, minutes * 60 * 20);
    }

    @Override
    public void run() {
        levelManager.setServerBonus(0);

        Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(
                colorize("&lSerwerowy Pierscien Doswiadczenia"),
                colorize("&7Zakonczyl swoje dzialanie...")
        ));
    }
}
