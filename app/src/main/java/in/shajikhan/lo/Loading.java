package in.shajikhan.lo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class Loading extends AppCompatActivity {

    void home () {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    void fail () {
        Intent intent = new Intent(this, Error.class);
        finish();
        startActivity(intent);
        finishAffinity();
        System.exit(1);
        return;
    }
    class Load extends AsyncTask <String, Void, Void> {
        @Override
        protected Void doInBackground(String... url) {
            Payload payload = new Payload(getApplicationContext()) ;
            try {
                if (! payload.load(url [0])) {
                    fail();
                    return null;
                }
//            payload.load("payload");
            } catch (IOException e) {
                e.printStackTrace();
                fail ();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                fail ();
            }
            /*finally {
                payload.debug();
            }*/
        return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            home();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent() ;
        String filename = intent.getStringExtra("payload");
        new Load ().execute(filename);
    }
}
