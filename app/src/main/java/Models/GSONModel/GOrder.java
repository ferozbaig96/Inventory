package Models.GSONModel;

import java.util.ArrayList;

public class GOrder {

    public int id;
    public String client_fname, client_lname, client_email;
    public String cAddress, cDeliveryTime, PaymentMethod;
    public long phone;
    public ArrayList<GProduct> products;
    public float subtotal, tax;
    public GUser delivery_boy, sales_man;
}
