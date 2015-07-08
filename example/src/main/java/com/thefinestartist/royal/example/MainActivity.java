package com.thefinestartist.royal.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.thefinestartist.royal.RoyalExport;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getInstance(this);
    }

    public void sendEmail(View v) {
        RoyalExport.sendEmail(this, "contact@thefinestartist.com", realm, realm);
    }

    public void moveTo(View v) {
        RoyalExport.moveTo(this, realm, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
