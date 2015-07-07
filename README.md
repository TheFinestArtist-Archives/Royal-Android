#Royal Android
**This project created to help developer to have better experience in using [Realm-Java](https://github.com/realm/realm-java)**

Realm is very fast database, but unfortunately there is some usability issues.
1. Realm, RealmObject can not be accessed by different thread other then which is was first created.
2. You have to open and close every time you use Realm.
3. You have to call realm.beginTransaction() and realm.commitTransaction() every time you want to save data.
4. You have to loop for-loop backward from i = size - 1 to i = 0 to update data inside it.
5. Very hard to use Realm browser since Android device doesn't give you permission to pull the "name.realm" file unless you have rooted the phone.

###RoyalTransaction

```java
User user = new User();
user.setName("Leonardo Taehwan Kim");
user.setEmail("contact@thefinestartist.com");
user.setUsername("TheFinestArtist");

RoyalTransaction.save(realm, user);
```
