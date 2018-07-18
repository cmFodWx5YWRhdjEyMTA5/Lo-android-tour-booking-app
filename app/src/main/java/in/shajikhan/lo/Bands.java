package in.shajikhan.lo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

public class Bands extends YouTubeBaseActivity {
    Cart cart = new Cart();
    Context context = this ;
    YouTubePlayer player = null ;
    YouTubeThumbnailLoader youTubeThumbnailLoader = null ;
    Payload payload = new Payload(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bands);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        /*setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        LinearLayout layout = (LinearLayout) findViewById(R.id.bands_layout_group);

        YouTubePlayerView view = (YouTubePlayerView) findViewById(R.id.bands_video);
        view.initialize("AIzaSyDBWVDezodM9rbYpnlWmZbcjVU13pHVYDU", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        player = youTubePlayer;
                        player.loadVideo("CZpbt-UAd3M");
                    }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Utility.message(context, "Player failed to start!", "Unable to initialize video player");
            }
        });

        ArrayList <Payload.Item> items = payload.getItems("bands");
        if (items == null || items.size() == 0) {
            Dialog dialog = Utility.messageDialog(this, "This feature is not available", "Please try again later.");
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });

            dialog.show();
            return ;
        }

        for (Payload.Item item: items){
            if (item.url == null) {
                continue;
            }

            LinearLayout h = new LinearLayout(this);
            h.setOrientation(LinearLayout.VERTICAL);
//            h.setPadding(16,16,16,16);
            LinearLayout v = new LinearLayout(this);
//            v.setPadding(16,16,16,16);
            v.setOrientation(LinearLayout.VERTICAL);
            ImageButton iv = new ImageButton(this);
            YouTubeThumbnailView thumbnailView = new YouTubeThumbnailView(this);

            final ProgressBar p = new ProgressBar(context);

            final Payload.Item final_item = item ;
            thumbnailView.initialize("AIzaSyDBWVDezodM9rbYpnlWmZbcjVU13pHVYDU", new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader thumbnailLoader) {
                    p.setVisibility(View.GONE);
                    thumbnailLoader.setVideo(final_item.url);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Utility.message(context, "Video thumbnail error", "Unable to initialize thumbnailer");
                }
            });

            thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player.loadVideo(final_item.url);
                }
            });

            iv.setMaxWidth(128);
            iv.setMaxHeight(128);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            payload.loadIcon(iv, item.icon);
            TextView label = new TextView(this);
            label.setText(item.name);
            label.setTextSize(22);
//            iv.setPadding(20,20,20,20);
            label.setPadding(20,20,20,20);
            Button b = new Button(this);
            b.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            b.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            b.setText("Add to cart");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.addAccessory(context, final_item.id);
                    Utility.snackBar(view, "Added to cart");
                }
            });

            h.addView(p);
            h.addView(thumbnailView);
//            h.addView(iv);
            v.addView(label);
            v.addView(b);
            h.addView(v);

            layout.addView(h);
        }
    }
}
