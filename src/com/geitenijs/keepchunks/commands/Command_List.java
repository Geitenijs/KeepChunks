package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Command_List implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

        if (args[0].equalsIgnoreCase("list")) {
            if (s.hasPermission("keepchunks.list")) {
                if (args.length == 1) {
                    final Set<String> chunks = new HashSet<>(Utilities.data.getStringList("chunks"));
                    if (chunks.isEmpty()) {
                        Utilities.msg(s, "&cThere are currently no marked chunks.");
                        return true;
                    }
                    Utilities.msg(s, "&aA list of all marked chunks will be shown below.");
                    Utilities.msg(s, "&7---");
                    for (final String chunk : chunks) {
                        final String[] chunkCoordinates = chunk.split("#");
                        final int x = Integer.parseInt(chunkCoordinates[0]);
                        final int z = Integer.parseInt(chunkCoordinates[1]);
                        final String world = chunkCoordinates[2];
                        Utilities.msg(s, "&fChunk &9(" + x + "," + z + ") &fin world &6'" + world + "'&f.");
                    }
                    Utilities.msg(s, "&7---");
                    Utilities.msg(s, "&aA total of &f" + Utilities.data.getStringList("chunks").size() + "&a chunks are currently marked.");
                } else {
                    Utilities.msg(s, Strings.LISTUSAGE);
                }
            } else {
                Utilities.msg(s, Strings.NOPERM);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        if (args[0].equals("list")) {
            if (s.hasPermission("keepchunks.list")) {
                tabs.clear();
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}