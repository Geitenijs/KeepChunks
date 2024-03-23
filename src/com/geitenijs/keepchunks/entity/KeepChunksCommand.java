package com.geitenijs.keepchunks.entity;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 *  Guarantees common behaviors shared by all commands in the plugin
 */
public interface KeepChunksCommand {
    /**
     * Return the enumerated name of this command
     * @return
     */
    CommandName getCommandName();
    /**
     * Returns a console safe string containing useful information/examples on how to run the command
     * @apiNote This output is intended for delivery to the end user
     * @return
     */
    String getCommandUsage();

    /**
     * Defines the runtime "behavior" for the command (e.g: When executed, what should it do?)
     * @param sender The executor of this command (An in-game player or NPE)
     * @param cmd The Bukkit alias for this command (contains some situationally useful context)
     * @param label The UI label(?) that this command had?
     * @param args Arguments for the command to interpret
     * @return True if the command executed properly
     */
    boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args);

    /**
     * Defines the tab completion behavior and any autocompletion values
     * @param sender
     * @param cmd
     * @param label
     * @param args
     * @return
     */
    List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args);
}
