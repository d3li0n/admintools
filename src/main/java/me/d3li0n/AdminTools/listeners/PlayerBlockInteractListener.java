package me.d3li0n.AdminTools.listeners;

import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.utils.InventoryManagerUtil;

public class PlayerBlockInteractListener implements Listener {
	private Main plugin;
	private InventoryManagerUtil inventory;
	
	public PlayerBlockInteractListener(Main plugin, InventoryManagerUtil inventory) {
		this.plugin = plugin;
		this.inventory = inventory;
	}
	
	@EventHandler
	public void onPlayerClickBlock(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;
		
		if(event.getClick().isLeftClick() || event.getClick().isRightClick()) {
			if(event.getCurrentItem().equals(getBlock())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Player player = (Player) event.getPlayer();
			if(player.hasPermission("admintools.gui.use") && player.hasPermission("admintools.gui"))
				if(player.getInventory().getItemInMainHand().equals(getBlock()))
					player.openInventory(inventory.getInventory());
		}	 
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (event.getItemDrop().getItemStack().getItemMeta().hasLore()) {
			event.getItemDrop().remove();
			PlayerInventory inventory = player.getInventory();
			inventory.setItem(plugin.getConfig().getInt("config.slot"), getBlock());
			player.updateInventory();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player entity = event.getEntity();
			Player player = entity.getPlayer();
			if (player.hasPermission("admintools.gui")) {
				event.setKeepInventory(true);
				event.getDrops().clear();
			}
		}
	}
	
	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if((player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasLore())
				|| (player.getInventory().getItemInOffHand().hasItemMeta() && player.getInventory().getItemInOffHand().getItemMeta().hasLore())) {
			event.setCancelled(true);
			player.getInventory().remove(getBlock());
			player.getInventory().setItem(plugin.getConfig().getInt("config.slot"), getBlock());
			if (player.getInventory().getItemInOffHand().isSimilar(getBlock())) { 
				player.getInventory().setItemInOffHand(null);
			}
		}
	}
	
	public ItemStack getBlock() {
		ItemStack item = new ItemStack(Material.BEDROCK, 1);
		ItemMeta meta = item.getItemMeta();
		
		meta.addEnchant(Enchantment.WATER_WORKER, 70, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("config.description")));

		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("config.title")));
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(player.hasPermission("admintools.gui.use") && player.hasPermission("admintools.gui")) {
			PlayerInventory inventory = player.getInventory();
			if(!inventory.contains(getBlock())) inventory.setItem(plugin.getConfig().getInt("config.slot"), getBlock());
		}
	}
}
