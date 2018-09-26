package in.shajikhan.lo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context = null ;
    FirebaseAuth mAuth ; //= FirebaseAuth.getInstance();
    FirebaseUser currentUser ;//= mAuth.getCurrentUser();


    public void crash() {
        throw new RuntimeException("This is a crash");
    }

    public boolean browsePackages (View view) {
        Intent intent = new Intent (this, Packages.class);
        startActivity(intent);
        return true;
    }

    public boolean browseAccessories (View view) {
        Intent intent = new Intent (this, Accessories.class);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());

        super.onCreate(savedInstanceState);
        try {
            onCreateReal(savedInstanceState);
        } catch (Exception e) {
            Dialog d = Utility.messageDialog(this, "Rohan toone bas kar di!", e.getMessage());
            d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    System.exit(2);
                }
            });

            d.show();
        }
    }

    protected void onCreateReal(Bundle savedInstanceState) {
        context = this ;

        SharedPreferences pref = this.getSharedPreferences ("settings", Context.MODE_PRIVATE) ;
        boolean intro_played = pref.getBoolean("intro_played", false);

        int allow_launches = pref.getInt("allow_launches", 0);
        if (allow_launches < 1) {
            startActivity(new Intent(this, Invitation.class));
            allow_launches = pref.getInt("allow_launches", 0);
            if (allow_launches < 1) {
                /*Dialog d = Utility.messageDialog(this, "Beta has expired", "This service is no longer available");
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finishAffinity();
                    }
                });*/

                System.exit(1);
            }

        }

        allow_launches -- ;
        pref.edit().putInt("allow_launches", allow_launches).commit();

        if (! intro_played) {
            Intent intent = new Intent(this, Intro.class);
            startActivity(intent);
        }

        /*if (! Utility.isSomeoneLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
        }*/

        Payload payload = new Payload(getApplicationContext());
        if (! payload.isLoaded()) {
            Intent intent = new Intent(this, Loading.class);
            intent.putExtra("payload", "http://shajikhan.in/apps/dil/payload");
            startActivity(intent);
        }

/*        if (! payload.isLoaded()) {
            Intent intent = new Intent(this, Error.class);
            finish();
            startActivity(intent);
            return;
        }*/

        Payload.Item gift = payload.getItem(403);
        if (gift != null) {
            Intent intent = new Intent(this, Error.class);
            intent.putExtra("icon", String.valueOf(gift.icon));
            intent.putExtra("msg", String.valueOf(gift.description));
            intent.putExtra("url", String.valueOf(gift.name));
            finish();
            startActivity(intent);
            finishAffinity();
            return;

        }

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
                .showImageOnLoading(R.drawable.loading)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*toolbar.setLogo(R.drawable.dil_red);
        toolbar.setTitle("Go Dil");
        toolbar.setLogoDescription("Go Dil")*/;
        /*ImageButton sup = new ImageButton(this);
        sup.setImageDrawable(getResources().getDrawable(R.drawable.login));

        toolbar.addView(sup);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();*/

                Snackbar.make(view, currentUser.getEmail(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // connect "signals"
        ImageButton p = (ImageButton) findViewById(R.id.imageButton2) ;
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), Packages.class);
                startActivity(intent);
                }
        });

        ImageButton q = (ImageButton) findViewById(R.id.imageButton3) ;
        q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), DynamicAccessories.class);
                intent.putExtra("type", "themes");

                startActivity(intent);
            }
        });

        ImageButton r = (ImageButton) findViewById(R.id.imageButton5) ;
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), ShoppingCart.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            TextView t = (TextView) findViewById(R.id.header_user) ;
            if ( t!= null)
                t.setText(currentUser.getEmail());
            else
                t.setText("Welcome");
        }

        // Init glue code
        init ();
        Utility.message(this, "This service is currently in public beta", String.format("You can launch this app %d more times before it will be disabled.", allow_launches + 1));

        /*
        Payload payload = new Payload(getApplicationContext()) ;
        try {
            payload.load("http://shajikhan.in/public/apps/lo/payload");
//            payload.load("payload");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        finally {
            payload.debug();
        }
*/

        /*ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,                  *//* host Activity *//*
                drawer,         *//* DrawerLayout object *//*
                null,  *//* nav drawer icon to replace 'Up' caret *//*
                R.string.drawer_open,  *//* "open drawer" description *//*
                R.string.drawer_close *//* "close drawer" description *//*
        ) {

            *//** Called when a drawer has settled in a completely closed state. *//*

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };*/

        // Set the drawer toggle as the DrawerListener
