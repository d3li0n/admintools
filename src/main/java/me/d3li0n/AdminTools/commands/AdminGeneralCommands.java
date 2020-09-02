package me.d3li0n.AdminTools.commands;

import me.d3li0n.AdminTools.utils.FileManagerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminGeneralCommands implements CommandExecutor  {
    private final FileManagerUtil file;

    public AdminGeneralCommands(FileManagerUtil file) {
        this.file = file;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("admins")) {
            if (sender instanceof Player) {
                if (sender.hasPermission("admintools.admins")) {
                    String adminsList = "";
                    int counter = 0;
                    List<String> list = file.getLangConfig().getStringList("messages.commands.admins.message");
                    for (Player player : Bukkit.getServer().getOnlinePlayers())
                        if (player.hasPermission("admintools.admins.show") && !player.isOp() && !player.hasPermission("admitools.admins.hide")) {
                            adminsList += list.get(1).replace("%player%", player.getName()) + "\n";
                            counter++;
                        }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', list.get(0) + "\n" + adminsList + list.get(2).replace("%total%", String.valueOf(counter))));
                } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
            }
            else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.console_has_no_permissions")));
            return true;
        }
        return false;
    }
}
