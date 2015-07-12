package com.thefinestartist.royal.databases;

import com.thefinestartist.royal.RoyalDatabase;
import com.thefinestartist.royal.entities.Cat;
import com.thefinestartist.royal.entities.Dog;
import com.thefinestartist.royal.entities.Owner;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.annotations.RealmModule;

/**
 * Created by TheFinestArtist on 7/12/15.
 */
public class SecondaryDatabase extends RoyalDatabase {

    public String getFileName() {
        return "secondary";
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

    public Set<Object> getModules() {
        Set<Object> set = new HashSet<>();
        set.add(new SecondaryModule());
        return set;
    }

    @Override
    public long execute(Realm realm, long version) {
        return getVersion();
    }

    @RealmModule(classes = {Dog.class, Cat.class, Owner.class})
    public static class SecondaryModule {
    }
}
