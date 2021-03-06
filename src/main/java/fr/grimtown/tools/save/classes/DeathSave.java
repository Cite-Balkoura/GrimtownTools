package fr.grimtown.tools.save.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity(value = "save")
public class DeathSave {
    @Id
    private Object id;
    private UUID playerUuid;
    private Date date;
    private String server;
    private Map<String, Object> location;
    private DamageCause cause;
    private UUID killer;
    private String killerType;
    private SavedInventory savedInventory;

    public DeathSave() {}

    public DeathSave(UUID playerUuid, Date date) {
        this.playerUuid = playerUuid;
        this.date = date;
        this.server = Bukkit.getServer().getName();
        this.savedInventory = new SavedInventory();
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location!=null ? Location.deserialize(location) : null;
    }

    public void setLocation(Location location) {
        this.location = location.serialize();
    }

    public DamageCause getCause() {
        return cause;
    }

    public void setCause(DamageCause cause) {
        this.cause = cause;
    }

    public UUID getKiller() {
        return killer;
    }

    public void setKiller(UUID killer) {
        this.killer = killer;
    }

    public String getKillerType() {
        return killerType;
    }

    public void setKillerType(String killerType) {
        this.killerType = killerType;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }

    public String getServer() {
        return server;
    }
}
