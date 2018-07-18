package in.shajikhan.lo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.internal.zzbmj;
import com.google.android.gms.internal.zzbml;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.PendingIntent.getActivity;
import static java.lang.String.valueOf;

public class COD extends AppCompatActivity {
    FirebaseAuth mAuth ; //= FirebaseAuth.getInstance();
    FirebaseUser currentUser ;//= mAuth.getCurrentUser();

    void home() {
        Intent intent = new Intent(this, OrderPlaced.class);
        startActivity(intent);
    }

    public Dialog message(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Cart c = new Cart();
        int total = c.getTotal(getApplicationContext());
        TextView amt = (TextView) findViewById(R.id.cod_amount);
        amt.setText("₹ " + valueOf(total));

        final TextView name = (TextView) findViewById(R.id.cod_name);
        final TextView phone = (TextView) findViewById(R.id.cod_phone);
        final TextView address = (TextView) findViewById(R.id.cod_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(currentUser.getUid());
            if (ref != null) {
                final Dialog p = Utility.progressDialog(this, "Getting info", "Please wait...");
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
        }

        final ProgressBar bar = (ProgressBar) findViewById(R.id.cod_progress);
        final Cart cart = new Cart();
        final Button pay = (Button) findViewById(R.id.cod_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser == null) {
                    Snackbar.make(view, "No user logged in!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    pay.setEnabled(true);
                    return;
                }

                name.setError(null);
                address.setError(null);
                phone.setError(null);
                String a = name.getText().toString();
                String d = address.getText().toString();
                String p = phone.getText().toString();

                if (a.equals("")) {
                    name.setError("Name cannot be blank");
                    bar.setVisibility(View.GONE);
                    pay.setEnabled(true);
                    return;
                }

                if (p.equals("")) {
                    phone.setError("No Hotline Bling?");
                    bar.setVisibility(View.GONE);
                    pay.setEnabled(true);
                    return;
                }

                if (d.equals("")) {
                    address.setError("You don\'t know where you live? Our plans aren\'t for homeless people.");
                    bar.setVisibility(View.GONE);
                    pay.setEnabled(true);
                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();

                if (currentUser == null) {
                    message("Log in first!", "You have to log in first in order to place your order");
                    name.setError(null);
                    address.setError(null);
                    phone.setError(null);
                    bar.setVisibility(View.GONE);
                    pay.setEnabled(true);
                    return ;
                }

                bar.setVisibility(View.VISIBLE);

                String txn = c.generateTransactionID(currentUser.getUid());

                mdatabase.child(currentUser.getUid()).child("name").setValue(a, new Complete());
                mdatabase.child(currentUser.getUid()).child("address").setValue(d, new Complete());
                mdatabase.child(currentUser.getUid()).child("phone").setValue(p, new Complete());
                mdatabase.child(currentUser.getUid()).child("email").setValue(currentUser.getEmail(), new Complete());
                mdatabase.child(currentUser.getUid()).child("orders").child(txn).child("payment_method").setValue("COD", new Complete());
                mdatabase.child(currentUser.getUid()).child("orders").child(txn).child("date").setValue(cart.getDate(getApplicationContext()), new Complete());
                mdatabase.child(currentUser.getUid()).child("orders").child(txn).child("amount").setValue(cart.getTotal(getApplicationContext()), new Complete());

                mdatabase.child(currentUser.getUid()).child("orders").child(txn).child ("cart").setValue(cart.getCart(getApplicationContext()), new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError e, DatabaseReference ref) {
                        if (e != null) {
                            message("Failed to add order! Try again.", e.getMessage());
                            name.setError(null);
                            address.setError(null);
                            bar.setVisibility(View.GONE);
                            pay.setEnabled(true);
                        } else {
//                            message("Congratulations", "Your order has been successfully placed").show();
                            finishAffinity();
                            home();

                        }
                    }
                });
            }
        });

    }

    class Complete implements DatabaseReference.CompletionListener {
        public void onComplete (DatabaseError e, DatabaseReference ref) {
            if (e != null) {
                message("Failed to add order! Try again.", e.getMessage());
            }
        }
    }
}