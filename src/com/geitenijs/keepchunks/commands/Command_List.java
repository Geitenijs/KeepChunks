package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Command_List implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 1) {
            if (Utilities.chunks.isEmpty()) {
                Utilities.msg(s, "&cThere don't seem to be any marked chunks.");
            } else {
                Utilities.msg(s, "&aA list of all marked chunks will be shown below.");
                Utilities.msg(s, "&7---");
                for (final String chunk : Utilities.chunks) {
                    final String[] chunkCoordinates = chunk.split("#");
                    final int x = Integer.parseInt(chunkCoordinates[0]);
                    final int z = Integer.parseInt(chunkCoordinates[1]);
                    final String world = chunkCoordinates[2];
                    Utilities.msg(s, "&fChunk &9(" + x + "," + z + ") &fin world &6'" + world + "'&f.");
                }
                Utilities.msg(s, "&7---");
                Utilities.msg(s, "&aA total of &f" + Utilities.chunks.size() + "&a chunks are currently marked.");
            }
        } else {
            Utilities.msg(s, Strings.LISTUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.clear();
        return CommandWrapper.filterTabs(tabs, args);
    }
}