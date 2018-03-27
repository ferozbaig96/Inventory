package com.app.fbulou.inventory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.DatabaseReference;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.List;

import Models.RealmModel.Category;
import Models.RealmModel.Inventory;
import Models.RealmModel.MyInteger;
import Models.RealmModel.Order;
import Models.RealmModel.Product;
import Models.RealmModel.User;
import getMyApplicationContext.MyApplication;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login_btn;

    DatabaseReference myRef = MyApplication.getInstance().getMyDatabaseRef();
    private Realm realm;

    long phoneno;
    long type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting up stetho for viewing the realm database
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        // Getting the Realm configuration
        //RealmConfiguration realmConfig = MyApplication.getInstance().getRealmConfig();

        //TODO Resetting realm
        // Realm.deleteRealm(realmConfig);

        // Getting realm
        realm = MyApplication.getInstance().getRealm();

        initialise();
    }

    int i = 0;

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (i) {
                    case 0:
                        //addProduct("new", 355, 45, 12, 35.678);
                        addUsers("newuser", "abc@aasd", "pwd", 2313, 2);
                        break;

                    case 1:
                        // editProduct("k0", "edited", 6777, 11, 1, 11111.11);
                        getUsers();

                        break;

                    case 2:
                        removeProduct("k0");
                        break;

                    case 3:
                        //getProducts();
                        break;
                }

                i++;
            }
        });

    }

    @Override
    protected void onDestroy() {
        realm.close(); // Remember to close Realm when done.
        super.onDestroy();
    }

    private void initialise() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });

