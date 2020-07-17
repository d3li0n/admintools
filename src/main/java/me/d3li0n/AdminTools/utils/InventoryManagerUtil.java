package me.d3li0n.AdminTools.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginDescriptionFile;

public class InventoryManagerUtil {
	private Inventory inventory;
	private final int INVENTORY_SIZE = 54;
	private PluginDescriptionFile plugin;
	
	public InventoryManagerUtil(PluginDescriptionFile plugin) {
		this.plugin = plugin;
		createInventory();
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void createInventory() {
		inventory = Bukkit.createInventory(null, INVENTORY_SIZE, "AdminTools " + getVersion());
	}
	
	public String getVersion() {
		return this.plugin.getVersion();
	}
}
