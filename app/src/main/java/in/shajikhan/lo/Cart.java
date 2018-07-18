package in.shajikhan.lo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by djshaji on 5/28/17.
 */

public class Cart {
    void addPackage (Context context, int id) {
        add (context, "packages", id) ;
    }

    void addAccessory (Context context, int id) {
        add (context, "accessories", id) ;
    }

    ArrayList getPackages (Context context) {
        return get (context, "packages") ;
    }

    ArrayList getAccessories (Context context) {
        return get (context, "accessories") ;
    }

    String getDate (Context context) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        return pref.getString("date", "01/01/2017");
    }
    String getDateOld (Context context) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        int day = pref.getInt("dated", 1),
                month = pref.getInt("datem", 1),
                year = pref.getInt("datey", 2017);
        return String.format ("%d-%d-%d", day,month,year);
    }
    String getCart (Context context) {
        ArrayList packages = getPackages(context);
        ArrayList accessories = getAccessories(context);

        String cart = "";
        if (packages != null) {
            for (Object o : packages) {
                if (cart == "") {
                    cart = String.valueOf(o);
                } else {
                    cart += ";" + String.valueOf(o);
                }
            }
        }

        if (accessories != null) {
            for (Object o : accessories) {
                if (cart == "") {
                    cart = String.valueOf(o);
                } else {
                    cart += ";" + String.valueOf(o);
                }
            }
        }

        return cart;
    }

    String generateTransactionID (String seed) {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.getTimeInMillis()) + "-" + String.valueOf(seed);
    }

    int getTotal (Context context) {
        ArrayList packages = getPackages(context);
        ArrayList accessories = getAccessories(context);

        int total = 0 ;
        Payload p = new Payload(context);

        if (packages!= null) {
            for (Object o : packages) {
                String i = String.valueOf(o);
                if (i.equals("")) {
                    continue;
                }

                Payload.ItemGroup g = p.getItemGroup(Integer.valueOf(i));
                if (g != null && ! g.equals("")) {
                    total += Integer.valueOf(g.price);
                }
            }
        }

        if (accessories != null) {
            for (Object o : accessories) {
                String i = String.valueOf(o);
                if (i.equals("")) {
                    continue;
                }

                Payload.Item g = p.getItem(Integer.valueOf(i));
                total += Integer.valueOf(g.price);
            }
        }

        return total;
    }
    void add (Context context, String type, int id) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        System.out.println(id) ;
        String s = pref.getString(type, null) ;
        if (s == null)
            s = String.valueOf(id) ;
        else {
            if (! s.equals(String.valueOf(id)) && ! s.equals(";" + String.valueOf(id)) && ! s.equals(String.valueOf(id) + ";") && ! s.contains (String.format("%d;", id)) && ! s.contains (String.format(";%d", id))) {
                s += ';' + String.valueOf(id);
                Log.e(s, s);
            }
        }

        pref.edit ().putString(type, s).commit() ;
        System.out.println(s) ;
    }

    void debug (Context context) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        Log.e ("accessories",pref.getString("accessories", null));
        Log.e("packages", pref.getString("packages", null));

    }

    ArrayList get (Context context, String type) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        String s = pref.getString(type, null) ;
        if (s == null) {
            System.out.println("no packages found");
            return null;
        }

        String [] p = s.split (";") ;
        ArrayList packages = new ArrayList() ;
        for (String v: p) {
            if (v!="")
                packages.add(v) ;
        }
        if (packages.size() > 0)
            return packages ;
        else
            return null ;
    }

    void clear (Context context) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        pref.edit ().putString("packages", "").commit() ;
        pref.edit ().putString("accessories", "").commit() ;
    }

    void remove (Context context, String type, int id) {
        SharedPreferences pref = context.getSharedPreferences ("cart", Context.MODE_PRIVATE) ;
        String s = pref.getString(type, null) ;
        if (s == null) {
            System.out.println("no packages found");
            return ;
        }

        String [] p = s.split (";") ;
        String result = new String () ;

        for (String a: p) {
            if (a == "")
                continue ;
            int ai = -1 ;
            try {
                ai = Integer.valueOf(a) ;
            }
            catch (NumberFormatException e) {
                continue ;
            }

            if (ai != id) {
                if (result.length() == 0)
                    result = a;
                else
                    result += ";" + a;
            }
        }

        pref.edit ().putString(type, result).commit() ;
    }
}
