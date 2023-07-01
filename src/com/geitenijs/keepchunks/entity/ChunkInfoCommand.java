package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WE;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WG;
import com.geitenijs.keepchunks.service.DatabaseService;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunkInfoCommand extends KeepChunkCommand{
    private CommandExecutor WEChunkinfo;
    private CommandExecutor WGChunkinfo;
    private TabCompleter WEChunkinfoTab;
    private TabCompleter WGChunkinfoTab;

    //TODO: Update these WE/WG references later...
    public ChunkInfoCommand() {
        if (Hooks.WorldEdit) {
            WEChunkinfo = new Chunkinfo_WE();
            WEChunkinfoTab = new Chunkinfo_WE();
        }
        if (Hooks.WorldGuard) {
            WGChunkinfo = new Chunkinfo_WG();
            WGChunkinfoTab = new Chunkinfo_WG();
        }
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        if(args.length == 0){
            //TODO: Print command usage msg...
            return false;
        }
        //TODO: Add WG/WE Hook checks to these bools
        final boolean userGaveCurrentLocation = args.length == 2 && args[1].equalsIgnoreCase("current") && s instanceof Player;
        final boolean usingWorldEdit = args.length == 2 && args[1].equalsIgnoreCase("worldedit");
        final boolean usingWorldGuard = args.length == 4 && args[1].equalsIgnoreCase("worldguard");
        final boolean userGaveCoords = args.length == 5 && args[1].equalsIgnoreCase("coords");

        if(userGaveCurrentLocation){
            final Location currentLocation = ((Entity) s).getLocation();
            final Chunk currentChunk = currentLocation.getChunk();
            final int chunkX = currentChunk.getX();
            final int chunkZ = currentChunk.getZ();
            final World world = currentChunk.getWorld();
            final com.geitenijs.keepchunks.entity.Chunk chunk = new com.geitenijs.keepchunks.entity.Chunk(x, z, world);

            //Generate chunk information message
            final String chunkInfoMsg = String.format(
                    """
                    &7---
                    &fPlayer Coords: &c("{}", "{}", "{}")
                    &fChunk Coords: &9("{}", "{}")
                    &fWorld: &6 {}
                    &fMarked: {}
                    &fForce-loaded: {}
                    &fCurrently in memory: {}
                    &7---
                    """,
                currentLocation.getBlockX(),currentLocation.getBlockY(),currentLocation.getBlockZ(),
                chunkX,chunkZ,
                world.getName(),
                DatabaseService.getInstance().getChunks().contains(chunk) ? "&2Yes" : "&4No",
                world.isChunkForceLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No",
                world.isChunkLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No"
            );
            //Send chunk info to user
            msgCommandSender(s,chunkInfoMsg);
            return true;
        }

        if(userGaveCoords){
            final String chunkString = String.format("{}#{}#{}",args[2], args[3], args[4]);
            final String[] validChunk = DatabaseService.validateChunkString(chunkString);

            if(validChunk == null){
                //TODO: Print error message
                return false;
            }

            final Location currentLocation = ((Entity) s).getLocation();
            final Chunk currentChunk = currentLocation.getChunk();
            final int chunkX = currentChunk.getX();
            final int chunkZ = currentChunk.getZ();
            final World world = currentChunk.getWorld();
            final com.geitenijs.keepchunks.entity.Chunk chunk = new com.geitenijs.keepchunks.entity.Chunk(x, z, world);

            //Generate chunk information message
            final String chunkInfoMsg = String.format(
                    """
                    &7---
                    &fChunk Coords: &9("{}", "{}")
                    &fWorld: &6 {}
                    &fMarked: {}
                    &fForce-loaded: {}
                    &fCurrently in memory: {}
                    &7---
                    """,
                    chunkX,chunkZ,
                    world.getName(),
                    DatabaseService.getInstance().getChunks().contains(chunk) ? "&2Yes" : "&4No",
                    world.isChunkForceLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No",
                    world.isChunkLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No"
            );
            //Send chunk info to user
            msgCommandSender(s,chunkInfoMsg);
            return true;
        }

        if(usingWorldEdit){
        //Do WE hook
        }

        if(usingWorldGuard){
            //Do WG hook
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 1) {
            tabs.add("current");
            tabs.add("coords");
            tabs.add("worldedit");
            tabs.add("worldguard");
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
        if (args[1].equals("worldedit")) {
            if (Hooks.WorldEdit) {
                return WEChunkinfoTab.onTabComplete(s, c, label, args);
            }
        }
        if (args[1].equals("worldguard")) {
            if (Hooks.WorldGuard) {
                return WGChunkinfoTab.onTabComplete(s, c, label, args);
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}
