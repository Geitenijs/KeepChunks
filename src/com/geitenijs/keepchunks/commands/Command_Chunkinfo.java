package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WE;
import com.geitenijs.keepchunks.commands.hooks.Chunkinfo_WG;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Command_Chunkinfo implements CommandExecutor, TabCompleter {

    int totalChunks = 0;
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
        totalChunks = 0;
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    final Chunk currentChunk = ((Entity) s).getLocation().getChunk();
                    final int x = currentChunk.getX();
                    final int z = currentChunk.getZ();
                    final int playerX = ((Player) s).getLocation().getBlockX();
                    final int playerY = ((Player) s).getLocation().getBlockY();
                    final int playerZ = ((Player) s).getLocation().getBlockZ();
                    final String world = currentChunk.getWorld().getName();
                    final String chunk = x + "#" + z + "#" + world;
                    Utilities.msg(s, Strings.LINE);
                    Utilities.msg(s, "&fPlayer: &c(" + playerX + ", " + playerY + ", " + playerZ + ")");
                    Utilities.msg(s, "&fChunk coords: &9(" + x + ", " + z + ")");
                    Utilities.msg(s, "&fWorld: &6" + world);
                    Utilities.msg(s, "");
                    if (Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&fMarked: &2Yes");
                    } else {
                        Utilities.msg(s, "&fMarked: &4No");
                    }
                    if (Bukkit.getServer().getWorld(world).isChunkForceLoaded(x, z)) {
                        Utilities.msg(s, "&fForce-loaded: &2Yes");
                    } else {
                        Utilities.msg(s, "&fForce-loaded: &4No");
                    }
                    if (Bukkit.getServer().getWorld(world).isChunkLoaded(x, z)) {
                        Utilities.msg(s, "&fCurrently in memory: &2Yes");
                    } else {
                        Utilities.msg(s, "&fCurrently in memory: &4No");
                    }
                    if (Utilities.chunks.contains(chunk) && !Bukkit.getServer().getWorld(world).isChunkForceLoaded(x, z)) {
                        Utilities.msg(s, "&c&oPlease reload the plugin to update force-loaded chunks.");
                    }
                    Utilities.msg(s, Strings.LINE);
                } else {
                    Utilities.msg(s, Strings.ONLYPLAYER);
                }
            } else if (args[1].equalsIgnoreCase("worldedit")) {
                if (Hooks.WorldEdit) {
                    return WEChunkinfo.onCommand(s, c, label, args);
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
                    if (Bukkit.getWorld(world) == null) {
                        Utilities.msg(s, Strings.IGPREFIX + "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
                        return false;
                    }
                    final String chunk = x + "#" + z + "#" + world;
                    Utilities.msg(s, Strings.LINE);
                    Utilities.msg(s, "&fChunk coords: &9(" + x + ", " + z + ")");
                    Utilities.msg(s, "&fWorld: &6" + world);
                    Utilities.msg(s, "");
                    if (Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&fMarked: &2Yes");
                    } else {
                        Utilities.msg(s, "&fMarked: &4No");
                    }
                    if (Bukkit.getServer().getWorld(world).isChunkForceLoaded(x, z)) {
                        Utilities.msg(s, "&fForce-loaded: &2Yes");
                    } else {
                        Utilities.msg(s, "&fForce-loaded: &4No");
                    }
                    if (Bukkit.getServer().getWorld(world).isChunkLoaded(x, z)) {
                        Utilities.msg(s, "&fCurrently in memory: &2Yes");
                    } else {
                        Utilities.msg(s, "&fCurrently in memory: &4No");
                    }
                    if (Utilities.chunks.contains(chunk) && !Bukkit.getServer().getWorld(world).isChunkForceLoaded(x, z)) {
                        Utilities.msg(s, "&c&oPlease reload the plugin to update force-loaded chunks.");
                    }
                    Utilities.msg(s, Strings.LINE);
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.CHUNKINFOUSAGE);
            }
        } else if (args.length == 7) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int X1 = Integer.parseInt(args[2]);
                    final int Z1 = Integer.parseInt(args[3]);
                    final int X2 = Integer.parseInt(args[4]);
                    final int Z2 = Integer.parseInt(args[5]);
                    final int minX = min(X1, X2);
                    final int minZ = min(Z1, Z2);
                    final int maxX = max(X1, X2);
                    final int maxZ = max(Z1, Z2);
                    final String world = args[6];
                    if (Bukkit.getWorld(world) == null) {
                        Utilities.msg(s, Strings.IGPREFIX + "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
                        return false;
                    }
                    for (int x = minX; x <= maxX; ++x) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            ++totalChunks;
                        }
                    }
                    Utilities.msg(s, Strings.LINE);
                    Utilities.msg(s, "&fChunk coords: &9(" + minX + ", " + minZ + ") (" + maxX + ", " + maxZ + ")");
                    Utilities.msg(s, "&fWorld: &6" + world);
                    Utilities.msg(s, "&fTotal amount of chunks: &c" + totalChunks);
                    Utilities.msg(s, Strings.LINE);
                } catch (NumberFormatException ex) {
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
            if (s instanceof Player player) {
                Location loc = player.getLocation();
                if (newArgs.length == 2) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
                if (newArgs.length == 4) {
                    tabs.add(String.valueOf(loc.getChunk().getX()));
                    tabs.add(loc.getWorld().getName());
                }
                if (newArgs.length == 5) {
                    tabs.add(String.valueOf(loc.getChunk().getZ()));
                }
                if (newArgs.length == 6) {
                    tabs.add(loc.getWorld().getName());
                }
            } else {
                if (newArgs.length == 2) {
                    tabs.add("<x1>");
                }
                if (newArgs.length == 3) {
                    tabs.add("<z1>");
                }
                if (newArgs.length == 4) {
                    tabs.add("<x2>");
                    tabs.add("<world>");
                }
                if (newArgs.length == 5) {
                    tabs.add("<z2>");
                }
                if (newArgs.length == 6) {
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