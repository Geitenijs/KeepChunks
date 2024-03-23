package com.geitenijs.keepchunks.entity;

/**
 * Enumerates all possible command names in the plugin
 */
public enum CommandName {
    //General Commands
    HELP,
    CHUNK_INFO,
    LIST_CHUNKS,

    //Loading Commands
    KEEP_CHUNK,
    KEEP_RAIL,
    KEEP_REGION,

    //Unloading Commands
    RELEASE_ALL,
    RELEASE_CHUNK,
    RELEASE_RAIL,
    RELEASE_REGION
}
