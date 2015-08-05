package com.thefinestartist.royal;

import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.thefinestartist.royal.entities.AllTypes;
import com.thefinestartist.royal.entities.Dog;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RoyalAccess;

/**
 * Created by TheFinestArtist on 7/8/15.
 */
public class RsonTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testToJsonString1() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testToJsonString1.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        AllTypes allTypes1 = new AllTypes();
        allTypes1.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
        allTypes1.setColumnBoolean(true);
        allTypes1.setColumnDate(new Date());
        allTypes1.setColumnDouble(Double.MAX_VALUE);
        allTypes1.setColumnFloat(Float.MAX_VALUE);
        allTypes1.setColumnLong(Long.MAX_VALUE);
        allTypes1.setColumnString("allType1");

        Dog dog1 = new Dog();
        dog1.setName("Kitty1");
        dog1.setAge(1);
        allTypes1.setColumnRealmObject(dog1);

        Dog dog2 = new Dog();
        dog2.setName("Kitty2");
        dog2.setAge(2);
        Dog dog3 = new Dog();
        dog3.setName("Kitty3");
        dog3.setAge(3);
        allTypes1.setColumnRealmList(new RealmList<>(dog2, dog3));

        realm1.beginTransaction();
        AllTypes allTypes2 = realm1.createObject(AllTypes.class);
        allTypes2.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
        allTypes2.setColumnBoolean(true);
        allTypes2.setColumnDate(new Date());
        allTypes2.setColumnDouble(Double.MAX_VALUE);
        allTypes2.setColumnFloat(Float.MAX_VALUE);
        allTypes2.setColumnLong(Long.MAX_VALUE);
        allTypes2.setColumnString("allType2");

//        Dog dog4 = realm1.createObject(Dog.class);
//        dog4.setName("Kitty4");
//        dog4.setAge(4);
//        allTypes2.setColumnRealmObject(dog4);

        Dog dog5 = realm1.createObject(Dog.class);
        dog5.setName("Kitty5");
        dog5.setAge(5);
        Dog dog6 = realm1.createObject(Dog.class);
        dog6.setName("Kitty6");
        dog6.setAge(6);
        allTypes2.setColumnRealmList(new RealmList<>(dog5, dog6));
//        allTypes2.setColumnRealmList(new RealmList<>(dog5));
//        allTypes2.getColumnRealmList().get(0).setName("Kitty7");
        realm1.commitTransaction();

        // 3. Assert
        Logger.d("allTypes1: " + RoyalAccess.isProxy(allTypes1));
        Logger.d("allTypes2: " + RoyalAccess.isProxy(allTypes2));
        Logger.json(Rson.toJsonString(allTypes1));
        Logger.json(Rson.toJsonString(allTypes2));

