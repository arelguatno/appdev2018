package com.example.appdev.appdev2018.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by aguatno on 3/11/18.
 */

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    protected static MediaPlayer bgMusic;
    private int bgMusicLength = 0;
    static boolean iBgMusicsPlaying = false;

    protected FirebaseFirestore db;
    protected FirebaseAuth mAuth;

    public static final String SONG_SPACE = "_";

    // Collections
    static final String DB_LIST_OF_SONGS = "list of songs";
    static final String DB_LIST_OF_GENRES = "list of genres";
    static final String DB_USERS_COLLECTION_NAME = "users";

    //Documents
    static final String DB_GENRES = "genres";


    // Sub Collections
    static final String DB_POP = "pop";

    // Fields
    static final String DB_LAST_LOGIN_FIELD = "lastLogin";




    protected void setToFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void bgPauseMusic() {
        if (bgMusic.isPlaying()) {
            bgMusic.pause();
            bgMusicLength = bgMusic.getCurrentPosition();

        }
    }

    public void bgResumeMusic() {
        if (bgMusic.isPlaying() == false) {
            bgMusic.seekTo(bgMusicLength);
            bgMusic.start();
        }
    }

    public void bgStopMusic() {
        bgMusic.stop();
        bgMusic.release();
        bgMusic = null;
    }

    public static int getImageIdByName(Context c, String ImageName) {
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }

    public static int getSongIdByName(Context c, String ImageName) {
        return c.getResources().getIdentifier(ImageName, "raw", c.getPackageName());
    }

}
