package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.service.ChunkService;
import static com.geitenijs.keepchunks.Strings.*;

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

public class ChunkInfoCommand extends BasicKeepChunksCommand {
    private CommandExecutor WEChunkinfo;
    private CommandExecutor WGChunkinfo;
    private TabCompleter WEChunkinfoTab;
    private TabCompleter WGChunkinfoTab;

    @Override
    public CommandName getCommandName(){
        return CommandName.CHUNK_INFO;
    }

    @Override
    public String getCommandUsage(){
        return CHUNKINFOUSAGE;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //TODO: I don't think this can even be triggered, under the correct context
        if(args.length == 0){
            //TODO: Print command usage msg...
            return false;
        }

        final boolean userGaveCurrentLocation = args.length == 2 && args[1].equalsIgnoreCase("current") && sender instanceof Player;
        final boolean userGaveCoords = args.length == 5 && args[1].equalsIgnoreCase("coords");
        final boolean usingWorldEdit = configs.isWorldEditSupported() && args.length == 2 && args[1].equalsIgnoreCase("worldedit");
        final boolean usingWorldGuard = configs.isWorldGuardSupported() && args.length == 4 && args[1].equalsIgnoreCase("worldguard");

        if(usingWorldEdit){
            //Run WorldEdit hook
            return true;
        }

        if(usingWorldGuard){
            //Run WorldGuard hook
            return true;
        }

        if(userGaveCurrentLocation){
            final Location currentLocation = ((Entity) sender).getLocation();
            final org.bukkit.Chunk currentChunk = currentLocation.getChunk();
            final int chunkX = currentChunk.getX();
            final int chunkZ = currentChunk.getZ();
            final World world = currentChunk.getWorld();
            final Chunk chunk = new Chunk(currentChunk);

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
                ChunkService.getInstance().getChunks().contains(chunk) ? "&2Yes" : "&4No",
                world.isChunkForceLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No",
                world.isChunkLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No"
            );
            //Send chunk info to user
            msgCommandSender(sender,chunkInfoMsg);
            return true;
        }

        if(userGaveCoords){
            final String chunkString = String.format("{}#{}#{}",args[2], args[3], args[4]);
            final String[] validChunk = ChunkService.validateChunkString(chunkString);

            if(validChunk == null){
                //TODO: Print error message
                return false;
            }

            final Location currentLocation = ((Entity) sender).getLocation();
            final org.bukkit.Chunk currentChunk = currentLocation.getChunk();
            final int chunkX = currentChunk.getX();
            final int chunkZ = currentChunk.getZ();
            final World world = currentChunk.getWorld();

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
                    chunkDatabase.getChunks().contains(chunk) ? "&2Yes" : "&4No",
                    world.isChunkForceLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No",
                    world.isChunkLoaded(chunkX,chunkZ) ? "&2Yes" : "&4No"
            );
            //Send chunk info to user
            msgCommandSender(sender,chunkInfoMsg);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 1) {
            tabs.add("current");
            tabs.add("coords");
            tabs.add("worldedit");
            tabs.add("worldguard");
        }
        if (args[1].equals("coords")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
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
                return WEChunkinfoTab.onTabComplete(sender, cmd, label, args);
            }
        }
        if (args[1].equals("worldguard")) {
            if (Hooks.WorldGuard) {
                return WGChunkinfoTab.onTabComplete(sender, cmd, label, args);
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}
