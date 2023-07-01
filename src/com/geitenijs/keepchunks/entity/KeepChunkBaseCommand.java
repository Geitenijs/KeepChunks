package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.service.DatabaseService;
import com.geitenijs.keepchunks.service.PluginConfigurationService;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

abstract class KeepChunkBaseCommand implements CommandExecutor, TabCompleter {
    public DatabaseService databaseService = DatabaseService.getInstance();
    public PluginConfigurationService configurationService = PluginConfigurationService.getInstance();
    public abstract boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args);
    public abstract List<String> onTabComplete(CommandSender s, Command c, String label, String[] args);

    public void msgCommandSender(CommandSender s, String msg){
        if (s instanceof Player) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        } else {
            msg = ChatColor.translateAlternateColorCodes('&', Strings.INTERNALPREFIX + msg);
            if (!configurationService.colorfulConsoleEnabled) {
                msg = ChatColor.stripColor(msg);
            }
        }
        s.sendMessage(msg);
    }
}
