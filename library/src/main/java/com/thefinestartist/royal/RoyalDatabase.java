package com.thefinestartist.royal;

import java.util.ArrayList;
import java.util.List;

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
    public List<Object> getModules() {
        List<Object> modules = new ArrayList<>();
        modules.add(Realm.getDefaultModule());
        return modules;
    }

    @Override
    public long execute(Realm realm, long version) {
        return getVersion();
    }
}
