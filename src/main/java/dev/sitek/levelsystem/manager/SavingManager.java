package dev.sitek.levelsystem.manager;

import dev.sitek.levelsystem.database.DatabaseManager;
import org.bukkit.Bukkit;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class SavingManager implements Runnable {

    private final DatabaseManager databaseManager;
    private final LevelManager levelManager;

    public SavingManager(DatabaseManager databaseManager, LevelManager levelManager) {
        this.databaseManager = databaseManager;
        this.levelManager = levelManager;
    }

    @Override
    public void run() {
        databaseManager.saveAllPlayersData();
        levelManager.setupTopPlayers();

        Bukkit.broadcastMessage(colorize("&lTRYRPG &8» &7Pomyslnie przeladowano &aBaze Danych &7odswiezono rowniez &2Topke Graczy!"));
    }

}
