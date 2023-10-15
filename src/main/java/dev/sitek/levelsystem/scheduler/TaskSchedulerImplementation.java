package dev.sitek.levelsystem.scheduler;

import dev.sitek.levelsystem.LevelSystem;
import org.bukkit.Bukkit;

public class TaskSchedulerImplementation implements ITaskScheduler {

    private final LevelSystem plugin;

    public TaskSchedulerImplementation(LevelSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public void runLaterAsync(Runnable runnable, long delay) {
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, runnable, delay);
    }

    @Override
    public void runTimerAsync(Runnable runnable, long delay, long period) {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, runnable, delay, period);
    }

}
