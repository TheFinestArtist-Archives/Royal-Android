package io.realm;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.UncheckedRow;

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

    public static Table getTable(@NonNull RealmObject object) {
        return object.row == null ? null : object.row.getTable();
    }

    public static boolean isProxy(@NonNull RealmObject object) {
        return object instanceof RealmObjectProxy;
    }

    public static boolean hasPrimaryKey(@NonNull Realm realm, @NonNull RealmObject object) {
        return realm.getTable(object.getClass()).hasPrimaryKey();
    }

    public static Class getClass(@NonNull Realm realm, @NonNull Table table) {
        List<Class<? extends RealmObject>> classes = realm.getConfiguration().getSchemaMediator().getModelClasses();
        for (Class<? extends RealmObject> clazz: classes)
            if (realm.getTable(clazz).getName().equals(table.getName()))
                return clazz;

        return null;
    }

    public static <E extends RealmObject> E get(@NonNull Realm realm, Class<E> clazz, long rowIndex) {
        Table table = realm.getTable(clazz);
        UncheckedRow row = table.getUncheckedRow(rowIndex);
        E result = realm.getConfiguration().getSchemaMediator().newInstance(clazz);
        result.row = row;
        result.realm = realm;
        return result;
    }

    public static RealmObject get(@NonNull Realm realm, @NonNull Table table, long rowIndex) {
        UncheckedRow row = table.getUncheckedRow(rowIndex);
        RealmObject result = realm.getConfiguration().getSchemaMediator().newInstance(getClass(realm, table));
        result.row = row;
        result.realm = realm;
        return result;
    }

    // TODO: Free RealmObject from Realm


    // TODO: Clone RealmObject without Realm information
    // TODO: Problem #1 Serializing RealmObject has same speed of just committing it using Realm

}
