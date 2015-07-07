package com.thefinestartist.royal;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import io.realm.RoyalAccess;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class Rson {

    private static final Object lock = new Object();
    private static Gson gson;

    private static Gson getGson() {
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
        if (!RoyalAccess.isProxy(object)) {
            return getGson().toJson(object);
        } else {
            return object.toString();
        }
    }
}
