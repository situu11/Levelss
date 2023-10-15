package dev.sitek.levelsystem.manager;

import com.mongodb.client.MongoCollection;
import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.database.DatabaseManager;
import dev.sitek.levelsystem.instance.bonus.Bonus;
import dev.sitek.levelsystem.instance.Level;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static dev.sitek.levelsystem.util.TextUtil.*;

public class LevelManager {

    private final DatabaseManager databaseManager;

    private final Map<UUID, Level> usersMap = new HashMap<>();
    private final Map<UUID, Bonus> userBonusMap = new HashMap<>();
    private List<String> topPlayers = new ArrayList<>();

    private double serverBonus = 0;

    /*
     * LevelManager initializing
     */

    public LevelManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /*
     * Setup player data
     */

    public void setupPlayer(UUID uuid) {
        usersMap.computeIfAbsent(uuid, key -> new Level(key,
                        databaseManager.getLevel(key),
                        databaseManager.getExp(key),
                        databaseManager.getRequiredExp(key),
                        databaseManager.getBonusExp(key)))
                .setPlayer(Bukkit.getPlayer(uuid));
    }

    /*
     * Tools
     */

    public void showInfo(Player player, UUID uuid) {
        Level level = getOfflinePlayerLevel(uuid);

        String bonusPrefix = hasBonus(uuid) ? "&a✔" : "&c✖";
        String bonusColor = hasBonus(uuid) ? "&a" : "&c";
        String bonusText = String.format("   &8• &7Pierscien Doswiadczenia: %s &8(%s+%.2f%%&8)", bonusPrefix, bonusColor, level.getTemporaryBonusExp());

        msgPlayer(player, "",
                String.format("       &8[&6Statystyki: &e%s&8]       ", level.getPlayerName()),
                "",
                "   &8• &7Poziom: &f" + level.getLevel(),
                "   &8• &7EXP: &f" + format(level.getExp()) + "&8/&f" + format(level.getRequiredExp()),
                "   &8• &7Postep: &e" + format(level.getPercent()) + "%",
                "   &8• &7Dodatkowy EXP: &3+" + level.getBonusExp() + "% &8(&b+" + this.serverBonus + "%&8)",
                bonusText,
                "");
    }

    /*
     * LevelManager logic
     */

    public Map<UUID, Level> getUsersMap() {
        return usersMap;
    }

    public Level getLevel(UUID uuid) {
        return usersMap.get(uuid);
    }

    public void removePlayer(UUID uuid) {
        usersMap.remove(uuid);
    }

    public Level getOfflinePlayerLevel(UUID uuid) {
        return usersMap.computeIfAbsent(uuid, key -> {
            setupPlayer(key);
            return usersMap.get(key);
        });
    }

    /*
     * User Bonus
     */

    public void bonusExpPlayer(UUID uuid, double bonus, long period) {
        userBonusMap.put(uuid, new Bonus(uuid, bonus, period));
    }

    public boolean hasBonus(UUID uuid) {
        return userBonusMap.containsKey(uuid);
    }

    public void removeBonusList(UUID uuid) {
        userBonusMap.remove(uuid);
    }

    /*
     * Server Bonus
     */

    public double getServerBonus() {
        return serverBonus;
    }

    public void setServerBonus(double serverBonus) {
        this.serverBonus = serverBonus;
    }

    /*
     * Players top logic
     */

    public void setupTopPlayers() {
        MongoCollection<Document> playersDocument = databaseManager.getLevels();
        Map<String, Integer> data = new HashMap<>();

        for (Document playerDocument : playersDocument.find()) {
            data.put(playerDocument.getString("name"), playerDocument.getInteger("level"));
        }

        topPlayers = data.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getTopPlayers() {
        return topPlayers;
    }

}
