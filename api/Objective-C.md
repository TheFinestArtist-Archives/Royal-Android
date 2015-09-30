#iOS API Design (Objective-C)

##RLMDatabase

####Default Database
```[Realm database]``` or ```[Realm defaultDatabase]``` to retrieve default RLMDatabase.

####Custom Database
```objective-c
@interface SecondaryDatabase : RLMDatabase
@end
@implementation

- (NSString *)databaseName {
    return @"Secondary";
}

- (NSString *)fileName {
    return @"secondary";
}

// Cached database will only use memory (not using file system)
// Cached database will automatically deleted or garbage collected whenever Application is removed from memory
- (BOOL)forCache {
    return NO;
}
@end
```

####getDatabase
```objective-c
[Realm database:[SecondaryDatabase class]];
```

##RLMObject

####Fields
```objective-c
BOOL
bool
int
NSInteger
long
long long
float
double
CGFloat
NSString
NSDate truncated to the second
NSData
RLMObject
RLMArray<RLMObject>
```

####Models
```objective-c
@interface BaseObject : RLMObject
@property int id;
@property (retain) NDDate *createdAt;
@property (retain) NDDate *updatedAt;
@end

@implementation BaseObject
@dynamic id;
@dynamic createdAt;
@dynamic updatedAt;
@end

@interface User : BaseObject
+ (NSString *)RLMClassName;
@property (retain) NSString *email;
@property (retain) NSString *fullname;
@property (retain) NSString *fullnameUpper;
@property (retain) NSString *profileUrl;
@property (retain) NSString *username;
@property (retain) Settings *settings;
@property (retain) RLMArray<Pet> *pets;
@end

@implementation User
@dynamic email;
@dynamic fullname;
@dynamic fullnameUpper;
@dynamic profileUrl;
@dynamic username;
@dynamic settings;
+ (NSString *)RLMClassName {
   return @”_User”;
}
@end

@interface Settings : RLMObject
@property BOOL isNotificationOn;
@property (retain) RLMArray<Payment> *payments;
@end

@implementation Settings
@dynamic isNotificationOn;
@dynamic payments;
@end

@interface Payment : BaseObject
@property (retain) NSString *cardType;
@property (retain) NSString *cardNumber;
@property (retain) NSString *cardHolderName;
@property int expiredYear;
@property int expiredMonth;
@property int cvc;

@implementation Payment
@dynamic cardType;
@dynamic cardNumber;
@dynamic cardHolderName;
@dynamic expiredYear;
@dynamic expiredMonth;
@dynamic cvc;
@end

@interface Pet : BaseObject
@property (retain) NSString *name;
@property (retain) NSString *type;
@end

@implementation Pet
@dynamic name;
@dynamic type;
@end
```

####Construct RealmObject
```objective-c
User *user = [User object];
user.id = 1;
user.createdAt = [NSDate date];
user.updatedAt = [NSDate date];
user.email = @"contact@thefinestartist.com";
user.fullname = @"Leonardo Taehwan Kim";
```

####Create RealmObject
```objective-c
User user;
Pet pet;
RealmList<Pet> pets;

user.createInBackground();
user.createInBackground(new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

user.createInBackground(SecondaryDatabase.class);
user.createInBackground(SecondaryDatabase.class, new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

pets.createInBackground();
pets.createInBackground(new OnRealmListUpdatedListener<Pet>() {
   public void onUpdated(RealmList<Pet> pets, RealmUpdateError error) {}
});

Realm.getDatabase().createAllInBackground(user, pet, pets, new OnRealmDatbaseUpdatedListener() {
   public void onUpdated(RealmDatabaseError error);
});
```

####Update RealmObject
```objective-c
User user;
Pet pet;
RealmList<Pet> pets;

user.updateInBackground();
user.updateInBackground(new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

user.updateInBackground(SecondaryDatabase.class);
user.updateInBackground(SecondaryDatabase.class, new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

pets.updateInBackground();
pets.updateInBackground(new OnRealmListUpdatedListener<Pet>() {
   public void onUpdated(RealmList<Pet> pets, RealmUpdateError error) {}
});

Realm.getDatabase().updateAllInBackground(user, pet, pets, new OnRealmDatbaseUpdatedListener() {
   public void onUpdated(RealmDatabaseError error);
});
```

