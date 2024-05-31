package com.geitenijs.keepchunks;

import java.util.Arrays;
import java.util.List;

public class Strings {

    public static final String PLUGINCOLOURED = "&2&lKeep&8&lChunks";
    public static final String IGFULLPREFIX = PLUGINCOLOURED + " &7&l» ";
    public static final String IGPREFIX = "&2&lK&8&lC &7&l» ";
    public static final String VERSION = "1.7.2";
    public static final String AUTHOR = "Geitenijs";
    public static final String NOPERM = Strings.IGPREFIX + "&cYou don't have permission to do that.";
    public static final String UNUSABLE = Strings.IGPREFIX + "&cOne or more values you've entered are unusable.";
    public static final String ONLYCONSOLE = Strings.IGPREFIX + "&cYou can only do that from the console.";
    public static final String ONLYPLAYER = Strings.IGPREFIX + "&cYou can only do that as an in-game player.";
    public static final String WEFIRST = Strings.IGPREFIX + "&cPlease select an area with WorldEdit first.";
    public static final String NOWE = Strings.IGPREFIX + "&cPlease install WorldEdit 7.0.0+ or FAWE 2.0.0+ first.";
    public static final String NOWG = Strings.IGPREFIX + "&cPlease install WorldGuard 7.0.0+ first.";
    public static final String LINE = "&8&m----------&r " + PLUGINCOLOURED + " &8&m----------";
    public static final String HELPUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc help";
    public static final String RELOADUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc reload";
    public static final String LISTUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc list";
    public static final String CHUNKINFOUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc chunkinfo &f(&ccoords <x> <z> <world> &f|&c coords <x1> <z1> <x2> <z2> <world> &f|&c current &f|&c worldedit &f|&c worldguard <region> <world>&f)";
    public static final String KEEPCHUNKUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc keepchunk &f(&ccoords <x> <z> <world> &f|&c current&f)";
    public static final String KEEPREGIONUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc keepregion &f(&ccoords <x1> <z1> <x2> <z2> <world> &f|&c worldedit &f| &cworldguard <region> <world>&f)";
    public static final String KEEPRAILUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc keeprail &f(&ccoords <x> <y> <z> <world> &f|&c current&f)";
    public static final String RELEASEALLUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc releaseall";
    public static final String RELEASECHUNKUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc releasechunk &f(&ccoords <x> <z> <world> &f|&c current&f)";
    public static final String RELEASERAILUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc releaserail &f(&ccoords <x> <y> <z> <world> &f|&c current&f)";
    public static final String RELEASEREGIONUSAGE = Strings.IGPREFIX + "&fUsage: &c/kc releaseregion &f(&ccoords <x1> <z1> <x2> <z2> <world> &f|&c worldedit &f| &cworldguard <region> <world>&f)";
    static final String PLUGIN = "KeepChunks";
    static final String INTERNALPREFIX = "[KeepChunks] ";
    static final int RESOURCEID = 23307;
    static final String WEBSITE = "https://www.spigotmc.org/resources/" + Strings.RESOURCEID;
    static final List<String> ASCIILOGO = Arrays.asList(
            "",
            " _     _                   _______ _                 _          ",
            "(_)   | |                 (_______) |               | |  v" + Strings.VERSION,
            " _____| |_____ _____ ____  _      | |__  _   _ ____ | |  _  ___ ",
            "|  _   _) ___ | ___ |  _ \\| |     |  _ \\| | | |  _ \\| |_/ )/___)",
            "| |  \\ \\| ____| ____| |_| | |_____| | | | |_| | | | |  _ (|___ |",
            "|_|   \\_)_____)_____)  __/ \\______)_| |_|____/|_| |_|_| \\_|___/ ",
            "                    |_|                                         ",
            ""
    );
    static final String NOSTAT = "None";
}
