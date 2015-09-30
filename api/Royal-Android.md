#Android API Design

##Application
```java
public class BaseApplication extends Application {

   @Override
   public void onCreate() {
      super.onCreate();
      Real.initialize(this);
   }
}
```

##RealDatabase

####Default Database
```java
Real.getDefaultDatabase();
Real.getDatabase(DefaultDatabase.class);
```

####Custom Database
```java
// RealDatabase are abstract class
public class SecondaryDatabase extends RealDatabase {

   @Override
   public String getFileName() {
      return "secondary"; // Real always uses .Real as file extension
   }

   // Cached database will only use memory (not using file system)
   // Cached database will automatically deleted or garbage collected whenever Application is removed from memory
   @Override
   public boolean forCache() {
      Real false;
   }

   @Override
   public byte[] getEncryptionKey() {
      return null; // null for no encryption
   }
}
```

####getDatabase
```java
Real.getDatabase(SecondaryDatabase.class);
```

##RealObject

####Fields
```java
boolean
short
int
long
float
double
String
Date
byte[]
RealObject
RealList<? extends RealObject>
```

####RealObject, RealList
```java
public abstract class RealObject {
   ...
}

public class RealList<E extends RealObject> extends RealObject implements List<E> {
   ...
}
```

####Models
**Rule #1**  
Create every field in public and don't bother to create a `getter` and `setter`  
**Rule #2**  
Use `setter` only if the data type of the field and parameter is different (e.g. `setDate(String date)`)  

**Why?**  
A model class will never be a library class. (No need to follow java conventions.)  
A model class usually has no method or function which have high dependency on each field.
In other language, they usually make fields in public without `setter` and `getter`.  
Easy in integrating Gson or other mapping libraries.

```java
/**
 * Annotations
 *
 * @RealClassName
 * @RealFieldName
 *
 * @RealPrimaryKey
 * @RealUnique
 * @RealIndex
 * @RealIgnore
 *
 * @RelamDefault
 * @RealValidation
 */

public class Base extends RealObject {

   @RealPrimaryKey
   @RealFieldName("_id")
   public int id;

   public Date createdAt;
   public Date updatedAt;
}

@RealClassName("_User")
public class User extends Base {

   @RealUnique
   public String email;

   @RealIndex
   public String fullname;

   public String getFullnameUpper() {
      return this.fullnameUpper;
   }

   @RealIgnore
   public String sessionId;

   private static final String VALIDATION_REGEX_URL = "(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?";
   @RealValidation(
      regex = VALIDATION_REGEX_URL
   )
   public String profileUrl;

   @RealUnique
   @RealValidation(
      minLength = 5,
      maxLength = 20
   )
   public String username;

   public Settings settings;

   public RealList<Pet> pets;
}

public class Settings extends RealObject {

   @RelamDefault(true)
   public boolean isNotificationOn;

   public RealList<Payment> payments;
}

public class Payment extends Base {
   public String cardType;
   public String cardNumber;
   public String cardHolderName;
   public int expiredYear;
   public int expiredMonth;
   public int cvc;
}

// Using with Enum
public class Pet extends Base {

   public String name;
   private String type; // This field can only be accessed by setter and getter for safety and usability

   public PetType getType() {
      return PetType.fromAttribute(this.type);
   }

   public setType(PetType petType) {
       this.type = petType.toAttribute();
   }

   public enum PetType {
      GIRL("girl"),
      DOG("dog"),
      CAT("cat");

      private String attribute;

      PetType(String attribute) {
        this.attribute = attribute;
      }

      public String toAttribute() {
         return this.attribute;
      }

      public static PetType fromAttribute(String attribute) {
         for (PetType petType : values())
            if (petType.toAttribute().equals(attribute))
               return petType.toAttribute();

         return null;
      }
   }
}
```

####Construct RealObject
```java
User user = new User();
user.id = 1;
user.createdAt = new Date();
user.updatedAt = new Date();
user.email = "contact@thefinestartist.com";
user.fullname = "Leonardo Taehwan Kim";
```

####Save RealObject
```java
public abstract class RealObject {
   public void save() { ... }
   public void save(Class<? extends RealDatabase> clazz) { ... }
   public void save(RealTransaction transaction) { ... }
   public void save(Class<? extends RealDatabase> clazz, RealTransaction transaction) { ... }

   public void saveInBackground() { ... }
   public void saveInBackground(Class<? extends RealDatabase> clazz) { ... }
   public void saveInBackground(RealTransaction transaction) { ... }
   public void saveInBackground(OnRealUpdatedListener listener) { ... }
   public void saveInBackground(Class<? extends RealDatabase> clazz, RealTransaction transaction) { ... }
   public void saveInBackground(Class<? extends RealDatabase> clazz, OnRealUpdatedListener listener) { ... }
   public void saveInBackground(RealTransaction transaction, OnRealUpdatedListener listener) { ... }
   public void saveInBackground(Class<? extends RealDatabase> clazz, RealTransaction transaction, OnRealUpdatedListener listener) { ... }
}

public class RealList<E extends RealObject> extends RealObject implements List<E> {
   public RealList(RealObject... objects) {
      ...
   }
}
```

