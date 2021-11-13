package fr.grimtown.tools.save.events;

import fr.grimtown.tools.Main;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.managers.SaveManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;

public class Death implements Listener {
    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getKeepInventory()) return;
        ItemStack[] armor = event.getEntity().getInventory().getArmorContents().clone();
        ItemStack[] inv = event.getEntity().getInventory().getContents().clone();
        int levels = event.getEntity().getLevel();
        Location location = event.getEntity().getLocation();
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(Main.class), ()-> {
            DeathSave deathSave = new DeathSave(event.getEntity().getUniqueId(), new Date());
            deathSave.setLocation(location);
            if (event.getEntity().getLastDamageCause()!=null) {
                deathSave.setCause(event.getEntity().getLastDamageCause().getCause());
                try {
                    Entity entity = (((EntityDamageByEntityEvent)event.getEntity().getLastDamageCause()).getDamager());
                    deathSave.setKiller(entity.getUniqueId());
                    deathSave.setKillerType(String.valueOf(entity.getType()));
                } catch (Exception ignore) {}
            }
            deathSave.getSavedInventory().setArmor(armor);
            deathSave.getSavedInventory().setInv(inv);
            deathSave.getSavedInventory().setLevels(levels);
            SaveManager.save(deathSave);
        });
    }
}
