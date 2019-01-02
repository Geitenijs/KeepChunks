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
    public void onChunkUnload(final ChunkUnloadEvent e) {
        final Chunk currentChunk = e.getChunk();
        final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#"
                + currentChunk.getWorld().getName();
        if (new HashSet<Object>(Utilities.data.getStringList("chunks")).contains(chunk)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(final WorldUnloadEvent e) {
        final List<String> chunks = Utilities.data.getStringList("chunks");
        final List<String> worlds = new ArrayList<>();
        for (final String chunk : chunks) {
            final String world = chunk.split("#")[2];
            worlds.add(world.toLowerCase());
        }
        if (worlds.contains(e.getWorld().getName().toLowerCase())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(final WorldLoadEvent e) {
        if (Utilities.config.getBoolean("chunkload.onworldload")) {
            final HashSet<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));
            for (final String chunk : chunks) {
                final String[] chunkCoordinates = chunk.split("#");
                final int x = Integer.parseInt(chunkCoordinates[0]);
                final int z = Integer.parseInt(chunkCoordinates[1]);
                final String world = chunkCoordinates[2];
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
                }
                try {
                    Main.plugin.getServer().getWorld(world).loadChunk(x, z);
                    if (Utilities.config.getBoolean("chunkload.force")) {
                        try {
                            Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                        } catch (NoSuchMethodError ex) {
                            Utilities.consoleMsgPrefixed("Your server version doesn't support force-loaded chunks. " + "Please use the latest build of 1.13.2 to use this functionality.");
                        }
                    }
                } catch (NullPointerException ex) {
                    if (Utilities.config.getBoolean("general.debug")) {
                        Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdateAvailable(PlayerJoinEvent e) {
        if ((e.getPlayer().hasPermission("keepchunks.notify.update")) && Utilities.config.getBoolean("updates.check") && Utilities.config.getBoolean("updates.notify")
                && Utilities.updateAvailable()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, () -> {
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fA new release of &a" + Strings.PLUGIN + "&f is available!");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fCurrent version: &a" + Strings.VERSION + "&f; New version: &a" + Utilities.updateVersion() + "&f.");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&fTo download the update, visit this website:");
                Utilities.msg(e.getPlayer(), Strings.GAMEPREFIX + "&a" + Strings.WEBSITE + "&f.");
            }, 90L);
        }
    }
}
