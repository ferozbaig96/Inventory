package Models.RealmModel;

import io.realm.RealmObject;

public class Product extends RealmObject {
    public long id;
    public String name;
    public long category;
    public double price;
    public long current_stock;
    public long minimum_stock;

    public String key;

    /*
        id
        name
        category (fk -> category)
        price - float
        current stock - int
        minium stock - int
    */
}
