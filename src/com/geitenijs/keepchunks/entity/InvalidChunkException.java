package com.geitenijs.keepchunks.entity;

/**
 * Represents any error regarding the reference of or attempt to create references to an invalid chunk
 * If you encounter this error, this means that you are attempting to mark/unmark/interact with areas on the server that do not exist
 * @apiNote A chunk is considered invalid if any of the x/z/world name are unusable or do not correspond to a real area on the server
 */
public class InvalidChunkException extends Exception {
    public InvalidChunkException(){
        super();
    }
    public InvalidChunkException(String args, Exception e) {
        super(args,e);
    }
}
