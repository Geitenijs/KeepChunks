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
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Releaseregion_WE implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

        final Set<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));

        if (args[1].equalsIgnoreCase("worldedit")) {
            if (s instanceof Player) {
                try {
                    Player player = ((OfflinePlayer) s).getPlayer();
                    BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                    LocalSession session = WorldEdit.getInstance().getSessionManager().get(bPlayer);
                    final Region sel = session.getSelection(bPlayer.getWorld());
                    BlockVector3 max = sel.getMaximumPoint();
                    BlockVector3 min = sel.getMinimumPoint();
                    Location maxPoint = new Location(player.getWorld(), max.getBlockX(),
                            max.getBlockY(), max.getBlockZ());
                    Location minPoint = new Location(player.getWorld(), min.getBlockX(),
                            min.getBlockY(), min.getBlockZ());
                    final Chunk chunkMax = maxPoint.getChunk();
                    final Chunk chunkMin = minPoint.getChunk();
                    final int maxZ = chunkMax.getZ();
                    final int maxX = chunkMax.getX();
                    final int minX = chunkMin.getX();
                    final int minZ = chunkMin.getZ();
                    final String world = Objects.requireNonNull(sel.getWorld()).getName();
                    for (int x = minX; x <= maxX; ++x) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            final String chunk = x + "#" + z + "#"
                                    + world;
                            if (!chunks.contains(chunk)) {
                                Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'"
                                        + world + "'&c isn't marked.");
                            } else {
                                chunks.remove(chunk);
                                Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z
                                        + ")&f in world &6'" + world + "'&f.");
                            }
                        }
                    }
                    Utilities.data.set("chunks", new ArrayList<Object>(chunks));
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                } catch (IncompleteRegionException e) {
                    Utilities.msg(s, Strings.WEFIRST);
                }
            } else {
                Utilities.msg(s, Strings.ONLYPLAYER);
            }
        } else {
            Utilities.msg(s, Strings.RELEASEREGIONUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        Player player = (Player) s;
        Location loc = player.getLocation();
        String locSerialized = loc.getWorld().getName() + "," + loc.getChunk().getX() + "," + loc.getChunk().getZ();
        String[] locString = locSerialized.split(",");
        if (args[1].equals("worldedit")) {
            tabs.clear();
            return CommandWrapper.filterTabs(tabs, args);
        }
        if (args[1].equals("coords")) {
            try {
                Player weplayer = ((OfflinePlayer) s).getPlayer();
                BukkitPlayer bPlayer = BukkitAdapter.adapt(weplayer);
                LocalSession session = WorldEdit.getInstance().getSessionManager().get(bPlayer);
                final Region sel = session.getSelection(bPlayer.getWorld());
                BlockVector3 max = sel.getMaximumPoint();
                BlockVector3 min = sel.getMinimumPoint();
                Location maxPoint = new Location(weplayer.getWorld(), max.getBlockX(), max.getBlockY(),
                        max.getBlockZ());
                Location minPoint = new Location(weplayer.getWorld(), min.getBlockX(), min.getBlockY(),
                        min.getBlockZ());
                final Chunk chunkMax = maxPoint.getChunk();
                final Chunk chunkMin = minPoint.getChunk();
                final int maxZ = chunkMax.getZ();
                final int maxX = chunkMax.getX();
                final int minX = chunkMin.getX();
                final int minZ = chunkMin.getZ();
                final String world = Objects.requireNonNull(sel.getWorld()).getName();
                String weLocSerialized = world + "," + minX + "," + minZ + "," + maxX + "," + maxZ;
                String[] weLocString = weLocSerialized.split(",");
                if (newArgs.length == 2) {
                    tabs.add(weLocString[1]);
                }
                if (newArgs.length == 3) {
                    tabs.add(weLocString[2]);
                }
                if (newArgs.length == 4) {
                    tabs.add(weLocString[3]);
                }
                if (newArgs.length == 5) {
                    tabs.add(weLocString[4]);
                }
                if (newArgs.length == 6) {
                    tabs.add(weLocString[0]);
                }
                if (newArgs.length > 6) {
                    tabs.clear();
                }
            } catch (IncompleteRegionException e) {
                if (newArgs.length == 2) {
                    tabs.add(locString[1]);
                }
                if (newArgs.length == 3) {
                    tabs.add(locString[2]);
                }
                if (newArgs.length == 4) {
                    tabs.add(locString[1]);
                }
                if (newArgs.length == 5) {
                    tabs.add(locString[2]);
                }
                if (newArgs.length == 6) {
                    tabs.add(locString[0]);
                }
                if (newArgs.length > 6) {
                    tabs.clear();
                }
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}