package dev.sitek.levelsystem.listener;

import dev.sitek.levelsystem.manager.LevelManager;
import dev.sitek.levelsystem.database.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnect implements Listener {

    private final LevelManager levelManager;
    private final DatabaseManager databaseManager;

    public PlayerConnect(LevelManager levelManager, DatabaseManager databaseLevel) {
        this.levelManager = levelManager;
        this.databaseManager = databaseLevel;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();

        databaseManager.createUser(uuid, e.getPlayer().getName());

        levelManager.setupPlayer(uuid);
        levelManager.getLevel(uuid).setupProgress();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        databaseManager.savePlayerData(uuid);

        if (!levelManager.hasBonus(uuid)) {
            levelManager.removePlayer(uuid);
        }
    }

}
