package fr.grimtown.tools.save.classes;

import dev.morphia.annotations.Entity;
import fr.grimtown.tools.save.InventoryUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Entity(value = "save")
public class SavedInventory {
    private ArrayList<String> invB64;
    private ArrayList<String> armorB64;
    private int levels;

    public SavedInventory() {}

    public ItemStack[] getInv() {
        return InventoryUtils.getContents(invB64);
    }

    public void setInv(ItemStack[] invB64) {
        this.invB64 = InventoryUtils.getContentsString(invB64);
    }

    public ItemStack[] getArmor() {
        return InventoryUtils.getContents(armorB64);
    }

    public void setArmor(ItemStack[] armor) {
        this.armorB64 = InventoryUtils.getContentsString(armor);
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }
}
