package com.thefinestartist.royal;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmMigration;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public abstract class RoyalDatabase implements RealmMigration {

    public String getFileName() {
        return "default";
    }

    public boolean forCache() {
        return false;
    }

    public byte[] getEncryptionKey() {
        return null;
    }

    public int getVersion() {
        return 0;
    }

    public boolean shouldDeleteIfMigrationNeeded() {
        return false;
    }

    // TODO: change it like getModels
    public Set<Object> getModules() {
        Set<Object> set = new HashSet<>();
        set.add(Realm.getDefaultModule());
        return set;
    }

    @Override
    public long execute(Realm realm, long version) {
        return getVersion();
    }
}
