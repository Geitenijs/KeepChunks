package com.geitenijs.keepchunks;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onEnable() {
        Main.plugin = this;
        Hooks.registerHooks();
        Utilities.createConfigs();
        Utilities.registerCommandsAndCompletions();
        Utilities.registerEvents();
        Utilities.loadChunks();
        Utilities.startTasks();
        Utilities.startMetrics();
        Utilities.pluginBanner();
        Utilities.done();
    }

    public void onDisable() {
        Utilities.reloadConfigFile();
        Utilities.saveConfigFile();
        Utilities.reloadDataFile();
        Utilities.saveDataFile();
        Utilities.stopTasks();
    }
}