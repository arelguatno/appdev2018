package com.example.appdev.appdev2018.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appdev.appdev2018.R;
import com.example.appdev.appdev2018.pojos.SongsTitleForFillup;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TwoPlayerOffline.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TwoPlayerOffline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoPlayerOffline extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MediaPlayer mp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LinearLayout player1_linear_row1, player1_linear_row2, player2_linear_row1, player2_linear_row2;
    private Button player1_hitme_Button, player2_hitme_Button;
    private ProgressBar progressBar1, progressBar2;
    private int player1_buttons[] = {R.id.button13, R.id.button14, R.id.button15, R.id.button16};
    private int player2_buttons[] = {R.id.button9, R.id.button10, R.id.button11, R.id.button12};
    private int whereIsCorrectAnswer = 0;
    private ScheduledExecutorService service;
    SongsTitleForFillup songsTitleForFillup = new SongsTitleForFillup();

    public TwoPlayerOffline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TwoPlayerOffline.
     */
    // TODO: Rename and change types and number of parameters
    public static TwoPlayerOffline newInstance(String param1, String param2) {
        TwoPlayerOffline fragment = new TwoPlayerOffline();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_two_player_offline, container, false);

        player1_linear_row1 = view.findViewById(R.id.player1_linear_row1);
        player1_linear_row2 = view.findViewById(R.id.player1_linear_row2);

        player2_linear_row1 = view.findViewById(R.id.player2_linear_row1);
        player2_linear_row2 = view.findViewById(R.id.player2_linear_row2);

        progressBar1 = view.findViewById(R.id.progress1);
        progressBar2 = view.findViewById(R.id.progress2);

        play_music();

        player1_hitme_Button = view.findViewById(R.id.player1_hitme_Button);
        player1_hitme_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                player1_hitme_Button(view, "Humble");
            }
        });

        player2_hitme_Button = view.findViewById(R.id.player2_hitme_Button);
        player2_hitme_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                player2_hitme_Button(view, "Humble");
            }
        });


        return view;
    }

    private void player1_hitme_Button(View view, String humble) {
        player1_linear_row1.setVisibility(View.VISIBLE);
        player1_linear_row2.setVisibility(View.VISIBLE);
        player2_hitme_Button.setEnabled(false);

        player1_hitme_Button.setVisibility(View.GONE);

        loadTextButtons(view, humble, player1_buttons);

        service.shutdown();
        mp.stop();

        progressBar1.setMax(100);
        progressBar1.setProgress(100);

        progressBar2.setMax(100);
        progressBar2.setProgress(100);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar1, "progress", 0);
        animation.setDuration(5000); // 5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        ObjectAnimator animation2 = ObjectAnimator.ofInt(progressBar2, "progress", 0);
        animation2.setDuration(5000); // 5 second
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Timer end 5 seconds
                player1_linear_row1.setVisibility(View.GONE);
                player1_linear_row2.setVisibility(View.GONE);

                player2_hitme_Button.setEnabled(true);
                player1_hitme_Button.setEnabled(false);

                player1_hitme_Button.setVisibility(View.VISIBLE);
                progressBar1.setMax(mp.getDuration());
                progressBar1.setProgress(mp.getDuration());

                progressBar2.setMax(mp.getDuration());
                progressBar2.setProgress(mp.getDuration());
                play_music();
                return;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation2.start();
    }

    private void player2_hitme_Button(View view, String humble) {
        player2_linear_row1.setVisibility(View.VISIBLE);
        player2_linear_row2.setVisibility(View.VISIBLE);
        player1_hitme_Button.setEnabled(false);
        player2_hitme_Button.setVisibility(View.GONE);

        loadTextButtons(view, humble, player2_buttons);

        service.shutdown();
        mp.stop();

        progressBar1.setMax(100);
        progressBar1.setProgress(100);

        progressBar2.setMax(100);
        progressBar2.setProgress(100);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar1, "progress", 0);
        animation.setDuration(5000); // 5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        ObjectAnimator animation2 = ObjectAnimator.ofInt(progressBar2, "progress", 0);
        animation2.setDuration(5000); // 5 second
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Timer end 5 seconds
                player2_linear_row1.setVisibility(View.GONE);
                player2_linear_row2.setVisibility(View.GONE);

                player1_hitme_Button.setEnabled(true);
                player2_hitme_Button.setEnabled(false);

                player2_hitme_Button.setVisibility(View.VISIBLE);
                progressBar1.setMax(mp.getDuration());
                progressBar1.setProgress(mp.getDuration());

                progressBar2.setMax(mp.getDuration());
                progressBar2.setProgress(mp.getDuration());
                play_music();
                return;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animation2.start();
    }

    private void loadTextButtons(View v, String answer, int arrays[]) {
        Button btn;
        // Clear buttons text
        for (int x = 0; x < arrays.length; x++) {
            btn = v.findViewById(arrays[x]);
            btn.setText("");
        }

        int getRandPos = (int) ((Math.random() * 4) + 1);
        whereIsCorrectAnswer = getRandPos;
        switch (getRandPos) {
            case 1:
                btn = v.findViewById(arrays[0]);
                btn.setText(answer);
                break;
            case 2:
                btn = v.findViewById(arrays[1]);
                btn.setText(answer);
                break;
            case 3:
                btn = v.findViewById(arrays[2]);
                btn.setText(answer);
                break;
            case 4:
                btn = v.findViewById(arrays[3]);
                btn.setText(answer);
                break;
        }

        for (int x = 0; x < arrays.length; x++) {
            btn = v.findViewById(arrays[x]);
            if (btn.getText().toString() == "") {
                int getRand = (int) ((Math.random() * songsTitleForFillup.getListofSongs.length));
                btn.setText(songsTitleForFillup.getListofSongs[getRand].toString());

            }
        }

    }

    private void play_music() {
        mp = MediaPlayer.create(getContext(), R.raw.pop3_humble);

        progressBar1.setMax(mp.getDuration());
        progressBar1.setProgress(mp.getDuration());

        progressBar2.setMax(mp.getDuration());
        progressBar2.setProgress(mp.getDuration());

        if (!mp.isPlaying()) {
            mp.start();

            service = Executors.newScheduledThreadPool(1);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    service.shutdown();
                    player1_hitme_Button.setEnabled(true);
                    player2_hitme_Button.setEnabled(true);

                    progressBar1.setMax(0);
                    progressBar2.setMax(0);
                }
            });
            service.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Log.d("arel", "dsadsadsasad");
                    progressBar1.setProgress(mp.getDuration() - mp.getCurrentPosition());
                    progressBar2.setProgress(mp.getDuration() - mp.getCurrentPosition());
                }
            }, 1, 1, TimeUnit.MICROSECONDS);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
