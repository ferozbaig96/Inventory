package Models.RealmModel;

import io.realm.RealmList;
import io.realm.RealmObject;

public class JSONSource extends RealmObject {

    public RealmList<Category> categories;
    public RealmList<Inventory> inventory;
    public RealmList<Order> orders;
    public RealmList<Product> products;
    public RealmList<User> users;
}
