package me.d3li0n.AdminTools.listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.d3li0n.AdminTools.utils.FileManagerUtil;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.utils.PluginInventoryManagerUtil;

public class PlayerListener implements Listener {

	private final Main plugin;
	private final PluginInventoryManagerUtil util;
	private final FileManagerUtil file;

	public PlayerListener(Main main, PluginInventoryManagerUtil util, FileManagerUtil file) {
		this.plugin = main;
		this.util = util;
		this.file = file;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		
		item.setType(Material.ENCHANTED_BOOK);
		List<String> lore = new ArrayList<>();
		
		meta.setDisplayName("Server Stats");
		lore.add("Online: " + Bukkit.getServer().getOnlinePlayers().size());
		meta.setLore(lore);
		item.setItemMeta(meta);
		util.getInventory().setItem(19, item);
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.KICK_BANNED) {
			SimpleDateFormat form = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			List<String> list = file.getLangConfig().getStringList("errors.ban.message_show");
			String mes = "";
			for (String message : list)
				mes = mes + message + "\n";
			event.setKickMessage(ChatColor.translateAlternateColorCodes('&', mes)
					.replace("%reason%", Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getReason())
					.replace("%time%", (Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration() == null) ? "Forever" :
							form.format(Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration())));
        }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();

		if (player.hasPermission("admintools.chat.login") && plugin.getConfig().getBoolean("config.on_player_join_message") && !player.isOp())
			for (Player p : Bukkit.getOnlinePlayers())
				if(p.isOnline() && player.hasPermission("admintools.chat.login"))
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', file.getLangConfig().getString("messages.events.admin_join")
																								.replace("%player%", player.getName())
																								.replace("%ip%", player.getAddress().getHostString())));

		if (player.hasPermission("admintools.gui.use") && player.hasPermission("admintools.gui")) {
			PlayerInventory inventory = player.getInventory();
			if(!inventory.contains(util.getBlock())) inventory.setItem(plugin.getConfig().getInt("config.slot"), util.getBlock());
		}
		
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		
		item.setType(Material.ENCHANTED_BOOK);
		List<String> lore = new ArrayList<String>();
		
		meta.setDisplayName("Server Stats");
		lore.add("Online: " + Bukkit.getServer().getOnlinePlayers().size());
		meta.setLore(lore);
		item.setItemMeta(meta);
		util.getInventory().setItem(19, item);
	}
}
