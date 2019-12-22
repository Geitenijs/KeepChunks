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
    private CommandExecutor KeeprailCommand;
    private CommandExecutor ReleaseallCommand;
    private CommandExecutor ReleasechunkCommand;
    private CommandExecutor ReleaseregionCommand;
    private CommandExecutor ReleaserailCommand;
    private TabCompleter MainTab;
    private TabCompleter HelpTab;
    private TabCompleter ReloadTab;
    private TabCompleter ListTab;
    private TabCompleter ChunkinfoTab;
    private TabCompleter KeepchunkTab;
    private TabCompleter KeepregionTab;
    private TabCompleter KeeprailTab;
    private TabCompleter ReleaseallTab;
    private TabCompleter ReleasechunkTab;
    private TabCompleter ReleaseregionTab;
    private TabCompleter ReleaserailTab;

    public CommandWrapper() {
        MainCommand = new Command_Main();
        HelpCommand = new Command_Help();
        ReloadCommand = new Command_Reload();
        ListCommand = new Command_List();
        ChunkinfoCommand = new Command_Chunkinfo();
        KeepchunkCommand = new Command_Keepchunk();
        KeepregionCommand = new Command_Keepregion();
        KeeprailCommand = new Command_Keeprail();
        ReleaseallCommand = new Command_Releaseall();
        ReleasechunkCommand = new Command_Releasechunk();
        ReleaseregionCommand = new Command_Releaseregion();
        ReleaserailCommand = new Command_Releaserail();
        MainTab = new Command_Main();
        HelpTab = new Command_Help();
        ReloadTab = new Command_Reload();
        ListTab = new Command_List();
        ChunkinfoTab = new Command_Chunkinfo();
        KeepchunkTab = new Command_Keepchunk();
        KeepregionTab = new Command_Keepregion();
        KeeprailTab = new Command_Keeprail();
        ReleaseallTab = new Command_Releaseall();
        ReleasechunkTab = new Command_Releasechunk();
        ReleaseregionTab = new Command_Releaseregion();
        ReleaserailTab = new Command_Releaserail();
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
        if (c.getName().equalsIgnoreCase("keepchunks") || c.getName().equalsIgnoreCase("kc")) {
            if (args.length == 0) {
                return MainCommand.onCommand(s, c, label, args);
            } else if (args[0].equalsIgnoreCase("help")) {
                if (s.hasPermission("keepchunks.help")) {
                    return HelpCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (s.hasPermission("keepchunks.reload")) {
                    return ReloadCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (s.hasPermission("keepchunks.list")) {
                    return ListCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("chunkinfo")) {
                if (s.hasPermission("keepchunks.chunkinfo")) {
                    return ChunkinfoCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("keepchunk")) {
                if (s.hasPermission("keepchunks.keepchunk")) {
                    return KeepchunkCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("keepregion")) {
                if (s.hasPermission("keepchunks.keepregion")) {
                    return KeepregionCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("keeprail")) {
                if (s.hasPermission("keepchunks.keeprail")) {
                    return KeeprailCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("releaseall")) {
                if (s.hasPermission("keepchunks.releaseall")) {
                    return ReleaseallCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("releasechunk")) {
                if (s.hasPermission("keepchunks.releasechunk")) {
                    return ReleasechunkCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("releaseregion")) {
                if (s.hasPermission("keepchunks.releaseregion")) {
                    return ReleaseregionCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else if (args[0].equalsIgnoreCase("releaserail")) {
                if (s.hasPermission("keepchunks.releaserail")) {
                    return ReleaserailCommand.onCommand(s, c, label, args);
                } else {
                    Utilities.msg(s, Strings.NOPERM);
                }
            } else {
                Utilities.msg(s, Strings.GAMEPREFIX + "&cThat command does not exist.");
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender s, Command c, String label, String[] args) {
        if (c.getName().equalsIgnoreCase("keepchunks") || c.getName().equalsIgnoreCase("kc")) {
            if (args.length == 1) {
                if (s.hasPermission("keepchunks.help")) {
                    return MainTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                if (s.hasPermission("keepchunks.help")) {
                    return HelpTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (s.hasPermission("keepchunks.reload")) {
                    return ReloadTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (s.hasPermission("keepchunks.list")) {
                    return ListTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("chunkinfo")) {
                if (s.hasPermission("keepchunks.chunkinfo")) {
                    return ChunkinfoTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("keepchunk")) {
                if (s.hasPermission("keepchunks.keepchunk")) {
                    return KeepchunkTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("keepregion")) {
                if (s.hasPermission("keepchunks.keepregion")) {
                    return KeepregionTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("keeprail")) {
                if (s.hasPermission("keepchunks.keeprail")) {
                    return KeeprailTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("releaseall")) {
                if (s.hasPermission("keepchunks.releaseall")) {
                    return ReleaseallTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("releasechunk")) {
                if (s.hasPermission("keepchunks.releasechunk")) {
                    return ReleasechunkTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("releaseregion")) {
                if (s.hasPermission("keepchunks.releaseregion")) {
                    return ReleaseregionTab.onTabComplete(s, c, label, args);
                }
            } else if (args[0].equalsIgnoreCase("releaserail")) {
                if (s.hasPermission("keepchunks.releaserail")) {
                    return ReleaserailTab.onTabComplete(s, c, label, args);
                }
            }
        }
        return null;
    }
}