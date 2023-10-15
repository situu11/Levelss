package dev.sitek.levelsystem.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextUtil {

    private TextUtil() {
    }

    public static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String format(double value) {
        return String.format("%.2f", value);
    }

    public static void msgPlayer(Player player, String... strings) {
        for (String string : strings) {
            player.sendMessage(colorize(string));
        }
    }
}
