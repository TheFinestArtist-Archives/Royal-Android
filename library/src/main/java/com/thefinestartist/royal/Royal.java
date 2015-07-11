package com.thefinestartist.royal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

/**
 * Created by TheFinestArtist on 7/5/15.
 */
public class Royal {

    private static Context context;

    public static void joinWith(@NonNull Context context) {
        Royal.context = context.getApplicationContext();
        Logger.init("Royal");
    }

    public static Context getApplicationContext() {
        if (Royal.context == null)
            throw new NullPointerException("Please call Royal.joinWith(context) within your Application onCreate() method.");
        return Royal.context;
    }
}
