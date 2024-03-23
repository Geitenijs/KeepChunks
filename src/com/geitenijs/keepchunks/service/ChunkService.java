package com.geitenijs.keepchunks.service;

import com.geitenijs.keepchunks.entity.Chunk;
import com.geitenijs.keepchunks.entity.InvalidChunkException;
import com.geitenijs.keepchunks.service.FileSystemClient.FileType;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * System responsible for marking/unmarking chunks for force loading
 */
public class ChunkService implements ChunkDatabase {
    private static ChunkService INSTANCE = null;
    private static int defaultDbSize = 50_000;

    private static int saveIntervalSeconds = 300;

    /**
     * Provides the current configurations/settings for the plugin
     */
    private static PluginConfigurationService configs = PluginConfigurationService.getInstance();

    /**
     * Manages the chunk database file that is stored on disk
     */
    private static FileSystemClient fileSystem = FileSystemClient.getInstance();

    /**
     * Manages the chunk database that is stored in memory
     */
    private HashSet<Chunk> database = new HashSet<>(defaultDbSize);

    private ChunkService() {
        loadDbFromFile();
    }

    public static ChunkService getInstance(){
        if(INSTANCE == null)
            INSTANCE = new ChunkService();
        return INSTANCE;
    }

    /**
     * Determines if this chunk is already marked by the plugin
     * @param c
     * @return
     */
    public boolean isMarked(org.bukkit.Chunk c){
        return database.contains(new Chunk(c));
    }

    /**
     * Marking Operations
     */

    /**
     * Given a chunk, set it to be force loaded and update the database
     */
    public void markChunk(org.bukkit.Chunk c){
        c.setForceLoaded(true);
        database.add(new Chunk(c));
    }

    @Override
    public void markChunk(int x, int z, String world) throws InvalidChunkException {
        database.add(new Chunk(x,z,world));
    }

    /**
     *  Given a list of valid chunks, set them to be force loaded
     * @apiNote This operation is "all or nothing", and nothing will be force loaded in the event of {@link InvalidChunkException}
     */
    public void markChunks(List<String> chunksToAdd) throws InvalidChunkException {
        try {
            List<Chunk> validChunks = chunksToAdd.stream()
                    .map(Chunk::new)
                    .toList();
        }catch (InvalidChunkException e){
            throw e;
        }

        database.addAll(validChunks);
        if(configs.isDebuggingEnabled()){
            Bukkit.getLogger().info(String.format("Marked {} chunks: {} ", validChunks.size(), validChunks));
        }
    }

    @Override
    public void unmarkChunk(int x, int z, String world) {

    }

    /**
     * Unmarking Operations
     */

    /**
     * Unmark a chunk to no longer be force loaded and update the in-memory database
     * @apiNote If you're loading many chunks, use {@link this#markChunks(List)}
     */
    public void unmarkChunk(String chunkString) throws InvalidChunkException {
        Chunk c = new Chunk(chunkString);
        Bukkit.getWorld(c.getWorld()).setChunkForceLoaded(c.getX(), c.getZ(),false);
        database.add(c);
    }

    /**
     *  Given a list of valid chunks, set them to be force loaded
     */
    public void unmarkChunks(List<String> chunksToAdd){
        List<Chunk> validChunks = chunksToAdd.stream()
                .map(Chunk::new)
                .collect(Collectors.toList());

        database.addAll(validChunks);
        writeDbToFile();
        if(configs.isDebuggingEnabled()){
            Bukkit.getLogger().info(String.format("Unmarked {} chunks: {} ", validChunks.size(), validChunks));
        }
    }

    /**
     * Disk Operations
     */

    /**
     * Loads the existing chunk database from the save file on disk
     */
    private void loadDbFromFile() {
        Set<Chunk> chunksToLoad = fileSystem.getFileAttributes(FileType.DATA, "chunks").stream()
                .map(Chunk::new)
                .collect(Collectors.toSet());
        database.addAll(chunksToLoad);
    }

    /**
     * TODO: Schedule this to be executed on an interval, avoiding surge/excessive writes
     */
    private void writeDbToFile(){
        fileSystem.saveFile(FileType.DATA);
    }
}
