package com.geitenijs.keepchunks.commands.hooks;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Keepregion_WG implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

        final Set<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));

        if (args[1].equalsIgnoreCase("worldguard")) {
            final String region = args[2];
            final String world = args[3];
            if (Bukkit.getWorld(world) == null) {
                Utilities.msg(s,
                        "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
            } else {
                World realWorld = Bukkit.getWorld(world);
                com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(realWorld);
                RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);
                assert manager != null;
                if (manager.getRegion(region) == null) {
                    Utilities.msg(s, "&cRegion &f'" + region + "'&c doesn't exist, or is invalid.");
                } else {
                    BlockVector3 max = Objects.requireNonNull(manager.getRegion(region)).getMaximumPoint();
                    BlockVector3 min = Objects.requireNonNull(manager.getRegion(region)).getMinimumPoint();
                    Location maxPoint = new Location(realWorld, max.getBlockX(), max.getBlockY(),
                            max.getBlockZ());
                    Location minPoint = new Location(realWorld, min.getBlockX(), min.getBlockY(),
                            min.getBlockZ());
                    final Chunk chunkMax = maxPoint.getChunk();
                    final Chunk chunkMin = minPoint.getChunk();
                    final int maxZ = chunkMax.getZ();
                    final int maxX = chunkMax.getX();
                    final int minX = chunkMin.getX();
                    final int minZ = chunkMin.getZ();
                    for (int x = minX; x <= maxX; ++x) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            final String chunk = x + "#" + z + "#" + world;
                            if (chunks.contains(chunk)) {
                                Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world
                                        + "'&c is already marked.");
                            } else {
                                chunks.add(chunk);
                                Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world
                                        + "'&f.");
                                if (Utilities.config.getBoolean("chunkload.dynamic")) {
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
                    }
                    Utilities.data.set("chunks", new ArrayList<Object>(chunks));
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                }
            }
        } else {
            Utilities.msg(s, Strings.KEEPREGIONUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (args[1].equals("worldguard")) {
            if (newArgs.length == 2) {
                tabs.add("<region>");
            }
            if (s instanceof Player) {
                Player player = (Player) s;
                Location loc = player.getLocation();
                String locSerialized = loc.getWorld().getName() + "," + loc.getChunk().getX() + "," + loc.getChunk().getZ();
                String[] locString = locSerialized.split(",");
                if (newArgs.length == 3) {
                    tabs.add(locString[0]);
                }
            } else {
                if (newArgs.length == 3) {
                    tabs.add("<world>");
                }
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}