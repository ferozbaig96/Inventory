package Models.RealmModel;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Order extends RealmObject {

    public int id;
    public String client_fname, client_lname, client_email;
    public String cAddress, cDeliveryTime, PaymentMethod;
    public long cPhone;
    public RealmList<MyInteger> products;
    public float shipping, subtotal, tax;
    public int delivery_boy, sales_man;

    /*
        id
        client_fname- varchar
        client_lname - varchar
        client_email - varchar
        cAddress - varchar
        cPhone - varchar
        cDeliveryTime - varchar
        PaymentMethod - varchar (choices : Prepaid, COD)
        products -> ManyToMany(Products)
        subtotal - float
        tax - float
        shipping - float
        delivery_boy -> fk(users)
        sales_man -> fk(users)
    */
}