/*

To add an object

        GCategory category = new GCategory();
        category.id=234;
        category.name="ddd";

        myRef.child("categories").push().setValue(category);

        myRef.child("categories").child("serially_in_order").setValue(category);

        For serially_in_order, we can use :
        myRef.child("categories").getChildrenCount() + 1;       // + 1 for currently added category
*/
    }


    private void validateForm() {
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_error_red_500_24dp, null);
        assert d != null;
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());


        if (username.getText().length() == 0) {
            username.setError("Username cannot be empty", d);
            username.requestFocus();

        } else if (password.getText().length() == 0) {
            password.setError("Password cannot be empty", d);
            password.requestFocus();

        } else if (password.getText().length() < 8) {
            password.setError("Minimum 8 characters required", d);
            password.requestFocus();

        } else {

            if (login(username.getText().toString(), password.getText().toString()))
                startActivity(new Intent(MainActivity.this, AdminActivity.class));

        }
    }

    boolean login(String username, String password) {

        //TODO check where is realm getting closed

        realm = MyApplication.getInstance().getRealm();

        User user = realm.where(User.class).equalTo("email", username).findFirst();

        if (user == null) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!user.password.equals(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
        phoneno = user.phone;
        type = user.type;

        return true;
    }


    //--------------------------Functionalities----------------------


    //Category
    void addCatgeory(String categoryName) {

        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        int nCatgeory = realm.where(Category.class).findAll().size();

        Category category = new Category();
        category.id = nCatgeory + 1;
        category.name = categoryName;

        myRef.child("categories").push().setValue(category);
    }

    void removeCategory(String categoryKey) {
        myRef.child("categories").child(categoryKey).removeValue();
    }

    void editCategory(String categoryKey, String categoryNewName) {
        Category category = new Category();
        category.name = categoryNewName;

        myRef.child("categories").child(categoryKey).setValue(category);
    }

    List<Category> getCategories() {
        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        RealmResults<Category> realmResults = realm.where(Category.class).sort("id", Sort.DESCENDING).findAll();

        Log.e("TAGGY", realmResults.toString());
        return realmResults;
    }

    //Product
    void addProduct(String productName, long category, long curStockVal, long minStockVal, double price) {

        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        int nProduct = realm.where(Product.class).findAll().size();

        Product product = new Product();
        product.id = nProduct + 1;
        product.name = productName;
        product.category = category;
        product.current_stock = curStockVal;
        product.minimum_stock = minStockVal;
        product.price = price;

        myRef.child("products").push().setValue(product);
    }

    void removeProduct(String productKey) {
        myRef.child("products").child(productKey).removeValue();
    }

    void editProduct(String productKey, String productName, long category, long curStockVal, long minStockVal, double price) {
        Product product = new Product();
        product.name = productName;
        product.category = category;
        product.current_stock = curStockVal;
        product.minimum_stock = minStockVal;
        product.price = price;
        myRef.child("products").child(productKey).setValue(product);
    }

    List<Product> getProducts() {
        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        RealmResults<Product> realmResults = realm.where(Product.class).sort("id", Sort.DESCENDING).findAll();

        Log.e("TAGGY", realmResults.toString());
        return realmResults;
    }

    //Inventory
    List<Inventory> getInventoryList() {
        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        RealmResults<Inventory> realmResults = realm.where(Inventory.class).sort("id", Sort.DESCENDING).findAll();
        Log.e("TAGGY", realmResults.toString());
        return realmResults;
    }

    //Users
    void addUsers(String userName, String email, String password, long phone, long type) {

        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        int nUsers = realm.where(User.class).findAll().size();

        User user = new User();
        user.id = nUsers + 1;
        user.name = userName;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.type = type;

        myRef.child("users").push().setValue(user);
    }

    void removeUser(String userKey) {
        myRef.child("users").child(userKey).removeValue();
    }

    void editUser(String userKey, String userName, String email, String password, long phone, long type) {

        User user = new User();
        user.name = userName;
        user.email = email;
        user.password = password;
        user.phone = phone;
        user.type = type;

        myRef.child("users").child(userKey).setValue(user);
    }

    List<User> getUsers() {
        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        RealmResults<User> realmResults = realm.where(User.class).sort("id", Sort.DESCENDING).findAll();

        Log.e("TAGGY", realmResults.toString());
        return realmResults;
    }

    //Orders
    void addOrders(String cfname, String clname, String cemail, long phone, String payMethod,
                   String address, String deliveryTime, long deliveryBoy, long salesman, double shipping,
                   double subtotal, double tax, RealmList<MyInteger> products) {

        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        int nOrders = realm.where(Order.class).findAll().size();

        Order order = new Order();
        order.id = nOrders + 1;
        order.products = products;
        order.subtotal = subtotal;
        order.shipping = shipping;
        order.client_lname = clname;
        order.client_fname = cfname;
        order.client_email = cemail;
        order.cPhone = phone;
        order.PaymentMethod = payMethod;
        order.cAddress = address;
        order.delivery_boy = deliveryBoy;
        order.tax = tax;
        order.cDeliveryTime = deliveryTime;
        order.sales_man = salesman;

        myRef.child("orders").push().setValue(order);
    }

    void removeOrder(String orderKey) {
        myRef.child("orders").child(orderKey).removeValue();
    }

    void editOrder(String orderKey, String cfname, String clname, String cemail, long phone, String payMethod,
                   String address, String deliveryTime, long deliveryBoy, long salesman, double shipping,
                   double subtotal, double tax, RealmList<MyInteger> products) {

        Order order = new Order();
        order.products = products;
        order.subtotal = subtotal;
        order.shipping = shipping;
        order.client_lname = clname;
        order.client_fname = cfname;
        order.client_email = cemail;
        order.cPhone = phone;
        order.PaymentMethod = payMethod;
        order.cAddress = address;
        order.delivery_boy = deliveryBoy;
        order.tax = tax;
        order.cDeliveryTime = deliveryTime;
        order.sales_man = salesman;

        myRef.child("orders").child(orderKey).setValue(order);
    }

    List<Order> getOrders() {
        //TODO check where is realm getting closed
        realm = MyApplication.getInstance().getRealm();

        RealmResults<Order> realmResults = realm.where(Order.class).sort("id", Sort.DESCENDING).findAll();

        Log.e("TAGGY", realmResults.toString());
        return realmResults;
    }
}
