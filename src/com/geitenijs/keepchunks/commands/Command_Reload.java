package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Hooks;
import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Command_Reload implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 1) {
            Utilities.reloadConfigFile();
            Utilities.reloadDataFile();
            Hooks.registerHooks();
            Utilities.msg(s, Strings.IGPREFIX + "&aThe configuration file has been reloaded.");
            if (!Utilities.chunks.isEmpty()) {
                Utilities.msg(s, Strings.IGPREFIX + "&7&oReloading all chunks...");
                Utilities.loadChunks();
                Utilities.msg(s, Strings.IGPREFIX + "&aAll &f" + Utilities.chunks.size() + "&a chunks have been reloaded.");
            }
        } else {
            Utilities.msg(s, Strings.RELOADUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.clear();
        return CommandWrapper.filterTabs(tabs, args);
    }
}