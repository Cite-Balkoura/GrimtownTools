package fr.grimtown.tools;

import dev.morphia.Datastore;
import fr.grimtown.tools.save.SaveManagers;
import fr.grimtown.tools.utils.Event;
import fr.grimtown.tools.utils.EventsManager;
import fr.grimtown.tools.utils.MongoDB;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin {
    /* MongoDB */
    private static HashMap<String, Datastore> datastoreMap = new HashMap<>();
    /* Event */
    private static Event mcEvent;

    @Override
    public void onEnable() {
        /* MongoDB */
        datastoreMap = MongoDB.getDatastoreMap(getConfig());
        /* Event load */
        mcEvent = EventsManager.getEvent(getConfig().getString("data.event-name"));
        Bukkit.getLogger().info("Loaded event: " + mcEvent.getName());
        new SaveManagers(this);
        Bukkit.getPluginManager().registerEvents(new DamageModifiers(), this);
    }

    /**
     * Get loaded Event shortcut
     */
    public static Event getEvent() { return mcEvent; }

    /**
     * MongoDB Connection (Morphia Datastore) to query
     */
    public static Datastore getDatastore(String dbName) {
        return datastoreMap.get(dbName).startSession();
    }
}
