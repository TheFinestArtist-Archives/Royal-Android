```java
public class TransactionHelper {

   public enum Transaction { CREATE, CREATE_OR_UPDATE }

   public static void save (Realm realm, RealmObject... objects) {
      realm.beginTransaction();
      for (RealmObject object : objects)
         realm.copyToRealmOrUpdate(object);
      realm.commitTransaction();
   }

   public static void saveInBackground (Realm realm, RealmObject... objects) {
      {
         realm.beginTransaction();
         for (RealmObject object : objects)
            realm.copyToRealmOrUpdate(object);
         realm.commitTransaction();
      }
   }
}
```
```objective-c
```
```swift
import Foundation

/**
    Provides the default transaction block for perform a Realm transaction.

    :param: block The block to perform the transaction. The realm is used to store the transaction.
*/
func realmTransaction(block: (realm: RLMRealm) -> Void) {
    let realm = RLMRealm.defaultRealm()

    realm.beginWriteTransaction()
    block(realm: realm)
    realm.commitWriteTransaction()
}
```
