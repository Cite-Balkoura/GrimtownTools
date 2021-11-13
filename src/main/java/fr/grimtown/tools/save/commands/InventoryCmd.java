package fr.grimtown.tools.save.commands;

import fr.grimtown.tools.save.classes.ConnectionSave;
import fr.grimtown.tools.save.classes.DeathSave;
import fr.grimtown.tools.save.classes.ManualSave;
import fr.grimtown.tools.save.managers.SaveManager;
import fr.grimtown.tools.save.utils.CheckGui;
import fr.grimtown.tools.utils.classes.Profile;
import fr.grimtown.tools.utils.managers.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InventoryCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length==2 && args[0].equalsIgnoreCase("see")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target==null || !target.isOnline()) {
                sender.sendMessage("§cPlayer not online in: " + Bukkit.getServer().getName());
                return true;
            }
            ((Player) sender).openInventory(target.getInventory());
        } else if (args.length==3 && args[0].equalsIgnoreCase("check")) {
            Profile profile = ProfileManager.getProfile(args[2]);
            if (profile==null) {
                sender.sendMessage("§cPlayer not found");
                return true;
            }
            switch (args[1].toLowerCase(Locale.ROOT)) {
                case "all" -> new CheckGui(profile, SaveManager.getSaves(profile.getUuid()))
                        .open((Player) sender);
                case "death" -> new CheckGui(profile, SaveManager.getObjects(DeathSave.class, profile.getUuid()))
                        .open((Player) sender);
                case "log" -> new CheckGui(profile, SaveManager.getObjects(ConnectionSave.class, profile.getUuid()))
                        .open((Player) sender);
                case "manual" -> new CheckGui(profile, SaveManager.getObjects(ManualSave.class, profile.getUuid()))
                        .open((Player) sender);
            }
        } else if (args.length==2 && args[0].equalsIgnoreCase("save")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target==null || !target.isOnline()) {
                sender.sendMessage("§cPlayer not online in: " + Bukkit.getServer().getName());
                return true;
            }
            SaveManager.save(new ManualSave(target.getUniqueId(), new Date(), ((Player) sender).getUniqueId()));
            sender.sendMessage("Save done.");
        } else sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6Inventory help");
        sender.sendMessage("§6/inv check <type> <player>: §rSee player inventories history");
        sender.sendMessage("§6/inv save <player>:§r Snapshot a player inventory");
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) return List.of("check", "see", "save");
        else if (args.length <= 2 && args[0].equalsIgnoreCase("check")) return List.of("all", "death", "log", "manual");
        return null;
    }
}
