package getMyApplicationContext;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import Models.GSONModel.GJSONSource;
import Models.RealmModel.JSONSource;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static MyApplication Instance;

    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference myDatabaseRef;

    private static Realm realm;
    private static RealmConfiguration realmConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(Instance.getApplicationContext()).name("myRealm.realm").build();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseRef = firebaseDatabase.getReference();
        addFirebaseListeners();
    }

    public static MyApplication getInstance() {
        return Instance;
    }

    public static Context getAppContext() {
        return Instance.getApplicationContext();
    }

    public DatabaseReference getMyDatabaseRef() {
        return myDatabaseRef;
    }

    public Realm getRealm() {

        if (realm == null || realm.isClosed()) {
            try {
                // Open the Realm
                realm = Realm.getInstance(realmConfig);
            } catch (Exception e) {
                Log.e("TAG MyRealmSingleton", e.toString());
            }
        }

        return realm;
    }

    public RealmConfiguration getRealmConfig() {
        return realmConfig;
    }

    void addFirebaseListeners() {
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("TAGGYSD", dataSnapshot.getChildrenCount() + "");
                Log.e("TAGG catchildrenCount", dataSnapshot.child("categories").getChildrenCount() + "");
                Log.e("TAGGYSD", dataSnapshot + "");

                handleRecievedFirebaseData(dataSnapshot);

                /*Toast.makeText(MainActivity.this, "Database updated. Please reopen app for the changes to take place", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("TAG", "addValueEventListener onDataChange");
                handleRecievedFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void handleRecievedFirebaseData(DataSnapshot dataSnapshot) {
        GJSONSource obj = dataSnapshot.getValue(GJSONSource.class);
        String data = new Gson().toJson(obj);

        JSONObject json = null;
        try {
            json = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        realm.close();
        Realm.deleteRealm(realmConfig);     //resetting realm
        realm=getRealm();
        loadJsonFromJsonObject(realm, json);

    }

    private void loadJsonFromJsonObject(Realm realm, final JSONObject json) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(JSONSource.class, json);
            }
        });
    }
}
