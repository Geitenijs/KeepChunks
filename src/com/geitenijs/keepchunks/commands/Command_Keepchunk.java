package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Keepchunk implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    final Chunk currentChunk = ((Player) s).getLocation().getChunk();
                    final String chunk = currentChunk.getX() + "#" + currentChunk.getZ() + "#" + currentChunk.getWorld().getName();
                    if (Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&cChunk &f(" + currentChunk.getX() + "," + currentChunk.getZ() + ")&c in world &f'" + currentChunk.getWorld().getName() + "'&c is already marked.");
                    } else {
                        final String world = currentChunk.getWorld().getName();
                        final int x = currentChunk.getX();
                        final int z = currentChunk.getZ();
                        Utilities.chunks.add(chunk);
                        Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                        Utilities.saveDataFile();
                        Utilities.reloadDataFile();
                        if (Utilities.config.getBoolean("chunkload.dynamic")) {
                            if (Utilities.config.getBoolean("general.debug")) {
                                Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
                            }
                            try {
                                Main.plugin.getServer().getWorld(world).loadChunk(x, z);
                                Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                            } catch (NullPointerException ex) {
                                if (Utilities.config.getBoolean("general.debug")) {
                                    Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
                                }
                            }
                        }
                        Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
                    }
                } else {
                    Utilities.msg(s, Strings.ONLYPLAYER);
                }
            } else {
                Utilities.msg(s, Strings.KEEPCHUNKUSAGE);
            }
        } else if (args.length == 5) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int x = Integer.parseInt(args[2]);
                    final int z = Integer.parseInt(args[3]);
                    final String world = args[4];
                    final String chunk = x + "#" + z + "#" + world;
                    if (Utilities.chunks.contains(chunk)) {
                        Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world + "'&c is already marked.");
                    } else {
                        Utilities.chunks.add(chunk);
                        Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                        Utilities.saveDataFile();
                        Utilities.reloadDataFile();
                        if (Utilities.config.getBoolean("chunkload.dynamic")) {
                            if (Utilities.config.getBoolean("general.debug")) {
                                Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
                            }
                            try {
                                Main.plugin.getServer().getWorld(world).loadChunk(x, z);
                                Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                            } catch (NullPointerException ex) {
                                if (Utilities.config.getBoolean("general.debug")) {
                                    Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
                                }
                            }
                        }
                        Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world + "'&f.");
                    }
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.KEEPCHUNKUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.KEEPCHUNKUSAGE);
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