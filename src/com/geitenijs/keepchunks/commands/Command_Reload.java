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

        if (args[0].equalsIgnoreCase("reload")) {
            if (s.hasPermission("keepchunks.reload")) {
                if (args.length == 1) {
                    Utilities.reloadConfigFile();
                    Utilities.reloadDataFile();
                    Hooks.registerHooks();
                    Utilities.msg(s, "&aConfiguration & hooks reloaded successfully.");
                } else {
                    Utilities.msg(s, Strings.RELOADUSAGE);
                }
            } else {
                Utilities.msg(s, Strings.NOPERM);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        if (args[0].equals("reload")) {
            if (s.hasPermission("keepchunks.reload")) {
                tabs.clear();
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}