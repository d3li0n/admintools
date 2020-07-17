package me.d3li0n.AdminTools.listeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.helpers.ChatCooldown;
import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.utils.FileManagerUtil;

public class ChatListener implements Listener {
	// private Main plugin;
	private FileManagerUtil util;
	private ChatManager manager;
	
	public ChatListener(Main plugin, FileManagerUtil util, ChatManager manager) {
		// this.plugin = plugin;
		this.util = util;
		this.manager = manager;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();
		
		if(!player.hasPermission("admintools.mutechat.bypass")) {
			if(!Main.CHAT_STATE) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + util.getLangConfig().getString("errors.chat.chat_disabled"));
			}
		}
		
		if(!player.hasPermission("admintools.chat.cooldown.bypass")) 
		{
			if(!Main.CHAT_SLOW_STATE && Main.CHAT_STATE) {
				if(manager.hasCooldown(id)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + (String) (util.getLangConfig().getString("errors.chat.chat_slowdown")).replace("%seconds%", "" + (manager.getTimeRemaining(id) / 1000)));
				} else manager.add(new ChatCooldown(id, ChatManager.cooldownTimer, System.currentTimeMillis()));
			} else if(manager.hasCooldown(id)) manager.remove(id);
		}
	}
	
	/* public Main getPlugin() {
		return this.plugin;
	} */
}