package com.geitenijs.keepchunks.service;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

/**
 * System responsible for saving/loading/serving the configuration and data files to other services in this plugin
 */
public final class FileSystemService {
    private static FileSystemService INSTANCE = null;
    private File configFile = new File(Main.plugin.getDataFolder(), "config.yml");
    private File dataFile = new File(Main.plugin.getDataFolder(), "data.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
    private FileSystemService(){}
    public FileConfiguration getPluginConfigs(){
        return config;
    }
    public FileConfiguration getPluginData(){
        return data;
    }
    public static FileSystemService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new FileSystemService();
            return INSTANCE;
        }
        return INSTANCE;
    }
    public void saveDataFile(){
        data.save(dataFile);
        //TODO: Add graceful shutdown logic
    }
    public void refreshDataFile(){
        data = YamlConfiguration.loadConfiguration(dataFile);
    }
    public void saveConfigFile(){
        config.save(configFile);
        //TODO: Add graceful shutdown logic
    }

    public void refreshConfigFile() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void createDefaultDataFile(){
        data.options().header(Strings.ASCIILOGO
                + "Keeping your chunks loaded since 2015! By " + Strings.AUTHOR +
                "\nInformation & Support: " + Strings.WEBSITE
                + "\n\nUnless you know what you're doing, it's best not to touch this file. All configurable options can be found in config.yml");
        data.addDefault("chunks", new ArrayList<>());
        data.options().copyHeader(true);
        data.options().copyDefaults(true);
    }
    private void createDefaultConfigFile(){
        config.options().header(Strings.ASCIILOGO
                + "Keeping your chunks loaded since 2015! By " + Strings.AUTHOR +
                "\nInformation & Support: " + Strings.WEBSITE
                + "\n\ngeneral:"
                + "\n  pluginbanner: Whether or not to display the fancy banner in your console on server startup."
                + "\n  colourfulconsole: Console messages will be coloured when this is enabled."
                + "\n  debug: When set to true, the plugin will log more information to the console."
                + "\n  releaseallprotection: Do you want to restrict the 'release all' command to the console?"
                + "\nupdates:"
                + "\n  check: When enabled, the plugin will check for updates. No automatic downloads, just a subtle notification in the console."
                + "\n  notify: Would you like to get an in-game reminder of a new update? Requires permission 'keepchunks.notify.update'.");
        config.addDefault("general.pluginbanner", true);
        config.addDefault("general.colourfulconsole", true);
        config.addDefault("general.debug", false);
        config.addDefault("general.releaseallprotection", true);
        config.addDefault("updates.check", true);
        config.addDefault("updates.notify", true);
        config.options().copyHeader(true);
        config.options().copyDefaults(true);
    }
}
