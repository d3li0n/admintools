package me.d3li0n.AdminTools.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportInventoryManagerUtil {
    /* TODO: finish later */
    private Inventory reportInventory;
    private final int INVENTORY_SIZE = 9;

    public ReportInventoryManagerUtil() {
        init();
    }

    public void init () {
        reportInventory = Bukkit.createInventory(null, INVENTORY_SIZE, "Report GUI");

        ItemStack item = new ItemStack(Material.WHITE_WOOL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Bullying");

        item.setItemMeta(meta);
        reportInventory.setItem(1, item);
    }

    public Inventory getReportInventory () {
        return reportInventory;
    }
}
