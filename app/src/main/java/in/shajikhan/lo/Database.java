package in.shajikhan.lo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by djshaji on 7/11/17.
 */

public class Database {
    DatabaseReference mDatabase = null;
    Database () {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

}
