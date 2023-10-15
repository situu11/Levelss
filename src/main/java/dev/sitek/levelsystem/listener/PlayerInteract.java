package dev.sitek.levelsystem.listener;

import dev.sitek.levelsystem.manager.LevelManager;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class PlayerInteract implements Listener {

    private final LevelManager levelManager;

    private final Pattern PIERSCIEN_PATTERN = Pattern.compile("Pierscien Doswiadczenia.*\\+(\\d+)%.*\\((\\d+)m\\)");
    private final Pattern VIP_PATTERN = Pattern.compile("Ranga VIP \\((\\d+)d\\)");
    private final Pattern REKAWICA_PATTERN = Pattern.compile("Rekawica Szczescia \\((\\d+)d\\)");

    public PlayerInteract(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractPierscien(PlayerInteractEvent e) {
        if (!isCorrectItem(e, "Pierscien Doswiadczenia", PIERSCIEN_PATTERN, "&8[&c✖&8] &cMasz wiecej niz &71 Pierscien Doswiadczenia &cw rece!")) return;
        Player player = e.getPlayer();

        if (levelManager.hasBonus(player.getUniqueId())) {
            player.sendMessage(colorize("&8[&c✖&8] &cAktualnie posiadasz aktywny Pierscien Doswiadczenia!"));
            return;
        }

        player.getInventory().removeItem(e.getItem());

        Matcher matcher = PIERSCIEN_PATTERN.matcher(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()));
        if (matcher.find()) {
            int bonus = Integer.parseInt(matcher.group(1));
            int time = Integer.parseInt(matcher.group(2));
            levelManager.bonusExpPlayer(player.getUniqueId(), bonus, time);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractVip(PlayerInteractEvent e) {
        if (!isCorrectItem(e, "Ranga VIP", VIP_PATTERN, "&8[&c✖&8] &cMasz wiecej niz &71 aktywacje rangi &cw rece!")) return;
        Player player = e.getPlayer();

        User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);

        if (user.getPrimaryGroup().contains("vip")) {
            player.sendMessage(colorize("&8[&c✖&8] &cNie mozesz uzyc, poniewaz aktualnie posiadasz &7Range VIP!"));
            return;
        }

        player.getInventory().removeItem(e.getItem());

        Matcher matcher = VIP_PATTERN.matcher(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName()));
        if (matcher.find()) {
            int days = Integer.parseInt(matcher.group(1));
            String command = String.format("lp user %s parent addtemp vip %dd", player.getName(), days);

            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
            player.sendTitle(colorize("&lRanga VIP"), colorize("&7Aktywowano &6Range VIP &7na czas: &e" + days + " dni!"));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteractRekawica(PlayerInteractEvent e) {
        if (!isCorrectItem(e, "Rekawica Szczescia", REKAWICA_PATTERN, null)) return;
        Player player = e.getPlayer();

        // TODO: Checking if has bonus
        player.getInventory().removeItem(e.getItem());
        // TODO: Pattern checker and setting the bonus
    }

    private boolean isCorrectItem(PlayerInteractEvent e, String itemName, Pattern pattern, String amountMessage) {
        ItemStack clickedItem = e.getItem();
        Player player = e.getPlayer();

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK
            || clickedItem == null
            || !clickedItem.hasItemMeta()
            || !clickedItem.getItemMeta().getDisplayName().contains(itemName)) return false;

        if (clickedItem.getAmount() > 1) {
            player.sendMessage(colorize(amountMessage));
            return false;
        }

        Matcher matcher = pattern.matcher(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
        return matcher.find();
    }

}
