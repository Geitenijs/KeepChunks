package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.hooks.Keepregion_WE;
import com.geitenijs.keepchunks.commands.hooks.Keepregion_WG;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Keepregion implements CommandExecutor, TabCompleter {

    private CommandExecutor WEKeepregion;
    private CommandExecutor WGKeepregion;
    private TabCompleter WEKeepregionTab;
    private TabCompleter WGKeepregionTab;

    Command_Keepregion() {
        if (Hooks.WorldEdit) {
            WEKeepregion = new Keepregion_WE();
            WEKeepregionTab = new Keepregion_WE();
        }
        if (Hooks.WorldGuard) {
            WGKeepregion = new Keepregion_WG();
            WGKeepregionTab = new Keepregion_WG();
        }
    }

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("worldedit")) {
                if (Hooks.WorldEdit) {
                    return WEKeepregion.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldEdit) {
                    Utilities.msg(s, Strings.UPDATEWE);
                } else {
                    Utilities.msg(s, Strings.NOWE);
                }
            } else {
                Utilities.msg(s, Strings.KEEPREGIONUSAGE);
            }
        } else if (args.length == 4) {
            if (args[1].equalsIgnoreCase("worldguard")) {
                if (Hooks.WorldGuard) {
                    return WGKeepregion.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldGuard) {
                    Utilities.msg(s, Strings.UPDATEWG);
                } else {
                    Utilities.msg(s, Strings.NOWG);
                }
            } else {
                Utilities.msg(s, Strings.KEEPREGIONUSAGE);
            }
        } else if (args.length == 7) {
            if (args[1].equalsIgnoreCase("coords")) {
                try {
                    final int minX = Integer.parseInt(args[2]);
                    final int minZ = Integer.parseInt(args[3]);
                    final int maxX = Integer.parseInt(args[4]);
                    final int maxZ = Integer.parseInt(args[5]);
                    final String world = args[6];
                    for (int x = minX; x <= maxX; ++x) {
                        for (int z = minZ; z <= maxZ; ++z) {
                            final String chunk = x + "#" + z + "#" + world;
                            if (Utilities.chunks.contains(chunk)) {
                                Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world
                                        + "'&c is already marked.");
                            } else {
                                Utilities.chunks.add(chunk);
                                Utilities.msg(s, "&fMarked chunk &9(" + x + "," + z + ")&f in world &6'" + world
                                        + "'&f.");
                                if (Utilities.config.getBoolean("chunkload.dynamic")) {
                                    if (Utilities.config.getBoolean("general.debug")) {
                                        Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "Loading chunk (" + x + "," + z + ") in world '" + world + "'.");
                                    }
                                    try {
                                        Main.plugin.getServer().getWorld(world).loadChunk(x, z);
                                        if (Main.version.contains("v1_14_R1")) {
                                            try {
                                                Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, true);
                                            } catch (NoSuchMethodError ignored) {
                                            }
                                        }
                                    } catch (NullPointerException ex) {
                                        if (Utilities.config.getBoolean("general.debug")) {
                                            Utilities.consoleMsgPrefixed(Strings.DEBUGPREFIX + "The world '" + world + "' could not be found. Has it been removed?");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Utilities.data.set("chunks", new ArrayList<>(Utilities.chunks));
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.KEEPREGIONUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.KEEPREGIONUSAGE);
        }
        return true;
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