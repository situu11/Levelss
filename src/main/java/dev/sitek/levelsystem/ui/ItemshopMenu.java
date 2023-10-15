package dev.sitek.levelsystem.ui;

import dev.sitek.levelsystem.LevelSystem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static dev.sitek.levelsystem.util.TextUtil.colorize;

public class ItemshopMenu {

    private final LevelSystem plugin;

    private Gui gui;
    private final int playerPLNs;

    public ItemshopMenu(LevelSystem plugin, Player player) {
        this.plugin = plugin;

        playerPLNs = plugin.getDatabaseManager().getwPLN(player.getUniqueId());
        initializeGui(player);
        initializeClickAction(player);

        gui.open(player);
    }

    private void initializeGui(Player player) {
        GuiItem gray = ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)).setName(" ").asGuiItem();
        GuiItem black = ItemBuilder.from(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).setName(" ").asGuiItem();

        List<GuiItem> items = new ArrayList<>();
        items.add(createShopItem(Material.GOLD_BLOCK, "&7Ranga VIP &8(&eOkres: 7d&8)", 20, "&7Kliknij LPM, aby zakupic range &6VIP &7na 7 dni!"));
        items.add(createShopItem(Material.GOLD_BLOCK, "&7Ranga VIP &8(&eOkres: 30d&8)", 50, "&7Kliknij LPM, aby zakupic range &6VIP &7na 30 dni!"));
        items.add(createShopItem(Material.SHEARS, "&7Rekawica Szczescia &8(&aOkres: 60m&8)", 5, "&7Kliknij LPM, aby zakupic &aRekawice Szczescia +75% &7na 60 minut!"));
        items.add(createShopItem(Material.GOLD_NUGGET, "&7Pierscien Doswiadczenia &8(&dOkres: 30m&8)", 3, "&7Kliknij LPM, aby zakupic &5Pierscien Doswiadczenia +50% &7na 30 minut!"));
        items.add(createShopItem(Material.GOLD_NUGGET, "&7Pierscien Doswiadczenia &8(&dOkres: 60m&8)", 5, "&7Kliknij LPM, aby zakupic &5Pierscien Doswiadczenia +50% &7na 60 minut!"));

        GuiItem playerHead = ItemBuilder.skull().owner(player).setName(colorize("&7Informacje: &f" + player.getName()))
                .setLore("", colorize(String.format("&7Twoj stan konta wynosi: &f%d &7wPLN", playerPLNs))).asGuiItem();
        GuiItem info = ItemBuilder.from(Material.BOOK).setName(colorize("&7Jak zakupic walute &fwPLN?"))
                .setLore("", colorize("&7Przelicznik: &f1zl = 1wPLN!"), "",
                        colorize("&7Walute &fwPLN &7mozesz zakupic na stronie: &fwww.tryrpg.pl"),
                        colorize("&7Dostepne metody platnosci: &fPrzelew Bankowy, Blik, PSC, SMS"), "",
                        colorize("&7Odnosnie zakupu &fwPLN &7za pomoca skinow w CS2 lub giftow w lolu - kontakt &csitu &7na discordzie!")).asGuiItem();

        gui = Gui.gui()
                .title(Component.text("Itemshop"))
                .rows(3)
                .create();

        gui.getFiller().fill(gray);
        gui.getFiller().fillBetweenPoints(1,1, 1, 2, black);
        gui.getFiller().fillBetweenPoints(1,8, 1, 9, black);
        gui.getFiller().fillBetweenPoints(3,1, 3, 2, black);
        gui.getFiller().fillBetweenPoints(3,8, 3, 9, black);

        int[] itemSlots = {11, 12, 13, 14, 15};
        for (int i = 0; i < items.size(); i++) {
            gui.setItem(itemSlots[i], items.get(i));
        }

        gui.setItem(9, info);
        gui.setItem(17, playerHead);
    }

    private void initializeClickAction(Player player) {
        gui.setDefaultClickAction(e -> {
            e.setCancelled(true);
            player.closeInventory();

            int[] cost = {20, 50, 5, 3, 5};
            int slot = e.getRawSlot() - 11;


            if (slot >= 0 && slot <= 4) {

                if (!(playerPLNs >= cost[slot])) {
                    player.sendMessage(colorize("&8[&c✖&8] &cNie masz wystarczajacej ilosci &7wPLN!"));
                    return;
                }

                if (slot == 0) {
                    player.getInventory().addItem(ItemBuilder.from(Material.GOLD_BLOCK).setName(colorize("&7Ranga VIP &8(&67d&8)"))
                            .setLore(colorize("&8Kliknij PPM, aby aktywowac range VIP na 7 dni!")).build());
                    Bukkit.broadcastMessage(colorize(String.format("&lITEMSHOP &8» &7Gracz &6%s &7zakupil &eRange VIP &7na okres &e7 dni!", player.getName())));
                } else if (slot == 1) {
                    player.getInventory().addItem(ItemBuilder.from(Material.GOLD_BLOCK).setName(colorize("&7Ranga VIP &8(&630d&8)"))
                            .setLore(colorize("&8Kliknij PPM, aby aktywowac range VIP na 30 dni!")).build());
                    Bukkit.broadcastMessage(colorize(String.format("&lITEMSHOP &8» &7Gracz &6%s &7zakupil &eRange VIP &7na okres &e30 dni!", player.getName())));
                } else if (slot == 2) {
                    player.getInventory().addItem(ItemBuilder.from(Material.SHEARS).setName(colorize("&7Rekawica Szczescia &8(&a+75%&8) &8(&260m&8)"))
                            .setLore(colorize("&8Kliknij PPM, aby aktywowac Rekawice Szczescia!")).build());
                    Bukkit.broadcastMessage(colorize(String.format("&lITEMSHOP &8» &7Gracz &2%s &7zakupil &aRekawice Szczescia &7na okres &a60 minut!", player.getName())));
                } else if (slot == 3) {
                    player.getInventory().addItem(ItemBuilder.from(Material.GOLD_NUGGET).setName(colorize("&7Pierscien Doswiadczenia &8(&d+50%&8) &8(&530m&8)"))
                            .setLore(colorize("&8Kliknij PPM, aby aktywowac Pierscien Doswiadczenia!")).build());
                    Bukkit.broadcastMessage(colorize(String.format("&lITEMSHOP &8» &7Gracz &5%s &7zakupil &dPierscien Doswiadczenia &7na okres &d30 minut!", player.getName())));
                } else {
                    player.getInventory().addItem(ItemBuilder.from(Material.GOLD_NUGGET).setName(colorize("&7Pierscien Doswiadczenia &8(&d+50%&8) &8(&560m&8)"))
                            .setLore(colorize("&8Kliknij PPM, aby aktywowac Pierscien Doswiadczenia!")).build());
                    Bukkit.broadcastMessage(colorize(String.format("&lITEMSHOP &8» &7Gracz &5%s &7zakupil &dPierscien Doswiadczenia &7na okres &d60 minut!", player.getName())));
                }

                plugin.getDatabaseManager().updateItemshopData(player.getUniqueId(), cost[slot]);
            }
        });
    }

    private GuiItem createShopItem(Material material, String name, int cost, String description) {
        return ItemBuilder.from(material)
                .setName(colorize(name))
                .setLore("", colorize("&7Kwota: &f" + cost + " wPLN"), colorize(description))
                .asGuiItem();
    }

}
