package com.thefinestartist.royal.example;

import android.app.Application;

import com.thefinestartist.royal.Royal;

/**
 * Created by TheFinestArtist on 8/3/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Royal.joinWith(this);
    }
}
