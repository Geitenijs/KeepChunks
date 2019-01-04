package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.hooks.Releaseregion_WE;
import com.geitenijs.keepchunks.commands.hooks.Releaseregion_WG;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Command_Releaseregion implements CommandExecutor, TabCompleter {

    private CommandExecutor WEReleaseregion;
    private CommandExecutor WGReleaseregion;
    private TabCompleter WEReleaseregionTab;
    private TabCompleter WGReleaseregionTab;

    Command_Releaseregion() {
        if (Hooks.WorldEdit) {
            WEReleaseregion = new Releaseregion_WE();
            WEReleaseregionTab = new Releaseregion_WE();
        }
        if (Hooks.WorldGuard) {
            WGReleaseregion = new Releaseregion_WG();
            WGReleaseregionTab = new Releaseregion_WG();
        }
    }

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        final Set<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("worldedit")) {
                if (Hooks.WorldEdit) {
                    return WEReleaseregion.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldEdit) {
                    Utilities.msg(s, Strings.UPDATEWE);
                } else {
                    Utilities.msg(s, Strings.NOWE);
                }
            } else {
                Utilities.msg(s, Strings.RELEASEREGIONUSAGE);
            }
        } else if (args.length == 4) {
            if (args[1].equalsIgnoreCase("worldguard")) {
                if (Hooks.WorldGuard) {
                    return WGReleaseregion.onCommand(s, c, label, args);
                } else if (Hooks.incompatibleWorldGuard) {
                    Utilities.msg(s, Strings.UPDATEWG);
                } else {
                    Utilities.msg(s, Strings.NOWG);
                }
            } else {
                Utilities.msg(s, Strings.RELEASEREGIONUSAGE);
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
                            if (!chunks.contains(chunk)) {
                                Utilities.msg(s, "&cChunk &f(" + x + "," + z + ")&c in world &f'" + world
                                        + "'&c isn't marked.");
                            } else {
                                chunks.remove(chunk);
                                Utilities.msg(s, "&fReleased chunk &9(" + x + "," + z + ")&f in world &6'"
                                        + world + "'&f.");
                            }
                        }
                    }
                    Utilities.data.set("chunks", new ArrayList<Object>(chunks));
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                } catch (NumberFormatException ex) {
                    Utilities.msg(s, Strings.UNUSABLE);
                }
            } else {
                Utilities.msg(s, Strings.RELEASEREGIONUSAGE);
            }
        } else {
            Utilities.msg(s, Strings.RELEASEREGIONUSAGE);
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
                String locSerialized = loc.getWorld().getName() + "," + loc.getChunk().getX() + "," + loc.getChunk().getZ();
                String[] locString = locSerialized.split(",");
                if (Hooks.WorldEdit) {
                    return WEReleaseregionTab.onTabComplete(s, c, label, args);
                } else {
                    if (newArgs.length == 2) {
                        tabs.add(locString[1]);
                    }
                    if (newArgs.length == 3) {
                        tabs.add(locString[2]);
                    }
                    if (newArgs.length == 4) {
                        tabs.add(locString[1]);
                    }
                    if (newArgs.length == 5) {
                        tabs.add(locString[2]);
                    }
                    if (newArgs.length == 6) {
                        tabs.add(locString[0]);
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
                return WEReleaseregionTab.onTabComplete(s, c, label, args);
            }
        }
        if (args[1].equals("worldguard")) {
            if (Hooks.WorldGuard) {
                return WGReleaseregionTab.onTabComplete(s, c, label, args);
            }
        }
        return CommandWrapper.filterTabs(tabs, args);
    }
}