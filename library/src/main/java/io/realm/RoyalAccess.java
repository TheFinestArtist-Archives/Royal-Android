package io.realm;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import io.realm.internal.ColumnType;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;

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
//        return object.row == null ? null : object.row.getTable();
        if (object.row != null)
            return object.row.getTable();
        return null;
    }

    public static void drawTable(@NonNull RealmObject object) {
        Table table = getTable(object);
        if (table == null)
            throw new IllegalStateException("Object has no table.");

        for (int i = 0; i < table.getColumnCount(); i++) {
            ColumnType columnType = table.getColumnType(i);
            switch (columnType) {
                case BOOLEAN:
                    Logger.d(columnType.toString() + " : " + object.row.getBoolean(i));
                    break;
                case INTEGER:
                    Logger.d(columnType.toString() + " : " + object.row.getLong(i));
                    break;
                case FLOAT:
                    Logger.d(columnType.toString() + " : " + object.row.getFloat(i));
                    break;
                case DOUBLE:
                    Logger.d(columnType.toString() + " : " + object.row.getDouble(i));
                    break;
                case STRING:
                    Logger.d(columnType.toString() + " : " + object.row.getString(i));
                    break;
                case BINARY:
                    Logger.d(columnType.toString() + " : " + object.row.getBinaryByteArray(i).toString());
                    break;
                case DATE:
                    Logger.d(columnType.toString() + " : " + object.row.getDate(i));
                    break;
                case TABLE:
                    break;
                case MIXED:
                    break;
                case LINK:
                    Logger.d(columnType.toString() + " : " + object.row.getLink(i));
                    break;
                case LINK_LIST:
                    Logger.d(columnType.toString() + " : " + object.row.getLinkList(i));
                    break;
            }
        }
    }

    public static boolean isProxy(@NonNull RealmObject object) {
        return object instanceof RealmObjectProxy;
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

    // TODO: Free RealmObject from Realm


    // TODO: Clone RealmObject without Realm information
    // TODO: Problem #1 Serializing RealmObject has same speed of just committing it using Realm

}
