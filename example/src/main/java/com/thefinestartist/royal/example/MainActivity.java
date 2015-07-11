package com.thefinestartist.royal.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thefinestartist.royal.RoyalExport;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MainActivity extends AppCompatActivity {

    Realm realm;
    Realm encryptedRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getInstance(this);
        encryptedRealm = Realm.getInstance(new RealmConfiguration.Builder(this)
                .name("encrypted.realm")
                .encryptionKey(new byte[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4})
                .build());
    }

    public void sendEmail(View v) {
        RoyalExport.toEmail(this, "contact@thefinestartist.com", realm, encryptedRealm);
//        RoyalExport.toEmail(this, "contact@thefinestartist.com");
    }

    public void moveTo(View v) {
        RoyalExport.toExternalStorage(realm, encryptedRealm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        encryptedRealm.close();
    }
}
