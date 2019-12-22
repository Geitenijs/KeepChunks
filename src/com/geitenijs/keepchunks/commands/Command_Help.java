package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Command_Help implements CommandExecutor, TabCompleter {

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 1) {
            Utilities.msg(s, "&8/&akc help  &7-&f  Shows this list");
            Utilities.msg(s, "&8/&akc reload  &7-&f  Reload the plugin");
            Utilities.msg(s, "&8/&akc list  &7-&f  List all marked chunks");
            Utilities.msg(s, "&8/&akc chunkinfo  &7-&f  Info about chunks");
            Utilities.msg(s, "&8/&akc keepchunk  &7-&f  Keep a single chunk loaded");
            Utilities.msg(s, "&8/&akc keepregion  &7-&f  Keep multiple chunks loaded");
            Utilities.msg(s, "&8/&akc keeprail  &7-&f  Keep a railroad loaded");
            Utilities.msg(s, "&8/&akc releaseall  &7-&f  Release all marked chunks");
            Utilities.msg(s, "&8/&akc releasechunk  &7-&f  Release a single chunk");
            Utilities.msg(s, "&8/&akc releaseregion  &7-&f  Release multiple chunks");
            Utilities.msg(s, "&8/&akc releaserail  &7-&f  Release a railroad");
        } else {
            Utilities.msg(s, Strings.HELPUSAGE);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        return CommandWrapper.filterTabs(tabs, args);
    }
}