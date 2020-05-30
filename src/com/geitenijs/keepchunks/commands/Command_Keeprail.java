package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class Command_Keeprail implements CommandExecutor, TabCompleter {

    int totalRails = 0;
    int totalChunks = 0;

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("current")) {
                if (s instanceof Player) {
                    Location loc = ((Player) s).getLocation();
                    loc.setX(loc.getBlockX());
                    loc.setY(loc.getBlockY());
                    loc.setZ(loc.getBlockZ());
                    loc.setPitch(0.0f);
                    loc.setYaw(0.0f);
                    Material m = loc.getBlock().getType();
                    boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
                    HashSet<Location> explored = new HashSet<>();
                    Queue<Location> agenda = new LinkedList<>();
                    if (!isRail) {
                        Utilities.msg(s, "&cThere doesn't seem to be a rail at your location.");
                        return true;
                    } else {
                        Utilities.msg(s, "&7&oLooking for rails...");
                        agenda.add(loc);
                        while (!agenda.isEmpty()) {
                            Location cur = agenda.peek();
                            agenda.remove();
                            explored.add(cur);
                            getAdjacent(cur, explored, agenda);
                            ++totalRails;
                        }
                        Utilities.msg(s, "&fFound &c" + totalRails + "&f rails!");
                        Utilities.msg(s, "&fMarked a total of &9" + totalChunks + "&f chunks in world &6'" + loc.getWorld().getName() + "'&f.");
                    }
                    totalRails = 0;
                    totalChunks = 0;
                } else {
                    Utilities.msg(s, Strings.ONLYPLAYER);
                }
            } else {
                Utilities.msg(s, Strings.KEEPRAILUSAGE);
            }
        } else if (args.length == 6) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int x = Integer.parseInt(args[2]);
                    final int y = Integer.parseInt(args[3]);
                    final int z = Integer.parseInt(args[4]);
                    final String world = args[5];
                    if (Bukkit.getWorld(world) == null) {
                        Utilities.msg(s, "&cWorld &f'" + world + "'&c doesn't exist, or isn't loaded in memory.");
                        return false;
                    }
                    World realWorld = Bukkit.getWorld(world);
                    Location loc = new Location(realWorld, x, y, z);
                    loc.setX(loc.getBlockX());
                    loc.setY(loc.getBlockY());
                    loc.setZ(loc.getBlockZ());
                    loc.setPitch(0.0f);
                    loc.setYaw(0.0f);
                    Material m = loc.getBlock().getType();
                    boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
                    HashSet<Location> explored = new HashSet<>();
                    Queue<Location> agenda = new LinkedList<>();
                    if (!isRail) {
                        Utilities.msg(s, "&cThere doesn't seem to be a rail at that location.");
                        return true;
                    } else {
                        Utilities.msg(s, "&7&oLooking for rails...");
                        agenda.add(loc);
                        while (!agenda.isEmpty()) {
                            Location cur = agenda.peek();
                            agenda.remove();
                            explored.add(cur);
                            getAdjacent(cur, explored, agenda);
                            ++totalRails;
                        }
                        Utilities.msg(s, "&fFound &c" + totalRails + "&f rails!");
                        Utilities.msg(s, "&fMarked a total of &9" + totalChunks + "&f chunks in world &6'" + loc.getWorld().getName() + "'&f.");
                    }
                    totalRails = 0;
                    totalChunks = 0;
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.KEEPRAILUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.KEEPRAILUSAGE);
        }
        return true;
    }

    public void getN(Location pos, HashSet<Location> history, Queue<Location> todo) {
        for (int i = -1; i < 2; ++i) {
            Location candidate = new Location(pos.getWorld(), pos.getBlockX(), pos.getBlockY() + i, pos.getBlockZ() - 1);
            Material m = candidate.getBlock().getType();
            boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
            if (isRail && !history.contains(candidate) && !todo.contains(candidate)) {
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsg(Strings.DEBUGPREFIX + "Found chunk (" + pos.getChunk().getX() + "," + pos.getChunk().getZ() + ") in world '" + pos.getWorld().getName() + "' while discovering rails at (" + pos.getBlockX() + "," + pos.getBlockY() + "," + pos.getBlockZ() + ").");
                }
                updateData(candidate.getChunk());
                todo.add(candidate);
            }
        }
    }

    public void getE(Location pos, HashSet<Location> history, Queue<Location> todo) {
        for (int i = -1; i < 2; ++i) {
            Location candidate = new Location(pos.getWorld(), pos.getBlockX() + 1, pos.getBlockY() + i, pos.getBlockZ());
            Material m = candidate.getBlock().getType();
            boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
            if (isRail && !history.contains(candidate) && !todo.contains(candidate)) {
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsg(Strings.DEBUGPREFIX + "Found chunk (" + pos.getChunk().getX() + "," + pos.getChunk().getZ() + ") in world '" + pos.getWorld().getName() + "' while discovering rails at (" + pos.getBlockX() + "," + pos.getBlockY() + "," + pos.getBlockZ() + ").");
                }
                updateData(candidate.getChunk());
                todo.add(candidate);
            }
        }
    }

    public void getS(Location pos, HashSet<Location> history, Queue<Location> todo) {
        for (int i = -1; i < 2; ++i) {
            Location candidate = new Location(pos.getWorld(), pos.getBlockX(), pos.getBlockY() + i, pos.getBlockZ() + 1);
            Material m = candidate.getBlock().getType();
            boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
            if (isRail && !history.contains(candidate) && !todo.contains(candidate)) {
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsg(Strings.DEBUGPREFIX + "Found chunk (" + pos.getChunk().getX() + "," + pos.getChunk().getZ() + ") in world '" + pos.getWorld().getName() + "' while discovering rails at (" + pos.getBlockX() + "," + pos.getBlockY() + "," + pos.getBlockZ() + ").");
                }
                updateData(candidate.getChunk());
                todo.add(candidate);
            }
        }
    }

    public void getW(Location pos, HashSet<Location> history, Queue<Location> todo) {
        for (int i = -1; i < 2; ++i) {
            Location candidate = new Location(pos.getWorld(), pos.getBlockX() - 1, pos.getBlockY() + i, pos.getBlockZ());
            Material m = candidate.getBlock().getType();
            boolean isRail = (m == Material.RAIL || m == Material.POWERED_RAIL || m == Material.ACTIVATOR_RAIL || m == Material.DETECTOR_RAIL);
            if (isRail && !history.contains(candidate) && !todo.contains(candidate)) {
                if (Utilities.config.getBoolean("general.debug")) {
                    Utilities.consoleMsg(Strings.DEBUGPREFIX + "Found chunk (" + pos.getChunk().getX() + "," + pos.getChunk().getZ() + ") in world '" + pos.getWorld().getName() + "' while discovering rails at (" + pos.getBlockX() + "," + pos.getBlockY() + "," + pos.getBlockZ() + ").");
                }
                updateData(candidate.getChunk());
                todo.add(candidate);
            }
        }
    }

    public void getAdjacent(Location pos, HashSet<Location> history, Queue<Location> todo) {
        getN(pos, history, todo);
        getE(pos, history, todo);
        getS(pos, history, todo);
        getW(pos, history, todo);
    }

    public void updateData(Chunk currentChunk) {
        final String world = currentChunk.getWorld().getName();
        for (int i = -1; i < 2; ++i) {
            final int x = currentChunk.getX() + i;
            for (int j = -1; j < 2; ++j) {
                final int z = currentChunk.getZ() + j;
                final String chunk = x + "#" + z + "#" + world;
                if (!Utilities.chunks.contains(chunk)) {
                    if (Utilities.config.getBoolean("general.debug")) {
                        Utilities.consoleMsg(Strings.DEBUGPREFIX + "Marking chunk (" + x + "," + z + ") in world '" + world + "'...");
                    }
                    Utilities.chunks.add(chunk);
                    Bukkit.getServer().getWorld(world).loadChunk(x, z);
                    Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                    Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                    ++totalChunks;
                }
            }
        }
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
                    tabs.add(String.valueOf(loc.getBlockX()));
                }
                if (newArgs.length == 3) {
                    tabs.add(String.valueOf(loc.getBlockY()));
                }
                if (newArgs.length == 4) {
                    tabs.add(String.valueOf(loc.getBlockZ()));
                }
                if (newArgs.length == 5) {
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
                    tabs.add("<0>");
                }
                if (newArgs.length == 5) {
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