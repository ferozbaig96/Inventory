package Singleton;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.internal.Context;

public class RealmSingleton {

    public Realm realm;

    private static RealmSingleton Instance = null;

    private RealmSingleton(Context ctx) {
        try {
            // Create the Realm configuration
            RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("myRealm.realm").build();

            // Open the Realm
            realm = Realm.getInstance(realmConfig);
        } catch (Exception e) {
            Log.e("TAG RealmSingleton", e.toString());
        }
    }

    public static RealmSingleton getInstance(Context ctx) {
        if (Instance == null) {
            Instance = new RealmSingleton(ctx);
        }

        return Instance;
    }

    public Realm getRealm() {
        return realm;
    }

}
