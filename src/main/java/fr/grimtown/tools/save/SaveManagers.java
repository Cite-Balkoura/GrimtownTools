package fr.grimtown.tools.save;

import fr.grimtown.tools.save.events.Connection;
import fr.grimtown.tools.save.events.Death;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveManagers {
    public SaveManagers(JavaPlugin plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new Connection(), plugin);
        pm.registerEvents(new Death(), plugin);
    }
}
