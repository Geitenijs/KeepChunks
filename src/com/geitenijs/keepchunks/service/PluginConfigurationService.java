package com.geitenijs.keepchunks.service;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * System responsible for maintaining plugin configurations
 */
public final class PluginConfigurationService {
    private static PluginConfigurationService INSTANCE = null;
    private FileSystemService fileSystemService;
    public boolean debuggingEnabled; //Debug mode enabled?
    public boolean bannerEnabled; //Show start-up banner?
    public boolean releaseAllProtection; //Release all chunks command limited to console users?
    public boolean colorfulConsoleEnabled; //Use colorful text in console output?
    public boolean updateCheckingEnabled; //Check for a new plugin version?

    private PluginConfigurationService(){
        fileSystemService = fileSystemService.getInstance();
        loadPluginConfigurations();
    }

    public static PluginConfigurationService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PluginConfigurationService();
            return INSTANCE;
        }
        return INSTANCE;
    }

    /**
     * Reads the provided config file and updates plugin configs accordingly
     */
    private void loadPluginConfigurations(){
        final FileConfiguration configs = fileSystemService.getPluginConfigs();
        debuggingEnabled = configs.getBoolean("general.debug");
        bannerEnabled = configs.getBoolean("general.pluginbanner");
        releaseAllProtection = configs.getBoolean("general.releaseallprotection");
        colorfulConsoleEnabled = configs.getBoolean("general.colourfulconsole");
        updateCheckingEnabled = configs.getBoolean("updates.check");
    }

    /**
     * Reloads the plugin's configurations
     */
    public void reloadConfigs(){
        loadPluginConfigurations();
    }
}
