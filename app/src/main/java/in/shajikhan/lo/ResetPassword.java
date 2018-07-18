package in.shajikhan.lo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.reset_progress);

        Button reset = (Button) findViewById(R.id.reset_password_btn) ;
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                EditText t = (EditText) findViewById(R.id.reset_email);
                t.setError(null);
                String email = t.getText().toString() ;

                if (TextUtils.isEmpty(email)) {
                    t.setError(getString(R.string.error_field_required));
                    t.requestFocus() ;
                    bar.setVisibility(View.GONE);
                    return ;
                }

                final FirebaseAuth auth = FirebaseAuth.getInstance() ;
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                final TextView v = (TextView) findViewById(R.id.reset_msg);

                                if (task.isSuccessful()) {
                                    v.setText(getString(R.string.reset_msg_sent));
                                } else {
                                    v.setText(getString(R.string.reset_msg_sent_failed));
                                }

                                bar.setVisibility(View.GONE);
                            }
                        });

            }
        });
    }
}
