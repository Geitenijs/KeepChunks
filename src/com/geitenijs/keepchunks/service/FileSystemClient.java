package com.geitenijs.keepchunks.service;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.geitenijs.keepchunks.Strings.*;

/**
 * Provides controlled access to any/all files created by the KeepChunks plugin
 * @apiNote This class should only be visible to other services in the current package. You should be very mindful about who can use this class and why
 * @implNote The files created/maintained are found at "/plugins/KeepChunks"
 */
final class FileSystemClient {
    private static FileSystemClient INSTANCE = null;
    private static final String FILE_NAME_DATA = "data.yml";
    private static final String FILE_NAME_CONFIGS = "config.yml";

    /**
     * Enumerates the type of files that this plugin manages
     * @apiNote These are mostly used as a specifier when calling the universal file save/reload methods
     */
    protected enum FileType {
        DATA,
        CONFIGS
    }

    //Java I/O File instances
    private File configFile = new File(Main.plugin.getDataFolder(), FILE_NAME_CONFIGS);
    private File dataFile = new File(Main.plugin.getDataFolder(), FILE_NAME_DATA);

    //Bukkit Wrappers for Java's File instances
    private FileConfiguration configs = YamlConfiguration.loadConfiguration(configFile);
    private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
    private FileSystemClient(){

    }
    public static FileSystemClient getInstance(){
        if(INSTANCE == null)
            INSTANCE = new FileSystemClient();
        return INSTANCE;
    }
    public FileConfiguration getPluginConfigs(){
        return configs;
    }
    public FileConfiguration getPluginData(){
        return data;
    }

    /**
     * TODO: Make this more generic
     * @param f
     * @param attributeName
     * @return
     */
    public List<String> getFileAttributes(final FileType f, final String attributeName){
        FileConfiguration fileToRead = switch (f){
            case CONFIGS -> configs;
            case DATA -> data;
        };
        return fileToRead.getStringList(attributeName);
    }

    /**
     * Saves the file of specified {@link FileType} to the disk
     * @param f
     * @throws RuntimeException If any I/O issues occurred, meaning the save failed
     */
    public void saveFile(FileType f){
        try {
            switch (f) {
                case CONFIGS -> configs.save(configFile);
                case DATA -> data.save(dataFile);
            }
        }catch (IOException e){
            throw new RuntimeException("Failed to save {} file".formatted(f), e);
        }
    }
    public void reloadFile(FileType f){
        switch (f) {
            case CONFIGS -> configs = YamlConfiguration.loadConfiguration(configFile);
            case DATA -> data = YamlConfiguration.loadConfiguration(dataFile);
        }
    }

    private void createDefaultDataFile(){
        data.options().header(ASCIILOGO
                + "Keeping your chunks loaded since 2015! By " + AUTHOR +
                "\nInformation & Support: " + WEBSITE
                + "\n\nUnless you know what you're doing, it's best not to touch this file. All configurable options can be found in config.yml");
        data.addDefault("chunks", new ArrayList<>());
        data.options().copyHeader(true);
        data.options().copyDefaults(true);
    }
    private void createDefaultConfigFile(){
        configs.options().header(ASCIILOGO
                + "Keeping your chunks loaded since 2015! By " + AUTHOR +
                "\nInformation & Support: " + WEBSITE
                + "\n\ngeneral:"
                + "\n  pluginbanner: Whether or not to display the fancy banner in your console on server startup."
                + "\n  colourfulconsole: Console messages will be coloured when this is enabled."
                + "\n  debug: When set to true, the plugin will log more information to the console."
                + "\n  releaseallprotection: Do you want to restrict the 'release all' command to the console?"
                + "\nupdates:"
                + "\n  check: When enabled, the plugin will check for updates. No automatic downloads, just a subtle notification in the console."
                + "\n  notify: Would you like to get an in-game reminder of a new update? Requires permission 'keepchunks.notify.update'.");
        configs.addDefault("general.pluginbanner", true);
        configs.addDefault("general.colourfulconsole", true);
        configs.addDefault("general.debug", false);
        configs.addDefault("general.releaseallprotection", true);
        configs.addDefault("updates.check", true);
        configs.addDefault("updates.notify", true);
        configs.options().copyHeader(true);
        configs.options().copyDefaults(true);
    }
}
