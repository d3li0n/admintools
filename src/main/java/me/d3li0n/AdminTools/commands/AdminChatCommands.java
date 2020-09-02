package me.d3li0n.AdminTools.commands;

import me.d3li0n.AdminTools.helpers.PlayerChatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.utils.FileManagerUtil;

import java.util.List;

public class AdminChatCommands implements CommandExecutor {
	private FileManagerUtil util;
	private ChatManager manager;

	public AdminChatCommands(FileManagerUtil util, ChatManager manager) {
		this.util = util;
		this.manager = manager;
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("clearchat") || label.equalsIgnoreCase("cc")) {
			if(sender instanceof Player) {
				if(sender.hasPermission("admintools.clearchat")) {
					for (Player p : Bukkit.getOnlinePlayers())
						if(p.isOnline()) {
							for(int i = 0; i < 100; i++) p.sendMessage("");
							p.sendMessage(ChatColor.RED + util.getLangConfig().getString("messages.commands.cleanchat").replace("%player%", sender.getName()));
						}
				} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
			} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.console_has_no_permissions")));

			return true;
		} 
		else if (label.equalsIgnoreCase("mutechat")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("admintools.mutechat")) {
					Main.CHAT_STATE = !Main.CHAT_STATE;
					for (Player p : Bukkit.getOnlinePlayers())  
						if (p.isOnline())
							if (!Main.CHAT_STATE) p.sendMessage(ChatColor.RED + util.getLangConfig().getString("messages.commands.mutechat.disabled").replace("%player%", sender.getName()));
							else p.sendMessage(ChatColor.GREEN + util.getLangConfig().getString("messages.commands.mutechat.enabled").replace("%player%", sender.getName()));
				} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
			} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.console_has_no_permissions")));

			return true;
		} else if(label.equalsIgnoreCase("slowchat")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("admintools.slowchat")) {
					try {
						if(Integer.parseInt(args[0]) > 0 && Integer.parseInt(args[0]) <= 50) {
							Main.CHAT_SLOW_STATE = !Main.CHAT_SLOW_STATE;
							for (Player p : Bukkit.getOnlinePlayers()) if(p.isOnline()) p.sendMessage(ChatColor.RED + (String) (util.getLangConfig().getString("messages.commands.slowchat.disabled")).replace("%player%", sender.getName()));
							ChatManager.playerTimer = Integer.parseInt(args[0]) * 1000;
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
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.console_has_no_permissions")));
				return false;
			}
		} else if (label.equalsIgnoreCase("mute")) {
			if (sender.hasPermission("admintools.mute") || !(sender instanceof Player)) {
				if (args.length > 1) {
					if (sender.getName().equals(args[0])) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.mute.yourself")));
						return true;
					}

					Player target = Bukkit.getServer().getPlayer(args[0]);

					if(target == null || !target.isOnline()) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.player_not_found")));
						return true;
					}

					if (sender instanceof Player && (target.hasPermission("admintools.mute.bypass") || target.isOp())) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.has_higher_permissions_than_you")));
						return true;
					}

					if (manager.isDisabled(target.getUniqueId(), true)) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.mute.player_was_muted")));
						return true;
					}

					long time = 0;

					if (args[1] != null) {
						try {
							if (args[1] != null && (args[1].charAt(args[1].length() - 1) == 'd' ||
									args[1].charAt(args[1].length() - 1) == 'h' ||
									args[1].charAt(args[1].length() - 1) == 'm')) {
								time = Integer.parseInt(args[1].substring(0, args[1].length() - 1));

								if (time <= 0) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.invalid_parameter")));
									return true;
								}

								if (args[1].charAt(args[1].length() - 1) == 'd') time = time * 24 * 3600 * 1000;
								else if (args[1].charAt(args[1].length() - 1) == 'h') time = time * 3600 * 1000;
								else time = time * 60 * 1000;

								if (args[2] != null) {
									manager.add(new PlayerChatManager(target.getUniqueId(), time, System.currentTimeMillis()), true);
									List<String> list = util.getLangConfig().getStringList("messages.commands.mute.message_to_players");
									for (Player player : Bukkit.getServer().getOnlinePlayers()) {
										list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%target_player%", target.getName()).replace("%executed_player%", sender.getName()))));
									}
									target.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.mute.to_player").replace("%executed_player%", sender.getName()).replace("%time%", "" + Integer.parseInt(args[1].substring(0, args[1].length() - 1)) + args[1].charAt(args[1].length() - 1)).replace("%reason%", args[2]) ));
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.mute.to_executor").replace("%target_player%", target.getName()).replace("%time%", "" + Integer.parseInt(args[1].substring(0, args[1].length() - 1)) + args[1].charAt(args[1].length() - 1)).replace("%reason%", args[2]) ));
								} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.invalid_parameter")));
							} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.invalid_parameter")));
							Bukkit.getLogger().info(target.getName() + " was muted by " + sender.getName());
						} catch (Exception e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.invalid_parameter")));
							return true;
						}
					}
				} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.mute.use")));
			} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
			return true;
		} else if (label.equalsIgnoreCase("unmute")) {
			if (sender.hasPermission("admintools.mute") || !(sender instanceof Player)) {
				if (args.length > 0) {
					if (sender.getName().equals(args[0])) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.unmute.yourself")));
						return true;
					}

					Player target = Bukkit.getServer().getPlayer(args[0]);

					if(target == null || !target.isOnline()) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.general.player_not_found")));
						return true;
					}

					if (!manager.isDisabled(target.getUniqueId(), true)) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.unmute.player_was_not_muted")));
						return true;
					}

					manager.remove(target.getUniqueId(), true);

					List<String> list = util.getLangConfig().getStringList("messages.commands.unmute.message_to_players");
					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%target_player%", target.getName()).replace("%executed_player%", sender.getName()))));
					}
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.unmute.to_player").replace("%executed_player%", sender.getName())));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.unmute.to_executor").replace("%target_player%", target.getName())));

					Bukkit.getLogger().info(target.getName() + " was unmuted by " + sender.getName());
				} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.commands.mute.use")));
			} else sender.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
			return true;
		}
		return false;
	}
}
