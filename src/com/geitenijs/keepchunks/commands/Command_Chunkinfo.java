package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WE;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WG;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Command_Chunkinfo implements CommandExecutor, TabCompleter {

    private CommandExecutor WEChunkinfo;
    private CommandExecutor WGChunkinfo;
    private TabCompleter WEChunkinfoTab;
    private TabCompleter WGChunkinfoTab;

    Command_Chunkinfo() {
        if (Hooks.WorldEdit) {
            WEChunkinfo = new Chunkinfo_WE();
            WEChunkinfoTab = new Chunkinfo_WE();
        }
        if (Hooks.WorldGuard) {
            WGChunkinfo = new Chunkinfo_WG();
            WGChunkinfoTab = new Chunkinfo_WG();
        }
    }

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        final Set<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    final Chunk currentChunk = ((Entity) s).getLocation().getChunk();
                    final int x = currentChunk.getX();
                    final int z = currentChunk.getZ();
                    final int playerX = ((Player) s).getLocation().getBlockX();
                    final int playerZ = ((Player) s).getLocation().getBlockZ();
                    final String world = currentChunk.getWorld().getName();
                    final String chunk = x + "#" + z + "#" + world;
                    Utilities.msg(s, "&2Your current chunk:");
                    Utilities.msg(s, "");
                    Utilities.msg(s, "&fChunk coords: &6(" + x + ", " + z + ")");
                    Utilities.msg(s, "&fCoordinates: &9(" + playerX + ", " + playerZ + ")");
                    Utilities.msg(s, "&fWorld: &c" + world);
                    if (chunks.contains(chunk)) {
                        Utilities.msg(s, "&fMarked by KC: &2Yes");
                    } else {
                        Utilities.msg(s, "&fMarked by KC: &4No");
                    }
                    if (Main.plugin.getServer().getWorld(world).isChunkLoaded(x, z)) {
                        Utilities.msg(s, "&fCurrently loaded: &2Yes");
                    } else {
                        Utilities.msg(s, "&fCurrently loaded: &4No");
                    }
                } else {
                    Utilities.msg(s, Strings.ONLYPLAYER);
                }
            } else if (args[1].equalsIgnoreCase("worldedit")) {
                if (Hooks.WorldEdit) {
                    return WEChunkinfo.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldEdit) {
                    Utilities.msg(s, Strings.UPDATEWE);
                } else {
                    Utilities.msg(s, Strings.NOWE);
                }
            } else {
                Utilities.msg(s, Strings.CHUNKINFOUSAGE);
            }
        } else if (args.length == 4) {
            if (args[1].equalsIgnoreCase("worldguard")) {
                if (Hooks.WorldGuard) {
                    return WGChunkinfo.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldGuard) {
                    Utilities.msg(s, Strings.UPDATEWG);
                } else {
                    Utilities.msg(s, Strings.NOWG);
                }
            } else {
                Utilities.msg(s, Strings.CHUNKINFOUSAGE);
            }
        } else if (args.length == 5) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int x = Integer.parseInt(args[2]);
                    final int z = Integer.parseInt(args[3]);
                    final String world = args[4];
                    final String chunk = x + "#" + z + "#" + world;
                    Utilities.msg(s, "&2The specified chunk:");
                    Utilities.msg(s, "");
                    Utilities.msg(s, "&fChunk coords: &6(" + x + ", " + z + ")");
                    Utilities.msg(s, "&fWorld: &c" + world);
                    if (chunks.contains(chunk)) {
                        Utilities.msg(s, "&fMarked by KC: &2Yes");
                    } else {
                        Utilities.msg(s, "&fMarked by KC: &4No");
                    }
                    if (Main.plugin.getServer().getWorld(world).isChunkLoaded(x, z)) {
                        Utilities.msg(s, "&fCurrently loaded: &2Yes");
                    } else {
                        Utilities.msg(s, "&fCurrently loaded: &4No");
                    }
                } catch (NullPointerException | NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.CHUNKINFOUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.CHUNKINFOUSAGE);
        }
        return true;
    }

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
                String locSerialized = loc.getWorld().getName() + "," + loc.getChunk().getX() + "," + loc.getChunk().getZ();
                String[] locString = locSerialized.split(",");
                if (newArgs.length == 2) {
                    tabs.add(locString[1]);
                }
                if (newArgs.length == 3) {
                    tabs.add(locString[2]);
                }
                if (newArgs.length == 4) {
                    tabs.add(locString[0]);
                }
                if (newArgs.length > 4) {
                    tabs.clear();
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
                if (newArgs.length > 4) {
                    tabs.clear();
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