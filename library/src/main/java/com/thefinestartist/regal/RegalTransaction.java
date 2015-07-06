package com.thefinestartist.regal;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.thefinestartist.regal.listener.OnRegalUpdatedListener;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RegalAccess;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class RegalTransaction {

    public enum Transaction {CREATE, CREATE_OR_UPDATE}

    /**
     * @param realm
     * @param objects
     */
    public static void save(@NonNull Realm realm, RealmObject... objects) {
        save(Transaction.CREATE_OR_UPDATE, realm, objects);
    }

    public static void save(@NonNull Transaction transaction, @NonNull Realm realm, RealmObject... objects) {
        realm.beginTransaction();
        switch (transaction) {
            case CREATE:
                for (RealmObject object : objects)
                    realm.copyToRealm(object);
                break;
            case CREATE_OR_UPDATE:
                for (RealmObject object : objects) {
                    if (RegalAccess.hasPrimaryKey(realm, object))
                        realm.copyToRealmOrUpdate(object);
                    else
                        realm.copyToRealm(object);
                }
                break;
        }
        realm.commitTransaction();
    }

    public static void saveInBackground(Realm realm, OnRegalUpdatedListener listener, RealmObject... objects) {
        new SaveTask(realm, listener).execute(objects);
    }

    static class SaveTask extends AsyncTask<RealmObject, Void, Void> {

        RealmConfiguration configuration;
        Realm realm;
        OnRegalUpdatedListener listener;

        private SaveTask(Realm realm, OnRegalUpdatedListener listener) {
            this.configuration = realm.getConfiguration();
            this.realm = Realm.getInstance(configuration);
            this.listener = listener;
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
            realm.close();
            listener.onUpdated();
        }
    }

//    public static void saveInBackground(Realm realm, OnRegalUpdatedListener listener, RealmObject... objects) {
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
//        private final WeakReference<OnRegalUpdatedListener> listenerWeakReference;
//
//        public StaticHandler(OnRegalUpdatedListener listener) {
//            listenerWeakReference = new WeakReference<>(listener);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            OnRegalUpdatedListener listener = listenerWeakReference.get();
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
