package io.realm;

import android.test.AndroidTestCase;

import com.thefinestartist.royal.entities.Dog;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class RealmObjectTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }


    public void testRealmObjectNull1() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testRealmObjectNull1.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        realm1.beginTransaction();
        Dog dog3 = realm1.createObject(Dog.class);
        realm1.commitTransaction();

        RealmQuery<Dog> query = realm1.where(Dog.class);
        RealmResults<Dog> dogs = query.findAll();

        // 3. Assert
        assertNotNull(dogs.get(0));
        assertEquals("", dogs.get(0).getName());

        // 4. Realm Close
        realm1.close();
    }
}
