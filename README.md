#Royal Android
**This project created to help developer to have better experience in using [Realm-Java](https://github.com/realm/realm-java)**


###Dependencies
```java
dependencies {
   compile 'com.thefinestartist:royal:0.82.1.0'
}
```


###Royal
```java
public class App extends Application {

   @Override
   public void onCreate() {
      super.onCreate();
      Royal.joinWith(this);
      Royal.addDatabase(new DefaultDatabase());
   }
}
```

###Royal Database
```java
public class DefaultDatabase extends RoyalDatabase {

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    @Override
    public boolean forCache() {
        return super.forCache();
    }

    @Override
    public byte[] getEncryptionKey() {
        return super.getEncryptionKey();
    }

    @Override
    public boolean shouldDeleteIfMigrationNeeded() {
        return super.shouldDeleteIfMigrationNeeded();
    }

    @Override
    public List<Object> getModules() {
        return super.getModules();
    }

    @Override
    public int getVersion() {
        return super.getVersion();
    }

    @Override
    public long execute(Realm realm, long version) {
        return super.execute(realm, version);
    }
}
```

```java
RealmConfiguration configuration = Royal.getConfigurationOf(SecondaryDatabase.class);
```

###RoyalTransaction
```java
// RealmList, RealmResults, RealmObject, List<? extends RealmObject>
RoyalTransaction.create(Class<? extends RoyalDatabase> clazz, Object... objects);
RoyalTransaction.create(RealmConfiguration configuration, Object... objects);
RoyalTransaction.create(Realm realm, Object... objects);

RoyalTransaction.save(Class<? extends RoyalDatabase> clazz, Object... objects);
RoyalTransaction.save(RealmConfiguration configuration, Object... objects);
RoyalTransaction.save(Realm realm, Object... objects);

RoyalTransaction.delete(Class<? extends RoyalDatabase> clazz, Object... objects);
RoyalTransaction.delete(RealmConfiguration configuration, Object... objects);
RoyalTransaction.delete(Realm realm, Object... objects);
```

```java
User user = new User();
user.setName("Leonardo Taehwan Kim");
user.setEmail("contact@thefinestartist.com");
user.setUsername("TheFinestArtist");

RoyalTransaction.save(realm, user);
```

```java
User user1 = new User();
user1.setName("Leonardo Taehwan Kim");
user1.setEmail("contact@thefinestartist.com");
user1.setUsername("TheFinestArtist");

User user2 = new User();
user2.setName("Leonardo Taehwan Kim");
user2.setEmail("contact@thefinestartist.com");
user2.setUsername("TheFinestArtist");

RoyalTransaction.save(realm, user1, user2);
```

###RoyalExport
```java
RoyalExport.toEmail(Class<? extends RoyalDatabase>... clazzes);
RoyalExport.toEmail(String email, Class<? extends RoyalDatabase>... clazzes);
RoyalExport.toEmail(String email, Intent intent, Class<? extends RoyalDatabase>... clazzes);

RoyalExport.toEmail(RealmConfiguration... configurations);
RoyalExport.toEmail(String email, RealmConfiguration... configurations);
RoyalExport.toEmail(String email, Intent intent, RealmConfiguration... configurations);

RoyalExport.toEmail(Realm... realms);
RoyalExport.toEmail(String email, Realm... realms);
RoyalExport.toEmail(String email, Intent intent, Realm... realms);

RoyalExport.toEmailAsRawFile();
RoyalExport.toEmailAsRawFile(String email);
RoyalExport.toEmailAsRawFile(String email, Intent intent);

RoyalExport.toExternalStorage(Class<? extends RoyalDatabase>... clazzes)
RoyalExport.toExternalStorage(RealmConfiguration... configurations);
RoyalExport.toExternalStorage(Realm... realms);
RoyalExport.toExternalStorageAsRawFile();
```

###Rson
```java
Gson gson = Rson.getGson();

Rson.toJsonString(RealmObject object);
Rson.toJsonString(RealmObject object, int depth);
```

###RealmBaseAdapter
for RecyclerView

## License
```
The MIT License (MIT)

Copyright (c) 2015 TheFinestArtist

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