Basically, saving does createOrUpdate.
```java
User user;
Pet pet;
RealList<Pet> pets;

user.save();
user.saveInBackground();
user.saveInBackground(new OnRealUpdatedListener<User>() {
   public void onUpdated(User user, RealException exception) {}
});

user.save(SecondaryDatabase.class);
user.saveInBackground(SecondaryDatabase.class);
user.saveInBackground(SecondaryDatabase.class, new OnRealUpdatedListener<User>() {
   public void onUpdated(User user, RealException exception) {}
});

pets.save();
pets.saveInBackground();
pets.saveInBackground(new OnRealUpdatedListener<RealList<Pet>>() {
   public void onUpdated(RealList<Pet> pets, RealException exception) {}
});

// Saving Multiple RealObjects and RealList at once
new RealList<RealObject>(user, pet, pets).save();
new RealList<RealObject>(user, pet, pets).saveInBackground(new OnRealUpdatedListener<RealList<RealObject>>() {
   public void onUpdated(RealList<RealObject> updates, RealException exception) {}
});

// You can also set whether to create or update or createOrUpdate to make transaction faster
user.save(RealTransaction.CREATE);
user.save(RealTransaction.UPDATE);
user.save(RealTransaction.CREATE_OR_UPDATE); // default option
```

####Delete RealObject
// Work in progress

##RealQuery
**RealQuery can't be modified after it's build** (Mainly because of RealObserver)
```java
RealQuery.Builder queryBuilder = new RealQuery.Builder()
   .from(SecondaryDatabase.class)
   .of(User.class)
   .include("settings", "pets")
   // .includeDeeply("settings", "payments")
   .includeAll() // Try not to use this methods
   // .includeAllDeeply() // Don't ever use this methods
   .whereEqualTo("playerName", "Dan Stemkoski")
   .whereEqualTo("playerName", Arrays.asList({"Jonathan Walsh", "Dario Wunsch", "Shawn Simon"})
   .whereNotEqualTo("playerName", "Michael Yabuti")
   .whereNotEqualTo("playerName", Arrays.asList({"Jonathan Walsh", "Dario Wunsch", "Shawn Simon"})
   .whereGreaterThan("playerAge", 18)
   .whereGreaterThanOrEqualTo("wins", 50)
   .whereLessThan("wins", 50)
   .whereLessThanOrEqualTo("wins", 50)
   .orderByAscending("score", "playerName")
   .orderByDescending("score", "playerName")
   .whereExists("score")
   .whereDoesNotExist("score");

RealQuery query = queryBuilder.build();

User user = query.findFirst();
query.findFirstInBackground(new OnRealUpdatedListener<User>() {
   public void onUpdated(User user, RealException exception) {}
});

RealList<User> friends = query.findAll();
query.findAllInBackground(new OnRealUpdatedListener<RealList<User>>() {
   public void onUpdated(RealList<User> friends, RealException exception) {}
});

// Paging
RealList<User> friends = query.findSome(10); // Find maximum 10 users
RealList<User> friends = query.findSome(10, 30); // Find maximum 10 users skipping first 30 users
query.findSomeInBackground(10, new OnRealUpdatedListener<User>() {
   public void onUpdated(RealList<User> friends, RealException exception) {}
});
query.findSomeInBackground(10, 30, new OnRealUpdatedListener<RealList<User>>() {
   public void onUpdated(RealList<User> friends, RealException exception) {}
});
```

##RealObserver
```java
RealQuery query = queryBuilder.build();
RealObserver<User> observer = new RealObserver<>(query);
observer.addOnRealUpdatedListener(new OnRealUpdatedListener<User>() {
   public void onUpdated(User user, RealException exception) {}
});

User user = query.findFirst();
RealObserver<User> observer = new RealObserver(user);
observer.addOnRealUpdatedListener(new OnRealUpdatedListener<User>() {
   public void onUpdated(User user, RealException exception) {}
});

RealObserver<RealList<User>> observer = new RealObserver(query);
observer.addOnRealUpdatedListener(new OnRealUpdatedListener<RealList<User>>() {
   public void onUpdated(RealList<User> users, RealException exception) {}
});

// This would have different results from the upper RealObserver
RealList<User> friends = query.findAll();
RealObserver<RealList<User>> observer = new RealObserver(friends);
observer.addOnRealUpdatedListener(new OnRealUpdatedListener<RealList<User>>() {
   public void onUpdated(RealList<User> users, RealException exception) {}
});
```

##OnRealUpdatedListener
One single listener for all. The `onUpdated` method will be run in the UI thread.
```java
public interface OnRealUpdatedListener<E extends RealObject> {
   void onUpdated(E RealObject, RealException exception);
}

public class RealException extends exception {}
```

####Usage
```java
public class RealAdapter extends BaseAdapter implements OnRealUpdatedListener<RealList<Post>> {

   RealList<Post> posts;

   public RealAdapter() {}

   @Override
   public void onUpdated(RealList<Post> posts, RealException exception) {
      this.posts = posts;
      notifyDataSetChanged();
   }
}

RealAdapter adapter = new RealAdapter();

Post post = new Post();
new RealList<Post>(post).saveInBackground(adapter);

RealQuery query = new RealQuery.Builder().of(Post.class).build();
query.findAllInBackground(adapter);

RealObserver observer = new RealObserver(query, adapter);
```

##Json Support
```java
public abstract class RealObject {
   public static T fromJson(Class<T> clazz, String string) { ... }
   public static T fromJson(Class<T> clazz, JsonObject object) { ... }

   public String toJsonString() { ... }
   public JsonObject toJsonObject() { ... }
}
```

####Usage
```java
User user = RealObject.fromJson(User.class, String string);
User user = RealObject.fromJson(User.class, JsonObject object);

user.toJsonString();
user.toJsonObject();
```

##Migration
Use Migration API in Realm-Java
```java
// Auto Migration
// public class BaseApplication extends Application {
//
//    @Override
//    public void onCreate() {
//       super.onCreate();
//       Real.initialize(this);
//       Real.setAutoMigration(true);
//       Real.setAutoMigration(DefaultDatabase.class);
//       Real.setAutoMigration(SecondaryDatabase.class);
//    }
// }
```

##More


##Author
```
Name     : Leonardo Taehwan Kim
Email    : contact@thefinestartist.com
Website  : http://www.thefinestartist.com
```
