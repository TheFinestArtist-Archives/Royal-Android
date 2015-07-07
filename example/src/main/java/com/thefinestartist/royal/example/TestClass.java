package com.thefinestartist.royal.example;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.thefinestartist.royal.RoyalTransaction;
import com.thefinestartist.royal.example.entities.Dog;
import com.thefinestartist.royal.listener.OnRoyalUpdatedListener;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by TheFinestArtist on 7/6/15.
 */
public class TestClass {

    public static void testSaveInBackground1(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1. Realm Setup
                final RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(context).name("1testSaveInBackground1.realm").build();
                Realm.deleteRealm(realmConfig1);
                Realm realm1 = Realm.getInstance(realmConfig1);

                // 2. Object Setup
                Dog dog1 = new Dog();
                dog1.setName("Kitty1");

                // 3. RoyalTransaction.saveInBackground()
                final long threadId = Thread.currentThread().getId();
                Logger.d("threadId : " + threadId);
                RoyalTransaction.saveInBackground(realm1, new OnRoyalUpdatedListener() {
                    @Override
                    public void onUpdated() {
                        Logger.d("threadId : " + Thread.currentThread().getId());

                        // 4. Query
                        Realm realm = Realm.getInstance(realmConfig1);
                        RealmQuery<Dog> query = realm.where(Dog.class);
                        RealmResults<Dog> dogs = query.findAll();

                        // 5. Assert
                        if (threadId == Thread.currentThread().getId())
                            throw new IllegalStateException("Should return at the same thread");
                        if (dogs == null)
                            throw new IllegalStateException("Dogs should not be null");
                        if (1 == dogs.size())
                            throw new IllegalStateException("Dog size should be 1");
                        if ("Kitty1".equals(dogs.get(0).getName()))
                            throw new IllegalStateException("Dog size should be 1");
                    }
                }, dog1);

                // 6. Realm Close
                realm1.close();
            }
        }).start();
    }
}
