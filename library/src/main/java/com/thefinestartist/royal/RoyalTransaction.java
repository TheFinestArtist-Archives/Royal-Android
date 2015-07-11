package com.thefinestartist.royal;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.thefinestartist.royal.listener.OnRoyalListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RoyalAccess;
import io.realm.exceptions.RealmException;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class RoyalTransaction {

    enum Type {CREATE, CREATE_OR_UPDATE, DELETE}

    // TODO: Support List<? extends RealmObject>, RealmResults
    // Support RealmObject, List, RealmList, RealmResults
    public static void create(@NonNull Realm realm, Object... objects) {
        crud(Type.CREATE, realm, objects);
    }

    public static void save(@NonNull Realm realm, Object... objects) {
        crud(Type.CREATE_OR_UPDATE, realm, objects);
    }

    public static void delete(@NonNull Realm realm, Object... objects) {
        crud(Type.DELETE, realm, objects);
    }

    static void crud(@NonNull Type type, @NonNull Realm realm, Object... objects) {

        List<RealmObject> realmObjects = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof RealmObject) {
                realmObjects.add((RealmObject) object);
            } else if (object instanceof List) {
                for (Object realmObject : (List) object) {
                    if (realmObject instanceof RealmObject)
                        realmObjects.add((RealmObject) realmObject);
                    else if (realmObject != null)
                        throw new IllegalArgumentException(realmObject.getClass().getName() + " is not allowed to save in Realm.");
                }
            } else if (object != null)
                throw new IllegalArgumentException(object.getClass().getName() + " is not allowed to save in Realm.");
        }

        boolean transactionStarted = false;
        try {
            realm.beginTransaction();
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("commitTransaction"))
                transactionStarted = true;
        }

        try {
            switch (type) {
                case CREATE:
                    for (RealmObject realmObject : realmObjects)
                        realm.copyToRealm(realmObject);
                    break;
                case CREATE_OR_UPDATE:
                    for (RealmObject realmObject : realmObjects) {
                        if (RoyalAccess.hasPrimaryKey(realm, realmObject))
                            realm.copyToRealmOrUpdate(realmObject);
                        else
                            realm.copyToRealm(realmObject);
                    }
                    break;
                case DELETE:
                    for (RealmObject realmObject : realmObjects)
                        realmObject.removeFromRealm();
                    break;
            }

            if (!transactionStarted)
                realm.commitTransaction();
        } catch (RuntimeException e) {
//            if (!transactionStarted)
            realm.cancelTransaction();
            throw new RealmException("Exception during RoyalTransaction.save().", e);
        } catch (Error e) {
            if (!transactionStarted)
                realm.cancelTransaction();
            throw e;
        }
    }

//    public static void saveInBackground(@NonNull Realm realm, OnRoyalListener listener, RealmObject... objects) {
//        if (Thread.currentThread().getId() != 1)
//            throw new IllegalStateException("Please call RoyalTransaction.saveInBackground() method in main thread. " +
//                    "If you are not in main thread, please use RoyalTransaction.save() method instead.");
//
//        for (RealmObject object : objects) {
//            Realm objectRealm = RoyalAccess.getRealm(object);
//            if (objectRealm != null) {
//                objectRealm.beginTransaction();
//                object.removeFromRealm();
//                objectRealm.commitTransaction();
//            }
//        }
//
//        Set<Realm> realms = getRealms(objects);
//        new SaveTask(realm.getConfiguration(), realms, listener).execute(objects);
//
//        new SaveTask(realm.getConfiguration(), null, listener).execute(objects);
//    }

    private static Set<Realm> getRealms(RealmObject[] objects) {
        Set<Realm> realms = new HashSet<>();
        for (RealmObject object : objects)
            realms.add(RoyalAccess.getRealm(object));
        realms.remove(null);
        return realms;
    }

    private static void incrementReferenceCount(Set<Realm> realms) {
        for (Realm realm : realms) {
            Realm newRealm = Realm.getInstance(realm.getConfiguration());
        }
    }

    private static void decrementReferenceCount(Set<Realm> realms) {
        for (Realm realm : realms)
            realm.close();
    }

    static class SaveTask extends AsyncTask<RealmObject, Void, Void> {

        RealmConfiguration configuration;
        Set<Realm> realms;
        OnRoyalListener listener;

        private SaveTask(RealmConfiguration configuration, Set<Realm> realms, OnRoyalListener listener) {
            this.configuration = configuration;
            this.realms = realms;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
//            incrementReferenceCount(realms);
        }

        @Override
        protected Void doInBackground(RealmObject... objects) {
            Realm realm = Realm.getInstance(configuration);
            realm.beginTransaction();
            for (RealmObject object : objects)
                realm.copyToRealm(object);
            realm.commitTransaction();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            decrementReferenceCount(realms);
            if (listener != null)
                listener.onUpdated();
        }
    }

//    public static void saveInBackground(Realm realm, OnRoyalUpdatedListener listener, RealmObject... objects) {
//        final RealmConfiguration configuration = realm.getConfiguration();
//        // Increment referenceCount
//        Realm.getInstance(configuration);
//
//        Looper.prepare();
//        StaticHandler handler = new StaticHandler(listener);
//        Thread thread = new MyTask(handler, realm.getConfiguration(), objects);
//        thread.start();
//        Looper.loop();
//    }
//
//    static class StaticHandler extends Handler {
//        //Using a weak reference means you won't prevent garbage collection
//        private final WeakReference<OnRoyalUpdatedListener> listenerWeakReference;
//
//        public StaticHandler(OnRoyalUpdatedListener listener) {
//            listenerWeakReference = new WeakReference<>(listener);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            OnRoyalUpdatedListener listener = listenerWeakReference.get();
//            if (listener != null) {
//                listener.onUpdated();
//            }
//        }
//    }
//
//    static class MyTask extends Thread {
//
//        Handler handler;
//        RealmConfiguration configuration;
//        RealmObject[] objects;
//
//        public MyTask(Handler handler, RealmConfiguration configuration, RealmObject... objects) {
//            this.handler = handler;
//            this.configuration = configuration;
//            this.objects = objects;
//        }
//
//        @Override
//        public void run() {
//            Realm realm = Realm.getInstance(configuration);
//            Logger.d(configuration.getRealmFileName());
//            realm.beginTransaction();
//            for (RealmObject object : objects) {
//                Logger.d(object.toString());
//                realm.copyToRealm(object);
//            }
//            realm.commitTransaction();
//            realm.close();
//            handler.sendMessageAtFrontOfQueue(Message.obtain(handler));
//        }
//    }
}
