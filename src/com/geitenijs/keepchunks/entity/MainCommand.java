package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends KeepChunkBaseCommand{

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        msgCommandSender(s, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION);
        msgCommandSender(s, Strings.GAMEPREFIX + "&fMade by &6" + Strings.AUTHOR + "&f, since 2015");
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