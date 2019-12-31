package com.geitenijs.keepchunks;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Hooks {

    public static boolean WorldEdit;
    public static boolean WorldGuard;
    public static boolean incompatibleWorldEdit;
    public static boolean incompatibleWorldGuard;

    public static void registerHooks() {
        hookWorldEdit();
        hookWorldGuard();
    }

    private static void hookWorldEdit() {
        final Plugin wePlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (!(wePlugin instanceof WorldEditPlugin)) {
            WorldEdit = false;
            incompatibleWorldEdit = false;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WE_MISSING);
            }
            return;
        }
        String exactWorldEditVersion = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit").getDescription().getVersion();
        int worldEditVersion;
        try {
            worldEditVersion = Integer.parseInt(exactWorldEditVersion.replace(".", "").substring(0, 3));
        } catch (Exception ex) {
            WorldEdit = false;
            incompatibleWorldEdit = true;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WE_INCOMPATIBLE);
            }
            return;
        }
        if (worldEditVersion >= 700) {
            WorldEdit = true;
            incompatibleWorldEdit = false;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WE_COMPATIBLE);
            }
        } else {
            WorldEdit = false;
            incompatibleWorldEdit = true;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WE_INCOMPATIBLE);
            }
        }
    }

    private static void hookWorldGuard() {
        final Plugin wgPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (!(wgPlugin instanceof WorldGuardPlugin)) {
            WorldGuard = false;
            incompatibleWorldGuard = false;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WG_MISSING);
            }
            return;
        }
        String exactWorldGuardVersion = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
        int worldGuardVersion;
        try {
            worldGuardVersion = Integer.parseInt(exactWorldGuardVersion.replace(".", "").substring(0, 3));
        } catch (Exception ex) {
            WorldGuard = false;
            incompatibleWorldGuard = true;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WG_INCOMPATIBLE);
            }
            return;
        }
        if (worldGuardVersion >= 700) {
            WorldGuard = true;
            incompatibleWorldGuard = false;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WG_COMPATIBLE);
            }
        } else {
            WorldGuard = false;
            incompatibleWorldGuard = true;
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + Strings.DEPENDENCIES_WG_INCOMPATIBLE);
            }
        }
    }
}

