package com.geitenijs.keepchunks;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onEnable() {
        Main.plugin = this;
            Utilities.pluginBanner();
            Hooks.registerHooks();
            Utilities.createConfigs();
            Utilities.registerCommandsAndCompletions();
            Utilities.registerEvents();
            Utilities.loadChunks();
            Utilities.startSchedulers();
            Utilities.startMetrics();
            Utilities.done();
    }

    public void onDisable() {
        Utilities.reloadConfigFile();
        Utilities.saveConfigFile();
        Utilities.reloadDataFile();
        Utilities.saveDataFile();
        Utilities.stopSchedulers();
    }
}