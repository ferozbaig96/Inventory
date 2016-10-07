package Models.RealmModel;

import io.realm.RealmObject;

public class User extends RealmObject {
    public int id;
    public String name, email, password;
    public long phone;
    public int type;


    /*
        id - int - pk
        name - varchar
        email - varchar
        password - varchar
        phone - varchar
        type - int (limit to 1, 2 and 3). 1 is for admin, 2 for sales and 3 for delivery
    */
}
