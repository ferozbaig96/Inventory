package Models.RealmModel;

import io.realm.RealmObject;

public class Category extends RealmObject {

    public long id;
    public String name;

    public String key;

    /*
        id - int -pk
        name - varchar
    */
}
