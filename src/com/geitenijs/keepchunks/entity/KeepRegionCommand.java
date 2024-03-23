package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.service.ChunkService;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KeepRegionCommand extends BasicKeepChu {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if(args.length == 0){
            //TODO: Error
        }
        final boolean usingWorldEdit = args.length == 2 && args[1].equalsIgnoreCase("worldedit");
        final boolean usingWorldGuard = args.length == 4 && args[1].equalsIgnoreCase("worldguard");
        final boolean usingCoords = args.length == 7 && args[1].equalsIgnoreCase("coords");

        if(usingWorldEdit){
            //TODO: Do WE stuff
        }

        if(usingWorldGuard){
            //TODO: Do WG stuff
        }

        if(usingCoords){
            final String[] validMinChunk = ChunkService.validateChunkString(String.format("{}#{}#{}",args[2],args[3],args[6]));
            final String[] validMaxChunk = ChunkService.validateChunkString(String.format("{}#{}#{}",args[4],args[5],args[6]));
            final boolean coordsAreInvalid = validMinChunk == null || validMaxChunk == null;

            if(coordsAreInvalid){
                //TODO: Error, probably provide format (Lower left corner, top right corner, etc.)
            }

            final Chunk minChunk = new Chunk(validMinChunk);
            final Chunk maxChunk = new Chunk(validMaxChunk);
            final String world = args[6];

            List<String> chunksToLoad = new ArrayList<>();
            for (int x = minChunk.getX(); x <= maxChunk.getX(); ++x) {
                for (int z = minChunk.getZ(); z <= maxChunk.getZ(); ++z) {
                    chunksToLoad.add(String.format("{}#{}#{}", x, z, world));
                }
            }
            ChunkService.getInstance().markChunks(chunksToLoad);
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 1) {
            tabs.add("coords");
            tabs.add("worldedit");
            tabs.add("worldguard");
        }
        if (args[1].equals("coords")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                Location loc = player.getLocation();
                if (Hooks.WorldEdit) {
                    return WEKeepregionTab.onTabComplete(s, c, label, args);
                } else {
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
                    if (newArgs.length > 6) {
                        tabs.clear();
                    }
                    return CommandWrapper.filterTabs(tabs, args);
                }
            } else {
                if (newArgs.length == 2) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 3) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 4) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 5) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 6) {
                    tabs.add("<world>");
                }
                if (newArgs.length > 6) {
                    tabs.clear();
                }
            }
        }
        if (args[1].equals("worldedit")) {
            if (Hooks.WorldEdit) {
                return WEKeepregionTab.onTabComplete(s, c, label, args);
            }
        }
        if (args[1].equals("worldguard")) {
            if (Hooks.WorldGuard) {
                return WGKeepregionTab.onTabComplete(s, c, label, args);
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}