package in.shajikhan.lo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;

import static android.R.color.white;

public class Accessories extends AppCompatActivity {
    public Context context ; //= getApplicationContext() ;

    public Accessories (Context newContext) {
        this.context = newContext ;
    }

    public Accessories () {
        //context = getApplicationContext() ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext() ;
        setContentView(R.layout.activity_accessories);
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.accessories_layout) ;
        LinearLayout l = buildView("default") ;
        if (l != null) {
            layout.addView(l);
            ;
        }

        //Snackbar.make(layout, String.valueOf(things.size()), Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
    }

    public ArrayList getAccessories () {
        Field[] things = R.raw.class.getFields();
        ArrayList accessories = new ArrayList();
        //Resources resources = getApplicationContext().getResources();
        Resources resources = context.getResources();

        for (int count = 0; count < things.length; count++) {
            //Log.i("Raw Asset: ", fields[count].getName());
            InputStream rawResource ;
            try {
                rawResource = resources.openRawResource(things[count].getInt(null));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
                return null ;
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }

            Properties properties = new Properties();
            try {
                properties.load(rawResource);
            } catch (IOException e) {
                continue;
            }

            ArrayList item = new ArrayList();

            String name = properties.getProperty("name");
            String icon = properties.getProperty("icon");
            String description = properties.getProperty("description");
            String price = properties.getProperty("price");
            String id = properties.getProperty("id");
            String type = properties.getProperty("type");

            item.add(name);
            item.add(icon);
            item.add(description);
            item.add(price);
            item.add(id);
            item.add(type);

            accessories.add(item);
        }

        return accessories;
    }

    public ArrayList getAccessoriesAsMaps () {
        Field[] things = R.raw.class.getFields();
        ArrayList accessories = new ArrayList();
        //Resources resources = getApplicationContext().getResources();
        Resources resources = context.getResources();

        for (int count = 0; count < things.length; count++) {
            //Log.i("Raw Asset: ", fields[count].getName());
            InputStream rawResource ;
            try {
                rawResource = resources.openRawResource(things[count].getInt(null));
            }
            catch (IllegalAccessException e) {
                return null ;
            }

            Properties properties = new Properties();
            try {
                properties.load(rawResource);
            } catch (IOException e) {
                continue;
            }

            ArrayMap item = new ArrayMap();

            item.put("name", properties.getProperty("name"));
            item.put ("icon", properties.getProperty("icon"));
            item.put("description", properties.getProperty("description"));
            item.put ("price", properties.getProperty("price"));
            item.put("id", properties.getProperty("id"));
            item.put("type", properties.getProperty("type"));

            accessories.add(item);
        }

        return accessories;
    }

    public ArrayMap getAccessoryById (int id) {
        ArrayList list = getAccessoriesAsMaps() ;
        for (Object o: list) {
            ArrayMap a = (ArrayMap) o ;
            int i = Integer.valueOf(String.valueOf(a.get ("id"))) ;
            if (i == id)
                return a ;
        }

        System.out.println("ID" + String.valueOf(id) + "not found!");
        return null ;
    }

    public LinearLayout buildView (String accessory) {
        if (context == null) {
            System.out.println("buildview: context is null!");
            return null ;

        }

        if (this == null) {
            System.out.print("this is null. wtf?");
            return null ;
        }
        else
            System.out.println(this);

        System.out.println(166);
        LinearLayout l = new LinearLayout(this) ;
        System.out.println(173);
        l.setOrientation(LinearLayout.VERTICAL);
        ArrayList things = getAccessories () ;
        if (things == null) {
            Log.e (String.valueOf(this), "getAccessories returned null!");
            return null ;
        }

        final Cart c = new Cart () ;
        System.out.println(171);
        for (Object o: things)
        {
            ArrayList a = (ArrayList) o ;
            String name = (String) a.get (0) ;
            String icon = (String) a.get (1) ;
            String description = (String) a.get (2) ;
            String price = (String) a.get (3) ;
            final String id = (String) a.get (4) ;
            String type = (String) a.get (5) ;

            System.out.println(182);
            if (!type.equals(accessory)) {
                System.out.println(type);
                continue;
            }

            ImageView im = new ImageView (this) ;
            try {
                im.setImageResource(context.getResources().getIdentifier(icon, "drawable", this.getPackageName()));
            }
            catch (NullPointerException e) {
                System.out.println(e);
                return null;
            }
            //im.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            l.addView (im);

            TextView tv = new TextView(this),
                    tv2 = new TextView(this),
                    tv3 = new TextView(this) ;

            tv.setText(name);
            tv3.setText(price);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PT, 24);
            try {
                tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            catch (NullPointerException e) {
                System.out.println(e);
                return null;
            }
            tv2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv2.setText(description);
            l.addView (tv);
            l.addView (tv2);
            l.addView (tv3);

            Button b = new Button (this) ;
            b.setText("Add to Cart");
            b.setMaxWidth(300);
            b.setBackgroundColor (white);
            try {
                b.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            catch (NullPointerException e) {
                System.out.println(e);
                return null;
            }

            System.out.println(233);
            l.addView(b);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.addAccessory(getApplicationContext(), Integer.valueOf(id));
                    Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });
        }

        return l ;
    }
}
