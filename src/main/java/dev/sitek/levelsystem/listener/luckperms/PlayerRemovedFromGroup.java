package dev.sitek.levelsystem.listener.luckperms;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.manager.LevelManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class PlayerRemovedFromGroup {

    private final LevelSystem plugin;
    private final LevelManager levelManager;
    private final LuckPerms luckPerms;

    public PlayerRemovedFromGroup(LevelSystem plugin, LevelManager levelManager, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.levelManager = levelManager;
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(this.plugin, NodeRemoveEvent.class, this::onNodeRemove);
    }

    private void onNodeRemove(NodeRemoveEvent e) {
        if (!e.isUser()) {
            return;
        }

        Node node = e.getNode();
        InheritanceNode inheritanceNode = (InheritanceNode) node;
        UUID uuid = ((User) e.getTarget()).getUniqueId();
        String groupName = inheritanceNode.getGroupName();

        if (groupName.equalsIgnoreCase("vip")) {
            levelManager.getOfflinePlayerLevel(uuid).removeBonusExp(50);

            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendTitle(colorize("&lRanga VIP"), colorize("&7Dobiegla konca..."));
            }

        }
    }

}
