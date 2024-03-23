package com.geitenijs.keepchunks.entity;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * A simple wrapper over a Bukkit Chunk {@see org.bukkit.Chunk}
 * @apiNote This class should be referenced anywhere a chunk is manipulated/observed in the plugin
 * @implNote This mainly allows us to be concise about how we use the Spigot API but leaves room in the future to add our own metadata as needed
 */
@Data
public class Chunk {
    private int x;
    private int z;
    private String world;

    private final Chunk[] adjacentChunks = getAdjacentChunks();

    //TODO: Are we allowed to store this?
    private final org.bukkit.Chunk srcChunkData;

    /**
     * Instantiates using pre-validated chunk data from the Bukkit API
     * @implNote org.bukkit.Chunk guarantees valid data and thus doesn't throw {@link InvalidChunkException}
     * @param c
     */
    public Chunk(org.bukkit.Chunk c){
        x = c.getX();
        z = c.getZ();
        world = c.getWorld().toString();
        srcChunkData = c;
    }

    /**
     * Instantiates using the chunk information provided as a pound (#) delimited string
     * @throws If the chunk string was invalid or the chunk coordinates to not correspond with a real place in the world
     * @param chunkString
     */
    public Chunk(String chunkString) throws InvalidChunkException {
        try{
            String[] chunkData = chunkString.split("#");
            x = Integer.parseInt(chunkData[0]);
            z = Integer.parseInt(chunkData[1]);
            world = chunkData[2];
            srcChunkData = Bukkit.getWorld(world).getChunkAt(x, z);
        }catch (Exception e){
            throw new InvalidChunkException("Unusable chunk data provided: X: %s, Z: %s, World: %s".formatted(x,z,world),e);
        }
    }

    public Chunk(int x, int z, String world) throws InvalidChunkException {
        try {
            srcChunkData = Bukkit.getWorld(world).getChunkAt(x, z);
            this.x = x;
            this.z = z;
            this.world = world;
        }catch (Exception e){
            throw new InvalidChunkException("Unusable chunk data provided: X: %s, Z: %s, World: %s".formatted(x,z,world),e);
        }
    }

    /**
     * Returns the four cardinally adjacent chunks to this one, indexed sequentially as: NSEW
     * @apiNote In the rare cases where one or more chunks are NOT present (e.g: Near the world border), null shall be returned for those values
     * @return
     */
    private Chunk[] getAdjacentChunks(){
        final Chunk[] neighbors = new Chunk[4];
        final World w = Bukkit.getWorld(world);
        for(int i  = 0; i < 4; i++){
            neighbors[i] = new Chunk(switch (i){
                case 0 -> w.getChunkAt(x - 1, z);
                case 1 -> w.getChunkAt(x + 1, z);
                case 2 -> w.getChunkAt(x, z - 1);
                case 3 -> w.getChunkAt(x, z + 1);
                default -> throw new IllegalStateException("Unexpected value: " + i);
            });
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object o){
        Chunk other = (Chunk) o;
        return other instanceof Chunk && other.world.equalsIgnoreCase(this.world) && other.x == this.x && other.z == this.z;
    }
}
