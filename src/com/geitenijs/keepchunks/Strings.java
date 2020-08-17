package com.geitenijs.keepchunks;

public class Strings {

    static final String PLUGIN = "KeepChunks";
    static final String INTERNALPREFIX = "[KeepChunks] ";
    public static final String DEBUGPREFIX = "[DEBUG] ";
    public static final String GAMEPREFIX = "&2Keep&8Chunks &7// ";
    public static final String VERSION = "1.6.6";
    public static final String AUTHOR = "Geitenijs";
    public static final String COPYRIGHT = "2015-2020";
    static final int RESOURCEID = 23307;
    static final String WEBSITE = "https://www.spigotmc.org/resources/" + Strings.RESOURCEID;
    static final String ASCIILOGO = "" +
            "\n _     _                   _______ _                 _          " +
            "\n(_)   | |                 (_______) |               | |  v" + Strings.VERSION +
            "\n _____| |_____ _____ ____  _      | |__  _   _ ____ | |  _  ___ " +
            "\n|  _   _) ___ | ___ |  _ \\| |     |  _ \\| | | |  _ \\| |_/ )/___)" +
            "\n| |  \\ \\| ____| ____| |_| | |_____| | | | |_| | | | |  _ (|___ |" +
            "\n|_|   \\_)_____)_____)  __/ \\______)_| |_|____/|_| |_|_| \\_|___/ " +
            "\n                    |_|                                         " +
            "\n\n";

    public static final String NOPERM = Strings.GAMEPREFIX + "&cYou don't have permission to do that.";
    public static final String UNUSABLE = "&cOne or more values you've entered are unusable.";
    public static final String ONLYCONSOLE = "&cYou can only do that from the console.";
    public static final String ONLYPLAYER = "&cYou can only do that as an in-game player.";
    static final String NOSTAT = "None";
    public static final String WEFIRST = "&cYou have to select an area with WorldEdit first.";
    public static final String NOWE = "&cThis requires you to have WorldEdit installed.";
    public static final String NOWG = "&cThis requires you to have WorldGuard installed.";
    public static final String UPDATEWE = "&cThis requires you to have WorldEdit version 7.0.0 or newer installed.";
    public static final String UPDATEWG = "&cThis requires you to have WorldGuard version 7.0.0 or newer installed.";

    static final String DEPENDENCIES_WE_COMPATIBLE = "Found a compatible version of WorldEdit! (7.0.0+)";
    static final String DEPENDENCIES_WE_INCOMPATIBLE = "The required version of WorldEdit for " + Strings.PLUGIN + " v" + Strings.VERSION + " is 7.0.0 or newer.";
    static final String DEPENDENCIES_WE_MISSING = "WorldEdit plugin not found. " + Strings.PLUGIN + " will have reduced functionality.";
    static final String DEPENDENCIES_WG_COMPATIBLE = "Found a compatible version of WorldGuard! (7.0.0+)";
    static final String DEPENDENCIES_WG_INCOMPATIBLE = "The required version of WorldGuard for " + Strings.PLUGIN + " v" + Strings.VERSION + " is 7.0.0 or newer.";
    static final String DEPENDENCIES_WG_MISSING = "WorldGuard plugin not found. " + Strings.PLUGIN + " will have reduced functionality.";

    public static final String HELPUSAGE = "&fUsage: &c/kc help";
    public static final String RELOADUSAGE = "&fUsage: &c/kc reload";
    public static final String LISTUSAGE = "&fUsage: &c/kc list";
    public static final String CHUNKINFOUSAGE = "&fUsage: &c/kc chunkinfo &f(&ccoords <x> <z> <world> &f|&c current &f|&c worldedit &f| &cworldguard <region> <world>&f)";
    public static final String KEEPCHUNKUSAGE = "&fUsage: &c/kc keepchunk &f(&ccoords <x> <z> <world> &f|&c current&f)";
    public static final String KEEPREGIONUSAGE = "&fUsage: &c/kc keepregion &f(&ccoords <x1> <z1> <x2> <z2> <world> &f|&c worldedit &f| &cworldguard <region> <world>&f)";
    public static final String KEEPRAILUSAGE = "&fUsage: &c/kc keeprail &f(&ccoords <x> <y> <z> <world> &f|&c current&f)";
    public static final String RELEASEALLUSAGE = "&fUsage: &c/kc releaseall";
    public static final String RELEASECHUNKUSAGE = "&fUsage: &c/kc releasechunk &f(&ccoords <x> <z> <world> &f|&c current&f)";
    public static final String RELEASERAILUSAGE = "&fUsage: &c/kc releaserail &f(&ccoords <x> <y> <z> <world> &f|&c current&f)";
    public static final String RELEASEREGIONUSAGE = "&fUsage: &c/kc releaseregion &f(&ccoords <x1> <z1> <x2> <z2> <world> &f|&c worldedit &f| &cworldguard <region> <world>&f)";
}
