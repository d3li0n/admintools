package me.d3li0n.AdminTools.commands;

import me.d3li0n.AdminTools.utils.FileManagerUtil;
import me.d3li0n.AdminTools.utils.ReportInventoryManagerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands implements CommandExecutor {
    private final FileManagerUtil file;
    private ReportInventoryManagerUtil reportUtil;
    
    public PlayerCommands(FileManagerUtil file, ReportInventoryManagerUtil reportUtil) {
        this.file = file;
        this.reportUtil = reportUtil;
    }

    /* TODO: finish later */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("report")) {
                if (sender.hasPermission("admintools.report.use")) {
                    if (args.length == 1) {
                        Player target = Bukkit.getServer().getPlayer(args[0]);
                        Player player = (Player) sender;

                        if (target == null || !target.isOnline()) {
                            sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("errors.general.player_not_found"));
                            return true;
                        }

                        player.openInventory(reportUtil.getReportInventory());
                        sender.sendMessage(ChatColor.RED + "You cannot report yourself");
                        /* if (player.equals(target)) {
                            sender.sendMessage(ChatColor.RED + "You cannot report yourself");
                            return true;
                        } */

                        /* ReportInventoryManagerUtil inventory = new ReportInventoryManagerUtil(target.getName());
                        player.openInventory(inventory.getReportInventory()); */
                    } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.general.invalid_parameter")));
                } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
            }
        } else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.console_has_no_permissions")));
        return true;
    }
}
