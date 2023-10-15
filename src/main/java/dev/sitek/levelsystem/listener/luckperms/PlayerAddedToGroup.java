package dev.sitek.levelsystem.listener.luckperms;

import dev.sitek.levelsystem.LevelSystem;
import dev.sitek.levelsystem.manager.LevelManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;

public class PlayerAddedToGroup {

    private final LevelSystem plugin;
    private final LevelManager levelManager;
    private final LuckPerms luckPerms;

    public PlayerAddedToGroup(LevelSystem plugin, LevelManager levelManager, LuckPerms luckPerms) {
        this.plugin = plugin;
        this.levelManager = levelManager;
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(plugin, NodeAddEvent.class, this::onNodeAdd);
    }

    private void onNodeAdd(NodeAddEvent e) {
        if (!e.isUser()) {
            return;
        }

        Node node = e.getNode();
        InheritanceNode inheritanceNode = (InheritanceNode) node;
        User user = (User) e.getTarget();
        String groupName = inheritanceNode.getGroupName();

        if (groupName.equalsIgnoreCase("vip")) {
            levelManager.getLevel(user.getUniqueId()).addBonusExp(50);
        }
    }

}
