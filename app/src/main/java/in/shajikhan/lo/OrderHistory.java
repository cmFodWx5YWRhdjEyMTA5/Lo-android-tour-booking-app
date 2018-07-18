package in.shajikhan.lo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderHistory extends AppCompatActivity {
    ArrayList <Order> orders  = new ArrayList() ;
    Context context = null;
    ProgressBar bar = null ;
    Payload payload = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        context = this ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.order_history_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.order_layout);
        bar = (ProgressBar) findViewById(R.id.order_history_progress);
        payload = new Payload(this);
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Cart c = new Cart();
        if (currentUser == null) {
            Snackbar.make(layout, "No user logged in!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            finish();
        }

        DatabaseReference ref = database.getReference(currentUser.getUid() + "/orders");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders = new ArrayList ();
                Iterable <DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot snap: iterable) {
                    /*String path = String.valueOf(d.getValue());
                    DataSnapshot snap = d.child(path);*/
                    Order order = new Order();
                    order.userID = currentUser.getUid();
                    order.transactionID = snap.getKey();
                    order.paymentMethod = (String) snap.child("payment_method").getValue();
                    order.date = (String) snap.child("date").getValue();
                    order.cart = (String) snap.child("cart").getValue();
                    order.amount = "₹ " + String.valueOf(snap.child("amount").getValue());

                    orders.add(order);
                }

                populateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void populateUI () {
        LinearLayout layout = (LinearLayout) findViewById(R.id.order_layout);
        layout.removeAllViews();

        bar.setVisibility(View.VISIBLE);
        boolean cycle = false ;

        if (orders.size () == 0) {
            TextView message = new TextView(this);
            message.setText("You have not placed any orders yet.");
            message.setTextSize(24);
            message.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            message.setPadding(20, 20, 20, 20);
            message.setGravity(Gravity.CENTER);
            layout.addView(message);
        } else {
            for (Order o : orders) {
                LinearLayout v = new LinearLayout(this);
                if (cycle) {
                    v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                }

                cycle = !cycle;

                v.setOrientation(LinearLayout.VERTICAL);
                v.setPadding(20, 20, 20, 50);
                v.setDividerPadding(50);
                LinearLayout h = new LinearLayout(this);
                h.setOrientation(LinearLayout.VERTICAL);
                TextView head = new TextView(this);
                head.setTextSize(24);
                head.setText("Date: " + o.date);
                head.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                TextView t = new TextView(this);
                t.setTextSize(16);
                t.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                v.addView(head);
                h.addView(t);
                v.addView(h);

                Button rem = new Button(this);
                rem.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                rem.setTextColor(getResources().getColor(android.R.color.white));
                rem.setText("Cancel order");

                final Order order = o;
                rem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Cancel this order?");
                        builder.setMessage("Are you sure you want top cancel this order? It might save your life!");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final Dialog p = Utility.progressDialog(context, "Cancelling order", "Please weight...");
                                p.show();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(order.userID + "/orders/" + order.transactionID);
                                ref.removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        p.cancel();
                                        if (databaseError == null) {
                                            final Intent intent = getIntent();
                                            Dialog d = Utility.messageDialog(context, "Success", "Your order has been successfully cancelled.");
                                        /*d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                finish();
                                                startActivity(intent);
                                            }
                                        });*/
                                            d.show();
                                        } else {
                                            Utility.message(context, "Failed to cancel order", "Your order could not be cancelled. Please try again");

                                        }
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("No", null);
                        builder.create().show();
                    }
                });

                layout.addView(v);
                String s = String.format(
                        "<b>Transaction ID</b>: %s<br>" +
                                "<b>Payment Method</b>: %s<br>" +
                                "<b>Billed Amount</b>: %s<br>",
                        o.transactionID,
                        o.paymentMethod,
                        o.amount
                );

                t.setText(Html.fromHtml(s));
                Log.e("order", s);

                ArrayList<Payload.Item> list = payload.getItemsFromString(o.cart);
                TextView tv = new TextView(this);
                tv.setTextSize(16);
                v.addView(tv);
                v.addView(rem);
                tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                String st = new String("<i>Selected items</i><br>");

                for (Payload.Item item : list) {
                    st += String.format(
                            "%s: <b>₹%d</b><br>", item.name, item.price
                    );
                }

                tv.setText(Html.fromHtml(String.format(st)));
            }
        }

        bar.setVisibility(View.GONE);

    }
}
