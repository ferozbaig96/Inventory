package getMyApplicationContext;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.RealmModel.Category;
import Models.RealmModel.Inventory;
import Models.RealmModel.MyInteger;
import Models.RealmModel.Order;
import Models.RealmModel.Product;
import Models.RealmModel.User;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private static MyApplication Instance;

    private static DatabaseReference myDatabaseRef;

    private static Realm realm;
    private static RealmConfiguration realmConfig;

    private DataSnapshot mDataSnapshot;
    boolean isInitialDataLoad = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;

        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(Instance.getApplicationContext()).name("myRealm.realm").build();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myDatabaseRef = firebaseDatabase.getReference();
        addFirebaseListeners();
    }

    public static MyApplication getInstance() {
        return Instance;
    }

    public DatabaseReference getMyDatabaseRef() {
        return myDatabaseRef;
    }

    public Realm getRealm() {

        if (realm == null || realm.isClosed()) {
            try {
                //Realm.deleteRealm(realmConfig);
                // Open the Realm
                realm = Realm.getInstance(realmConfig);
            } catch (Exception e) {
                Log.e("TAG MyApplication", e.toString());
            }
        }

        return realm;
    }

    public RealmConfiguration getRealmConfig() {
        return realmConfig;
    }

    void addFirebaseListeners() {
        myDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDataSnapshot = dataSnapshot;

                Log.e("TAGGYSD", dataSnapshot.getChildrenCount() + "");
                Log.e("TAGG catchildrenCount", dataSnapshot.child("categories").getChildrenCount() + "");
                Log.e("TAGGYSD", dataSnapshot + "");

                deleteOldRealm();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        saveRecievedFirebaseData(mDataSnapshot);
                        isInitialDataLoad = false;

                        return null;
                    }
                }.execute();

/*
                Toast.makeText(MainActivity.this, "Database updated. Please reopen app for the changes to take place", Toast.LENGTH_SHORT).show();
*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


        myDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (isInitialDataLoad)
                    return;

                mDataSnapshot = dataSnapshot;
                Log.e("TAG", "addValueEventListener onDataChange");

                deleteOldRealm();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        saveRecievedFirebaseData(mDataSnapshot);
                        return null;
                    }
                }.execute();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteOldRealm() {
        if (!realm.isClosed())
            realm.close();
        Realm.deleteRealm(realmConfig);     //resetting realm
        realm = getRealm();
    }

    private void saveRecievedFirebaseData(DataSnapshot dataSnapshot) {

        Realm realm = Realm.getInstance(realmConfig);

        final DataSnapshot categoriesDS = dataSnapshot.child("categories");
        DataSnapshot inventoryDS = dataSnapshot.child("inventory");
        DataSnapshot ordersDS = dataSnapshot.child("orders");
        DataSnapshot productsDS = dataSnapshot.child("products");
        DataSnapshot usersDS = dataSnapshot.child("users");

        for (final DataSnapshot d : categoriesDS.getChildren()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Category category = realm.createObject(Category.class);
                    category.id = (long) d.child("id").getValue();
                    category.name = (String) d.child("name").getValue();

                    category.key = d.getKey();
                }
            });
        }

        for (final DataSnapshot d : inventoryDS.getChildren()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Inventory inventory = realm.createObject(Inventory.class);
                    inventory.id = (long) d.child("id").getValue();
                    inventory.price_per_item = (double) d.child("price_per_item").getValue();
                    inventory.product = (long) d.child("product").getValue();
                    inventory.quantity = (long) d.child("quantity").getValue();

                    inventory.key = d.getKey();
                }
            });
        }

        for (final DataSnapshot d : ordersDS.getChildren()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Order order = realm.createObject(Order.class);
                    order.id = (long) d.child("id").getValue();
                    order.PaymentMethod = (String) d.child("PaymentMethod").getValue();
                    order.cAddress = (String) d.child("cAddress").getValue();
                    order.cDeliveryTime = (String) d.child("cDeliveryTime").getValue();
                    order.cPhone = (Long) d.child("cPhone").getValue();
                    order.client_email = (String) d.child("client_email").getValue();
                    order.client_fname = (String) d.child("client_fname").getValue();
                    order.client_lname = (String) d.child("client_lname").getValue();
                    order.delivery_boy = (long) d.child("delivery_boy").getValue();
                    order.sales_man = (long) d.child("sales_man").getValue();
                    order.shipping = (double) d.child("shipping").getValue();
                    order.subtotal = (double) d.child("subtotal").getValue();
                    order.tax = (double) d.child("tax").getValue();

                    DataSnapshot prod = d.child("products");

                    for (DataSnapshot p : prod.getChildren()) {
                        MyInteger myInteger = realm.createObject(MyInteger.class);
                        myInteger.val = (long) p.child("val").getValue();
                        myInteger.key = p.getKey();

                        order.products.add(myInteger);
                    }

                    order.key = d.getKey();
                }
            });
        }

        for (final DataSnapshot d : productsDS.getChildren()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Product product = realm.createObject(Product.class);
                    product.id = (long) d.child("id").getValue();
                    product.category = (long) d.child("category").getValue();
                    product.current_stock = (long) d.child("current_stock").getValue();
                    product.minimum_stock = (long) d.child("minimum_stock").getValue();
                    product.name = (String) d.child("name").getValue();
                    product.price = (double) d.child("price").getValue();

                    product.key = d.getKey();
                }
            });
        }

        for (final DataSnapshot d : usersDS.getChildren()) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.createObject(User.class);
                    user.id = (long) d.child("id").getValue();
                    user.email = (String) d.child("email").getValue();
                    user.name = (String) d.child("name").getValue();
                    user.password = (String) d.child("password").getValue();
                    user.phone = (long) d.child("phone").getValue();
                    user.type = (long) d.child("type").getValue();

                    user.key = d.getKey();
                }
            });
        }

        //Closing realm (asynctask)
        realm.close();

    }
}
