package in.shajikhan.lo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by djshaji on 7/19/17.
 */

public class Utility {
    public static void message(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Okay", null);
        try {
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dialog messageDialog (Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Okay", null);
        return builder.create();
    }

    public static Dialog progressDialog (Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setView(new ProgressBar(context));
        return builder.create();
    }

    public static boolean isSomeoneLoggedIn () {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return false ;
        } else {
            return true ;
        }

    }

    public static void snackBar (View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
