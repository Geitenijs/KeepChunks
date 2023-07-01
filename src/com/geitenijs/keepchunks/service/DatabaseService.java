package com.geitenijs.keepchunks.service;

import com.geitenijs.keepchunks.entity.Chunk;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

/**
 * System responsible for marking/unmarking chunks for force loading
 */
public class DatabaseService {
    private static DatabaseService INSTANCE = null;
    private FileSystemService fileSystemService = FileSystemService.getInstance();
    private PluginConfigurationService configurationService;
    private Set<Chunk> chunks = new HashSet<>();

    private DatabaseService() {
        loadChunksFromDataFile(fileSystemService.getPluginData().getStringList("chunks"));
    }

    public static DatabaseService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DatabaseService();
            return INSTANCE;
        }
        return INSTANCE;
    }

    /**
     * API Method to safely mark an in-game chunk for force loading.
     * @implNote Changes are immediately saved to the data file
     * @param chunksToAdd
     */
    public void markChunks(List<String> chunksToAdd){
        //Determine valid chunks
        List<Chunk> validChunks = chunksToAdd.stream().parallel()
                .map(chunkString -> validateChunkString(chunkString))
                .filter(chunkInfo -> chunkInfo != null)
                .map(Chunk::new)
                .collect(Collectors.toList());
        validChunks.forEach(chunk -> markChunk(chunk));

        //Add to database
        chunks.addAll(validChunks);
        fileSystemService.saveDataFile();
        if(configurationService.debuggingEnabled){
            Bukkit.getLogger().info(String.format("Marked {} chunks: {} ", validChunks.size(), validChunks));
        }
    }

    /**
     * API Method to safely unmark an in-game chunk for force loading.
     * @implNote Changes are immediately saved to the data file
     * @param chunksToRemove
     */
    public void unmarkChunks(List<String> chunksToRemove){
        //Determine valid chunks
        List<Chunk> validChunks = chunksToRemove.stream().parallel()
                .map(chunkString -> validateChunkString(chunkString))
                .filter(chunkInfo -> chunkInfo != null)
                .map(Chunk::new)
                .collect(Collectors.toList());
        validChunks.forEach(chunk -> unmarkChunk(chunk));

        //Remove from database
        chunks.removeAll(validChunks);
        fileSystemService.saveDataFile();

        if(configurationService.debuggingEnabled){
            Bukkit.getLogger().info(String.format("Unmarked {} chunks: {} ", validChunks.size(), validChunks));
        }
    }

    /** Validates any chunk inputs to the plugin.
     * @param chunkString A raw x#z#world String
     * @return The x/z/world info, if the chunk is considered valid
     */
    public static String[] validateChunkString(final String chunkString){
        try {
            String[] chunkInfo = chunkString.split("#");
            int x = Integer.parseInt(chunkInfo[0]);
            int z = Integer.parseInt(chunkInfo[1]);
            World world = Bukkit.getWorld(chunkInfo[2]);
            return world != null
                ? chunkInfo
                : null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * Parses all valid chunks in the save file and sets the
     */
    private void loadChunksFromDataFile(final List<String> chunkList){
        Bukkit.getLogger().info(String.format("Attempting to load all {} chunks from save file: {} ", chunkList.size(), chunkList));
        markChunks(chunkList);
    }

    /**
     * Marks a valid chunk to be force loaded.
     * Commands should use markChunks and unmarkChunks
     */
    private void markChunk(Chunk c){
        var world = Bukkit.getWorld(c.getWorld());
        world.setChunkForceLoaded(c.getX(), c.getZ(),true);
        chunks.add(c);
    }
    /**
     * Unmarks a valid chunk to no longer be force loaded.
     */
    private void unmarkChunk(Chunk c){
        var world = Bukkit.getWorld(c.getWorld());
        world.setChunkForceLoaded(c.getX(), c.getZ(),false);
        chunks.remove(c);
    }
}
