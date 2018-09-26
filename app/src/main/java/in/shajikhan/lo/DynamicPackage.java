package in.shajikhan.lo;

/*public class DynamicPackage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_package);
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
    }
}*/

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.color.transparent;

public class DynamicPackage extends LinearLayout {

    Context context = null ;
    Payload payload = null ;
    Payload.ItemGroup group = null ;

    public DynamicPackage(Context context) {
        super(context);
        this.context = context;
        this.payload = new Payload(context);
        build();
    }

    public DynamicPackage(Context context, Payload.ItemGroup group) {
        super(context);
        this.context = context;
        this.payload = new Payload(context);
        this.group = group;
        build();
    }

    void build () {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        ImageView logo = new ImageView (context);

//        logo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        logo.setCropToPadding(true);
        logo.setAdjustViewBounds(true);
        logo.setPadding(0,0,0,20);
        addView(logo);//, layoutParams);

        int logo_id = payload.findIcon(group.icon);
        try {
            logo.setImageDrawable(getResources().getDrawable(logo_id));
        }
        catch (Exception e) {
            e.printStackTrace();
            payload.loadIcon(logo, group.icon);
        }

        ArrayList items = new ArrayList(),
                one = new ArrayList();
        items.add (one);
        one.add(null);
        one.add(group.id);

        TextView header = new TextView(context);
        header.setText(group.name);
        header.setTextSize(50);
        header.setTextColor(getResources().getColor(R.color.colorPrimary));
        header.setTypeface(Typeface.DEFAULT_BOLD);
        header.setGravity(Gravity.CENTER);
        // The "logos" are the text imprint
        // So we are commenting this out here
        // addView(header);//, layoutParams);

        TextView description = new TextView(context);
        description.setText(group.description);
        description.setTextSize(18);
        description.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
        description.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            description.setElegantTextHeight(true);
        }

        description.setPadding(20,20,20,20);
//        description.setMinimumWidth(100);

        TextView price = new TextView(context);
        price.setText("₹ " + String.valueOf(group.price));
        price.setTextSize(30);
        price.setTextColor(getResources().getColor(R.color.colorPrimary));
        price.setTypeface(Typeface.DEFAULT_BOLD);
        price.setGravity(Gravity.CENTER);
        price.setPadding(20,20,20,20);
        addView(price);//, layoutParams);

        Button fay = new Button(context) ;
        fay.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
        fay.setBackgroundColor(getResources().getColor(transparent));
        fay.setTypeface(Typeface.DEFAULT_BOLD);
        fay.setText("Add to cart");
        fay.setBackgroundColor (Color.parseColor("#FF8C00"));
        fay.setTextColor (Color.parseColor("#ffffff"));
        fay.setGravity(Gravity.CENTER);
        fay.setTransformationMethod(null);

        fay.setPadding(20,20,20,20);
//        addView (fay);

        addView(description);//, layoutParams);

