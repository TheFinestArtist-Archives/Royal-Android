package com.thefinestartist.royal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class Royal {

    private static Context context;

    public static void joinWith(@NonNull Context context) {
        Royal.context = context.getApplicationContext();
        Logger.init("Royal");
    }

    public static Context getApplicationContext() {
        if (Royal.context == null)
            throw new NullPointerException("Please call Royal.joinWith(context) within your Application onCreate() method.");
        return Royal.context;
    }

    public static Map<Class<? extends RoyalDatabase>, RealmConfiguration> map = new ConcurrentHashMap<>();

    public static void addDatabase(@NonNull RoyalDatabase royalDatabase) {
        Context context = Royal.getApplicationContext();
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder(context);
        builder.name(royalDatabase.getFileName() + ".realm");
        if (royalDatabase.forCache())
            builder.inMemory();
        if (royalDatabase.getEncryptionKey() != null)
            builder.encryptionKey(royalDatabase.getEncryptionKey());
        if (royalDatabase.shouldDeleteIfMigrationNeeded())
            builder.deleteRealmIfMigrationNeeded();

        List<Object> modules = royalDatabase.getModules();
        if (modules != null && modules.size() > 0) {
            Object baseModule;
            baseModule = modules.get(0);
            modules.remove(0);
            Object[] additionalModules = modules.toArray(new Object[modules.size()]);
            builder.setModules(baseModule, additionalModules);
        }

        builder.migration(royalDatabase);
        RealmConfiguration configuration = builder.build();
        map.put(royalDatabase.getClass(), configuration);
    }

    public static RealmConfiguration getConfigurationOf(Class<? extends RoyalDatabase> clazz) {
        return map.get(clazz);
    }

    public static Realm getRealmOf(Class<? extends RoyalDatabase> clazz) {
        RealmConfiguration configuration = map.get(clazz);
        return Realm.getInstance(configuration);
    }

//    public static Realm openRealm(Class<? extends RoyalDatabase> clazz) {
//        RealmConfiguration configuration = map.get(clazz);
//        return Realm.getInstance(configuration);
//    }
//
//    public static void closeRealm(Realm realm) {
//        realm.close();
//    }
}


//Realm is very fast database, but unfortunately there is some usability issues.
//
//        1. `Realm`, `RealmObject` can not be accessed by different thread other then which is was first created.
//        2. You have to `realm.getInstance()` and `realm.close()` every time you use Realm.
//        3. You have to call `realm.beginTransaction()` and `realm.commitTransaction()` every time you want to save data.
//        4. You have to loop for-loop backward `for(int i = results.size() - 1; i >= 0; i--)` to update data inside it.
//        5. Very hard to use Realm browser since Android device doesn't give you permission to pull the "name.realm" file unless you have rooted the phone.