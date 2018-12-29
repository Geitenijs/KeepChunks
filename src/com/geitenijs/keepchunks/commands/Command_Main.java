package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Command_Main implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

        if (args.length == 0) {
            if (Bukkit.getVersion().contains("Spigot")) {
                Utilities.msg(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cSpigot");
            } else if (Bukkit.getVersion().contains("Paper")) {
                Utilities.msg(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cPaper");
            } else if (Bukkit.getVersion().contains("Bukkit")) {
                Utilities.msg(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION + "&f on &cBukkit");
            } else {
                Utilities.msg(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION);
            }
            Utilities.msg(s, Strings.GAMEPREFIX + "&fMade by &6" + Strings.AUTHOR + "&f, © " + Strings.COPYRIGHT);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        String[] newArgs = CommandWrapper.getArgs(args);
        if (newArgs.length == 0) {
            if (s.hasPermission("keepchunks.help") || s.hasPermission("keepchunks.reload") || s.hasPermission("keepchunks.list")
                    || s.hasPermission("keepchunks.chunkinfo") || s.hasPermission("keepchunks.keepchunk")
                    || s.hasPermission("keepchunks.keepregion") || s.hasPermission("keepchunks.releaseall")
                    || s.hasPermission("keepchunks.releasechunk") || s.hasPermission("keepchunks.releaseregion")) {
                tabs.add("help");
                tabs.add("reload");
                tabs.add("list");
                tabs.add("chunkinfo");
                tabs.add("keepchunk");
                tabs.add("keepregion");
                tabs.add("releaseall");
                tabs.add("releasechunk");
                tabs.add("releaseregion");
                return CommandWrapper.filterTabs(tabs, args);
            }
        }
        return null;
    }
}