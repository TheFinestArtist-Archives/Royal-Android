package io.realm;

import android.support.annotation.NonNull;

import io.realm.internal.Row;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class RoyalAccess {

    public static Realm getRealm(@NonNull RealmObject object) {
        return object.realm;
    }

    public static Row getRow(@NonNull RealmObject object) {
        return object.row;
    }

    public static void clearObject(@NonNull RealmObject object) {
        Realm realm = object.realm;
        if (realm != null) {
            realm.beginTransaction();
            object.removeFromRealm();
            object.realm = null;
            object.row = null;
            realm.commitTransaction();
        }
    }

    public static boolean hasPrimaryKey(@NonNull Realm realm, @NonNull RealmObject object) {
        return realm.getTable(object.getClass()).hasPrimaryKey();
    }

}
