package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.entity.Chunk;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command that marks a single chunk for force-loading
 */
public class KeepChunkCommand extends BasicKeepChunksCommand {
    private static final String COMMAND_USAGE = "Figure it out :D";

    /**
     * Return the enumerated name for this command
     */
    @Override
    public CommandName getCommandName() {
        return CommandName.KEEP_CHUNK;
    }

    /**
     * Returns a console safe string containing useful information/examples on how to run the command
     *
     * @return
     * @apiNote This output is intended for delivery to some console, be it in-game or the server console
     */
    @Override
    public String getCommandUsage() {
        return COMMAND_USAGE;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final boolean userGaveCurrentLocation = sender instanceof Player && args.length == 2 && args[1].equalsIgnoreCase("current");
        final boolean userGaveCoords = args.length == 5 && args[1].equalsIgnoreCase("coords");

        if (userGaveCurrentLocation) {
            final Location currentLocation = ((Entity) sender).getLocation();
            final com.geitenijs.keepchunks.entity.Chunk currentChunk = new Chunk(currentLocation.getChunk());
            final String chunkString = String.format("{}#{}#{}", currentChunk.getX(),currentChunk.getZ(),currentChunk.getWorld());

            //Load chunks
            //TODO: Print success to user
            return true;
        }

        if (userGaveCoords) {
            final String chunkString = String.format("{}#{}#{}",args[2], args[3], args[4]);
            //Load chunks
            return true;
        }
        return false;
    }

    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        ArrayList<String> options = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        final boolean usingCoords = args[1].equals("coords") && sender instanceof Player;
        final boolean usingCurrentLoc = args[1].equals("current");
        final Location loc = usingCoords ? ((Player) sender).getLocation() : null; //To avoid provoking a casting exception
        final String x = usingCoords ? String.valueOf(loc.getChunk().getX()) : "<0>";
        final String z = usingCoords ? String.valueOf(loc.getChunk().getZ()) : "<0>";
        final String world = usingCoords ? loc.getWorld().getName() : "<world>";

        //If no args have been given, suggest either using coords or the player's location
        if (newArgs.length == 1) {
            options.add("current");
            options.add("coords");
        }

        if (usingCurrentLoc) {
            options.clear();
        }else {
            options.add(switch(newArgs.length) {
                case 2 -> x;
                case 3 -> z;
                case 4 -> world;
                default -> throw new IllegalStateException("Unexpected value: " + newArgs.length);
            });
        }

        return CommandWrapper.filterTabs(options, args);
    }
}
