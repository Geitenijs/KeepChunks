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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chunkinfo_WE implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

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
                    final int maxPointX = sel.getMaximumPoint().getBlockX();
                    final int maxPointZ = sel.getMaximumPoint().getBlockZ();
                    final int minPointX = sel.getMinimumPoint().getBlockX();
                    final int minPointZ = sel.getMinimumPoint().getBlockZ();
                    final Chunk chunkMax = maxPoint.getChunk();
                    final Chunk chunkMin = minPoint.getChunk();
                    final int maxZ = chunkMax.getZ();
                    final int maxX = chunkMax.getX();
                    final int minX = chunkMin.getX();
                    final int minZ = chunkMin.getZ();
                    final String world = Objects.requireNonNull(sel.getWorld()).getName();
                    Utilities.msg(s, "&2Your current WorldEdit selection:");
                    Utilities.msg(s, "");
                    Utilities.msg(s, "&fChunk coords: &6(" + minX + ", " + minZ + ") (" + maxX
                            + ", " + maxZ + ")");
                    Utilities.msg(s, "&fCoordinates: &9(" + minPointX + ", " + minPointZ + ") ("
                            + maxPointX + ", " + maxPointZ + ")");
                    Utilities.msg(s, "&fWorld: &c" + world);
                } catch (IncompleteRegionException e) {
                    Utilities.msg(s, Strings.WEFIRST);
                }
            } else {
                Utilities.msg(s, Strings.ONLYPLAYER);
            }
        } else {
            Utilities.msg(s, Strings.CHUNKINFOUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        if (args[1].equals("worldedit")) {
            tabs.clear();
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}