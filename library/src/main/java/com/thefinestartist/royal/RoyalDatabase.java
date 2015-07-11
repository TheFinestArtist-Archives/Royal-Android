package com.thefinestartist.royal;

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

    public Object getModules() {
        return Realm.getDefaultModule();
    }

    @Override
    public long execute(Realm realm, long version) {
        return getVersion();
    }
}
