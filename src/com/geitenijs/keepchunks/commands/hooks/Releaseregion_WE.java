package com.geitenijs.keepchunks.commands.hooks;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Releaseregion_WE implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (s instanceof Player) {
            try {
                Player player = ((OfflinePlayer) s).getPlayer();
                BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                LocalSession session = WorldEdit.getInstance().getSessionManager().get(bPlayer);
                final Region sel = session.getSelection(bPlayer.getWorld());
                BlockVector3 max = sel.getMaximumPoint();
                BlockVector3 min = sel.getMinimumPoint();
                assert player != null;
                Location maxPoint = new Location(player.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
                Location minPoint = new Location(player.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
                final Chunk chunkMax = maxPoint.getChunk();
                final Chunk chunkMin = minPoint.getChunk();
                final int maxZ = chunkMax.getZ();
                final int maxX = chunkMax.getX();
                final int minX = chunkMin.getX();
                final int minZ = chunkMin.getZ();
                final String world = sel.getWorld().getName();
                Utilities.msg(s, "&fReleasing chunks between &9(" + minX + ", " + minZ + ")&f and &9(" + maxX + ", " + maxZ + ")&f in world &6'" + world + "'&f...");
                for (int x = minX; x <= maxX; ++x) {
                    for (int z = minZ; z <= maxZ; ++z) {
                        final String chunk = x + "#" + z + "#" + world;
                        if (!Utilities.chunks.contains(chunk)) {
                            if (Utilities.config.getBoolean("general.debug")) {
                                Utilities.consoleMsg(Strings.DEBUGPREFIX + "Chunk (" + x + "," + z + ") in world '" + world + "' isn't marked.");
                            }
                        } else {
                            if (Utilities.config.getBoolean("general.debug")) {
                                Utilities.consoleMsg(Strings.DEBUGPREFIX + "Releasing chunk (" + x + "," + z + ") in world '" + world + "'...");
                            }
                            Utilities.chunks.remove(chunk);
                            Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, false);
                        }
                    }
                }
                Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                Utilities.saveDataFile();
                Utilities.reloadDataFile();
                Utilities.msg(s, "&fReleased chunks between &9(" + minX + ", " + minZ + ")&f and &9(" + maxX + ", " + maxZ + ")&f in world &6'" + world + "'&f.");
            } catch (IncompleteRegionException e) {
                Utilities.msg(s, Strings.WEFIRST);
            }
        } else {
            Utilities.msg(s, Strings.ONLYPLAYER);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        Player player = (Player) s;
        Location loc = player.getLocation();
        if (args[1].equals("worldedit")) {
            return CommandWrapper.filterTabs(tabs, args);
        }
        if (args[1].equals("coords")) {
            try {
                Player wePlayer = ((OfflinePlayer) s).getPlayer();
                BukkitPlayer bPlayer = BukkitAdapter.adapt(wePlayer);
                LocalSession session = WorldEdit.getInstance().getSessionManager().get(bPlayer);
                final Region sel = session.getSelection(bPlayer.getWorld());
                BlockVector3 max = sel.getMaximumPoint();
                BlockVector3 min = sel.getMinimumPoint();
                assert wePlayer != null;
                Location maxPoint = new Location(wePlayer.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
                Location minPoint = new Location(wePlayer.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
                final Chunk chunkMax = maxPoint.getChunk();
                final Chunk chunkMin = minPoint.getChunk();
                final int maxZ = chunkMax.getZ();
                final int maxX = chunkMax.getX();
                final int minX = chunkMin.getX();
                final int minZ = chunkMin.getZ();
                final String world = sel.getWorld().getName();
                if (newArgs.length == 2) {
                    tabs.add(String.valueOf(minX));
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(minZ));
                }
                if (newArgs.length == 4) {
                    tabs.add(String.valueOf(maxX));
                }
                if (newArgs.length == 5) {
                    tabs.add(String.valueOf(maxZ));
                }
                if (newArgs.length == 6) {
                    tabs.add(world);
                }
            } catch (IncompleteRegionException e) {
                if (newArgs.length == 2) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
                if (newArgs.length == 4) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                }
                if (newArgs.length == 5) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
                if (newArgs.length == 6) {
                    tabs.add(loc.getWorld().getName());
                }
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}