package in.shajikhan.lo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.color.transparent;
import static android.R.color.white;
import static java.lang.String.valueOf;

public class ViewOrder extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        LinearLayout layout = (LinearLayout) findViewById(R.id.show_order_layout);
        final Cart c = new Cart () ;
        int total = 0 ;

        ArrayList packages = c.getPackages(getApplicationContext()) ;
        ArrayList accessories = c.getAccessories(getApplicationContext()) ;

        LinearLayout lll = new LinearLayout(this);
        layout.addView(lll);
        lll.setOrientation(LinearLayout.VERTICAL);

        TextView bill = new TextView(this);
        bill.setTextSize(TypedValue.COMPLEX_UNIT_PT, 24);
        bill.setGravity(Gravity.CENTER);
        bill.setPadding(20,20,20,20);
        bill.setBottom(20);
        bill.setTextColor((getResources().getColor(R.color.colorPrimaryDark)));
        bill.setTypeface(Typeface.DEFAULT_BOLD);

        Payload payload = new Payload(getApplicationContext());

        if (packages != null) {
            TextView head = new TextView(this) ;
            head.setText("Packages");
            head.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
            head.setPadding(20, 30, 20, 30);
            head.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            head.setTextColor(getResources().getColor(white));
            layout.addView(head);
            Space sp = new Space(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                sp.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            sp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            sp.setMinimumWidth(10);
            layout.addView(sp);

            for (Object v : packages) {
                final String s = valueOf(v);
                System.out.println(s);

                Integer id = -1 ;
                try {
                    id = Integer.valueOf(s);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }

                Payload.ItemGroup item = payload.getItemGroup(id);
                if (item == null) {
                    continue;
                }

                ImageView im = new ImageView(this);
                int icon = payload.findIcon(item.icon);
                try {
                    im.setImageDrawable(getResources().getDrawable(payload.findIcon(item.icon)));
                } catch (Exception e) {
                    e.printStackTrace();
                    payload.loadIcon(im, item.icon);
                }

                im.setAdjustViewBounds(true);
                im.setMaxWidth(256);

                LinearLayout l = new LinearLayout(this),
                        h = new LinearLayout(this) ;
                l.addView(im) ;
                l.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv = new TextView(this);
                tv.setPadding(10,10,10,10);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
                tv.setTextColor (getResources().getColor(R.color.colorPrimary));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
/*
                String text ;
                try {
                    text = getString(getResources().getIdentifier("package" + s, "string", this.getPackageName())) ;
                }

                catch (Resources.NotFoundException e) {
                    System.out.println("Package" + s + " not found!");
                    continue;
                }
*/
                tv.setText(item.name);
                h.setOrientation(LinearLayout.VERTICAL);
                h.addView(tv);

                l.addView(h);
                layout.addView(l);
                /*String price = "20000";
                try {
                    price = getString(getResources().getIdentifier("package" + s + "price", "string", this.getPackageName()));
                }
                catch (Resources.NotFoundException e) {
                    price = "20000" ;
                }
*/
                total += Integer.valueOf(item.price) ;

                TextView tp = new TextView(this);
                tp.setText("₹ " + String.valueOf(item.price));
                tp.setTypeface(Typeface.DEFAULT_BOLD);
                tp.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12);
                tp.setTextColor (getResources().getColor(R.color.colorPrimaryDark));
                tp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                h.addView(tp);
                im.setPadding(20,20,20,20);
                tv.setPadding(20,20,20,20);
                tv.setGravity(Gravity.CENTER);

            }
        }
        /*else {
            Snackbar.make(layout, "No packages found!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }*/

        if (accessories != null) {
            Accessories ac = new Accessories(getApplicationContext()) ;
            TextView head = new TextView(this) ;
            head.setText("Accessories");
            head.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
            head.setPadding(20, 30, 20, 30);
            head.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            head.setTextColor(getResources().getColor(white));
            layout.addView(head);
            Space sp = new Space(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sp.setBackgroundTintMode(PorterDuff.Mode.DARKEN);
            }
            sp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            sp.setMinimumWidth(10);
            layout.addView(sp);

            for (Object v : accessories) {
                final String s = valueOf(v);
                System.out.println(s);

                if (s == "") {
                    Log.e("access", "empty strin");
                    continue;
                }

                Integer id = -1 ;
                try {
                    id = Integer.valueOf(s);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }

                Payload.Item item = payload.getItem(id);
                if (item == null) {
                    Log.e ("access", "item null for " + String.valueOf(id));
                    continue;
                }

                int icon = payload.findIcon(item.icon);

                ImageView im = new ImageView(this);

//                    im.setImageResource(getResources().getIdentifier(valueOf(map.get("icon")), "drawable", this.getPackageName()));
                try {
                    im.setImageDrawable(getResources().getDrawable(icon));
                } catch (Exception e) {
                    e.printStackTrace();
                    payload.loadIcon(im, item.icon);
                }

                im.setAdjustViewBounds(true);
                im.setMaxWidth(256);

                LinearLayout l = new LinearLayout(this),
                        h = new LinearLayout(this) ;
                l.addView(im) ;
                l.setOrientation(LinearLayout.HORIZONTAL);


                TextView tv = new TextView(this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
                tv.setTextColor (getResources().getColor(R.color.colorPrimary));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setText(item.name);
                tv.setPadding(10,10,10,10);
                total += Integer.valueOf (item.price);
                h.setOrientation(LinearLayout.VERTICAL);
                h.addView(tv);

                TextView tp = new TextView(this);
                tp.setText("₹ " + String.valueOf(item.price));
                tp.setTypeface(Typeface.DEFAULT_BOLD);
                tp.setTextSize(TypedValue.COMPLEX_UNIT_PT, 12);
                tp.setTextColor (getResources().getColor(R.color.colorPrimaryDark));
                tp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                h.addView(tp);

                l.addView(h);
                layout.addView(l);
                im.setPadding(20,20,20,20);
                tv.setPadding(20,20,20,20);
                tv.setGravity(Gravity.CENTER);

            }
        }

        bill.setText("₹ " + valueOf(total));

    }
}
