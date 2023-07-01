package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.commands.CommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends KeepChunkBaseCommand{

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length != 1) {
            msgCommandSender(s, Strings.HELPUSAGE);
            return false;
        }
        msgCommandSender(s,"""
            &8/&akc help  &7-&f  Shows this list
            &8/&akc reload  &7-&f  Reload the plugin
            &8/&akc list  &7-&f  List all marked chunks
            &8/&akc chunkinfo  &7-&f  Info about chunks
            &8/&akc keepchunk  &7-&f  Keep a single chunk loaded
            &8/&akc keepregion  &7-&f  Keep multiple chunks loaded
            &8/&akc keeprail  &7-&f  Keep a railroad loaded
            &8/&akc releaseall  &7-&f  Release all marked chunks
            &8/&akc releasechunk  &7-&f  Release a single chunk
            &8/&akc releaseregion  &7-&f  Release multiple chunks
            &8/&akc releaserail  &7-&f  Release a railroad
        """);
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}