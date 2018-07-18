package in.shajikhan.lo;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.Inflater;

public class Error extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_error,null);
        final Intent intent = getIntent();
        final String url = intent.getStringExtra("url"),
                icon = intent.getStringExtra("icon"),
                msg = intent.getStringExtra("msg");
        TextView tv = (TextView) findViewById(R.id.error_msg);
        if (msg != null) {
            tv.setText(msg);
        }

        ImageView iv = (ImageView) findViewById(R.id.error_logo);
        if (icon != null) {
            Payload payload = new Payload(getApplicationContext());
            payload.loadIcon(iv, icon);
        }

        Button b = (Button) findViewById(R.id.error_b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if (url != null) {
                    intent1.setData(Uri.parse(url));
                } else {
                    intent1.setData(Uri.parse("http://shajikhan.in/apps"));
                }
                startActivity(intent1);
            }
        });
    }
}
