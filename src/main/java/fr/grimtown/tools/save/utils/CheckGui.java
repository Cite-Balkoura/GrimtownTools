package fr.grimtown.tools.save.utils;

import fr.grimtown.tools.Main;
import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.classes.ManualSave;
import fr.grimtown.tools.save.classes.SavedInventory;
import fr.grimtown.tools.utils.classes.Profile;
import fr.milekat.utils.DateMileKat;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class CheckGui extends FastInv {
    private final Profile profile;
    private final Collection<?> inventories;
    public CheckGui(Profile profile, LinkedList<?> inventories) {
        super(27, "Saves of " + profile.getUsername());
        this.profile = profile;
        Collections.reverse(inventories);
        this.inventories = inventories;
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(Main.class), this::loadInventories);
    }

    private void loadInventories() {
        inventories.forEach(o -> {
            ItemBuilder builder = new ItemBuilder(Material.CHEST);
            SavedInventory savedInventory = null;
            if (o.getClass().equals(DeathSave.class)) {
                DeathSave save = (DeathSave) o;
                savedInventory = save.getSavedInventory();
                builder.name(DateMileKat.getDate(save.getDate()))
                        .addLore("Storage: " + Arrays.stream(save.getSavedInventory().getInv())
                                .filter(itemStack -> itemStack.getType().equals(Material.AIR)).count())
                        .addLore("Armor: " + Arrays.stream(save.getSavedInventory().getArmor())
                                .filter(itemStack -> itemStack.getType().equals(Material.AIR)).count())
                        .addLore("XP: " + save.getSavedInventory().getLevels());
                if (save.getServer()!=null) builder.addLore("Server: " + save.getServer());
                if (save.getCause()!=null) builder.addLore("Death cause: " + save.getCause().toString());
                if (save.getKillerType()!=null) builder.addLore("Type: " + save.getKillerType());
                if (save.getKiller()!=null) builder.addLore("Killer: " + save.getKiller().toString());
                if (save.getLocation()!=null) builder.addLore(
                        "Location -> W:" + save.getLocation().getWorld().getName()
                        + " X:" + Math.round(Math.round(save.getLocation().getX()))
                        + " Y:" + Math.round(Math.round(save.getLocation().getY()))
                        + " Z:" + Math.round(Math.round(save.getLocation().getZ())));
            } else if (o.getClass().equals(ConnectionSave.class)) {
                ConnectionSave save = (ConnectionSave) o;
                savedInventory = save.getSavedInventory();
                builder.name(DateMileKat.getDate(save.getDate()))
                        .addLore("Storage: " + save.getSavedInventory().getInv().length)
                        .addLore("Armor: " + save.getSavedInventory().getArmor().length)
                        .addLore("XP: " + save.getSavedInventory().getLevels())
                        .addLore("Server: " + save.getServer())
                        .addLore("Action: " + save.getAction().toString());
            } else if (o.getClass().equals(ManualSave.class)) {
                ManualSave save = (ManualSave) o;
                savedInventory = save.getSavedInventory();
                builder.name(DateMileKat.getDate(save.getDate()))
                        .addLore("Storage: " + save.getSavedInventory().getInv().length)
                        .addLore("Armor: " + save.getSavedInventory().getArmor().length)
                        .addLore("XP: " + save.getSavedInventory().getLevels())
                        .addLore("Server: " + save.getServer())
                        .addLore("Saver: " + save.getSenderUuid());
            }
            if (savedInventory!=null) {
                SavedInventory finalInventory = savedInventory;
                addItem(builder.build(), event -> {
                    Inventory inventory = Bukkit.createInventory(null, 54);
                    inventory.setContents(finalInventory.getInv());
                    IntStream.range(36, 45).forEach(slot ->
                            inventory.setItem(slot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));
                    inventory.setItem(45, finalInventory.getArmor()[0]);
                    inventory.setItem(46, finalInventory.getArmor()[1]);
                    inventory.setItem(47, finalInventory.getArmor()[2]);
                    inventory.setItem(48, finalInventory.getArmor()[3]);
                    inventory.setItem(49, new ItemBuilder(Material.EXPERIENCE_BOTTLE)
                            .name(String.valueOf(finalInventory.getLevels())).build());
                    event.getWhoClicked().openInventory(inventory);
                });
            }
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
