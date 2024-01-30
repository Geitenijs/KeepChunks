package com.geitenijs.keepchunks;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Hooks {

    public static boolean WorldEdit;
    public static boolean WorldGuard;

    public static void registerHooks() {
        hookWorldEdit();
        hookWorldGuard();
    }

    private static void hookWorldEdit() {
        final Plugin wePlugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        WorldEdit = wePlugin instanceof WorldEditPlugin;
    }

    private static void hookWorldGuard() {
        final Plugin wgPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        WorldGuard = wgPlugin instanceof WorldGuardPlugin;
    }
}

