package com.thefinestartist.royal;

import android.os.Handler;
import android.test.AndroidTestCase;

import com.thefinestartist.royal.entities.Dog;
import com.thefinestartist.royal.entities.DogPrimaryKey;
import com.thefinestartist.royal.listener.OnRoyalUpdatedListener;

import java.util.concurrent.CountDownLatch;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by TheFinestArtist on 7/5/15.
 */

public class RoyalTransactionTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        Royal.initialize(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testSave1() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSave1.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        // 3. RoyalTransaction.save()
        RoyalTransaction.save(realm1, dog1);

        // 4. Query
        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 5. Assert
        assertNotNull(dogs);
        assertEquals(1, dogs.size());
        assertEquals("Kitty1", dogs.get(0).getName());

        // 6. Realm Close
        realm1.close();
    }

    public void testSave2() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSave2.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        realm1.beginTransaction();
        Dog dog2 = realm1.createObject(Dog.class);
        dog2.setName("Kitty2");
        realm1.commitTransaction();

        // 3. RoyalTransaction.save()
        RoyalTransaction.save(realm1, dog1, dog2);

        // 4. Query
        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 5. Assert
        assertNotNull(dogs);
        assertEquals(2, dogs.size());
        assertEquals("Kitty2", dogs.get(0).getName());
        assertEquals("Kitty1", dogs.get(1).getName());

        // 6. Realm Close
        realm1.close();
    }

    public void testSave3() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSave3.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder(getContext()).name("2testSave3.realm").build();
        Realm.deleteRealm(realmConfig2);
        Realm realm2 = Realm.getInstance(realmConfig2);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        realm2.beginTransaction();
        Dog dog2 = realm2.createObject(Dog.class);
        dog2.setName("Kitty2");
        realm2.commitTransaction();

        // 3. RoyalTransaction.save()
        RoyalTransaction.save(realm1, dog1, dog2);

        // 4. Query
        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 5. Assert
        assertNotNull(dogs);
        assertEquals(2, dogs.size());
        assertEquals("Kitty1", dogs.get(0).getName());
        assertEquals("Kitty2", dogs.get(1).getName());

        // 6. Realm Close
        realm1.close();
    }

    public void testSave4() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSave4.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder(getContext()).name("2testSave4.realm").build();
        Realm.deleteRealm(realmConfig2);
        Realm realm2 = Realm.getInstance(realmConfig2);

        // 2. Object Setup
        DogPrimaryKey dog1 = new DogPrimaryKey();
        dog1.setId(1);
        dog1.setName("Kitty1");

        realm1.beginTransaction();
        DogPrimaryKey dog2 = realm1.createObject(DogPrimaryKey.class);
        dog2.setId(2);
        dog2.setName("Kitty2");
        realm1.commitTransaction();

        DogPrimaryKey dog3 = new DogPrimaryKey();
        dog3.setId(2);
        dog3.setName("Kitty3");

        DogPrimaryKey dog4 = new DogPrimaryKey();
        dog4.setId(3);
        dog4.setName("Kitty4");

        // 3. RoyalTransaction.save()
        RoyalTransaction.save(realm1, dog1, dog2, dog3, dog4);

        // 4. Query
        RealmQuery<DogPrimaryKey> query = realm1.where(DogPrimaryKey.class);
        RealmResults<DogPrimaryKey> dogs = query.findAll();

        // 5. Assert
        assertNotNull(dogs);
        assertEquals(3, dogs.size());
        assertEquals("Kitty3", dogs.get(0).getName());
        assertEquals("Kitty1", dogs.get(1).getName());
        assertEquals("Kitty4", dogs.get(2).getName());

        // 6. Realm Close
        realm1.close();
    }

    public void testSave5() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSave5.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        // 3. RoyalTransaction.save()
        RoyalTransaction.save(realm1);

        // 4. Query
        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 5. Assert
        assertNotNull(dogs);
        assertEquals(0, dogs.size());

        // 6. Realm Close
        realm1.close();
    }

    public void testSaveInBackground1() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 1. Realm Setup
                final RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSaveInBackground1.realm").build();
                Realm.deleteRealm(realmConfig1);
                Realm realm1 = Realm.getInstance(realmConfig1);

                // 2. Object Setup
                Dog dog1 = new Dog();
                dog1.setName("Kitty1");

                // 3. RoyalTransaction.saveInBackground()
                try {
                    RoyalTransaction.saveInBackground(realm1, new OnRoyalUpdatedListener() {
                        @Override
                        public void onUpdated() {
                            // 4. Query
                            Realm realm = Realm.getInstance(realmConfig1);
                            RealmQuery<Dog> query = realm.where(Dog.class);
                            RealmResults<Dog> dogs = query.findAll();

                            // 5. Assert
                            assertNotNull(dogs);
                            assertEquals(1, dogs.size());
                            assertEquals("Kitty1", dogs.get(0).getName());
                            assertEquals(1, Thread.currentThread().getId());
                            latch.countDown();
                        }
                    }, dog1);
                    fail("Please call RoyalTransaction.saveInBackground() method in main thread!! " +
                            "If you are not in main thread, please use RoyalTransaction.save() method :)");
                } catch (IllegalStateException e) {
                    latch.countDown();
                }

                // 6. Realm Close
                realm1.close();
            }
        }).start();
        latch.await();
    }

    public void testSaveInBackground2() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        new Handler(getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // 1. Realm Setup
                final RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testSaveInBackground2.realm").build();
                Realm.deleteRealm(realmConfig1);
                Realm realm1 = Realm.getInstance(realmConfig1);

                // 2. Object Setup
                Dog dog1 = new Dog();
                dog1.setName("Kitty1");

                // 3. RoyalTransaction.saveInBackground()
                RoyalTransaction.saveInBackground(realm1, new OnRoyalUpdatedListener() {
                    @Override
                    public void onUpdated() {
                        // 4. Query
                        Realm realm = Realm.getInstance(realmConfig1);
                        RealmQuery<Dog> query = realm.where(Dog.class);
                        RealmResults<Dog> dogs = query.findAll();

                        // 5. Assert
                        assertNotNull(dogs);
                        assertEquals(1, dogs.size());
                        assertEquals("Kitty1", dogs.get(0).getName());
                        assertEquals(1, Thread.currentThread().getId());
                        latch.countDown();
                    }
                }, dog1);

                // 6. Realm Close
                realm1.close();
            }
        });
        latch.await();
    }
}
