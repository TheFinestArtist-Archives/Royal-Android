package io.realm.internal.modules;

import io.realm.RealmObject;
import io.realm.internal.RealmProxyMediator;

/**
 * Created by TheFinestArtist on 7/7/15.
 */
public class RoyalInternalModulesAccess {

    public static RealmProxyMediator getMediator(CompositeMediator compositeMediator, Class<? extends RealmObject> clazz) {
        RealmProxyMediator mediator = compositeMediator.mediators.get(clazz);
        if (mediator == null) {
            throw new IllegalArgumentException(clazz.getSimpleName() + " is not part of the schema for this Realm");
        }
        return mediator;
    }
}
