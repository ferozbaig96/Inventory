package Models.GSONModel;

import java.util.ArrayList;

public class GOrder {

    public int id;
    public String client_fname, client_lname, client_email;
    public String cAddress, cDeliveryTime, PaymentMethod;
    public long cPhone;
    public ArrayList<GMyInteger> products;
    public float shipping, subtotal, tax;
    public int delivery_boy, sales_man;
}
