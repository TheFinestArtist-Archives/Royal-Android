package com.thefinestartist.royal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class Royal {

    static Context context;

    public static void joinWith(@NonNull Context context) {
        Royal.context = context.getApplicationContext();
        Logger.init("Royal");
    }

    static Context getApplicationContext() {
        return Royal.context;
    }
}
