package in.shajikhan.lo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class Flowers extends Accessories {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), ShoppingCart.class);
                startActivity(intent);

                return;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayout layout = (LinearLayout) findViewById(R.id.flowers_layout) ;
        Context context = getApplicationContext() ;
        if (context == null)
            Snackbar.make(layout, "Application context is null", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        LinearLayout l = buildView("flowers") ;
        if (l == null)
            Snackbar.make(layout, "Accessories returned null!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        else
            layout.addView (l);

    }
}
