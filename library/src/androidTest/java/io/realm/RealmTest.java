package io.realm;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.thefinestartist.regal.entities.Dog;

/**
 * Created by TheFinestArtist on 7/7/15.
 */
public class RealmTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testClose1() {
        RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testClose1.realm").build();
        Realm.deleteRealm(realmConfig1);

        Realm realm1 = Realm.getInstance(realmConfig1);
        realm1 = Realm.getInstance(realmConfig1);
        realm1.close();

        realm1.beginTransaction();
        Dog dog1 = new Dog();
        dog1.setName("Kitty1");
        realm1.commitTransaction();

        realm1.close();
    }

    public void testClose2() {
        final RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testClose2.realm").build();
        Realm.deleteRealm(realmConfig1);
        Realm realm1 = Realm.getInstance(realmConfig1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm realm1 = Realm.getInstance(realmConfig1);

                realm1.beginTransaction();
                Dog dog1 = new Dog();
                dog1.setName("Kitty1");
                realm1.commitTransaction();

                realm1.close();

            }
        }).start();
        realm1.close();
        realm1.close();
        realm1.close();
        realm1.close();
    }

    // Conclusion: Realm is not closable in other thread but somehow doesn't make exception
    // TODO: Make an exception if Realm.close() method is accessed by different thread
    public void testClose3() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                final RealmConfiguration realmConfig1 = new RealmConfiguration.Builder(getContext()).name("1testClose3.realm").build();
                Realm.deleteRealm(realmConfig1);
                final Realm realm1 = Realm.getInstance(realmConfig1);

                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Logger.d("handleMessage");
                        realm1.beginTransaction();
                        Dog dog1 = new Dog();
                        dog1.setName("Kitty1");
                        realm1.commitTransaction();
                        realm1.close();
                    }
                };

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        realm1.close(); // Doesn't work!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        handler.sendEmptyMessage(0);
                    }
                }).start();

                Looper.loop();
            }
        }).start();
    }
}
