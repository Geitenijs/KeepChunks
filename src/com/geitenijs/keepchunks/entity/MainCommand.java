package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends BasicKeepChunksCommand {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        msgCommandSender(sender, Strings.GAMEPREFIX + "&fRunning &9v" + Strings.VERSION);
        msgCommandSender(sender, Strings.GAMEPREFIX + "&fMade by &6" + Strings.AUTHOR + "&f, since 2015");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
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