        for (Object o: group.addons) {
            Payload.Item i = payload.getItem(Integer.valueOf(String.valueOf(o)));

            if (i != null) {
                LinearLayout l = new LinearLayout(context);
                l.setGravity(Gravity.CENTER);
                l.setPadding(20,20,20,20);
                l.setOrientation(HORIZONTAL);
                ImageView iv = new ImageView(context);
//                iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                iv.setAdjustViewBounds(true);
                iv.setMaxHeight(256);
                iv.setMaxWidth(256);
                int did = payload.findIcon(i.icon);
                try {
                    iv.setImageDrawable(getResources().getDrawable(did));
                } catch (Exception e) {
                    e.printStackTrace();
                    payload.loadIcon(iv, i.icon);
                }

                l.addView(iv);

                LinearLayout l2 = new LinearLayout(context);
                l2.setGravity(Gravity.CENTER);
                l2.setPadding(20,20,20,20);
                l2.setOrientation(VERTICAL);
                l.addView(l2);

                TextView label = new TextView(context);
                label.setTextSize(30);
                label.setTextColor(getResources().getColor(R.color.colorPrimary));
                label.setTypeface(Typeface.DEFAULT_BOLD);
                label.setGravity(Gravity.CENTER);
                label.setText(i.name);
                l2.addView(label);

                TextView label2 = new TextView(context);
                label2.setTextSize(20);
                label2.setTextColor(getResources().getColor(R.color.colorPrimary));
                label2.setTypeface(Typeface.DEFAULT_BOLD);
                label2.setGravity(Gravity.CENTER);
//                Log.e ("name", String.valueOf(i.price));
                label2.setText("₹ " + String.valueOf(i.price));
                l2.addView(label2);

                CheckBox cb = new CheckBox(context);
                cb.setPadding(20,20,20,20);
                l.addView(cb);

                ArrayList too = new ArrayList();
                too.add (cb);
                too.add (i.id);
                items.add(too);

                TextView desc2 = new TextView(context);
                desc2.setText(i.description);
                desc2.setTextSize(18);
                desc2.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
                desc2.setGravity(Gravity.CENTER);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    desc2.setElegantTextHeight(true);
                }

/*
                Button add = new Button(context);
                add.setText("Add to Cart");
                add.setGravity(Gravity.CENTER);
                add.setBackgroundColor(getResources().getColor(transparent));
                add.setTextColor (getResources().getColor(colorPrimaryDark));
*/

//                addView(add);
                addView(l);
                addView(desc2);
            } else {
                Payload.ItemGroup j = payload.getItemGroup(Integer.valueOf(String.valueOf(o)));
                if (j == null)
                    continue ;

                GradientDrawable border = new GradientDrawable();
//                border.setColor(); //white background
                border.setStroke(2, getResources().getColor(R.color.colorPrimaryDark)); //black border w

                LinearLayout swipe = new LinearLayout(context);
                swipe.setOrientation(HORIZONTAL);
                swipe.setGravity(Gravity.CENTER);
                swipe.setBackground(border);

                swipe.setPadding(20,20,20,20);

                LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                swipe.setLayoutParams(p);
                HorizontalScrollView sw = new HorizontalScrollView(context);
//                sw.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                addView(sw);
                sw.addView(swipe);
                ImageView scroll = new ImageView(context);
                scroll.setImageDrawable(getResources().getDrawable(R.drawable.scroll));
                scroll.setAdjustViewBounds(true);
                scroll.setMaxHeight(150);
                scroll.setMaxWidth(150);
                scroll.setPadding(20,20,20,20);
//                addView(swipe);
                swipe.addView(scroll);

                final ArrayList radioboxes = new ArrayList();

                for (Object x: j.addons) {
                    int id = (int) x;

                    Payload.Item item = payload.getItem(id);
                    LinearLayout l = new LinearLayout(context);
                    swipe.addView(l);
                    l.setLayoutParams(p);

                    l.setGravity(Gravity.CENTER);
                    l.setPadding(20,20,20,20);
                    l.setOrientation(HORIZONTAL);
                    ImageView iv = new ImageView(context);
                    iv.setAdjustViewBounds(true);
                    iv.setMaxHeight(256);
                    iv.setMaxWidth(256);
                    int did = payload.findIcon(item.icon);
                    if (did != -1) {
                        try {
                            iv.setImageDrawable(getResources().getDrawable(did));
                        } catch (Exception e) {
                            e.printStackTrace();
                            payload.loadIcon(iv, item.icon);
                        }
                    } else {
                        payload.loadIcon(iv, item.icon);
                    }

                    l.addView(iv);

                    LinearLayout l2 = new LinearLayout(context);
                    l2.setGravity(Gravity.CENTER);
                    l2.setPadding(20,20,20,20);
                    l2.setOrientation(VERTICAL);
                    l.addView(l2);

                    CheckBox cb = new CheckBox(context);
                    radioboxes.add (cb);
                    LinearLayout l3 = new LinearLayout(context);
                    l3.setGravity(Gravity.CENTER);

                    ArrayList too = new ArrayList();
                    too.add (cb);
                    too.add (id);
                    items.add (too);

                    TextView label = new TextView(context);
                    label.setTextSize(30);
                    label.setTextColor(getResources().getColor(R.color.colorPrimary));
                    label.setTypeface(Typeface.DEFAULT_BOLD);
                    label.setGravity(Gravity.CENTER);
                    label.setText(item.name);
                    l2.addView(label);

                    TextView label2 = new TextView(context);
                    label2.setTextSize(20);
                    label2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    label2.setTypeface(Typeface.DEFAULT_BOLD);
                    label2.setGravity(Gravity.CENTER);
//                Log.e ("name", String.valueOf(i.price));
                    label2.setText("₹ " + String.valueOf(item.price));
                    l2.addView(l3);
                    l3.addView(label2);
                    l3.addView(cb);


                }

                int counter = 0;
                for (counter = 0 ; counter < radioboxes.size() ; counter ++) {
                    CheckBox cb = (CheckBox) radioboxes.get(counter);
                    final int cunt = counter ;
                    cb.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int c = 0 ;
                            for (c = 0 ; c < radioboxes.size (); c ++) {
                                if (c != cunt) {
                                    CheckBox b = (CheckBox) radioboxes.get (c);
                                    b.setChecked(false);
                                }
                            }
                        }
                    });
                }

                ImageView scrollrev = new ImageView(context);
                scrollrev.setImageDrawable(getResources().getDrawable(R.drawable.scrollrev));
                scrollrev.setAdjustViewBounds(true);
                scrollrev.setMaxHeight(150);
                scrollrev.setMaxWidth(150);
                scrollrev.setPadding(20,20,20,20);
//                addView(swipe);
                swipe.addView(scrollrev);

            }

        }

        Button pay = new Button(context) ;
        pay.setTextSize(TypedValue.COMPLEX_UNIT_PT, 16);
        pay.setBackgroundColor(getResources().getColor(transparent));
        pay.setTypeface(Typeface.DEFAULT_BOLD);
        pay.setText("Add to cart");
        pay.setBackgroundColor (Color.parseColor("#FF8C00"));
        pay.setTextColor (Color.parseColor("#ffffff"));
        pay.setGravity(Gravity.CENTER);
        pay.setTransformationMethod(null);

        pay.setPadding(20,20,20,20);
        addView (pay);
        final ArrayList ayetums = items ;
        pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0 ;
                Cart cart = new Cart () ;
                for (i = 0 ; i < ayetums.size(); i ++) {
                    ArrayList list = (ArrayList) ayetums.get (i);
                    CheckBox cb = (CheckBox) list.get(0);
                    if (cb == null) {
                        cart.addPackage(context, (Integer) list.get(1));
                    }
                    else if (cb.isChecked()) {
                        cart.addAccessory(context, (Integer)list.get(1));
                    }
                    Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();                }
            }
        });

        fay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0 ;
                Cart cart = new Cart () ;
                for (i = 0 ; i < ayetums.size(); i ++) {
                    ArrayList list = (ArrayList) ayetums.get (i);
                    CheckBox cb = (CheckBox) list.get(0);
                    if (cb == null) {
                        cart.addPackage(context, (Integer) list.get(1));
                    }
                    else if (cb.isChecked()) {
                        cart.addAccessory(context, (Integer)list.get(1));
                    }
                    Snackbar.make(view, "Added to Cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();                }
            }
        });

    }
}