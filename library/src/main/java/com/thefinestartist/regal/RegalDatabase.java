package com.thefinestartist.regal;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public abstract class RegalDatabase {

    public String getFileName() {
        return null;
    }

    public boolean forCache() {
        return false;
    }

    public byte[] getEncryptionKey() {
        return null;
    }
}
