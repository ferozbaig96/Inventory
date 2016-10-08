package Singleton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseReferenceSingleton {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private static DatabaseReferenceSingleton Instance = null;


    private DatabaseReferenceSingleton() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    public static DatabaseReferenceSingleton getInstance() {

        if (Instance == null) {
            Instance = new DatabaseReferenceSingleton();
        }

        return Instance;
    }

    public DatabaseReference getMyDatabaseRef() {
        return myRef;
    }
}