//        {
//            // 2693
//            long timeMillis = System.currentTimeMillis();
//            for (int i = 0; i < 2000; i++)
//                Rson.toJsonString(allTypes1);
//            Logger.d("Time#1: " + (System.currentTimeMillis() - timeMillis));
//        }
//
//        {
//            // 3572
//            long timeMillis = System.currentTimeMillis();
//            for (int i = 0; i < 2000; i++)
//                Rson.toJsonString(allTypes2);
//            Logger.d("Time#2: " + (System.currentTimeMillis() - timeMillis));
//        }
//
//        {   // 880
//            long timeMillis = System.currentTimeMillis();
//            realm1.beginTransaction();
//            for (int i = 0; i < 2000; i++) {
//                AllTypes allTypes3 = realm1.createObject(AllTypes.class);
//                allTypes3.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
//                allTypes3.setColumnBoolean(true);
//                allTypes3.setColumnDate(new Date());
//                allTypes3.setColumnDouble(Double.MAX_VALUE);
//                allTypes3.setColumnFloat(Float.MAX_VALUE);
//                allTypes3.setColumnLong(Long.MAX_VALUE);
//                allTypes3.setColumnString("allType3");
//
//                Dog dog7 = realm1.createObject(Dog.class);
//                dog7.setName("Kitty7");
//                dog7.setAge(4);
//                allTypes3.setColumnRealmObject(dog7);
//
//                Dog dog8 = realm1.createObject(Dog.class);
//                dog8.setName("Kitty8");
//                dog8.setAge(5);
//                Dog dog9 = realm1.createObject(Dog.class);
//                dog9.setName("Kitty9");
//                dog9.setAge(6);
//                allTypes3.setColumnRealmList(new RealmList<>(dog8, dog9));
//            }
//            realm1.commitTransaction();
//            Logger.d("Time#3: " + (System.currentTimeMillis() - timeMillis));
//        }

        // 4. Realm Close
        realm1.close();
    }

    public void testToJsonString2() {
        // 1. Realm Setup
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testToJsonString2.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        // 2. Object Setup
        AllTypes allTypes1 = new AllTypes();
        allTypes1.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
        allTypes1.setColumnBoolean(true);
        allTypes1.setColumnDate(new Date());
        allTypes1.setColumnDouble(Double.MAX_VALUE);
        allTypes1.setColumnFloat(Float.MAX_VALUE);
        allTypes1.setColumnLong(Long.MAX_VALUE);
        allTypes1.setColumnString("allType1");

//        Dog dog1 = new Dog();
//        dog1.setName("Kitty1");
//        dog1.setAge(1);
//        allTypes1.setColumnRealmObject(dog1);
//
//        Dog dog2 = new Dog();
//        dog2.setName("Kitty2");
//        dog2.setAge(2);
//        Dog dog3 = new Dog();
//        dog3.setName("Kitty3");
//        dog3.setAge(3);
//        allTypes1.setColumnRealmList(new RealmList<>(dog2, dog3));

        realm1.beginTransaction();
        AllTypes allTypes2 = realm1.createObject(AllTypes.class);
        allTypes2.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
        allTypes2.setColumnBoolean(true);
        allTypes2.setColumnDate(new Date());
        allTypes2.setColumnDouble(Double.MAX_VALUE);
        allTypes2.setColumnFloat(Float.MAX_VALUE);
        allTypes2.setColumnLong(Long.MAX_VALUE);
        allTypes2.setColumnString("allType2");

//        Dog dog4 = realm1.createObject(Dog.class);
//        dog4.setName("Kitty4");
//        dog4.setAge(4);
//        allTypes2.setColumnRealmObject(dog4);
//
//        Dog dog5 = realm1.createObject(Dog.class);
//        dog5.setName("Kitty5");
//        dog5.setAge(5);
//        Dog dog6 = realm1.createObject(Dog.class);
//        dog6.setName("Kitty6");
//        dog6.setAge(6);
//        allTypes2.setColumnRealmList(new RealmList<>(dog5, dog6));
        realm1.commitTransaction();

        // 3. Assert
        Logger.json(Rson.toJsonString(allTypes1));
//        Logger.json(Rson.toJsonString(allTypes2));

        {
            // 2693
            long timeMillis = System.currentTimeMillis();
            for (int i = 0; i < 2000; i++)
                Rson.toJsonString(allTypes1);
            Logger.d("Time#1: " + (System.currentTimeMillis() - timeMillis));
        }

        {
            // 3572
            long timeMillis = System.currentTimeMillis();
            for (int i = 0; i < 2000; i++)
                Rson.toJsonString(allTypes2);
            Logger.d("Time#2: " + (System.currentTimeMillis() - timeMillis));
        }

        {   // 880
            long timeMillis = System.currentTimeMillis();
            realm1.beginTransaction();
            for (int i = 0; i < 2000; i++) {
                AllTypes allTypes3 = realm1.createObject(AllTypes.class);
                allTypes3.setColumnBinary(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
                allTypes3.setColumnBoolean(true);
                allTypes3.setColumnDate(new Date());
                allTypes3.setColumnDouble(Double.MAX_VALUE);
                allTypes3.setColumnFloat(Float.MAX_VALUE);
                allTypes3.setColumnLong(Long.MAX_VALUE);
                allTypes3.setColumnString("allType3");

//                Dog dog7 = realm1.createObject(Dog.class);
//                dog7.setName("Kitty7");
//                dog7.setAge(4);
//                allTypes3.setColumnRealmObject(dog7);
//
//                Dog dog8 = realm1.createObject(Dog.class);
//                dog8.setName("Kitty8");
//                dog8.setAge(5);
//                Dog dog9 = realm1.createObject(Dog.class);
//                dog9.setName("Kitty9");
//                dog9.setAge(6);
//                allTypes3.setColumnRealmList(new RealmList<>(dog8, dog9));
            }
            realm1.commitTransaction();
            Logger.d("Time#3: " + (System.currentTimeMillis() - timeMillis));
        }

        // 4. Realm Close
        realm1.close();
    }

    // String Builder is fast enough
    // TODO: Double, Float,
    public void testStringBuilder1() {
        {
            long timeMillis = System.currentTimeMillis();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 2000; i++)
                builder.append("Hello, ");
            for (int i = 0; i < 2000; i++)
                builder.append("\"");
            for (int i = 0; i < 2000; i++)
                builder.append(true);
            String string = builder.toString();
            Logger.d("String Build Time#1: " + (System.currentTimeMillis() - timeMillis) + ", " + string);
        }

        {
            long timeMillis = System.currentTimeMillis();
            for (int i = 0; i < 2000; i++) {
                StringBuilder builder = new StringBuilder();
                builder.append("{");
                builder.append("\"");
                builder.append("columnString");
                builder.append("\":");
                builder.append("allType2");
                builder.append(",");
                builder.append("\"");
                builder.append("columnLong");
                builder.append("\":");
//                builder.append(9223372036854775807l);
                builder.append(",");
                builder.append("\"");
                builder.append("columnFloat");
                builder.append("\":");
//                builder.append(3.4028235E38f);
                builder.append(",");
                builder.append("\"");
                builder.append("columnDouble");
                builder.append("\":");
//                builder.append(1.7976931348623157E308d);
                builder.append(",");
                builder.append("\"");
                builder.append("columnBoolean");
                builder.append("\":");
//                builder.append(new Date());
//                if (true)
//                    builder.append("true");
//                else
//                    builder.append("false");
                builder.append(true);
                builder.append("}");
                String string = builder.toString();
            }
            Logger.d("String Build Time#2: " + (System.currentTimeMillis() - timeMillis));
        }

        // 2000 conversion
        // long: 40ns
        // float: 100ns
        // double: 270ns
        // boolean: same as string
        // TODO: byte[], Date
    }
}
