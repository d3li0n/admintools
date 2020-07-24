package me.d3li0n.AdminTools.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.d3li0n.AdminTools.Main;
import me.d3li0n.AdminTools.utils.InventoryManagerUtil;

public class PlayerBlockInteractListener implements Listener {
	private Main plugin;
	private InventoryManagerUtil inventory;
	
	public PlayerBlockInteractListener(Main plugin, InventoryManagerUtil inventory) {
		this.plugin = plugin;
		this.inventory = inventory;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerClickBlock(InventoryClickEvent event) {
		switch(event.getAction()) {
			case HOTBAR_SWAP: event.setCancelled(true); break;
			case PLACE_ALL: {
				if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
				if (event.getCurrentItem().getItemMeta().hasLore()) event.setCancelled(true);
				break;
			}
			default: System.out.println(event.getAction());
		}
		
		if (event.getInventory().equals(inventory.getInventory())) event.setCancelled(true);
	}
	
	@EventHandler
	public void onDragEvent(InventoryCreativeEvent event) {
		switch(event.getAction()) {
			case PLACE_ALL: {
				if (event.getCurrentItem().equals(inventory.getBlock())) event.setCancelled(true);
				System.out.println(event.getSlot());
				System.out.println(event.getInventory().getItem(34));
				break;
			} 
			default: System.out.println(event.getAction());
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			Player player = (Player) event.getPlayer();
			if (player.hasPermission("admintools.gui.use") && player.hasPermission("admintools.gui"))
				if (player.getInventory().getItemInMainHand().equals(inventory.getBlock()))
					player.openInventory(inventory.getInventory());
		}	 
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (event.getItemDrop().getItemStack().getItemMeta().hasLore()) {
			event.getItemDrop().remove();
			player.getInventory().setItem(plugin.getConfig().getInt("config.slot"), inventory.getBlock());
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
			player.getInventory().remove(inventory.getBlock());
			player.getInventory().setItem(plugin.getConfig().getInt("config.slot"), inventory.getBlock());
			if (player.getInventory().getItemInOffHand().isSimilar(inventory.getBlock())) player.getInventory().setItemInOffHand(null);
		}
	}
}
