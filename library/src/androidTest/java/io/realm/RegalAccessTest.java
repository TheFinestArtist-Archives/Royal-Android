package io.realm;

import android.test.AndroidTestCase;

import com.thefinestartist.regal.entities.Dog;
import com.thefinestartist.regal.entities.DogPrimaryKey;

/**
 * Created by TheFinestArtist on 7/7/15.
 */
public class RegalAccessTest extends AndroidTestCase {

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
        assertNull(RegalAccess.getRealm(dog1));
        assertNull(RegalAccess.getRealm(dog2));
        assertEquals(realm1, RegalAccess.getRealm(dog3));
        assertEquals(realm2, RegalAccess.getRealm(dog4));

        // 4. Realm Close
        realm1.close();
        realm2.close();
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
        assertEquals(false, RegalAccess.hasPrimaryKey(realm1, dog1));
        assertEquals(true, RegalAccess.hasPrimaryKey(realm1, dog2));
        assertEquals(false, RegalAccess.hasPrimaryKey(realm1, dog3));
        assertEquals(true, RegalAccess.hasPrimaryKey(realm1, dog4));
        assertEquals(false, RegalAccess.hasPrimaryKey(realm2, dog1));
        assertEquals(true, RegalAccess.hasPrimaryKey(realm2, dog2));
        assertEquals(false, RegalAccess.hasPrimaryKey(realm2, dog3));
        assertEquals(true, RegalAccess.hasPrimaryKey(realm2, dog4));

        // 4. Realm Close
        realm1.close();
        realm2.close();
    }
}
