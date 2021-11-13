package fr.grimtown.tools.save.managers;

import dev.morphia.Datastore;
import dev.morphia.query.experimental.filters.Filters;
import fr.grimtown.tools.Main;
import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.classes.ManualSave;
import fr.grimtown.tools.save.classes.SavedInventory;
import org.bson.types.ObjectId;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SaveManager {
    private static final Datastore DATASTORE = Main.getDatastore(Main.getEvent().getDatabase());
    /**
     * Get all connections for this UUID of player
     */
    public static LinkedList<?> getObjects(Class<?> type, UUID uuid) {
        return new LinkedList<>(DATASTORE.find(type)
                .filter(Filters.eq("playerUuid", uuid))
                .iterator().toList());
    }

    /**
     * Weird method to get a saved inventory
     */
    public static SavedInventory getInventory(ObjectId id) {
        ConnectionSave connection = DATASTORE.find(ConnectionSave.class).filter(Filters.eq("_id", id)).first();
        if (connection!=null) return connection.getSavedInventory();
        DeathSave death = DATASTORE.find(DeathSave.class).filter(Filters.eq("_id", id)).first();
        if (death!=null) return death.getSavedInventory();
        ManualSave manual = DATASTORE.find(ManualSave.class).filter(Filters.eq("_id", id)).first();
        if (manual!=null) return manual.getSavedInventory();
        else return null;
    }

    /**
     * Get the last SavedInventory of uuid
     */
    public static LinkedList<Object> getSaves(UUID uuid) {
        ArrayList<Object> inventories = new ArrayList<>();
        inventories.addAll(getObjects(ConnectionSave.class , uuid));
        inventories.addAll(getObjects(DeathSave.class , uuid));
        inventories.addAll(getObjects(ManualSave.class , uuid));
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

    /**
     * Save
     */
    public static void save(ManualSave manualSave) {
        DATASTORE.save(manualSave);
    }
}

