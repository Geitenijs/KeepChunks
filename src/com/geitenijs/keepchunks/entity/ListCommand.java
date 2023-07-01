package com.geitenijs.keepchunks.entity;

import com.geitenijs.keepchunks.Utilities;
import com.geitenijs.keepchunks.commands.CommandWrapper;
import com.geitenijs.keepchunks.service.DatabaseService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

public class ListCommand extends KeepChunkCommand{
    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
        final boolean noChunksAreMarked = DatabaseService.getInstance().getChunks().isEmpty();

        if(args.length != 1){
            //Error
        }
        if(noChunksAreMarked){
            msgCommandSender(s,"&cThere don't seem to be any marked chunks.");
            return true;
        }
        //Generate marked chunk list message
        msgCommandSender(s, "&aA list of all marked chunks will be shown below.");
        msgCommandSender(s, "&7---");
        DatabaseService.getInstance().getChunks().forEach(chunk -> {
            msgCommandSender(s, String.format("&fChunk &9({},{}) &f in world: &6'{}'&f.",
                chunk.getX(),chunk.getZ(),chunk.getWorld()));
        });
        msgCommandSender(s, "&7---");
        msgCommandSender(s, "&aA total of &f" + DatabaseService.getInstance().getChunks().size() + "&a chunks are currently marked.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        ArrayList<String> tabs = new ArrayList<>();
        tabs.clear();
        return CommandWrapper.filterTabs(tabs, args);
    }
}
