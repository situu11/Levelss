package dev.sitek.levelsystem.instance;

import dev.sitek.levelsystem.LevelSystem;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static dev.sitek.levelsystem.util.TextUtil.*;

public class Level {

    private final LevelSystem plugin = LevelSystem.getInstance();

    private Player player;
    private final UUID uuid;
    private final String playerName;

    private int level;
    private double exp;
    private double requiredExp;
    private double bonusExp;

    private double temporaryBonusExp;

    /*
     * UserLevel initializing
     */

    public Level(UUID uuid, int level, double exp, double requiredExp, double bonusExp) {
        this.playerName = plugin.getDatabaseManager().getPlayerName(uuid);

        this.uuid = uuid;
        this.level = level;
        this.exp = exp;
        this.requiredExp = requiredExp;
        this.bonusExp = bonusExp;

        this.temporaryBonusExp = 0;
    }

    /*
     * Leveling logic
     */

    public void addExp(double amount) {
        amount += amount * ((getBonusExp() + getServerBonus() + getTemporaryBonusExp()) / 100);
        this.exp += amount;
        sendActionBar(amount);

        if (this.exp >= requiredExp) {
            levelUp();
        }
        setupProgress();
    }

    private void levelUp() {
        levelUpLogic();
        player.sendTitle(colorize("&lWyzszy Poziom!"), colorize("&7Awansowales na &e" + getLevel() + " &7poziom"));

        if (this.level % 10 == 0 || this.level >= 50 ) {
            Bukkit.broadcastMessage(colorize(String.format("&lEXP &8» &7Gracz &6%s &7awansowal na &e%d &7poziom!", this.playerName, this.level)));
        }
    }

    private void levelUpLogic() {
        this.level++;
        this.exp = 0;
        this.requiredExp *= this.level % 10 == 0 ? 1.3 : 1.1;
    }

    /*
     * Tools
     */

    public void sendActionBar(double amount) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(colorize("&lEXP &7+" + format(amount) + " &8(&e" + format(getPercent()) + "%&8)")), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public void setupProgress() {
        player.setLevel(this.level);
        player.setExp(getExpPercent());
    }

    /*
     * Bonus EXP logic
     */

    public void addBonusExp(double amount) {
        this.bonusExp += amount;
        savePlayerData();
    }

    public void removeBonusExp(double amount) {
        this.bonusExp -= amount;
        savePlayerData();
    }

    private void savePlayerData() {
        plugin.getDatabaseManager().savePlayerData(uuid);
    }

    /*
     * Getters
     */

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }

    public double getExp() {
        return exp;
    }

    public double getRequiredExp() {
        return requiredExp;
    }

    public double getBonusExp() {
        return bonusExp;
    }

    public double getPercent() {
        return (exp / requiredExp) * 100;
    }

    private float getExpPercent() {
        return (float) (exp / requiredExp);
    }

    private double getServerBonus() {
        return plugin.getLevelManager().getServerBonus();
    }

    public double getTemporaryBonusExp() {
        return temporaryBonusExp;
    }

    /*
     * Setters
     */

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTemporaryBonusExp(double temporaryBonusExp) {
        this.temporaryBonusExp = temporaryBonusExp;
    }

}
