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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;

public class Utilities {

    public static FileConfiguration config;
    public static FileConfiguration data;
    private static File configFile = new File(Main.plugin.getDataFolder(), "config.yml");
    private static File dataFile = new File(Main.plugin.getDataFolder(), "data.yml");
    private static boolean updateAvailable;
    private static String updateVersion;
    public static HashSet<String> chunks;
    public static HashSet<String> chunkloadAll;

    static {
        config = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), "config.yml"));
        data = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), "data.yml"));
        chunks = new HashSet<>(Utilities.data.getStringList("chunks"));
        chunkloadAll = new HashSet<>();
    }

    static void pluginBanner() {
        consoleBanner("");
        consoleBanner("&2 _     _                  &8 _______ _                 _          ");
        consoleBanner("&2(_)   | |                 &8(_______) |               | |  &2v" + Strings.VERSION);
        consoleBanner("&2 _____| |_____ _____ ____ &8 _      | |__  _   _ ____ | |  _  ___ ");
        consoleBanner("&2|  _   _) ___ | ___ |  _ \\&8| |     |  _ \\| | | |  _ \\| |_/ )/___)");
        consoleBanner("&2| |  \\ \\| ____| ____| |_| &8| |_____| | | | |_| | | | |  _ (|___ |");
        consoleBanner("&2|_|   \\_)_____)_____)  __/&8 \\______)_| |_|____/|_| |_|_| \\_|___/ ");
        consoleBanner("&2                    |_|   &8                                      ");
        consoleBanner("");
    }

    static void errorBanner() {
        consoleBanner("");
        consoleBanner("&c _______ ______  ______ _______ ______  ");
        consoleBanner("&c(_______|_____ \\(_____ (_______|_____ \\ ");
        consoleBanner("&c _____   _____) )_____) )     _ _____) )");
        consoleBanner("&c|  ___) |  __  /|  __  / |   | |  __  / ");
        consoleBanner("&c| |_____| |  \\ \\| |  \\ \\ |___| | |  \\ \\ ");
        consoleBanner("&c|_______)_|   |_|_|   |_\\_____/|_|   |_|");
        consoleBanner("");
    }

    static void createConfigs() {
        config.options().header(Strings.ASCIILOGO
                + "Copyright © " + Strings.COPYRIGHT + " " + Strings.AUTHOR + ", all rights reserved." +
                "\nInformation & Support: " + Strings.WEBSITE
                + "\n\ngeneral:"
                + "\n  colourfulconsole: Console messages will be coloured when this is enabled."
                + "\n  debug: When set to true, the plugin will log more information to the console."
                + "\n  releaseallprotection: Do you want to restrict the 'release all' command to the console?"
                + "\nupdates:"
                + "\n  check: When enabled, the plugin will check for updates. No automatic downloads, just a subtle notification in the console."
                + "\n  notify: Would you like to get an in-game reminder of a new update? Requires permission 'keepchunks.notify.update'.");
        config.addDefault("general.colourfulconsole", true);
        config.addDefault("general.debug", false);
        config.addDefault("general.releaseallprotection", true);
        config.addDefault("updates.check", true);
        config.addDefault("updates.notify", true);
        config.set("chunkload.dynamic", null);
        config.set("chunkload.onstartup", null);
        config.set("chunkload.onworldload", null);
        config.set("chunkload.all", null);
        config.set("chunkload.force", null);
        config.set("chunkload", null);
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

    public static void loadChunks() {
        for (final String chunk : chunks) {
            final String[] chunkCoordinates = chunk.split("#");
            final int x = Integer.parseInt(chunkCoordinates[0]);
            final int z = Integer.parseInt(chunkCoordinates[1]);
            final String world = chunkCoordinates[2];
            if (config.getBoolean("general.debug")) {
                consoleMsg(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
            }
            try {
                Bukkit.getServer().getWorld(world).loadChunk(x, z);
                Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
            } catch (NullPointerException ex) {
                if (config.getBoolean("general.debug")) {
                    consoleMsg(Strings.DEBUGPREFIX + "World '" + world + "' doesn't exist, or isn't loaded in memory.");
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
        metrics.addCustomChart(new Metrics.SingleLineChart("loadedChunks", () -> chunks.size()));
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
        metrics.addCustomChart(new Metrics.SimplePie("colourfulConsoleEnabled", () -> config.getString("general.colourfulconsole")));
        metrics.addCustomChart(new Metrics.SimplePie("debugEnabled", () -> config.getString("general.debug")));
        metrics.addCustomChart(new Metrics.SimplePie("releaseallProtectionEnabled", () -> config.getString("general.releaseallprotection")));
        metrics.addCustomChart(new Metrics.SimplePie("updateCheckEnabled", () -> config.getString("updates.check")));
        metrics.addCustomChart(new Metrics.SimplePie("updateNotificationEnabled", () -> config.getString("updates.notify")));
    }

    static void done() {
        consoleMsg(Strings.PLUGIN + " v" + Strings.VERSION + " has been enabled");
    }

    private static void checkForUpdates() {
        if (config.getBoolean("updates.check")) {
            UpdateCheck
                    .of(Main.plugin)
                    .resourceId(Strings.RESOURCEID)
                    .handleResponse((versionResponse, version) -> {
                        switch (versionResponse) {
                            case FOUND_NEW:
                                consoleMsg("A new release of " + Strings.PLUGIN + ", v" + version + ", is available! You are still on v" + Strings.VERSION + ".");
                                consoleMsg("To download this update, head over to " + Strings.WEBSITE + "/updates in your browser.");
                                updateVersion = version;
                                updateAvailable = true;
                                break;
                            case LATEST:
                                consoleMsg("You are running the latest version.");
                                updateAvailable = false;
                                break;
                            case UNAVAILABLE:
                                consoleMsg("An error occurred while checking for updates.");
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
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
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
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + dataFile, ex);
        }
    }

    public static void msg(final CommandSender s, String msg) {
        if (s instanceof Player) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        } else {
            msg = ChatColor.translateAlternateColorCodes('&', Strings.INTERNALPREFIX + msg);
            if (!config.getBoolean("general.colourfulconsole")) {
                msg = ChatColor.stripColor(msg);
            }
        }
        s.sendMessage(msg);
    }

    public static void consoleMsg(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', Strings.INTERNALPREFIX + msg);
        if (!config.getBoolean("general.colourfulconsole")) {
            msg = ChatColor.stripColor(msg);
        }
        Bukkit.getServer().getConsoleSender().sendMessage(msg);
    }

    private static void consoleBanner(final String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
