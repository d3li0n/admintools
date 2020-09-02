package me.d3li0n.AdminTools.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.helpers.PlayerChatManager;
import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.utils.FileManagerUtil;

public class ChatListener implements Listener {
	private final FileManagerUtil util;
	private final ChatManager manager;
	
	public ChatListener(FileManagerUtil util, ChatManager manager) {
		this.util = util;
		this.manager = manager;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();

		if (player.hasPermission("admintools.chat")) {
			if (event.getMessage().charAt(0) == '#') {
				event.setCancelled(true);
				String mes = event.getMessage().substring(1);
				/*if (event.getMessage().charAt(1) == ' ') mes = event.getMessage().substring(2);
				else*/
				for (Player p : Bukkit.getServer().getOnlinePlayers())
					if (p.hasPermission("admintools.chat")) p.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("messages.adminchat.message").replace("%sender%", player.getName()).replace("%message%", mes)));
			}
		}

		if (!player.hasPermission("admintools.mutechat.bypass")) {
			if(!Main.CHAT_STATE) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.chat.chat_disabled"));
			}
		}

		if (!player.hasPermission("admintools.mute.bypass")) {
			if (manager.isDisabled(id, true)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.chat.player_has_been_muted")).replace("%time%", secToTime(manager.getTimeRemaining(id, true) / 1000)));
			}
		}

		if (!player.hasPermission("admintools.chat.cooldown.bypass"))
		{
			if(!Main.CHAT_SLOW_STATE && Main.CHAT_STATE) {
				if(manager.isDisabled(id)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', util.getLangConfig().getString("errors.chat.chat_slowdown")).replace("%seconds%", "" + (manager.getTimeRemaining(id) / 1000)));
				} else manager.add(new PlayerChatManager(id, ChatManager.playerTimer, System.currentTimeMillis()));
			} else if(manager.isDisabled(id)) manager.remove(id);
		}
	}

	private String secToTime(long sec) {
		long seconds = sec % 60;
		long minutes = sec / 60;
		if (minutes >= 60) {
			long hours = minutes / 60;
			minutes %= 60;
			if (hours >= 24) {
				long days = hours / 24;
				return (util.getLangConfig().getString("messages.commands.mute.time_remaining_days")).replace("%days%", "" + days).replace("%hours%", "" + (hours % 24)).replace("%minutes%", "" + minutes).replace("%seconds%", "" + seconds);
			}
			return (util.getLangConfig().getString("messages.commands.mute.time_remaining_hours")).replace("%hours%", "" + (hours % 24)).replace("%minutes%", "" + minutes).replace("%seconds%", "" + seconds);
		}
		return (util.getLangConfig().getString("messages.commands.mute.time_remaining_minutes")).replace("%minutes%", "" + minutes).replace("%seconds%", "" + seconds);
	}
}