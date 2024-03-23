package com.geitenijs.keepchunks.entity;

import static com.geitenijs.keepchunks.Strings.*;
import com.geitenijs.keepchunks.service.ChunkDatabase;
import com.geitenijs.keepchunks.service.ChunkService;
import com.geitenijs.keepchunks.service.PluginConfigurationService;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * Defines the core behavior shared by all commands in the KeepChunks plugin
 * @apiNote You should extend this class whenever you want to make new commands for the plugin
 * @implNote Command responsibilities can heavily vary, and all have access to our plugin's "resources" (e.g: datastore/configuration singletons)
 */
public abstract class BasicKeepChunksCommand implements CommandExecutor, TabCompleter, KeepChunksCommand {
    //Provides access to the chunk database, for marking/unmarking force-loaded chunks
    public static final ChunkDatabase chunkDatabase = ChunkService.getInstance();

    //Provides access to plugin convars, which might influence a command's behavior
    public static final PluginConfigurationService configs = PluginConfigurationService.getInstance();

    /**
     *
     * @apiNote This method shall be the only safe solution for subclasses to write to the console as needed
     * @implNote This method concretely defines how all commands must write output to any console
     * @param s
     * @param msg
     */
    protected final void msgCommandSender(CommandSender s, String msg){
        msg = s instanceof Player ? ChatColor.translateAlternateColorCodes('&', msg)
                : ChatColor.translateAlternateColorCodes('&', PLUGIN_PREFIX + msg);
            if (!configs.colorfulConsoleEnabled) {
                msg = ChatColor.stripColor(msg);
        s.sendMessage(msg);
    }
}
