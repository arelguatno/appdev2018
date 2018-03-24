package com.example.appdev.appdev2018.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appdev.appdev2018.R;
import com.example.appdev.appdev2018.fragments.Single_player_4_buttons;
import com.example.appdev.appdev2018.fragments.Single_player_blank_fields;
import com.example.appdev.appdev2018.interfaces.Single_Player_4_buttons_ViewEvents;
import com.example.appdev.appdev2018.interfaces.Single_player_blank_fields_ViewEvents;
import com.example.appdev.appdev2018.pojos.Album;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SinglePlayerGameActivity extends BaseActivity implements Single_Player_4_buttons_ViewEvents, Single_player_blank_fields_ViewEvents {
    static MediaPlayer mp;
    ProgressBar progressBarCircle;
    private static final String TAG = "SinglePlayerGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToFullScreen();
        setContentView(R.layout.activity_single_player_game);
        db = FirebaseFirestore.getInstance();

//        typeOfGenre = getIntent().getExtras().getString("typeOfGenre");

        String songIdString = getIntent().getExtras().getString("songID");
        String answerCode = getIntent().getExtras().getString("answerCode");
        String songTitle = getIntent().getExtras().getString("songTitle");

        mp = MediaPlayer.create(getApplicationContext(), getSongIdByName(getApplicationContext(),songIdString));

        progressBarCircle = findViewById(R.id.progress_bar_circle);
        progressBarCircle.setMax(mp.getDuration());
        progressBarCircle.setProgress(mp.getDuration());

        bgPauseMusic();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("correct_answer", answerCode);
            bundle.putString("answerTitle", songTitle);

            // Random fragment generator
            randomFragmentGenerator(bundle);
        }
    }

    private void randomFragmentGenerator(Bundle bundle) {
//        1 = 4 buttons
//        2 = blank fields
        int getRandNum = (int) ((Math.random() * 2) + 1);

        if (getRandNum == 1) {
            Single_player_4_buttons firstFragment = new Single_player_4_buttons();
            firstFragment.setArguments(bundle);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

        } else {
            Single_player_blank_fields firstFragment = new Single_player_blank_fields();
            firstFragment.setArguments(bundle);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    public void start_playing_music_button(View v) {
        if (!mp.isPlaying()) {
            mp.start();

            final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    service.shutdown();
                    progressBarCircle.setProgress(mp.getDuration());
                }
            });
            service.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    progressBarCircle.setProgress(mp.getDuration() - mp.getCurrentPosition());
                }
            }, 1, 1, TimeUnit.MICROSECONDS);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bgResumeMusic();
        mp.stop();
        mp.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                bgResumeMusic();
                mp.stop();
                mp.release();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentSButton1Clicked() {
        Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentSButton2Clicked() {
        Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentSButton3Clicked() {
        Toast.makeText(this, "Button3", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentSButton4Clicked() {
        Toast.makeText(this, "Button4", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fieldText_anyButtonPress(String s) {

    }
}
