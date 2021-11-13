package fr.grimtown.tools.save.managers;

import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.tools.Main;
import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.classes.SavedInventory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SaveManager {
    private static final Datastore DATASTORE = Main.getDatastore(Main.getEvent().getDatabase());

    /**
     * Get all deaths for this UUID of player
     */
    public static LinkedList<DeathSave> getDeaths(UUID uuid) {
        return new LinkedList<>(DATASTORE.find(DeathSave.class)
                .filter(Filters.eq("playerUuid", uuid))
                .iterator().toList());
    }

    /**
     * Get all connections for this UUID of player
     */
    public static LinkedList<ConnectionSave> getConnections(UUID uuid) {
        return new LinkedList<>(DATASTORE.find(ConnectionSave.class)
                .filter(Filters.eq("playerUuid", uuid))
                .iterator().toList());
    }

    /**
     * Get the last DataDeath of uuid
     */
    public static DeathSave getLastDeath(UUID uuid) {
        return DATASTORE.find(DeathSave.class)
                .filter(Filters.eq("playerUuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last DataConnection of uuid
     */
    public static ConnectionSave getLastConnection(UUID uuid) {
        return DATASTORE.find(ConnectionSave.class)
                .filter(Filters.eq("playerUuid", uuid))
                .iterator(new FindOptions().sort(Sort.descending("_id")).limit(1)).tryNext();
    }

    /**
     * Get the last SavedInventory of uuid
     */
    public static SavedInventory getLastInventory(UUID uuid) {
        DeathSave deathSave = getLastDeath(uuid);
        ConnectionSave connectionSave = getLastConnection(uuid);
        if (deathSave.getDate().after(connectionSave.getDate())) return deathSave.getSavedInventory();
        else return connectionSave.getSavedInventory();
    }

    /**
     * Get the last SavedInventory of uuid
     */
    public static LinkedList<Object> getSaves(UUID uuid) {
        ArrayList<Object> inventories = new ArrayList<>();
        inventories.addAll(getDeaths(uuid));
        inventories.addAll(getConnections(uuid));
        TreeMap<Date, Object> inventoriesMap = new TreeMap<>();
        inventories.forEach(o -> {
            try {
                inventoriesMap.put ((Date) o.getClass().getMethod("getDate").invoke(o), o);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return new LinkedList<>(inventoriesMap.values());
    }

    /**
     * Save a new player Death Data
     */
    public static void save(DeathSave deathSave) {
        DATASTORE.save(deathSave);
    }

    /**
     * Save a new player Connection Data
     */
    public static void save(ConnectionSave connectionSave) {
        DATASTORE.save(connectionSave);
    }
}

