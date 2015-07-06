package io.realm;

import android.support.annotation.NonNull;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class RegalAccess {

//    public static void deleteRealm(@NonNull RealmObject object) {
//        object.realm = null;
//        object.row = null;
//    }

    public static Realm getRealm(@NonNull RealmObject object) {
        return object.realm;
    }

    public static boolean hasPrimaryKey(@NonNull Realm realm, @NonNull RealmObject object) {
        return realm.getTable(object.getClass()).hasPrimaryKey();
    }
}
