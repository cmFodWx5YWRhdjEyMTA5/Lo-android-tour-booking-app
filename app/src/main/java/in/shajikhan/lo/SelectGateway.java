package in.shajikhan.lo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class SelectGateway extends AppCompatActivity {
    Context context = null ;

    void start (Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_select_gateway);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RadioButton cod = (RadioButton) findViewById(R.id.payment_method_4);

        Button next = (Button) findViewById(R.id.gateway_b);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cod.isChecked()) {
                    start (COD.class);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
