package in.shajikhan.lo;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Invitation extends Activity {
    ArrayList <Long> codes = new ArrayList();
    Context c = null ;

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        c = this ;
        final EditText code = (EditText) findViewById(R.id.invitation_code);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.invitation_progress);
        final Button submit = (Button) findViewById(R.id.invitation_submit);

        progressBar.setVisibility(View.VISIBLE);
        code.setEnabled(false);
        submit.setEnabled(false);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference ("/invitation_codes");
        ref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  progressBar.setVisibility(View.VISIBLE);
                  code.setEnabled(false);
                  submit.setEnabled(false);
                  Dialog p = Utility.progressDialog(c, "Loading", "Please wait...");
                  p.show();
                  codes = new ArrayList();
                  Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                  for (DataSnapshot snap : iterable) {
                      Log.e ("fb", String.valueOf(snap));
                      codes.add(Long.valueOf(String.valueOf(snap.getValue(true))));
                  }
                  p.cancel();
                  progressBar.setVisibility(View.GONE);
                  submit.setEnabled(true);
                  code.setEnabled(true);
                  submit.setVisibility(View.VISIBLE);
                  code.setVisibility(View.VISIBLE);
                  findViewById(R.id.invitation_label).setVisibility(View.VISIBLE);
              }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utility.message(c, "Could not connect to database!", "The transaction was cancelled:\n\n" + String.valueOf(databaseError.getMessage() + databaseError.getDetails()));
                progressBar.setVisibility(View.GONE);
                submit.setEnabled(true);
                code.setEnabled(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code.setError(null);
                if (codes.size() == 0) {
                    Utility.message(c, "No valid codes found!", "Please make sure you are connected to the internet and try again! Try restarting the app if this error persists.");
                    return ;
                }
                code.setError(null);

                String inv = code.getText().toString();
                int num = -1 ;
                try {
                    num = Integer.valueOf(inv) ;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (inv == null || num == -1) {
                    code.setError("This field is required! Only enter numbers here plz ok bye tc lulz");
                    progressBar.setVisibility(View.GONE);
                    return ;
                }

                progressBar.setVisibility(View.VISIBLE);

                for (Long i: codes) {
                    if (i == num) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("invitation_codes/" + String.valueOf(i));
                        ref.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Utility.message(c, "Unable to check code!", databaseError.getMessage() + databaseError.getDetails());
                                    progressBar.setVisibility(View.GONE);
                                    submit.setEnabled(true);
                                    code.setEnabled(true);
                                    return;
                                } else {
//                                    Utility.message(c, "That was correct!", null);
                                    SharedPreferences pref = getSharedPreferences("settings", Context.MODE_PRIVATE);
                                    pref.edit().putInt("allow_launches", 7).commit();
                                    Intent intent = null ;

//                                    if (Utility.isSomeoneLoggedIn()) {
                                    if (true) {
                                        intent = new Intent(c, MainActivity.class);
                                    } else {
                                        intent = new Intent(c, LoginActivity.class);
                                    }

                                    finishAffinity();
                                    startActivity(intent);
                                }
                            }
                        });

                        return;
                    }

                }

                code.setError("The code you have entered is invalid.");
                progressBar.setVisibility(View.GONE);
                submit.setEnabled(true);
                code.setEnabled(true);
            }
        });
    }
}
