package com.geitenijs.keepchunks.commands.hooks;

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

import java.util.ArrayList;
import java.util.List;

public class Chunkinfo_WG implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        final String region = args[2];
        final String world = args[3];
        if (Bukkit.getWorld(world) == null) {
            Utilities.msg(s, "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
            return false;
        }
        World realWorld = Bukkit.getWorld(world);
        assert realWorld != null;
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(realWorld);
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(weWorld);
        assert manager != null;
        if (manager.getRegion(region) == null) {
            Utilities.msg(s, "&cRegion &f'" + region + "'&c doesn't exist, or is invalid.");
        } else {
            BlockVector3 max = manager.getRegion(region).getMaximumPoint();
            BlockVector3 min = manager.getRegion(region).getMinimumPoint();
            Location maxPoint = new Location(realWorld, max.getBlockX(), max.getBlockY(), max.getBlockZ());
            Location minPoint = new Location(realWorld, min.getBlockX(), min.getBlockY(), min.getBlockZ());
            final Chunk chunkMax = maxPoint.getChunk();
            final Chunk chunkMin = minPoint.getChunk();
            final int maxPointX = maxPoint.getBlockX();
            final int maxPointZ = maxPoint.getBlockZ();
            final int minPointX = minPoint.getBlockX();
            final int minPointZ = minPoint.getBlockZ();
            final int maxZ = chunkMax.getZ();
            final int maxX = chunkMax.getX();
            final int minX = chunkMin.getX();
            final int minZ = chunkMin.getZ();
            Utilities.msg(s, "&7---");
            Utilities.msg(s, "&fWorldGuard region &2" + region + "&f: &c(" + minPointX + ", " + minPointZ + ") (" + maxPointX + ", " + maxPointZ + ")");
            Utilities.msg(s, "&fChunk coords: &9(" + minX + ", " + minZ + ") (" + maxX + ", " + maxZ + ")");
            Utilities.msg(s, "&fWorld: &6" + world);
            Utilities.msg(s, "&7---");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 2) {
            tabs.add("<region>");
        }
        if (s instanceof Player) {
            Player player = (Player) s;
            Location loc = player.getLocation();
            if (newArgs.length == 3) {
                tabs.add(loc.getWorld().getName());
            }
        } else {
            if (newArgs.length == 3) {
                tabs.add("<world>");
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}