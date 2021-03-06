package fr.grimtown.tools.save.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

@Entity(value = "save")
public class ConnectionSave {
    @Id
    private Object id;
    private UUID playerUuid;
    private Date date;
    private String server;
    private Action action;
    private SavedInventory savedInventory;

    public enum Action { JOIN, LEAVE }

    public ConnectionSave() {}

    public ConnectionSave(UUID playerUuid, Date date) {
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

    public String getServer() {
        return server;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }
}
