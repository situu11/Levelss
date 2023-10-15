package dev.sitek.levelsystem.placeholder;

import dev.sitek.levelsystem.manager.LevelManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderImplementation extends PlaceholderExpansion {

    private final LevelManager levelManager;

    public PlaceholderImplementation(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "get-level";
    }

    @Override
    public @NotNull String getAuthor() {
        return "sitek";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        int level = levelManager.getLevel(player.getUniqueId()).getLevel();
        return String.valueOf(level);
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        int level = levelManager.getLevel(player.getUniqueId()).getLevel();
        return String.valueOf(level);
    }

}
