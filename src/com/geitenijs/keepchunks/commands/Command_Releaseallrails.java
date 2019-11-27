package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Main;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Releaseallrails implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 1) {
            if (Utilities.config.getBoolean("general.releaseallprotection")) {
                if (s instanceof Player) {
                    Utilities.msg(s, Strings.ONLYCONSOLE);
                    return true;
                }
            }
            for (final String chunk : Utilities.railchunks) {
                final String[] chunkCoordinates = chunk.split("#");
                final int x = Integer.parseInt(chunkCoordinates[0]);
                final int z = Integer.parseInt(chunkCoordinates[1]);
                final String world = chunkCoordinates[2];
                if (Main.version.contains("v1_14_R1")) {
                    try {
                        Main.plugin.getServer().getWorld(world).setChunkForceLoaded(x, z, false);
                    } catch (Exception ignored) {
                    }
                }
            }
            Utilities.railchunks.clear();
            Utilities.data.set("railchunks", new ArrayList<>());
            Utilities.saveDataFile();
            Utilities.reloadDataFile();
            Utilities.msg(s, "&aAll marked chunks have been released.");
        } else {
            Utilities.msg(s, Strings.RELEASEALLUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.clear();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
