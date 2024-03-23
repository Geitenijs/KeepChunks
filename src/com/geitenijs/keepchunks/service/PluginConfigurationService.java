package com.geitenijs.keepchunks.service;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Responsible for providing the plugin's current configuration values/state and ways to reload their latest values from the file system
 * @implNote Session-level configurations are also stored here, and are not persisted  (e.g: Checking WorldEdit/WorldGuard support)
 */
@Getter
public final class PluginConfigurationService {
    private static PluginConfigurationService INSTANCE = null;

    private FileSystemClient fileSystem = FileSystemClient.getInstance();

    //Does the server have support for the WorldEdit and/or WorldGuard plugin?
    public boolean worldEditSupported = false;
    public boolean worldGuardSupported = false;
    public boolean debuggingEnabled; //Debug mode enabled?
    public boolean bannerEnabled; //Show start-up banner?
    public boolean releaseAllProtection; //Limit releasing all chunks command to server console users?
    public boolean colorfulConsoleEnabled; //Use colorful text in the in-game console?
    public boolean updateCheckingEnabled; //Passively check for a new plugin version?

    private PluginConfigurationService(){
        parseValues();
    }

    public static PluginConfigurationService getInstance(){
        if(INSTANCE == null)
            INSTANCE = new PluginConfigurationService();
        return INSTANCE;
    }

    /**
     * Parses the provided configuration file, updating each convar
     */
    private void parseValues(){
        FileConfiguration configs = fileSystem.getPluginConfigs();
        fileSystem.reloadFile(FileSystemClient.FileType.CONFIGS);
        debuggingEnabled = configs.getBoolean("general.debug");
        bannerEnabled = configs.getBoolean("general.pluginbanner");
        releaseAllProtection = configs.getBoolean("general.releaseallprotection");
        colorfulConsoleEnabled = configs.getBoolean("general.colourfulconsole");
        updateCheckingEnabled = configs.getBoolean("updates.check");
    }

    /**
     * Updates all convars to reflect the latest version of the plugin configuration file on the disk
     */
    public void reloadConfigs(){
        fileSystem.reloadFile(FileSystemClient.FileType.CONFIGS);
        parseValues();
    }
}
