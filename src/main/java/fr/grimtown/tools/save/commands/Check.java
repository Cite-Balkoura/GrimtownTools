package fr.grimtown.tools.save.commands;

import fr.grimtown.tools.Main;
import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.managers.SaveManager;
import fr.grimtown.tools.utils.classes.Profile;
import fr.grimtown.tools.utils.managers.ProfileManager;
import fr.milekat.utils.DateMileKat;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Check implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==3 && args[0].equalsIgnoreCase("check")) {
            Profile profile = ProfileManager.getProfile(args[2]);
            if (profile==null) {
                sender.sendMessage("§cPlayer not found");
                return true;
            }
            switch (args[1].toLowerCase(Locale.ROOT)) {
                case "all" -> new InventoriesGui(profile, SaveManager.getSaves(profile.getUuid())).open((Player) sender);
                case "death" -> new InventoriesGui(profile, SaveManager.getDeaths(profile.getUuid())).open((Player) sender);
                case "log" -> new InventoriesGui(profile, SaveManager.getConnections(profile.getUuid())).open((Player) sender);
            }
        }
        return true;
    }

    private static class InventoriesGui extends FastInv {
        private final Profile profile;
        private final Collection<?> inventories;
        public InventoriesGui(Profile profile, LinkedList<?> inventories) {
            super(27, "Saves from " + profile.getUsername());
            this.profile = profile;
            Collections.reverse(inventories);
            this.inventories = inventories;
            Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(Main.class), this::loadInventories);
        }

        private void loadInventories() {
            inventories.forEach(o -> {
                if (o.getClass().equals(DeathSave.class)) {
                    DeathSave save = (DeathSave) o;
                    ItemBuilder builder = new ItemBuilder(Material.CHEST)
                            .name(DateMileKat.getDate(save.getDate()))
                            .addLore("Storage: " + Arrays.stream(save.getSavedInventory().getInv()).filter(itemStack -> itemStack.getType().equals(Material.AIR)).count())
                            .addLore("Armor: " + Arrays.stream(save.getSavedInventory().getArmor()).filter(itemStack -> itemStack.getType().equals(Material.AIR)).count())
                            .addLore("XP: " + save.getSavedInventory().getLevels());
                    if (save.getCause()!=null) builder.addLore("Death cause: " + save.getCause().toString());
                    if (save.getKillerType()!=null) builder.addLore("Type: " + save.getKillerType());
                    if (save.getKiller()!=null) builder.addLore("Killer: " + save.getKiller().toString());
                    if (save.getLocation()!=null) builder.addLore(
                            "Location -> W:" + save.getLocation().getWorld().getName()
                            + " X:" + Math.round(Math.round(save.getLocation().getX()))
                            + " Y:" + Math.round(Math.round(save.getLocation().getY()))
                            + " Z:" + Math.round(Math.round(save.getLocation().getZ())));
                    addItem(builder.build()// TODO: 13/11/2021 Open inv view , event -> {}
                    );
                } else if (o.getClass().equals(ConnectionSave.class)) {
                    ConnectionSave save = (ConnectionSave) o;
                    addItem(new ItemBuilder(Material.CHEST)
                            .name(DateMileKat.getDate(save.getDate()))
                            .addLore("Storage: " + save.getSavedInventory().getInv().length)
                            .addLore("Armor: " + save.getSavedInventory().getArmor().length)
                            .addLore("XP: " + save.getSavedInventory().getLevels())
                            .build());
                }
            });
        }

        @Override
        public void onClick(InventoryClickEvent event) {
            event.setCancelled(true);
        }

        // TODO: 13/11/2021 Tab !
    }
}
