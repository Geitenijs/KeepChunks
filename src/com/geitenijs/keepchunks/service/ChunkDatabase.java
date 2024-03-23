package com.geitenijs.keepchunks.service;

import com.geitenijs.keepchunks.entity.InvalidChunkException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

/**
 *  Guarantees methods for interacting with the chunk database
 */
public interface ChunkDatabase {
    // Chunk Marking Methods
    void markChunk(int x, int z, String world) throws InvalidChunkException;

    void markChunks(final List<String> chunks) throws InvalidChunkException;

    // Unmarking Methods
    void unmarkChunk(int x, int z, String world) throws InvalidChunkException;
    void unmarkChunks(final List<String> chunkStrings) throws InvalidChunkException;
}
