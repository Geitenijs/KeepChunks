package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Command_Main implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        Utilities.msg(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION);
        Utilities.msg(s, Strings.GAMEPREFIX + "&fMade by &6" + Strings.AUTHOR + "&f, © " + Strings.COPYRIGHT);
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("help");
        tabs.add("reload");
        tabs.add("list");
        tabs.add("chunkinfo");
        tabs.add("keepchunk");
        tabs.add("keepregion");
        tabs.add("keeprail");
        tabs.add("releaseall");
        tabs.add("releasechunk");
        tabs.add("releaseregion");
        tabs.add("releaserail");
        return CommandWrapper.filterTabs(tabs, args);
    }
}