package me.d3li0n.AdminTools;

import me.d3li0n.AdminTools.commands.*;
import me.d3li0n.AdminTools.utils.ReportInventoryManagerUtil;
import me.d3li0n.AdminTools.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.listeners.ChatListener;
import me.d3li0n.AdminTools.listeners.PlayerBlockInteractListener;
import me.d3li0n.AdminTools.listeners.PlayerListener;
import me.d3li0n.AdminTools.utils.FileManagerUtil;
import me.d3li0n.AdminTools.utils.PluginInventoryManagerUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static boolean CHAT_STATE = true;
	public static boolean CHAT_SLOW_STATE = true;
	public ReportInventoryManagerUtil reportUtil;

	private FileManagerUtil fileUtil;
	private PluginInventoryManagerUtil inventory;
	private final ChatManager manager = new ChatManager();

	@Override
	public void onEnable() {
		fileUtil = new FileManagerUtil(this);

		if (!fileUtil.validateConfigLang(getPluginLang())) {
			Bukkit.getLogger().severe("Config language was not found. Plugin is disabled");
			Bukkit.getPluginManager().disablePlugin(this);
		} else {

			/* Checking the version of plugin */
			if (!new UpdateChecker(this).isUpdated()) Bukkit.getLogger().severe("[AdminTools]: Current version seems to be out of date. Please update this plugin, as new versions may contain security fixes.");

			/* Opening and Reading Configuration Language File */
			fileUtil.readLangFile(getPluginLang());

			/* Creating Plugin's GUI Inventory */
			PluginDescriptionFile file = this.getDescription();
			inventory = new PluginInventoryManagerUtil(file, this);

			/* Creating Report GUI */
			reportUtil = new ReportInventoryManagerUtil();

			/* Registering Events */
			getServer().getPluginManager().registerEvents(new ChatListener(fileUtil, this.manager), this);
			getServer().getPluginManager().registerEvents(new PlayerBlockInteractListener(this, inventory), this);
			getServer().getPluginManager().registerEvents(new PlayerListener(this, inventory, fileUtil), this);

			/* Registering Plugin's Commands */
			registerCommands();
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		AdminChatCommands chatCommands = new AdminChatCommands(this.fileUtil, this.manager);
		getCommand("cc").setExecutor(chatCommands);
		getCommand("clearchat").setExecutor(chatCommands);
		getCommand("mutechat").setExecutor(chatCommands);
		getCommand("slowchat").setExecutor(chatCommands);
		getCommand("mute").setExecutor(chatCommands);
		getCommand("unmute").setExecutor(chatCommands);

		getCommand("ap").setExecutor(new AdminInterfaceCommands(fileUtil, inventory));

		getCommand("ban").setExecutor(new AdminPlayerCommands(fileUtil));
		getCommand("unban").setExecutor(new AdminPlayerCommands(fileUtil));
		getCommand("kick").setExecutor(new AdminPlayerCommands(fileUtil));

		getCommand("admins").setExecutor(new AdminGeneralCommands(fileUtil));

		getCommand("report").setExecutor(new PlayerCommands(fileUtil, reportUtil));
	}
	
	public String getPluginLang() {
		return this.getConfig().getString("config.lang");
	}
}