package com.thefinestartist.regal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class Regal {

    static Context context;

    public static void initialize(@NonNull Context context) {
        Regal.context = context.getApplicationContext();
        Logger.init("Regal");
    }

    static Context getApplicationContext() {
        return Regal.context;
    }
}
