package com.app.fbulou.inventory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.json.JSONException;
import org.json.JSONObject;

import Models.GSONModel.GJSONSource;
import Models.RealmModel.JSONSource;
import Models.RealmModel.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login_btn;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Realm realm;
    private RealmConfiguration realmConfig;

    long phoneno;
    int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting up stetho for viewing the realm database
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        initialise();

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(this).name("myRealm.realm").build();

        //Resetting realm TODO
        Realm.deleteRealm(realmConfig);

        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfig);
    }

    @Override
    protected void onDestroy() {
        realm.close(); // Remember to close Realm when done.
        super.onDestroy();
    }

    private void initialise() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });

        addListeners();

/*

To add an object

        GCategory category = new GCategory();
        category.id=234;
        category.name="ddd";

        myRef.child("categories").push().setValue(category);

        myRef.child("categories").child("serially_in_order").setValue(category);
*/
    }

    void addListeners() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("TAGGYSD", dataSnapshot.getChildrenCount() + "");
                Log.e("TAGG catchildrenCount", dataSnapshot.child("categories").getChildrenCount() + "");
                Log.e("TAGGYSD", dataSnapshot + "");

                recievedData(dataSnapshot);

                /*Toast.makeText(MainActivity.this, "Database updated. Please reopen app for the changes to take place", Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                recievedData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void recievedData(DataSnapshot dataSnapshot) {
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
        realm = Realm.getInstance(realmConfig);
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

    private void validateForm() {
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_red_500_24dp, null);
        assert d != null;
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());


        if (username.getText().length() == 0) {
            username.setError("Username cannot be empty", d);
            username.requestFocus();

        } else if (password.getText().length() == 0) {
            password.setError("Password cannot be empty", d);
            password.requestFocus();

        } else if (password.getText().length() < 8) {
            password.setError("Minimum 8 characters required", d);
            password.requestFocus();

        } else {

            if (login(username.getText().toString(), password.getText().toString()))
                startActivity(new Intent(MainActivity.this, AdminActivity.class));

        }
    }

    boolean login(String username, String password) {
        User user = realm.where(User.class).equalTo("email", username).findFirst();

        if (user == null) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!user.password.equals(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
        phoneno = user.phone;
        type = user.type;

        return true;
    }

}
