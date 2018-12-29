package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Command_Releaseall implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {

        if (args[0].equalsIgnoreCase("releaseall")) {
            if (s.hasPermission("keepchunks.releaseall")) {
                if (args.length == 1) {
                    if (Utilities.config.getBoolean("general.releaseallprotection")) {
                        if (s instanceof Player) {
                            Utilities.msg(s, Strings.ONLYCONSOLE);
                            return true;
                        }
                    }
                    Utilities.data.set("chunks", new ArrayList<>());
                    Utilities.saveDataFile();
                    Utilities.reloadDataFile();
                    Utilities.msg(s, "&aAll marked chunks have been released.");
                } else {
                    Utilities.msg(s, Strings.RELEASEALLUSAGE);
                }
            } else {
                Utilities.msg(s, Strings.NOPERM);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        if (args[0].equals("releaseall")) {
            if (s.hasPermission("keepchunks.releaseall")) {
                tabs.clear();
            }
            return CommandWrapper.filterTabs(tabs, args);
        }
        return null;
    }
}