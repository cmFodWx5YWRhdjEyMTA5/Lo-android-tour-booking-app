package in.shajikhan.lo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.TEXT_ALIGNMENT_VIEW_START;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class DynamicAccessories extends AppCompatActivity {

    Payload payload = null ;
    ArrayList <Payload.Item> items  = null;
    Context context = this;
    /*  Finally I learnt how to use lists in
        Java *properly* !
     */

    public DynamicAccessories () {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_accessories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        Log.e ("type", type);
        this.payload = new Payload(getApplicationContext());
        this.items = payload.getItems(type);

        NestedScrollView dyna = (NestedScrollView) findViewById(R.id.dynapackagescrollviewuhthisnameisbig);
        dyna.addView(build());
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    LinearLayout build () {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (Payload.Item i : items) {
            if (i != null) {
                LinearLayout l = new LinearLayout(getApplicationContext());
                l.setGravity(Gravity.CENTER);
                l.setPadding(20, 20, 20, 20);
                // Bharti i love you
                l.setOrientation(HORIZONTAL);
                ImageView iv = new ImageView(getApplicationContext());
                iv.setAdjustViewBounds(true);
//                iv.setCropToPadding(true);
                /*iv.setMaxHeight(256);
                iv.setMaxWidth(256);
                */

                int did = payload.findIcon(i.icon);
                if (did != -1) {
                    try {
                        iv.setImageDrawable(getResources().getDrawable(did));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        payload.loadIcon(iv, i.icon);
                    }
                }
                else {
                    payload.loadIcon(iv, i.icon);
                }

                layout.addView(iv);

                LinearLayout l2 = new LinearLayout(getApplicationContext());
                l2.setGravity(Gravity.CENTER);
                l2.setPadding(20, 20, 20, 20);
                l2.setOrientation(VERTICAL);
                l.addView(l2);

                TextView label = new TextView(getApplicationContext());
                label.setTextSize(30);
                label.setTextColor(getResources().getColor(R.color.colorPrimary));
                label.setTypeface(Typeface.DEFAULT_BOLD);
                label.setGravity(Gravity.CENTER);
                label.setText(i.name);
                l2.addView(label);

                TextView label2 = new TextView(getApplicationContext());
                label2.setTextSize(20);
                label2.setTextColor(getResources().getColor(R.color.colorPrimary));
                label2.setTypeface(Typeface.DEFAULT_BOLD);
                label2.setGravity(Gravity.CENTER);
//                Log.e ("name", String.valueOf(i.price));
                label2.setText("₹ " + String.valueOf(i.price));
                l2.addView(label2);

                Button cb = new Button(getApplicationContext());
                cb.setPadding(20, 20, 20, 20);
                cb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                cb.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                cb.setText("Add to\ncart");
                cb.setTypeface(Typeface.DEFAULT_BOLD);
//                cb.setTextSize(12);
//                l.addView(cb);

                final int id = i.id;
                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cart cart = new Cart();
                        cart.addAccessory(getApplicationContext(), id);
                        Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

                TextView desc2 = new TextView(getApplicationContext());
                desc2.setText(i.description);
                desc2.setTextSize(18);
                desc2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                desc2.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
                desc2.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    desc2.setElegantTextHeight(true);
                }

                desc2.setPadding(20,20,20,20);

                layout.addView(l);
                layout.addView(cb);
                layout.addView(desc2);

            }
        }

        return layout;
    }
}
