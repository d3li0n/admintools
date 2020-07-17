package me.d3li0n.AdminTools.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.utils.FileManagerUtil;

public class AdminChatCommands implements CommandExecutor {
	private FileManagerUtil util;
	
	public AdminChatCommands(FileManagerUtil util) {
		this.util = util;
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("clearchat") || label.equalsIgnoreCase("cc")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("admintools.clearchat")) {
					for (Player p : Bukkit.getOnlinePlayers())
						if(p.isOnline()) {
							for(int i = 0; i < 100; i++) p.sendMessage("");
							p.sendMessage(ChatColor.RED + (String) (util.getLangConfig().getString("messages.commands.cleanchat")).replace("%player%", sender.getName()));
						}
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.user_has_no_permissions"));
	                return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.console_has_no_permissions"));
                return false;
			}
		} 
		else if(label.equalsIgnoreCase("mutechat")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("admintools.mutechat")) {
					Main.CHAT_STATE = !Main.CHAT_STATE;
					for (Player p : Bukkit.getOnlinePlayers())  
						if(p.isOnline()) {
							if(!Main.CHAT_STATE) p.sendMessage(ChatColor.RED + (String) (util.getLangConfig().getString("messages.commands.mutechat.disabled")).replace("%player%", sender.getName()));
							else p.sendMessage(ChatColor.GREEN + (String) (util.getLangConfig().getString("messages.commands.mutechat.enabled")).replace("%player%", sender.getName()));
						}
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.user_has_no_permissions"));
	                return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.console_has_no_permissions"));
                return false;
			}
		}
		else if(label.equalsIgnoreCase("slowchat")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("admintools.slowchat")) {
					try {
						if(Integer.parseInt(args[0]) > 0 && Integer.parseInt(args[0]) <= 50) {
							Main.CHAT_SLOW_STATE = !Main.CHAT_SLOW_STATE;
							for (Player p : Bukkit.getOnlinePlayers()) if(p.isOnline()) p.sendMessage(ChatColor.RED + (String) (util.getLangConfig().getString("messages.commands.slowchat.disabled")).replace("%player%", sender.getName()));
							ChatManager.cooldownTimer = Integer.parseInt(args[0]) * 1000;
							sender.sendMessage(ChatColor.GREEN + util.getLangConfig().getString("messages.commands.slowchat.enabled_to_player").replace("%seconds%", "" + Integer.parseInt(args[0])));
						} else sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("messages.commands.slowchat.invalid_time"));
					} catch (Exception e) {
						if(!Main.CHAT_SLOW_STATE) {
							Main.CHAT_SLOW_STATE = !Main.CHAT_SLOW_STATE;
							for (Player p : Bukkit.getOnlinePlayers()) if(p.isOnline()) p.sendMessage(ChatColor.GREEN + (String) (util.getLangConfig().getString("messages.commands.slowchat.enabled")).replace("%player%", sender.getName()));
							sender.sendMessage(ChatColor.GREEN + util.getLangConfig().getString("messages.commands.slowchat.turned_off_to_player"));
						} else sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("messages.commands.slowchat.already_disabled"));	
					}
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.user_has_no_permissions"));
	                return false;
				}
			} else {
				sender.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.permissions.console_has_no_permissions"));
                return false;
			}
		}
		return false;
	}
}
