package com.geitenijs.keepchunks;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Events implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent e) {
        final Chunk currentChunk = e.getChunk();
        final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#"
                + currentChunk.getWorld().getName();
        if (new HashSet<>(Utilities.chunks).contains(chunk)) {
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + "Chunk (" + currentChunk.getX() + "," + currentChunk.getZ() + ") in world '" + currentChunk.getWorld().getName() + "' is unloading, while it should be force-loaded.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent e) {
        final List<String> worlds = new ArrayList<>();
        for (final String chunk : Utilities.chunks) {
            final String world = chunk.split("#")[2];
            worlds.add(world.toLowerCase());
        }
        if (worlds.contains(e.getWorld().getName().toLowerCase())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldLoadEvent e) {
        for (final String chunk : Utilities.chunks) {
            final String[] chunkCoordinates = chunk.split("#");
            final int x = Integer.parseInt(chunkCoordinates[0]);
            final int z = Integer.parseInt(chunkCoordinates[1]);
            final String world = chunkCoordinates[2];
            if (Utilities.config.getBoolean("general.debug")) {
                Utilities.consoleMsg(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
            }
            try {
                Bukkit.getServer().getWorld(world).loadChunk(x, z);
                Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
            } catch (NullPointerException ex) {
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsg(Strings.DEBUGPREFIX + "World '" + world + "' doesn't exist, or isn't loaded in memory.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdateAvailable(PlayerJoinEvent e) {
        if ((e.getPlayer().hasPermission("keepchunks.notify.update")) && Utilities.config.getBoolean("updates.check") && Utilities.config.getBoolean("updates.notify") && Utilities.updateAvailable()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fA new release of &a" + Strings.PLUGIN + "&f is available!");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fCurrent version: &a" + Strings.VERSION + "&f; New version: &a" + Utilities.updateVersion() + "&f.");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fTo download the update, visit this website:");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&a" + Strings.WEBSITE + "&f.");
            }, 90L);
        }
    }
}