//        drawer.setDrawerListener(mDrawerToggle);


        //      LinearLayout v = (LinearLayout) findViewById(R.id.listview);
//        v.setOrientation (LinearLayout.VERTICAL);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (currentUser != null) {
            Crashlytics.setUserEmail(currentUser.getEmail());
            MenuItem s = (MenuItem) menu.findItem (R.id.action_login) ;
            if (s != null)
                s.setTitle("Log out");
        }
        else {
            MenuItem s = (MenuItem) menu.findItem (R.id.action_login) ;
            if (s != null)
                s.setTitle("Log in");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent (getApplicationContext(), SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.action_crash) {
            crash();
            return true;
        }
        else if (id== R.id.action_login) {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser() ;
            if (! item.getTitle().equals ("Log out")) {
//            if (currentUser == null) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                return true;
            }
            else {
                Intent intent = new Intent(getApplicationContext(), LogoutScreen.class);
                startActivity(intent);

                return true;
            }

/*
            Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
            startActivity(intent);
*/

//            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_band) {
            Intent intent = new Intent (getApplicationContext(), Bands.class);
            startActivity(intent);

            return true;

        } else if (id == R.id.nav_cuisine) {
//            Intent intent = new Intent (getApplicationContext(), Cuisine.class);
            Intent intent = new Intent (getApplicationContext(), DynamicAccessories.class);
            intent.putExtra("type", "food");

            startActivity(intent);

            return true;

        } else if (id == R.id.nav_flowers) {
//            Intent intent = new Intent (getApplicationContext(), Flowers.class);
            Intent intent = new Intent (getApplicationContext(), DynamicAccessories.class);
            intent.putExtra("type", "flowers");
            startActivity(intent);

            return true;
        } else if (id == R.id.nav_theme) {
//            Intent intent = new Intent (getApplicationContext(), Theme.class);
            Intent intent = new Intent (getApplicationContext(), DynamicAccessories.class);
            intent.putExtra("type", "theme");

            startActivity(intent);

            return true;

        } else if (id == R.id.nav_share) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            share.putExtra(Intent.EXTRA_SUBJECT, "Share this app");
            share.putExtra(Intent.EXTRA_TEXT, "http://www.shajikhan.in/apps");

            startActivity(Intent.createChooser(share, "Share this app"));
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent (getApplicationContext(), Developer.class);
            startActivity(intent);

            return true;

        }else if (id == R.id.nav_sign_up) {
            Intent intent = new Intent (getApplicationContext(), Signup.class);
            startActivity(intent);

            return true;

        }else if (id == R.id.nav_login) {
            Intent intent = new Intent(getApplicationContext(), LogoutScreen.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.nav_orders) {
            if (! Utility.isSomeoneLoggedIn()) {
                Dialog dialog = Utility.messageDialog(context, "Log in first", "Log in first to see your order history");
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        startActivity(new Intent(context, LoginActivity.class));
                    }
                });
                dialog.show();
                return true;
            }

            Intent intent = new Intent(getApplicationContext(), OrderHistory.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.nav_info) {
            if (! Utility.isSomeoneLoggedIn()) {
                Dialog dialog = Utility.messageDialog(context, "Log in first", "Log in first to set user info");
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        startActivity(new Intent(context, LoginActivity.class));
                    }
                });
                dialog.show();
                return true;
            }

            Intent intent = new Intent(getApplicationContext(), UserInfo.class);
            startActivity(intent);

            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void init () {
        Context context = getApplicationContext();
    }
}
