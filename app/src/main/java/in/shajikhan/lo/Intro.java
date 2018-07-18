package in.shajikhan.lo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Intro extends YouTubeBaseActivity {
    YouTubePlayer player = null ;
    Context c = null ;

//    @Override
//    public void onBackPressed() {
//        System.exit(0);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        c = this ;

        YouTubePlayerView view = (YouTubePlayerView) findViewById(R.id.intro_video);
        view.initialize("AIzaSyDBWVDezodM9rbYpnlWmZbcjVU13pHVYDU", new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer ;
                play () ;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Utility.message(c, "Failed to initialize video player!", "The video player could not be initialized. Sorry :(");

            }
        });

        Button skip = (Button) findViewById(R.id.intro_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = c.getSharedPreferences ("settings", Context.MODE_PRIVATE) ;
                pref.edit ().putBoolean("intro_played", true).commit();
                finish();
            }
        });
    }

    void play () {
        player.loadVideo("XjJQBjWYDTs");
//        player.play ();
    }
}
