package com.app.fbulou.inventory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Models.RealmModel.User;
import getMyApplicationContext.MyApplication;
import io.realm.Realm;

public class CategoryFragment extends Fragment {

    private static final String TAG = "TAG";
    Realm realm;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        realm = MyApplication.getInstance().getRealm();

        String s = realm.where(User.class).findFirst().password;
        Log.e(TAG, "onCreateView() called " + s);

        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onDestroyView() {

        realm.close();
        Log.e(TAG, "onDestroyView() called");
        super.onDestroyView();
    }
}
