package fr.grimtown.tools.save.classes;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

@Entity(value = "save")
public class ManualSave {
    @Id
    private Object id;
    private UUID playerUuid;
    private Date date;
    private String server;
    private UUID senderUuid;
    private SavedInventory savedInventory;

    public ManualSave() {}

    public ManualSave(UUID playerUuid, Date date, UUID senderUuid) {
        this.playerUuid = playerUuid;
        this.date = date;
        this.server = Bukkit.getServer().getName();
        this.senderUuid = senderUuid;
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

    public UUID getSenderUuid() {
        return senderUuid;
    }

    public SavedInventory getSavedInventory() {
        return savedInventory;
    }
}
