package Models.RealmModel;

import io.realm.RealmObject;

public class Product extends RealmObject {
    public int id;
    public String name;
    public Category category;
    public float price;
    public int current_stock, minimum_stock;


    /*
        id
        name
        category (fk -> category)
        price - float
        current stock - int
        minium stock - int
    */
}
