package com.geitenijs.keepchunks;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.updatechecker.UpdateCheck;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class Utilities {

    public static FileConfiguration config;
    public static FileConfiguration data;
    private static File configFile = new File(Main.plugin.getDataFolder(), "config.yml");
    private static File dataFile = new File(Main.plugin.getDataFolder(), "data.yml");
    private static boolean updateAvailable;
    private static String updateVersion;

    static {
        config = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), "config.yml"));
        data = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), "data.yml"));
    }

    static void startupText() {
        consoleMsg("");
        consoleMsg("§2 _     _                  §8 _______ _                 _          ");
        consoleMsg("§2(_)   | |                 §8(_______) |               | |  §2v" + Strings.VERSION);
        consoleMsg("§2 _____| |_____ _____ ____ §8 _      | |__  _   _ ____ | |  _  ___ ");
        consoleMsg("§2|  _   _) ___ | ___ |  _ \\§8| |     |  _ \\| | | |  _ \\| |_/ )/___)");
        consoleMsg("§2| |  \\ \\| ____| ____| |_| §8| |_____| | | | |_| | | | |  _ (|___ |");
        consoleMsg("§2|_|   \\_)_____)_____)  __/§8 \\______)_| |_|____/|_| |_|_| \\_|___/ ");
        consoleMsg("§2                    |_|   §8                                      ");
        consoleMsg("");
    }

    static void errorText() {
        consoleMsg("");
        consoleMsg("§c _______ ______  ______ _______ ______  ");
        consoleMsg("§c(_______|_____ \\(_____ (_______|_____ \\ ");
        consoleMsg("§c _____   _____) )_____) )     _ _____) )");
        consoleMsg("§c|  ___) |  __  /|  __  / |   | |  __  / ");
        consoleMsg("§c| |_____| |  \\ \\| |  \\ \\ |___| | |  \\ \\ ");
        consoleMsg("§c|_______)_|   |_|_|   |_\\_____/|_|   |_|");
        consoleMsg("");
    }

    static void createConfigs() {
        config.options().header(Strings.ASCIILOGO
                + "Copyright © " + Strings.COPYRIGHT + " " + Strings.AUTHOR + ", all rights reserved." +
                "\nInformation & Support: " + Strings.WEBSITE
                + "\n\ngeneral:"
                + "\n  debug: When set to true, the plugin will log more information to the console."
                + "\n  releaseallprotection: Do you want to restrict the 'release all' command to the console?"
                + "\nupdates:"
                + "\n  check: When set to true, the plugin will check for updates. No automatic downloads, just a subtle notification in the console."
                + "\n  notify: Do you want to get an in-game reminder of a new update? Requires permission 'keepchunks.notify.update'."
                + "\nchunkload:"
                + "\n  force: Forcefully load chunks. (Requires the latest build of 1.13.2)"
                + "\n  dynamic: Enable to automatically load newly marked chunks."
                + "\n  onstartup: Enable to load all marked chunks on server startup."
                + "\n  onworldload: Enable to load all marked chunks in a world, once the world is loaded in memory.");
        config.addDefault("general.debug", false);
        config.addDefault("general.releaseallprotection", true);
        config.addDefault("updates.check", true);
        config.addDefault("updates.notify", true);
        config.addDefault("chunkload.force", false);
        config.addDefault("chunkload.dynamic", true);
        config.addDefault("chunkload.onstartup", true);
        config.addDefault("chunkload.onworldload", true);
        data.options().header(Strings.ASCIILOGO
                + "Copyright © " + Strings.COPYRIGHT + " " + Strings.AUTHOR + ", all rights reserved." +
                "\nInformation & Support: " + Strings.WEBSITE
                + "\n\nUnless you know what you're doing, it's best not to touch this file. All configurable options can be found in config.yml");
        data.addDefault("chunks", new ArrayList<>());
        config.options().copyHeader(true);
        config.options().copyDefaults(true);
        data.options().copyHeader(true);
        data.options().copyDefaults(true);
        saveConfigFile();
        reloadConfigFile();
        saveDataFile();
        reloadDataFile();
    }

    static void registerCommandsAndCompletions() {
        Main.plugin.getCommand("keepchunks").setExecutor(new CommandWrapper());
        Main.plugin.getCommand("kc").setExecutor(new CommandWrapper());
        Main.plugin.getCommand("keepchunks").setTabCompleter(new CommandWrapper());
        Main.plugin.getCommand("kc").setTabCompleter(new CommandWrapper());
    }

    static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Events(), Main.plugin);
    }

    static void loadChunks() {
        if (config.getBoolean("chunkload.onstartup")) {
            final Set<String> chunks = new HashSet<>(data.getStringList("chunks"));
            for (final String chunk : chunks) {
                final String[] chunkCoordinates = chunk.split("#");
                final int x = Integer.parseInt(chunkCoordinates[0]);
                final int z = Integer.parseInt(chunkCoordinates[1]);
                final String world = chunkCoordinates[2];
                if (config.getBoolean("general.debug")) {
                    consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
                }
                try {
                    Main.plugin.getServer().getWorld(world).loadChunk(x, z);
                    if (config.getBoolean("chunkload.force")) {
                        try {
                            Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                        } catch (NoSuchMethodError ex) {
                            consoleMsgPrefixed("Your server version doesn't support force-loaded chunks. " + "Please use the latest build of 1.13.2 to use this functionality.");
                        }
                    }
                } catch (NullPointerException ex) {
                    if (config.getBoolean("general.debug")) {
                        consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
                    }
                }
            }
        }
    }

    static void startSchedulers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, Utilities::checkForUpdates, 190L, 216000L);
    }

    static void stopSchedulers() {
        Bukkit.getScheduler().cancelTasks(Main.plugin);
    }

    static void startMetrics() {
        Metrics metrics = new Metrics(Main.plugin);
        metrics.addCustomChart(new Metrics.SingleLineChart("loadedChunks", () -> data.getStringList("chunks").size()));
        metrics.addCustomChart(new Metrics.SimplePie("worldeditVersion", () -> {
            final Plugin p = Bukkit.getPluginManager().getPlugin("WorldEdit");
            if (!(p instanceof WorldEditPlugin)) {
                return Strings.NOSTAT;
            }
            return Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();
        }));
        metrics.addCustomChart(new Metrics.SimplePie("worldguardVersion", () -> {
            final Plugin p = Bukkit.getPluginManager().getPlugin("WorldGuard");
            if (!(p instanceof WorldGuardPlugin)) {
                return Strings.NOSTAT;
            }
            return Bukkit.getServer().getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        }));
        metrics.addCustomChart(new Metrics.SimplePie("debugEnabled", () -> config.getString("general.debug")));
        metrics.addCustomChart(new Metrics.SimplePie("releaseallProtectionEnabled", () -> config.getString("general.releaseallprotection")));
        metrics.addCustomChart(new Metrics.SimplePie("updateCheckEnabled", () -> config.getString("updates.check")));
        metrics.addCustomChart(new Metrics.SimplePie("updateNotificationEnabled", () -> config.getString("updates.notify")));
        metrics.addCustomChart(new Metrics.SimplePie("chunkloadForceEnabled", () -> config.getString("chunkload.force")));
        metrics.addCustomChart(new Metrics.SimplePie("chunkloadDynamicEnabled", () -> config.getString("chunkload.dynamic")));
        metrics.addCustomChart(new Metrics.SimplePie("chunkloadOnStartupEnabled", () -> config.getString("chunkload.onstartup")));
        metrics.addCustomChart(new Metrics.SimplePie("chunkloadOnWorldloadEnabled", () -> config.getString("chunkload.onworldload")));
    }

    static void done() {
        consoleMsgPrefixed(Strings.PLUGIN + " v" + Strings.VERSION + " has been enabled");
    }

    private static void checkForUpdates() {
        if (config.getBoolean("updates.check")) {
            UpdateCheck
                    .of(Main.plugin)
                    .resourceId(Strings.RESOURCEID)
                    .handleResponse((versionResponse, version) -> {
                        switch (versionResponse) {
                            case FOUND_NEW:
                                consoleMsgPrefixed("A new release of " + Strings.PLUGIN + ", v" + version + ", is available! You are still on v" + Strings.VERSION + ".");
                                consoleMsgPrefixed("To download this update, head over to " + Strings.WEBSITE + "/updates in your browser.");
                                updateVersion = version;
                                updateAvailable = true;
                                break;
                            case LATEST:
                                consoleMsgPrefixed("You are running the latest version.");
                                updateAvailable = false;
                                break;
                            case UNAVAILABLE:
                                consoleMsgPrefixed("An error occurred while checking for updates.");
                                updateAvailable = false;
                        }
                    }).check();
        }
    }

    static boolean updateAvailable() {
        return updateAvailable;
    }

    static String updateVersion() {
        return updateVersion;
    }

    public static void reloadConfigFile() {
        if (configFile == null) {
            configFile = new File(Main.plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    static void saveConfigFile() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Main.plugin.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    public static void reloadDataFile() {
        if (dataFile == null) {
            dataFile = new File(Main.plugin.getDataFolder(), "data.yml");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public static void saveDataFile() {
        if (data == null || dataFile == null) {
            return;
        }
        try {
            data.save(dataFile);
        } catch (IOException ex) {
            Main.plugin.getLogger().log(Level.SEVERE, "Could not save " + dataFile, ex);
        }
    }

    public static void msg(final CommandSender s, final String message) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private static void consoleMsg(final String message) {
        Main.plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void consoleMsgPrefixed(final String message) {
        Main.plugin.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', Strings.INTERNALPREFIX + message));
    }
}
