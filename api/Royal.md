#Royal
* RoyalBrowser
* RoyalTransaction
* RoyalExport => Decrypt
* RoyalDatabase
* For-Loop Backward
* Realm Encryption => use xml or hardcode or code sign
* Extended RealmList

#RoyalExample
Concept
* Realm is super fast, don't bother to make a background thread to save it.

https://github.com/android10/Android-CleanArchitecture
https://github.com/antoniolg/androidmvp

* Butterknife
* Retrofit
* RecyclerView
* ListView
* CardView
* SwipeRefreshLayout
* Infinite Scroll
* PhotoView
* View Pager

ViewType (CRUD feature added)
1. Feed (Facebook/Twitter, Multiple ViewType, Like/Follow/Comment)
2. Message
3. Map


```java
RoyalBrowser.browse("default", "");
```

##Application
```java
public class BaseApplication extends Application {

   @Override
   public void onCreate() {
      super.onCreate();
      Royal.initialize(this);
   }
}
```

##Database

####Default Database
```java
Royal.getDefaultDatabase();
Royal.getDatabase(DefaultDatabase.class);
```

####Custom Database
1. Helps you open a Realm once and close Realm once automatically (Forget about opening and closing)
2. Save all realm instance for each thread and reuse it (Forget about thread)

```java
// RoyalDatabase are abstract class
public class SecondaryDatabase extends RoyalDatabase {

   @Override
   public String getFileName() {
      return "secondary"; // Royal always uses .realm as file extension
   }

   // Cached database will only use memory (not using file system)
   // Cached database will automatically deleted or garbage collected whenever Application is removed from memory
   @Override
   public boolean forCache() {
      return false;
   }

   @Override
   public byte[] getEncryptionKey() {
      return null; // null for no encryption
   }
}
```

####getDatabase
```java
Royal.getDatabase(SecondaryDatabase.class);
```
