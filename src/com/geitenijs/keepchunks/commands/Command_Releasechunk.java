package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Releasechunk implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    final Chunk currentChunk = ((Player) s).getLocation().getChunk();
                    final int x = currentChunk.getX();
                    final int z = currentChunk.getZ();
                    final String world = currentChunk.getWorld().getName();
                    final String chunk = x + "#" + z + "#" + world;
                    if (!Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c isn't marked.");
                    } else {
                        if (Utilities.config.getBoolean("general.debug")) {
                            Utilities.consoleMsg(Strings.DEBUGPREFIX + "Releasing chunk (" + x + "," + z + ") in world '" + world + "'...");
                        }
                        Utilities.chunks.remove(chunk);
                        Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, false);
                        Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                        Utilities.saveDataFile();
                        Utilities.reloadDataFile();
                        Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
                    }
                } else {
                    Utilities.msg(s, Strings.ONLYPLAYER);
                }
            } else {
                Utilities.msg(s, Strings.RELEASECHUNKUSAGE);
            }

        } else if (args.length == 5) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int x = Integer.parseInt(args[2]);
                    final int z = Integer.parseInt(args[3]);
                    final String world = args[4];
                    if (Bukkit.getWorld(world) == null) {
                        Utilities.msg(s, "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
                        return false;
                    }
                    final String chunk = x + "#" + z + "#" + world;
                    if (!Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c isn't marked.");
                    } else {
                        if (Utilities.config.getBoolean("general.debug")) {
                            Utilities.consoleMsg(Strings.DEBUGPREFIX + "Releasing chunk (" + x + "," + z + ") in world '" + world + "'...");
                        }
                        Utilities.chunks.remove(chunk);
                        Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, false);
                        Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                        Utilities.saveDataFile();
                        Utilities.reloadDataFile();
                        Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
                    }
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.RELEASECHUNKUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.RELEASECHUNKUSAGE);
        }
        return true;
    }

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
        return CommandWrapper.filterTabs(tabs, args);
    }
}