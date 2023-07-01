package com.geitenijs.keepchunks.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Chunk {
    private int x;
    private int z;
    private String world;

    /**
     * Constructor for a pre-validated chunk string.
     * @param chunkString
     * @throws IllegalArgumentException
     */
    public Chunk(String[] chunkString){
        x = Integer.parseInt(chunkString[0]);
        z = Integer.parseInt(chunkString[1]);
        world = chunkString[2];
    }
}
