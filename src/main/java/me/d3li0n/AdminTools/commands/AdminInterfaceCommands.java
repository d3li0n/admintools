package me.d3li0n.AdminTools.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.d3li0n.AdminTools.utils.FileManagerUtil;
import me.d3li0n.AdminTools.utils.InventoryManagerUtil;

public class AdminInterfaceCommands implements CommandExecutor {
	private FileManagerUtil file;
	private InventoryManagerUtil inventory;
	
	public AdminInterfaceCommands(FileManagerUtil file, InventoryManagerUtil inventory) {
		this.file = file;
		this.inventory = inventory;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if(label.equalsIgnoreCase("ap")) {
				if(sender.hasPermission("admintools.gui")) {
					Player player = (Player) sender;
					player.openInventory(inventory.getInventory());
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
					return false;
				}
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.console_has_no_permissions")));
			return false;
		}
		return false;
	}
}
