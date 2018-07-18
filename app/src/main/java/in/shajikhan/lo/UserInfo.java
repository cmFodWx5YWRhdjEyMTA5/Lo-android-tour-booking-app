package in.shajikhan.lo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfo extends AppCompatActivity {
    TextView name = null,
            address = null,
            phone = null;

    Context context = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.info_name);
        address = (TextView) findViewById(R.id.info_address);
        phone = (TextView) findViewById(R.id.info_phone);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Utility.message(this, "Log in first!", "You have to log in to update your information.");
            finish();
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(currentUser.getUid());
        if (ref != null) {
            final Dialog p = Utility.progressDialog(context, "Getting info", "Please wait...");
            p.show();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String no = (String) dataSnapshot.child("phone").getValue();
                    String username = (String) dataSnapshot.child("name").getValue();
                    String useraddress = (String) dataSnapshot.child("address").getValue();

                    phone.setText(no);
                    address.setText(useraddress);
                    name.setText(username);
                    p.cancel();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Button update = (Button) findViewById(R.id.info_pay);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                Dialog p = Utility.progressDialog(context, "Updating info", "Please wait whiel we update your information...");
                p.show();
                String name_ = name.getText().toString();
                String address_ = address.getText().toString();
                String phone_ = phone.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(currentUser.getUid());

                if (name_ != null) {
                    ref.child("name").setValue(name_);
                }

                if (address_ != null) {
                    ref.child("address").setValue(address_);
                }

                if (phone_ != null) {
                    ref.child("phone").setValue(phone_);
                }

                p.cancel();
                Utility.message(context, "Success!", "User info updated successfully");
                view.setEnabled(true);
            }
        });
    }
}