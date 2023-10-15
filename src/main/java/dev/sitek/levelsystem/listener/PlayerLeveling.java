package dev.sitek.levelsystem.listener;

import dev.sitek.levelsystem.manager.LevelManager;
import dev.sitek.levelsystem.instance.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerLeveling implements Listener {

    private final LevelManager levelManager;

    public PlayerLeveling(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void levelEvent(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            return;
        }

        e.setDroppedExp(0);

        Player killer = e.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        Level level = levelManager.getLevel(killer.getUniqueId());

        String entityName = ChatColor.stripColor(e.getEntity().getName());
        int index = entityName.indexOf(".") + 1;
        int entityLevel = Integer.parseInt(entityName.substring(index).trim());

        double exp = 0;

        if (entityLevel < 10) {
            exp = 5 + 0.5 * entityLevel;
        } else if (entityLevel < 20) {
            exp = 10 + 0.5 * entityLevel;
        }
        level.addExp(exp);
    }

}
