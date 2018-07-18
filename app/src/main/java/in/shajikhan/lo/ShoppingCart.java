package in.shajikhan.lo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.id;
import static android.R.color.transparent;
import static android.R.color.white;
import static in.shajikhan.lo.R.drawable.clear;
import static in.shajikhan.lo.R.raw.payload;
import static java.lang.String.valueOf;

public class ShoppingCart extends AppCompatActivity {
    Context context = this ;
    void selectGateway () {
        Intent intent = new Intent(this, SelectGateway.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.cart_layout) ;
        final Cart c = new Cart () ;
//        c.debug(getApplicationContext());
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

        LinearLayout box = new LinearLayout(this);
        box.setGravity(Gravity.CENTER);

        ImageButton cli = new ImageButton(this);
        cli.setBackgroundColor(getResources ().getColor(transparent));
//        cli.setForegroundGravity(Gravity.CENTER);
        cli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.clear(getApplicationContext());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        cli.setImageDrawable(getResources().getDrawable(clear));
        Button clear = new Button(this);
//        clear.setGravity(Gravity.CENTER);

        clear.setText("Empty Cart");
        clear.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        clear.setBackgroundColor(getResources().getColor(transparent));
        clear.setPadding(10,10,10,10);
        lll.addView(box);
        box.addView(cli);
        box.setGravity(Gravity.CENTER);
        box.addView(clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.clear(getApplicationContext());
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

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

                Button rem = new Button(this) ;
                rem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c.remove(getApplicationContext(), "packages", Integer.valueOf(s));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                rem.setBackgroundColor (getResources().getColor(transparent));
                rem.setTextColor (getResources().getColor(R.color.colorPrimaryDark));
                rem.setText("Remove");

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
                l.addView(rem);
                rem.setPadding(20,20,20,20);
                im.setPadding(20,20,20,20);
                tv.setPadding(20,20,20,20);
                tv.setGravity(Gravity.CENTER);
                rem.setGravity(Gravity.CENTER);

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

                Button rem = new Button(this) ;
                rem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c.remove(getApplicationContext(), "accessories", Integer.valueOf(s));
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                rem.setBackgroundColor (white);
                rem.setTextColor (getResources().getColor(R.color.colorPrimaryDark));
                rem.setText("Remove");

                l.addView(h);
                l.addView(rem);
//                l.setGravity(Gravity.CENTER);
                layout.addView(l);
                rem.setPadding(20,20,20,20);
                im.setPadding(20,20,20,20);
                tv.setPadding(20,20,20,20);
                tv.setGravity(Gravity.CENTER);
                rem.setGravity(Gravity.CENTER);

            }
        }
        /*else {
            Snackbar.make(layout, "No packages found!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }*/

        bill.setText("₹ " + valueOf(total));

        TextView head1 = new TextView(this) ;
        head1.setText("Select Date");
        head1.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
        head1.setPadding(20, 30, 20, 30);
        head1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        head1.setTextColor(getResources().getColor(white));
        head1.setPadding(10,10,10,10);
        layout.addView(head1);

        LinearLayout oo = new LinearLayout(this);

        final CalendarView calendar = new CalendarView(this) ;
        calendar.setPadding(10,10,10,10);
        calendar.setMinimumHeight(1000);
//        calendar.setShowWeekNumber(true);
        calendar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
                pref.edit ().putInt("datey", year).apply() ;
                pref.edit ().putInt("datem", month).apply() ;
                pref.edit ().putInt("dated", day).apply() ;
            }
        });

        oo.addView(calendar);
        layout.addView(oo);

        SharedPreferences pref = getApplicationContext().getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        int year = pref.getInt("datey",-1),
                month = pref.getInt("datem",-1),
                day = pref.getInt("dated",-1);

        Calendar cal = Calendar.getInstance() ;
        if (year != -1 && month != -1 && day != -1 ) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
            calendar.setDate(cal.getTimeInMillis());
        }


        TextView head2 = new TextView(this) ;
        head2.setText("Checkout");
        head2.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
        head2.setPadding(20, 30, 20, 30);
        head2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        head2.setTextColor(getResources().getColor(white));
        head2.setPadding(10,10,10,10);
//        layout.addView(head2);

        Button pay = new Button(this) ;
        pay.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
        pay.setBackgroundColor(getResources().getColor(transparent));
        pay.setTypeface(Typeface.DEFAULT_BOLD);
        pay.setText("Buy now");
        final int finalTotal = total;
        final Context context = this;
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalTotal == 0) {
                    Utility.message(context, "No items in cart", "You have not added any items to cart. Add something and try again.");
                    return ;
                }

                long date = calendar.getDate();
                SharedPreferences pref = getApplicationContext().getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
                String d = DateFormat.format ("dd/MM/yyyy", date).toString();
                pref.edit ().putString("date", d).apply() ;

                if (! Utility.isSomeoneLoggedIn()) {
                    Dialog dialog = Utility.messageDialog(context, "Log in first", "You have to log in to make a purchase. Click below to log in.");
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            startActivity(new Intent(context, LoginActivity.class));
                        }
                    });
                    dialog.show();
                } else {
                    selectGateway();
                }
            }
        });

        pay.setBackgroundColor (Color.parseColor("#FF8C00"));
        pay.setTextColor (Color.parseColor("#ffffff"));
        pay.setGravity(Gravity.CENTER);
        pay.setTransformationMethod(null);

        layout.addView(bill);
        layout.addView (pay);
    }
}
