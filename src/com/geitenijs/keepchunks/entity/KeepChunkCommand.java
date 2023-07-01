package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.service.DatabaseService;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeepChunkCommand extends KeepChunkBaseCommand{
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        final boolean userGaveCurrentLocation = args.length == 2 && args[1].equalsIgnoreCase("current") && s instanceof Player;
        final boolean userGaveCoords = args.length == 5 && args[1].equalsIgnoreCase("coords");

        if (userGaveCurrentLocation) {
            final Location currentLocation = ((Entity) s).getLocation();
            final Chunk currentChunk = currentLocation.getChunk();
            final String chunkString = String.format("{}#{}#{}", currentChunk.getX(),currentChunk.getZ(),currentChunk.getWorld());

            DatabaseService.getInstance().markChunks(Arrays.asList(chunkString));
            //TODO: Print success to user
            return true;
        }

        if (userGaveCoords) {
            final String chunkString = String.format("{}#{}#{}",args[2], args[3], args[4]);
            DatabaseService.getInstance().markChunks(Arrays.asList(chunkString));
            //TODO: Print success to user
            return true;
        }

        //TODO: Error
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 1) {
            tabs.add("current");
            tabs.add("coords");
        }
        if (args[1].equals("coords")) {
            if (s instanceof Player) {
                Player player = (Player) s;
                Location loc = player.getLocation();
                if (newArgs.length == 2) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
                if (newArgs.length == 4) {
                    tabs.add(loc.getWorld().getName());
                }
            } else {
                if (newArgs.length == 2) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 3) {
                    tabs.add("<0>");
                }
                if (newArgs.length == 4) {
                    tabs.add("<world>");
                }
            }
        }
        if (args[1].equals("current")) {
            tabs.clear();
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
    }
}
