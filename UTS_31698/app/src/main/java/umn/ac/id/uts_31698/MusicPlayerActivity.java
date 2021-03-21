package umn.ac.id.uts_31698;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvTitle, tvArtist;
    SeekBar seekBarTime;
    Button btnBack, btnPlay, btnNext;

    MediaPlayer musicPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Song song = (Song) getIntent().getSerializableExtra("song");

        seekBarTime = findViewById(R.id.seekBarTime);
        btnBack = findViewById(R.id.btnBack);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);

        musicPlayer.start();

        btnPlay.setOnClickListener(this);

        seekBarTime.setMax(musicPlayer.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser) {
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (musicPlayer != null) {
                    if(musicPlayer.isPlaying()) {
                        try {
                            final double current = musicPlayer.getCurrentPosition();
                            final String elapsedTime = millisecondsToString((int) current);

                            seekBarTime.setProgress((int) current);

                            Thread.sleep(1000);


                        } catch (InterruptedException e) {}
                    }
                }
            }
        }).start();

    }

    private String millisecondsToString(int time) {
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds < 10) {
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return elapsedTime;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnPlay) {
            if(musicPlayer.isPlaying()) {
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.play);
            } else {
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            if(musicPlayer.isPlaying()) {
                musicPlayer.stop();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}