####CreateOrUpdate RealmObject
```objective-c
User user;
Pet pet;
RealmList<Pet> pets;

user.createOrUpdateInBackground();
user.createOrUpdateInBackground(new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

user.createOrUpdateInBackground(SecondaryDatabase.class);
user.createOrUpdateInBackground(SecondaryDatabase.class, new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(User user, RealmUpdateError error) {}
});

pets.createOrUpdateInBackground();
pets.createOrUpdateInBackground(new OnRealmListUpdatedListener<Pet>() {
   public void onUpdated(RealmList<Pet> pets, RealmUpdateError error) {}
});

Realm.getDatabase().updateAllInBackground(user, pet, pets, new OnRealmDatbaseUpdatedListener() {
   public void onUpdated(RealmDatabaseError error);
});
```

##RealmQuery
**RealmQuery can't be modified after it's build** (Mainly because of RealmObserver)
```objective-c
RealmQuery.Builder queryBuilder = new RealmQuery.Builder()
                                                .from(SecondaryDatabase.class)
                                                .of(User.class)
                                                .include("settings", "pets")
                                                .includeDeeply("settings", "payments")
                                                .includeAll() // Try not to use this methods
                                                .includeAllDeeply() // Don't ever use this methods
                                                .whereEqualTo("playerName", "Dan Stemkoski")
                                                .whereNotEqualTo("playerName", "Michael Yabuti")
                                                .whereGreaterThan("playerAge", 18)
                                                .whereGreaterThanOrEqualTo("wins", 50)
                                                .whereLessThan("wins", 50)
                                                .whereLessThanOrEqualTo("wins", 50)
                                                .orderByAscending("score", "playerName")
                                                .orderByDescending("score", "playerName")
                                                .whereContainedIn("playerName", Arrays.asList({"Jonathan Walsh", "Dario Wunsch", "Shawn Simon"}))
                                                .whereExists("score")
                                                .whereDoesNotExist("score");

RealmQuery query = queryBuilder.build();

User user = query.findFirst();
query.findFirstInBackground(new OnRealmObjectFoundListener<User>() {
   public void onFound(User user, RealmQueryError error) {}
});

RealmList<User> friends = query.findAll();
query.findAllInBackground(new OnRealmListFoundListener<User>() {
   public void onFound(RealmList<User> friends, RealmQueryError error) {}
});

// Paging
RealmList<User> friends = query.findSome(10); // Find maximum 10 users
RealmList<User> friends = query.findSome(10, 30); // Find maximum 10 users skipping first 30 users
query.findSomeInBackground(10, new OnRealmListFoundListener<User>() {
   public void onFound(RealmList<User> friends, RealmQueryError error) {}
});
query.findSomeInBackground(10, 30, new OnRealmListFoundListener<User>() {
   public void onFound(RealmList<User> friends, RealmQueryError error) {}
});
```

##RealmObserver
```objective-c
RealmQuery query = queryBuilder.build();
RealmObserver observer = new RealmObserver(query, new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(RealmQuery query, User user, RealmUpdateError error) {}
});

User user = query.findFirst();
RealmObserver observer = new RealmObserver(user, new OnRealmObjectUpdatedListener<User>() {
   public void onUpdated(RealmQuery query, User user, RealmUpdateError error) {}
});

RealmObserver observer = new RealmObserver(query, new OnRealmListUpdatedListener<User>() {
   public void onUpdated(RealmQuery query, RealmList<User> users, RealmUpdateError error) {}
});

// This would have different results from the upper RealmObserver
RealmList<User> friends = query.findAll();
RealmObserver observer = new RealmObserver(friends, new OnRealmListUpdatedListener<User>() {
   public void onUpdated(RealmQuery query, RealmList<User> users, RealmUpdateError error) {}
});
```

##Migration
```objective-c
// Auto Migration
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.startWith(this);
        Realm.setAutoMigration();
        Realm.setAutoMigration(SecondaryDatabase.class);
    }
}
```

##More


##Author
```
Name     : Leonardo Taehwan Kim
Email    : contact@thefinestartist.com
Website  : http://www.thefinestartist.com
```
