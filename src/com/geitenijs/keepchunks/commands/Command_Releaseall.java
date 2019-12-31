package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Releaseall implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 1) {
            if (Utilities.config.getBoolean("general.releaseallprotection")) {
                if (s instanceof Player) {
                    Utilities.msg(s, Strings.ONLYCONSOLE);
                    return false;
                }
            }
            if (Utilities.chunks.isEmpty()) {
                Utilities.msg(s, "&cThere don't seem to be any marked chunks.");
            } else {
                Utilities.msg(s, "&7&oReleasing all chunks...");
                int totalChunks = Utilities.chunks.size();
                for (final String chunk : Utilities.chunks) {
                    final String[] chunkCoordinates = chunk.split("#");
                    final int x = Integer.parseInt(chunkCoordinates[0]);
                    final int z = Integer.parseInt(chunkCoordinates[1]);
                    final String world = chunkCoordinates[2];
                    Bukkit.getServer().getWorld(world).setChunkForceLoaded(x, z, false);
                }
                Utilities.chunks.clear();
                Utilities.data.set("chunks", new ArrayList<>());
                Utilities.saveDataFile();
                Utilities.reloadDataFile();
                Utilities.msg(s, "&aAll &f" + totalChunks + "&a marked chunks have been released.");
            }
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