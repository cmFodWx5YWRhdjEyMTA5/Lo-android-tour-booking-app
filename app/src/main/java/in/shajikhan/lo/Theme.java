package in.shajikhan.lo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

public class Theme extends Accessories {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.theme_layout) ;
        LinearLayout l = buildView("theme") ;
        if (l == null) {
            System.out.println("cannot propagate view: buildview returned null");
        }
        else
            layout.addView (l);

    }
}
