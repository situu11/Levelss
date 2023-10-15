package dev.sitek.levelsystem;

import dev.sitek.levelsystem.command.BonusCommand;
import dev.sitek.levelsystem.command.ItemshopCommand;
import dev.sitek.levelsystem.command.LevelCommand;
import dev.sitek.levelsystem.command.TopCommand;
import dev.sitek.levelsystem.listener.PlayerConnect;
import dev.sitek.levelsystem.listener.PlayerInteract;
import dev.sitek.levelsystem.listener.PlayerLeveling;
import dev.sitek.levelsystem.listener.luckperms.PlayerAddedToGroup;
import dev.sitek.levelsystem.listener.luckperms.PlayerRemovedFromGroup;
import dev.sitek.levelsystem.manager.LevelManager;
import dev.sitek.levelsystem.manager.SavingManager;
import dev.sitek.levelsystem.database.Database;
import dev.sitek.levelsystem.database.DatabaseManager;
import dev.sitek.levelsystem.placeholder.Placeholder;
import dev.sitek.levelsystem.placeholder.PlaceholderImplementation;
import dev.sitek.levelsystem.scheduler.ITaskScheduler;
import dev.sitek.levelsystem.scheduler.TaskSchedulerImplementation;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public final class LevelSystem extends JavaPlugin {

    public static LevelSystem instance;
    public static Database database;

    private DatabaseManager databaseManager;
    private LevelManager levelManager;

    private ITaskScheduler taskScheduler;
    private Placeholder placeholder;

    @Override
    public void onEnable() {
        instance = this;

        databaseManager = new DatabaseManager(this, database.getLevelsCollection(), database.getItemshop());

        levelManager = new LevelManager(databaseManager);
        levelManager.setupTopPlayers();

        taskScheduler = new TaskSchedulerImplementation(this);
        taskScheduler.runTimerAsync(new SavingManager(databaseManager, levelManager), 12000L, 12000L);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholder = new Placeholder();

            Stream.of(
                    new PlaceholderImplementation(levelManager)
            ).forEach(this.placeholder::register);
        }

        Stream.of(
                new PlayerConnect(levelManager, databaseManager),
                new PlayerLeveling(levelManager),
                new PlayerInteract(levelManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        LuckPerms luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        new PlayerAddedToGroup(this, levelManager, luckPerms).register();
        new PlayerRemovedFromGroup(this, levelManager, luckPerms).register();

        getCommand("lvl").setExecutor(new LevelCommand(this, levelManager));
        getCommand("top").setExecutor(new TopCommand(levelManager, databaseManager));
        getCommand("itemshop").setExecutor(new ItemshopCommand(this));
        getCommand("bonus").setExecutor(new BonusCommand(levelManager));
    }

    @Override
    public void onDisable() {
        databaseManager.saveAllPlayersData();

        if (this.placeholder != null) {
            this.placeholder.unregisterAll();
        }

        database.close();
    }

    public static LevelSystem getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public ITaskScheduler getTaskScheduler() {
        return taskScheduler;
    }
}
