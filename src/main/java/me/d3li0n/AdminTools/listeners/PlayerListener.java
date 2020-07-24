package me.d3li0n.AdminTools.listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import me.d3li0n.AdminTools.utils.InventoryManagerUtil;

public class PlayerListener implements Listener {

	private Main plugin;
	private InventoryManagerUtil util;
	
	public PlayerListener(Main main, InventoryManagerUtil util) {
		this.plugin = main;
		this.util = util;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
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
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.KICK_BANNED) {
			String expiration = "";
			
			if (Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration() == null)
				expiration = "Forever";
			else {
				SimpleDateFormat form = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				expiration = form.format(Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getExpiration());
			}
            event.setKickMessage(ChatColor.RED + "You are " + ChatColor.DARK_RED + "banned" + ChatColor.RED + " from the Dev Server.\n\n" + 
                      "Reason: " + ChatColor.GRAY + Bukkit.getBanList(Type.NAME).getBanEntry(event.getPlayer().getName()).getReason() +
                      ChatColor.RED + "\n Unban Time: " + ChatColor.GRAY + expiration + ".\n\nIf you think it was a mistake, fill out an appeal at " + ChatColor.GOLD + "\nhttps://example.com");
        }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(player.hasPermission("admintools.gui.use") && player.hasPermission("admintools.gui")) {
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
