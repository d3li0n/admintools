package me.d3li0n.AdminTools.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class FileManagerUtil {
	private final String[] language = {	"en", "ru" };
	private FileConfiguration langConfig;
	
	JavaPlugin plugin;
	
	public FileManagerUtil(JavaPlugin plugin) {
		this.plugin = plugin;
		
		createDefaultConfigFile("config.yml");
		createDefaultLanguageDirectory();
	}
	
	public FileConfiguration getLangConfig() {
        return this.langConfig;
    }
	
	public void readLangFile(String file) {
		File langFile = new File(plugin.getDataFolder(), "/languages/" + file + ".yml");
		this.langConfig = new YamlConfiguration();
		try {
            this.langConfig.load(langFile);
        } catch (InvalidConfigurationException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createDefaultConfigFile(String resource) {
		File folder = plugin.getDataFolder();
		
		if(!folder.exists()) folder.mkdir();
		
		File file = new File(folder, resource);
		if(!file.exists()) plugin.saveResource(resource, false);
	}
	
	public boolean validateConfigLang(String lang) {
		File file = new File(plugin.getDataFolder() + "/languages/" + lang + ".yml");
		return file.exists();
	}
	
	public void createDefaultLanguageDirectory() {
		File folder = new File(plugin.getDataFolder() + "/languages/");
		if(!folder.exists()) folder.mkdir();
		
		for(int i = 0; i < language.length; i++) {
			File file = new File(plugin.getDataFolder() + "/languages/" + language[i] + ".yml");
			if(!file.exists()) {
				try {
					plugin.saveResource(language[i] + ".yml", false);
					Files.move(Paths.get(plugin.getDataFolder() + "/" + language[i] + ".yml"),  
								Paths.get(plugin.getDataFolder() + "/languages/" + language[i] + ".yml")); 
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}