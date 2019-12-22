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

public class Chunkinfo_WE implements CommandExecutor, TabCompleter {

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
                final String world = sel.getWorld().getName();
                Utilities.msg(s, "&7---");
                Utilities.msg(s, "&fWorldEdit selection: &c(" + minPointX + ", " + minPointZ + ") (" + maxPointX + ", " + maxPointZ + ")");
                Utilities.msg(s, "&fChunk coords: &9(" + minX + ", " + minZ + ") (" + maxX + ", " + maxZ + ")");
                Utilities.msg(s, "&fWorld: &6" + world);
                Utilities.msg(s, "&7---");
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
        return CommandWrapper.filterTabs(tabs, args);
    }
}