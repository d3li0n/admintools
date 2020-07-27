package me.d3li0n.AdminTools.commands;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import me.d3li0n.AdminTools.utils.FileManagerUtil;

import java.util.Date;
import java.util.List;

public class AdminPlayerCommands implements CommandExecutor {
	private final FileManagerUtil file;

	public AdminPlayerCommands(FileManagerUtil file) {
		this.file = file;
	}

	public boolean validatePlayer(String player, CommandSender sender) {
		if (sender.getName().equals(player)) {
			sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("errors.ban.yourself"));
			return false;
		} else if (Bukkit.getBanList(BanList.Type.NAME).isBanned(player)) {
			sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("errors.ban.was_banned_already"));
			return false;
		}
		return true;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("ban")) {
			if (sender.hasPermission("admintools.ban") || !(sender instanceof Player)) {
				if (args.length == 0)  {
					sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("messages.commands.ban.use"));
					return true;
				}

				if (!validatePlayer(args[0], sender)) return true;

				Player target = Bukkit.getServer().getPlayer(args[0]);

				if(target == null || !target.isOnline()) {
					sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("errors.general.player_not_found"));
					return true;
				}

				if (sender instanceof Player && (target.hasPermission("admintools.ban.bypass") || target.isOp())) {
					sender.sendMessage(ChatColor.RED + file.getLangConfig().getString("errors.permissions.has_higher_permissions_than_you"));
					return true;
				}

				long time = 0;

				if (args.length > 1 && (args[1] != null || args[2] != null)) {
					try {
						if (args[1] != null && (args[1].charAt(args[1].length() - 1) == 'd' ||
								args[1].charAt(args[1].length() - 1) == 'h' ||
								args[1].charAt(args[1].length() - 1) == 'm')) {
							time = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
							if (time <= 0) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.general.invalid_parameter")));
								return true;
							}
						} else if (args[2] != null && (args[2].charAt(args[2].length() - 1) == 'd' ||
								args[2].charAt(args[2].length() - 1) == 'h' ||
								args[2].charAt(args[2].length() - 1) == 'm')) {
							time = Integer.parseInt(args[2].substring(0, args[2].length() - 1));
							if (time <= 0) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.general.invalid_parameter")));
								return true;
							}
						}
					} catch (Exception e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.general.invalid_parameter")));
						return true;
					}
				}

				if (args.length == 1) {

					Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + file.getLangConfig().getString("messages.commands.ban.default_ban_reason"), null, sender.getName());
					target.kickPlayer(ChatColor.translateAlternateColorCodes('&',
											file.getLangConfig().getString("messages.commands.ban.default_ban_message")
													.replace("%player_banned%", sender.getName())
													.replace("%reason%", file.getLangConfig().getString("messages.commands.ban.default_ban_reason"))));

					List<String> list = file.getLangConfig().getStringList("messages.commands.ban.permanent_ban_message_to_players");

					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message).replace("%target_player%", target.getName()).replace("%executed_player%", player.getName())));
					}

					Bukkit.getLogger().info(target.getName() + " was banned by " + sender.getName());

				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("-s")) {
						Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + file.getLangConfig().getString("messages.commands.ban.default_ban_reason"), null, sender.getName());
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&',
								file.getLangConfig().getString("messages.commands.ban.default_ban_message")
										.replace("%player_banned%", sender.getName())
										.replace("%reason%", file.getLangConfig().getString("messages.commands.ban.default_ban_reason"))));

					} else if (args[1].charAt(args[1].length() - 1) == 'd' ||
							args[1].charAt(args[1].length() - 1) == 'h' ||
							args[1].charAt(args[1].length() - 1) == 'm') {
						String reason;

						if (args[1].charAt(args[1].length() - 1) == 'd') {
							time = System.currentTimeMillis() + (time * 24 * 3600 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_days")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", file.getLangConfig().getString("messages.commands.ban.default_ban_reason"))
									.replace("%forDays%", args[1].substring(0, args[1].length() - 1));
						} else if (args[1].charAt(args[1].length() - 1) == 'h') {
							time = System.currentTimeMillis() + (time * 3600 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_hours")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", file.getLangConfig().getString("messages.commands.ban.default_ban_reason"))
									.replace("%forHours%", args[1].substring(0, args[1].length() - 1));
						} else {
							time = System.currentTimeMillis() + (time * 60 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_minutes")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", file.getLangConfig().getString("messages.commands.ban.default_ban_reason"))
									.replace("%forMinutes%", args[1].substring(0, args[1].length() - 1));
						}

						Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + file.getLangConfig().getString("messages.commands.ban.default_ban_reason"), new Date(time), sender.getName());
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));

					} else {
						Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + args[1], null, sender.getName());
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("messages.commands.ban.default_ban_message")
								.replace("%player_banned%", sender.getName())
								.replace("%reason%", args[1])));
					}

					List<String> list = file.getLangConfig().getStringList("messages.commands.ban.permanent_ban_message_to_players");

					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (args[1].equalsIgnoreCase("-s") && player.hasPermission("admintools.chat")) {
							list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message).replace("%target_player%", target.getName()).replace("%executed_player%", player.getName())));
						} else if (!args[1].equalsIgnoreCase("-s")) {
							list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message).replace("%target_player%", target.getName()).replace("%executed_player%", player.getName())));
						}
					}

					Bukkit.getLogger().info(target.getName() + " was banned by " + sender.getName());
				} else {

					if (args[1].charAt(args[1].length() - 1) == 'd' ||
							args[1].charAt(args[1].length() - 1) == 'h' ||
							args[1].charAt(args[1].length() - 1) == 'm') {
						String reason;

						if (args[1].charAt(args[1].length() - 1) == 'd') {
							time = System.currentTimeMillis() + (time * 24 * 3600 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_days")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", args[2])
									.replace("%forDays%", args[1].substring(0, args[1].length() - 1));
						} else if (args[1].charAt(args[1].length() - 1) == 'h') {
							time = System.currentTimeMillis() + (time * 3600 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_hours")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", args[2])
									.replace("%forDays%", args[1].substring(0, args[1].length() - 1));
						} else {
							time = System.currentTimeMillis() + (time * 60 * 1000);
							reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_minutes")
									.replace("%player_banned%", sender.getName())
									.replace("%default_ban_reason%", args[2])
									.replace("%forDays%", args[1].substring(0, args[1].length() - 1));
						}
						Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + args[2], new Date(time), sender.getName());
						target.kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));
					} else {
						if (args[2].charAt(args[2].length() - 1) == 'd' ||
								args[2].charAt(args[2].length() - 1) == 'h' ||
								args[2].charAt(args[2].length() - 1) == 'm') {
							String reason;

							if (args[2].charAt(args[2].length() - 1) == 'd') {
								time = System.currentTimeMillis() + (time * 24 * 3600 * 1000);
								reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_days")
										.replace("%player_banned%", sender.getName())
										.replace("%default_ban_reason%", args[1])
										.replace("%forDays%", args[2].substring(0, args[2].length() - 1));
							} else if (args[2].charAt(args[2].length() - 1) == 'h') {
								time = System.currentTimeMillis() + (time * 3600 * 1000);
								reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_hours")
										.replace("%player_banned%", sender.getName())
										.replace("%default_ban_reason%", args[1])
										.replace("%forDays%", args[2].substring(0, args[2].length() - 1));
							} else {
								time = System.currentTimeMillis() + (time * 60 * 1000);
								reason = file.getLangConfig().getString("messages.commands.ban.ban_message_to_player_minutes")
										.replace("%player_banned%", sender.getName())
										.replace("%default_ban_reason%", args[1])
										.replace("%forDays%", args[2].substring(0, args[2].length() - 1));
							}
							Bukkit.getBanList(BanList.Type.NAME).addBan(args[0], ChatColor.GRAY + args[1], new Date(time), sender.getName());
							target.kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));
						}
					}

					List<String> list = file.getLangConfig().getStringList("messages.commands.ban.permanent_ban_message_to_players");

					for (Player player : Bukkit.getServer().getOnlinePlayers()) {
						if (args[3].equalsIgnoreCase("-s") && player.hasPermission("admintools.chat")) {
							list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message).replace("%target_player%", target.getName()).replace("%executed_player%", player.getName())));
						} else if (!args[3].equalsIgnoreCase("-s") ) {
							list.forEach((message) -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message).replace("%target_player%", target.getName()).replace("%executed_player%", player.getName())));
						}
					}

					Bukkit.getLogger().info(target.getName() + " was banned by " + sender.getName());
				}
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
				return true;
			}
		} else if (label.equalsIgnoreCase("unban")) {
			if (sender.hasPermission("admintools.unban") || !(sender instanceof Player)) {

			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("errors.permissions.user_has_no_permissions")));
				return true;
			}
		}
		return false;
	}
}
