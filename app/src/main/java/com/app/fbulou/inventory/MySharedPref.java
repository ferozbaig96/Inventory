package com.app.fbulou.inventory;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class MySharedPref {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    Context context;

    public MySharedPref(Context ctx) {
        this.context = ctx;
        String TITLE = "com.app.fbulou.inventory" + "mPrefs";
        sharedPreferences = ctx.getSharedPreferences(TITLE, Context.MODE_PRIVATE);
    }

    public String loadPref(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void savePref(String key, String value) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(key, value);
        spEditor.apply();
    }
}
