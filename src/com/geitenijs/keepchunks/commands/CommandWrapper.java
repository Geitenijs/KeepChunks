package com.geitenijs.keepchunks.commands;

import com.geitenijs.keepchunks.Strings;
import com.geitenijs.keepchunks.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandWrapper implements CommandExecutor, TabCompleter {
    private CommandExecutor MainCommand;
    private CommandExecutor HelpCommand;
    private CommandExecutor ReloadCommand;
    private CommandExecutor ListCommand;
    private CommandExecutor ChunkinfoCommand;
    private CommandExecutor KeepchunkCommand;
    private CommandExecutor KeepregionCommand;
    private CommandExecutor ReleaseallCommand;
    private CommandExecutor ReleasechunkCommand;
    private CommandExecutor ReleaseregionCommand;
    private TabCompleter MainTab;
    private TabCompleter HelpTab;
    private TabCompleter ReloadTab;
    private TabCompleter ListTab;
    private TabCompleter ChunkinfoTab;
    private TabCompleter KeepchunkTab;
    private TabCompleter KeepregionTab;
    private TabCompleter ReleaseallTab;
    private TabCompleter ReleasechunkTab;
    private TabCompleter ReleaseregionTab;

    public CommandWrapper() {
        MainCommand = new Command_Main();
        HelpCommand = new Command_Help();
        ReloadCommand = new Command_Reload();
        ListCommand = new Command_List();
        ChunkinfoCommand = new Command_Chunkinfo();
        KeepchunkCommand = new Command_Keepchunk();
        KeepregionCommand = new Command_Keepregion();
        ReleaseallCommand = new Command_Releaseall();
        ReleasechunkCommand = new Command_Releasechunk();
        ReleaseregionCommand = new Command_Releaseregion();
        MainTab = new Command_Main();
        HelpTab = new Command_Help();
        ReloadTab = new Command_Reload();
        ListTab = new Command_List();
        ChunkinfoTab = new Command_Chunkinfo();
        KeepchunkTab = new Command_Keepchunk();
        KeepregionTab = new Command_Keepregion();
        ReleaseallTab = new Command_Releaseall();
        ReleasechunkTab = new Command_Releasechunk();
        ReleaseregionTab = new Command_Releaseregion();
    }

    public static ArrayList<String> filterTabs(ArrayList<String> list, String[] origArgs) {
        if (origArgs.length == 0)
            return list;

        Iterator<String> itel = list.iterator();
        String label = origArgs[origArgs.length - 1].toLowerCase();

        while (itel.hasNext()) {
            String name = itel.next();

            if (name.toLowerCase().startsWith(label))
                continue;

            itel.remove();
        }

        return list;
    }

    public static String[] getArgs(String[] args) {
        ArrayList<String> newArgs = new ArrayList<>();

        for (int i = 0; i < args.length - 1; i++) {
            String s = args[i];

            if (s.trim().isEmpty())
                continue;

            newArgs.add(s);
        }

        return newArgs.toArray(new String[0]);
    }

    public boolean onCommand(final CommandSender s, final Command c, final String label, final String[] args) {
        if (args.length == 0) {
            return MainCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("help")) {
            return HelpCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("reload")) {
            return ReloadCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("list")) {
            return ListCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("chunkinfo")) {
            return ChunkinfoCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("keepchunk")) {
            return KeepchunkCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("keepregion")) {
            return KeepregionCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releaseall")) {
            return ReleaseallCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releasechunk")) {
            return ReleasechunkCommand.onCommand(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releaseregion")) {
            return ReleaseregionCommand.onCommand(s, c, label, args);
        } else {
            Utilities.msg(s, Strings.GAMEPREFIX + "&cThat command does not exist.");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        if (args.length == 1) {
            return MainTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("help")) {
            return HelpTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("reload")) {
            return ReloadTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("list")) {
            return ListTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("chunkinfo")) {
            return ChunkinfoTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("keepchunk")) {
            return KeepchunkTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("keepregion")) {
            return KeepregionTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releaseall")) {
            return ReleaseallTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releasechunk")) {
            return ReleasechunkTab.onTabComplete(s, c, label, args);
        } else if (args[0].equalsIgnoreCase("releaseregion")) {
            return ReleaseregionTab.onTabComplete(s, c, label, args);
        }
        return null;
    }
}