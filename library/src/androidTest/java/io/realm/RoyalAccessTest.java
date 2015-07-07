package io.realm;

import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.thefinestartist.royal.entities.Dog;
import com.thefinestartist.royal.entities.DogPrimaryKey;

import io.realm.internal.Util;

/**
 * Created by TheFinestArtist on 7/7/15.
 */
public class RoyalAccessTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testGetRealm1() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testGetRealm1.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder(getContext()).name("2testGetRealm1.realm").build();
        Realm.deleteRealm(realmConfig2);
        Realm realm2 = Realm.getInstance(realmConfig2);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        DogPrimaryKey dog2 = new DogPrimaryKey();
        dog2.setName("Kitty1");

        realm1.beginTransaction();
        Dog dog3 = realm1.createObject(Dog.class);
        dog3.setName("Kitty3");
        realm1.commitTransaction();

        realm2.beginTransaction();
        DogPrimaryKey dog4 = realm2.createObject(DogPrimaryKey.class);
        dog4.setName("Kitty4");
        realm2.commitTransaction();

        // 3. Assert
        assertNull(RoyalAccess.getRealm(dog1));
        assertNull(RoyalAccess.getRealm(dog2));
        assertEquals(realm1, RoyalAccess.getRealm(dog3));
        assertEquals(realm2, RoyalAccess.getRealm(dog4));

        // 4. Realm Close
        realm1.close();
        realm2.close();
    }

    public void testGetRealm2() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testGetRealm2.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder(getContext()).name("2testGetRealm2.realm").build();
        Realm.deleteRealm(realmConfig2);
        Realm realm2 = Realm.getInstance(realmConfig2);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        DogPrimaryKey dog2 = new DogPrimaryKey();
        dog2.setId(1);
        dog2.setName("Kitty1");

        realm1.beginTransaction();
        realm1.copyToRealm(dog1);
        Dog dog3 = realm1.createObject(Dog.class);
        dog3.setName("Kitty3");
        realm1.commitTransaction();

        realm2.beginTransaction();
        realm2.copyToRealm(dog2);
        DogPrimaryKey dog4 = realm2.createObject(DogPrimaryKey.class);
        dog4.setId(2);
        dog4.setName("Kitty4");
        realm2.commitTransaction();

        // 3. Assert
        assertNull(RoyalAccess.getRealm(dog1)); // Calling the copyToRealm doesn't make the actual object to subordinate to realm
        assertNull(RoyalAccess.getRealm(dog2)); // Calling the copyToRealm doesn't make the actual object to subordinate to realm
        assertEquals(realm1, RoyalAccess.getRealm(dog3));
        assertEquals(realm2, RoyalAccess.getRealm(dog4));

        // 4. Realm Close
        realm1.close();
        realm2.close();
    }

    public void testGetRealm3() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testGetRealm3.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        realm1.beginTransaction();
        realm1.copyToRealm(dog1);
        Dog dog3 = realm1.createObject(Dog.class);
        dog3.setName("Kitty3");
        realm1.commitTransaction();

        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 3. Assert
        assertNotNull(RoyalAccess.getRealm(dogs.get(0)));
        assertEquals(realm1, RoyalAccess.getRealm(dogs.get(0)));

//        dogs.get(0).setAge(0);

        // 4. Realm Close
        realm1.close();
    }

    public void testHasPrimaryKey1() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testHasPrimaryKey1.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder(getContext()).name("2testHasPrimaryKey1.realm").build();
        Realm.deleteRealm(realmConfig2);
        Realm realm2 = Realm.getInstance(realmConfig2);

        // 2. Object Setup
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");

        DogPrimaryKey dog2 = new DogPrimaryKey();
        dog2.setName("Kitty1");

        realm1.beginTransaction();
        Dog dog3 = realm1.createObject(Dog.class);
        dog3.setName("Kitty3");
        realm1.commitTransaction();

        realm2.beginTransaction();
        DogPrimaryKey dog4 = realm2.createObject(DogPrimaryKey.class);
        dog4.setName("Kitty4");
        realm2.commitTransaction();

        // 3. Assert
        assertEquals(false, RoyalAccess.hasPrimaryKey(realm1, dog1));
        assertEquals(true, RoyalAccess.hasPrimaryKey(realm1, dog2));
        assertEquals(false, RoyalAccess.hasPrimaryKey(realm1, dog3));
        assertEquals(true, RoyalAccess.hasPrimaryKey(realm1, dog4));
        assertEquals(false, RoyalAccess.hasPrimaryKey(realm2, dog1));
        assertEquals(true, RoyalAccess.hasPrimaryKey(realm2, dog2));
        assertEquals(false, RoyalAccess.hasPrimaryKey(realm2, dog3));
        assertEquals(true, RoyalAccess.hasPrimaryKey(realm2, dog4));

        // 4. Realm Close
        realm1.close();
        realm2.close();
    }
}
