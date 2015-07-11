package com.thefinestartist.royal;

import android.test.AndroidTestCase;

import com.thefinestartist.royal.databases.SecondaryDatabase;

/**
 * Created by TheFinestArtist on 7/12/15.
 */
public class RoyalDatabaseTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        Royal.joinWith(getContext());
        Royal.addDatabase(new SecondaryDatabase());
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testConfiguration1() {
        assertEquals("secondary.realm", Royal.getConfigurationOf(SecondaryDatabase.class).getRealmFileName());
    }
}
