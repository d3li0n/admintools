package me.d3li0n.AdminTools.utils;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;

import me.d3li0n.AdminTools.Main;

public class PluginInventoryManagerUtil {
	private Inventory inventory;
	private final int INVENTORY_SIZE = 54;
	private PluginDescriptionFile plugin;
	private final Main main;
	
	public PluginInventoryManagerUtil(PluginDescriptionFile plugin, Main main) {
		this.plugin = plugin;
		this.main = main;
		createInventory();
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}

	public Inventory getReportInventory() { return this.main.reportUtil.getReportInventory(); }

	public void createInventory() {
		inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "AdminTools " + getVersion());
		
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName("Plugin information");
		List<String> lore = new ArrayList<String>();
		lore.add("Author: " + this.plugin.getAuthors().get(0));
		lore.add("Version: " + getVersion());
		meta.setLore(lore);
		item.setItemMeta(meta);
		inventory.setItem(13, item);
		
		item.setType(Material.ENCHANTED_BOOK);
		meta.setDisplayName("Server Stats");
		lore.add("Online: " + Bukkit.getServer().getOnlinePlayers().size());
		meta.setLore(lore);
		item.setItemMeta(meta);
		inventory.setItem(19, item);
	}
	
	public ItemStack getBlock() {
		ItemStack item = new ItemStack(Material.BEDROCK, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.addEnchant(Enchantment.WATER_WORKER, 70, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("config.description")));

		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("config.title")));
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public String getVersion() {
		return this.plugin.getVersion();
	}
}
