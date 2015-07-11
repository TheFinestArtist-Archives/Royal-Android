package com.thefinestartist.royal;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RoyalAccess;
import io.realm.internal.ColumnType;
import io.realm.internal.LinkView;
import io.realm.internal.Row;
import io.realm.internal.Table;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class Rson {

    private static final Object lock = new Object();
    private static Gson gson;

    public static Gson getGson() {
        synchronized (lock) {
            if (gson != null)
                return gson;
            gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();
            return gson;
        }
    }

    public static String toJsonString(@NonNull RealmObject object) {
        return toJsonString(object, 1);
    }

    @SuppressWarnings("ConstantConditions")
    public static String toJsonString(@NonNull RealmObject object, int depth) {
        if (!RoyalAccess.isProxy(object)) {
            return getGson().toJson(object);
        } else {
            Realm realm = RoyalAccess.getRealm(object);
            Row row = RoyalAccess.getRow(object);
            Table table = RoyalAccess.getTable(object);
            long columnCount = table.getColumnCount();

            StringBuilder builder = new StringBuilder();
            if (columnCount > 0)
                builder.append("{");
            String prefix = "";
            for (int i = 0; i < columnCount; i++) {
                ColumnType columnType = table.getColumnType(i);

                switch (columnType) {
                    case BOOLEAN:
                        builder
                                .append(prefix)
                                .append("\"")
                                .append(table.getColumnName(i))
                                .append("\":")
                                .append(row.getBoolean(i));
                        prefix = ",";
                        break;
                    case INTEGER:
                        builder
                                .append(prefix)
                                .append("\"")
                                .append(table.getColumnName(i))
                                .append("\":")
                                .append(row.getLong(i));
                        prefix = ",";
                        break;
                    case FLOAT:
                        builder
                                .append(prefix)
                                .append("\"")
                                .append(table.getColumnName(i))
                                .append("\":")
                                .append(row.getFloat(i));
                        prefix = ",";
                        break;
                    case DOUBLE:
                        builder
                                .append(prefix)
                                .append("\"")
                                .append(table.getColumnName(i))
                                .append("\":")
                                .append(row.getDouble(i));
                        prefix = ",";
                        break;
                    case STRING:
                        String string = row.getString(i);
                        if (string != null) {
                            builder
                                    .append(prefix)
                                    .append("\"")
                                    .append(table.getColumnName(i))
                                    .append("\":\"")
                                    .append(string).append("\"");
                            prefix = ",";
                        }
                        break;
                    case BINARY:
                        builder
                                .append(prefix)
                                .append("\"")
                                .append(table.getColumnName(i))
                                .append("\":")
                                .append(Arrays.toString(row.getBinaryByteArray(i)));
                        prefix = ",";
                        break;
                    case DATE:
                        Date date = row.getDate(i);
                        if (date != null) {
                            // TODO: Date Formatting
                            builder
                                    .append(prefix)
                                    .append("\"")
                                    .append(table.getColumnName(i))
                                    .append("\":\"")
                                    .append(date.toString())
                                    .append("\"");
                            prefix = ",";
                        }
                        break;
                    case TABLE:
                        break;
                    case MIXED:
                        break;
                    case LINK:
                        if (depth > 0) {
                            builder.append(prefix);
                            RealmObject linkedObject = RoyalAccess.get(realm, table.getLinkTarget(i), row.getLink(i));
                            if (linkedObject != null) {
                                builder
                                        .append("\"")
                                        .append(table.getColumnName(i))
                                        .append("\":")
                                        .append(Rson.toJsonString(linkedObject, depth - 1));
                                prefix = ",";
                            }
                        }
                        break;
                    case LINK_LIST:
                        if (depth > 0) {
                            // TODO: RealmList null checking
                            builder
                                    .append(prefix)
                                    .append("\"")
                                    .append(table.getColumnName(i))
                                    .append("\":");

                            LinkView linkView = row.getLinkList(i);
                            Table linkTable = table.getLinkTarget(i);

                            builder.append("[");
                            String prefix2 = "";
                            for (int j = 0; j < linkView.size(); j++) {
                                RealmObject linkedObject = RoyalAccess.get(realm, linkTable, linkView.getTargetRowIndex(j));
                                builder
                                        .append(prefix2)
                                        .append(Rson.toJsonString(linkedObject, depth - 1));
                                prefix2 = ",";
                            }
                            builder.append("]");
                            prefix = ",";
                        }
                        break;
                }
            }
            if (columnCount > 0)
                builder.append("}");
            return builder.toString();
        }
    }
}
