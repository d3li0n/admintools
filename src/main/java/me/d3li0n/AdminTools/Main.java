package me.d3li0n.AdminTools;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.d3li0n.AdminTools.commands.AdminChatCommands;
import me.d3li0n.AdminTools.helpers.ChatManager;
import me.d3li0n.AdminTools.listeners.ChatListener;
import me.d3li0n.AdminTools.utils.FileManagerUtil;

public class Main extends JavaPlugin {
	public static boolean CHAT_STATE = true;
	public static boolean CHAT_SLOW_STATE = true;
	
	private FileManagerUtil fileUtil;
	private ChatManager manager;
	
	@Override
	public void onEnable() {
		fileUtil =  new FileManagerUtil(this);
		
		if(!fileUtil.validateConfigLang(getPluginLang())) {
			Bukkit.getLogger().severe(("Config language was not found. Plugin is disabled"));
			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			fileUtil.readLangFile(getPluginLang());
			
			manager = new ChatManager();
			
			getServer().getPluginManager().registerEvents(new ChatListener(this, fileUtil, manager), this);
			registerCommands();
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void registerCommands() {
		getCommand("cc").setExecutor(new AdminChatCommands(fileUtil));
		getCommand("clearchat").setExecutor(new AdminChatCommands(fileUtil));
		getCommand("mutechat").setExecutor(new AdminChatCommands(fileUtil));
		getCommand("slowchat").setExecutor(new AdminChatCommands(fileUtil));
	}
	
	public String getPluginLang() {
		return this.getConfig().getString("config.lang");
	}
}