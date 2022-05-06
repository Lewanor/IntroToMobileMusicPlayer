package com.example.usermusicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class PlayerMusic extends AppCompatActivity {

    TextView txtName,txtArt,txtLastDur,txtCurrDur;
    ImageButton btStartPause,btNext,btPrev;
    SeekBar seekMusic;

    ArrayList<String> name, artist, aydi, duration, data;
    int position, total, current;
    Uri uri;
    boolean already;
    Thread seekStrength;

    MediaPlayer mediaPlay;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_music);

        name = getIntent().getStringArrayListExtra("name");
        artist = getIntent().getStringArrayListExtra("art");
        duration = getIntent().getStringArrayListExtra("dur");
        aydi = getIntent().getStringArrayListExtra("id");
        data = getIntent().getStringArrayListExtra("data");
        position = getIntent().getIntExtra("position", 0);

        txtName = findViewById(R.id.songNameee);
        txtArt = findViewById(R.id.artistiguess);
        txtCurrDur = findViewById(R.id.currDur);
        txtLastDur = findViewById(R.id.totalDur);

        btStartPause = findViewById(R.id.playpause);
        btNext = findViewById(R.id.nextBtn);
        btPrev = findViewById(R.id.prevBtn);

        seekMusic = findViewById(R.id.seekBar);

        txtName.setText(name.get(position));
        txtArt.setText(artist.get(position));

        already = false;

        uri = Uri.parse(data.get(position));
        mediaPlay = MediaPlayer.create(getApplicationContext(), uri);

        if (mediaPlay != null){
            mediaPlay.start();
            already = true;
            txtLastDur.setText(getTime(mediaPlay.getDuration()));
            seekMusic.setMax(mediaPlay.getDuration());
            mediaPlay.setOnCompletionListener(view -> btNext.performClick());

            seekStrength = new Thread(() -> {
                total = mediaPlay.getDuration();
                current = 0;

                while (current < total){
                    try {
                        Thread.sleep(500);
                        current = mediaPlay.getCurrentPosition();
                        seekMusic.setProgress(current);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            seekStrength.start();
        }

        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlay.seekTo(seekBar.getProgress());

            }
        });

        final Handler handle = new Handler();
        final int delay = 1000;

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtCurrDur.setText(getTime(mediaPlay.getCurrentPosition()));
                handle.postDelayed(this, delay);
            }
        }, delay);


        btStartPause.setOnClickListener(view -> {
            if (already) {
                btStartPause.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24);
                mediaPlay.pause();
                already = false;
            } else {
                btStartPause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
                mediaPlay.start();
                already = true;
            }
        });

        btNext.setOnClickListener(view -> {
            mediaPlay.stop();
            mediaPlay.release();
            position = ((position + 1) % name.size());
            uri = Uri.parse(data.get(position));
            mediaPlay = MediaPlayer.create(getApplicationContext(), uri);
            txtName.setText(name.get(position));
            txtArt.setText(artist.get(position));

            if (mediaPlay != null){
                mediaPlay.start();
                already = true;
                txtLastDur.setText(getTime(mediaPlay.getDuration()));
                seekMusic.setMax(mediaPlay.getDuration());
                seekMusic.setProgress(0);
                mediaPlay.setOnCompletionListener(v -> btNext.performClick());

                seekStrength = new Thread(() -> {
                    total = mediaPlay.getDuration();
                    current = 0;

                    while (current < total){
                        try {
                            Thread.sleep(500);
                            current = mediaPlay.getCurrentPosition();
                            seekMusic.setProgress(current);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                seekStrength.start();
            }
        });

        btPrev.setOnClickListener(view -> {
            mediaPlay.stop();
            mediaPlay.release();
            position = ((position - 1)<0) ? (name.size() - 1):position-1;
            uri = Uri.parse(data.get(position));
            mediaPlay = MediaPlayer.create(getApplicationContext(), uri);
            txtName.setText(name.get(position));
            txtArt.setText(artist.get(position));

            if (mediaPlay != null){
                mediaPlay.start();
                already = true;
                txtLastDur.setText(getTime(mediaPlay.getDuration()));
                seekMusic.setMax(mediaPlay.getDuration());
                seekMusic.setProgress(0);
                mediaPlay.setOnCompletionListener(v -> btNext.performClick());

                seekStrength = new Thread(() -> {
                    total = mediaPlay.getDuration();
                    current = 0;

                    while (current < total){
                        try {
                            Thread.sleep(500);
                            current = mediaPlay.getCurrentPosition();
                            seekMusic.setProgress(current);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                seekStrength.start();
            }

        });

    }

    public String getTime(int dur){
        String time = "";
        int min = dur/1000;
        int sec = min % 60;
        min /= 60;

        time = time+min+":";
        if (sec < 10)
            time += "0";
        time += sec;
        return time;
    }


}