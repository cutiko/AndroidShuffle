package cl.cutiko.androidshuffle.views;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cl.cutiko.androidshuffle.R;
import cl.cutiko.androidshuffle.background.PlayerService;

public class MainActivity extends AppCompatActivity {

    private PlayerService playerService;
    private ServiceConnection serviceConnection;
    private boolean isBound = false;
    private static final int READ_EXTERNAL_CONTENT_PERMISSION = 37;
    private TextView songInfo;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_CONTENT_PERMISSION);
        } else {
            bindToPlayer();
        }
    }

    private void bindToPlayer(){
        Intent intent = new Intent(this, PlayerService.class);
        serviceConnection = getServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
                playerService = binder.getService();
                playerService.setSongs();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setReceiver();
        setFilter();
        songInfo = (TextView) findViewById(R.id.songInfo);
        setPlay();
        setStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBound && songInfo != null) {
            songInfo.setText(playerService.getSongName());
        }
        registerReceiver(receiver, intentFilter);

    }

    private void setPlay(){
        ImageButton playBtn = (ImageButton) findViewById(R.id.playBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    playerService.playSong();
                }
            }
        });
    }

    private void setStop(){
        ImageButton stopBtn = (ImageButton) findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    playerService.stopSong();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_CONTENT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindToPlayer();
            }
        }
    }

    private void setFilter(){
        intentFilter = new IntentFilter();
        intentFilter.addAction(PlayerService.SONG_UPDATE);
    }

    private void setReceiver(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (action != null) {
                        if (PlayerService.SONG_UPDATE.equals(action)) {
                            songInfo.setText(playerService.getSongName());
                        }
                    }
                }
            }
        };
    }
}
