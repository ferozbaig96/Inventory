package com.app.fbulou.inventory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import Models.RealmModel.User;
import getMyApplicationContext.MyApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login_btn;

    private Realm realm;

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

        // Getting the Realm configuration
        RealmConfiguration realmConfig = MyApplication.getInstance().getRealmConfig();

        //TODO Resetting realm
        // Realm.deleteRealm(realmConfig);

        // Getting realm
        realm = MyApplication.getInstance().getRealm();

        initialise();
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = MyApplication.getInstance().getRealm();
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

/*

To add an object

        GCategory category = new GCategory();
        category.id=234;
        category.name="ddd";

        myRef.child("categories").push().setValue(category);

        myRef.child("categories").child("serially_in_order").setValue(category);
*/
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

        realm = MyApplication.getInstance().getRealm();

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
