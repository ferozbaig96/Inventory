package Models.RealmModel;

import io.realm.RealmObject;

public class Inventory extends RealmObject {

    public int id;
    public int product;
    public int quantity;
    public float price_per_item;

    /*
        id
        product (fk -> product)
        quantity -> int
        price_per_item - float
    */
}
