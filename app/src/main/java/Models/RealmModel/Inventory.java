package Models.RealmModel;

import io.realm.RealmObject;

public class Inventory extends RealmObject {

    public long id;
    public long product;
    public long quantity;
    public double price_per_item;

    public String key;


    /*
        id
        product (fk -> product)
        quantity -> int
        price_per_item - float
    */
}
